package com.github.fishio.multiplayer.client;

import com.github.fishio.Preloader;
import com.github.fishio.Util;
import com.github.fishio.control.MultiplayerGameController;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;
import com.github.fishio.multiplayer.FishMessage;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.util.concurrent.Future;

/**
 * Singleton class that represents this client in a multiplayer game
 * (that is, this FishIO application is the singleton client).<br>
 * <br>
 * This class is only used client side.
 */
public final class FishIOClient implements Runnable {
	private static FishIOClient instance;
	
	private volatile boolean connected;
	private volatile boolean connecting;
	private volatile Channel currentChannel;
	
	private MultiplayerClientPlayingField playingField;
	
	private String host;
	private int port;
	
	private FishIOClient() {
		MultiplayerGameController controller = Preloader.getControllerOrLoad("multiplayerGameScreen");
		playingField = new MultiplayerClientPlayingField(60, controller.getCanvas(), 1280, 720);
	}
	
	/**
	 * @return
	 * 		the FishIOClient instance.
	 */
	public static FishIOClient getInstance() {
		synchronized (FishIOClient.class) {
			if (instance == null) {
				instance = new FishIOClient();
			}
		}
		
		return instance;
	}
	
	/**
	 * Connects to the given host and port.
	 * 
	 * @param host
	 * 		the host to connect to
	 * @param port
	 * 		the port to connect to
	 */
	public void connect(String host, int port) {
		if (connected) {
			throw new IllegalStateException("You cannot connect to a server if you are already connected to one!");
		}
		if (connecting) {
			throw new IllegalStateException("Already connecting to a server!");
		}
		
		this.host = host;
		this.port = port;
		new Thread(this).start();
	}
	
	@SuppressWarnings("resource")
	@Override
	public void run() {
		synchronized (this) {
			if (connected) {
				throw new IllegalStateException("You cannot connect to a server if you are already connected to one!");
			}
			if (connecting) {
				throw new IllegalStateException("Already connecting to a server!");
			}
			
			connecting = true;
		}
		
		Log.getLogger().log(LogLevel.INFO, "[Client] Starting client");
		
		//Handles incoming/outgoing messages
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			Bootstrap b = new Bootstrap();
			b.group(workerGroup);
			b.channel(NioSocketChannel.class);
			b.option(ChannelOption.SO_KEEPALIVE, true);
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast("decoder",
							new ObjectDecoder(ClassResolvers.softCachingResolver(ClassLoader.getSystemClassLoader())));
					ch.pipeline().addLast("encoder", new ObjectEncoder());
					ch.pipeline().addLast("handler", new FishClientHandler());
				}
			});

			//Start the client.
			ChannelFuture f = b.connect(host, port).sync();
			
			synchronized (this) {
				currentChannel = f.channel();
			}
			
			//If the channel is active, we are connected.
			if (f.channel().isActive()) {
				connected = true;
			}
			
			//We are no longer connecting
			connecting = false;
			
			//Call onDisconnect when we disconnect from the server.
			f.channel().closeFuture().addListener((future) -> onDisconnect());
			
			//Send the player request
			f.channel().writeAndFlush(new FishClientRequestPlayerMessage());
			
			//Start the game
			getPlayingField().startGame();
			
			//Wait until the connection is closed.
			f.channel().closeFuture().sync();
		} catch (InterruptedException ex) {
			throw new RuntimeException(ex);
		} finally {
			synchronized (this) {
				connecting = false;
				connected = false;
				currentChannel = null;
			}
			
			//Initiate shutdown
			workerGroup.shutdownGracefully();
		}
	}
	
	/**
	 * @return
	 * 		the (current) channel used by this client.
	 */
	public synchronized Channel getChannel() {
		return this.currentChannel;
	}
	
	/**
	 * @return
	 * 		<code>true</code> if this client is able to connect to a new
	 * 		server. <code>false</code> if we are still connecting or
	 * 		connected to a server.
	 */
	public boolean canConnect() {
		return !isConnecting() && !isConnectedQuick();
	}
	
	/**
	 * @return
	 * 		<code>true</code> if this client is connecting to a server.
	 * 		<code>false</code> otherwise.
	 */
	public boolean isConnecting() {
		return connecting;
	}
	
	/**
	 * Performs a quick check to see if we are connected.<br>
	 * Use {@link #isConnected()} if you want to know for certain.
	 * 
	 * @return
	 * 		<code>true</code> if this client is connected to a server.
	 * 		<code>false</code> otherwise.
	 */
	public boolean isConnectedQuick() {
		return connected;
	}
	
	/**
	 * Note: this method can block under certain circumstances. Use
	 * {@link #isConnectedQuick()} to check without blocking.
	 * 
	 * @return
	 * 		<code>true</code> if this client is connected to a server.
	 * 		<code>false</code> otherwise.
	 */
	public synchronized boolean isConnected() {
		return currentChannel != null && currentChannel.isActive();
	}
	
	/**
	 * If this client is connected to a server, this method will tell the
	 * channel to close (non blocking).<br>
	 * If this client is not connected to a server, this method has no
	 * effect.
	 * 
	 * @see #disconnectAndWait(long)
	 */
	public synchronized void disconnect() {
		if (currentChannel != null) {
			currentChannel.close();
		}
	}
	
	/**
	 * If this client is connected to a server, this method will close
	 * the channel and wait for it to be closed.<br>
	 * If this client is not connected to a server, this method has no
	 * effect.
	 * 
	 * @param maxTime
	 * 		the maximum amount of time in milliseconds to wait for.
	 * 
	 * @return
	 * 		<code>true</code> if we are now disconnected.
	 * 		<code>false</code> if maxTime passes before we are disconnected.
	 * 
	 * @throws InterruptedException
	 * 		if we are interrupted while waiting.
	 */
	public boolean disconnectAndWait(long maxTime) throws InterruptedException {
		Future<Void> closeFuture;
		synchronized (this) {
			if (currentChannel == null) {
				return true;
			}
			
			closeFuture = currentChannel.close();
		}
		
		return closeFuture.await(maxTime);
	}
	
	/**
	 * Queues up the given message to send it to the server we are
	 * currently connected to.
	 * 
	 * @param message
	 * 		the message to queue up for sending.
	 * @param flush
	 * 		if <code>true</code>, the queue is flushed after this call.
	 * 
	 * @return
	 * 		<code>true</code> if the message was queued,
	 * 		<code>false</code> otherwise (e.g. not connected to server).
	 */
	public boolean queueMessage(FishMessage message, boolean flush) {
		Channel ch = getChannel();
		if (ch == null) {
			return false;
		}
		
		if (flush) {
			ch.writeAndFlush(message);
		} else {
			ch.write(message);
		}
		return true;
	}
	
	/**
	 * If this client is connected to a server, this method flushes all
	 * queued messages to the server.
	 */
	public void flush() {
		Channel ch = getChannel();

		if (ch != null) {
			ch.flush();
		}
	}
	
	/**
	 * @return
	 * 		the playingfield used by this client.
	 */
	public MultiplayerClientPlayingField getPlayingField() {
		return this.playingField;
	}
	
	/**
	 * Called when we are disconnected from the server.
	 */
	public void onDisconnect() {
		//Stop the game and clear the field
		getPlayingField().stopGame();
		getPlayingField().clear();
		
		//TODO #169 Show message to client?
		
		//Switch back to main menu
		Util.onJavaFX(() -> Preloader.switchTo("mainMenu", 1000));
	}
}
