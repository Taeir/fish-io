package com.github.fishio.multiplayer.client;

import javafx.beans.property.SimpleObjectProperty;

import com.github.fishio.Preloader;
import com.github.fishio.Util;
import com.github.fishio.control.MultiplayerGameController;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;
import com.github.fishio.multiplayer.FishMessage;
import com.github.fishio.multiplayer.server.FishServerSettingsMessage;

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
	private static final FishIOClient INSTANCE = new FishIOClient();
	
	private volatile boolean connected;
	private volatile boolean connecting;
	private volatile Channel currentChannel;
	
	private SimpleObjectProperty<MultiplayerClientPlayingField> playingFieldProperty = new SimpleObjectProperty<>();
	
	private String host;
	private int port;
	
	private FishServerSettingsMessage settings;
	
	private FishIOClient() { }
	
	/**
	 * @return
	 * 		the FishIOClient instance.
	 */
	public static FishIOClient getInstance() {
		return INSTANCE;
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
			
			//Call our onConnect methods, to indicate that we are connected.
			onConnect();
			
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
	 * 		a ChannelFuture that can be used to determine if and when the
	 * 		message is actually sent to the server.
	 * 		<code>null</code> if the message was not queued (e.g. not
	 * 		connected to server).
	 */
	public ChannelFuture queueMessage(FishMessage message, boolean flush) {
		Channel ch = getChannel();
		if (ch == null) {
			return null;
		}
		
		ChannelFuture cf;
		if (flush) {
			cf = ch.writeAndFlush(message);
		} else {
			cf = ch.write(message);
		}
		return cf;
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
		return playingFieldProperty.get();
	}
	
	/**
	 * @return
	 * 		the playingfield property used by this client.
	 */
	public SimpleObjectProperty<MultiplayerClientPlayingField> getPlayingFieldProperty() {
		return this.playingFieldProperty;
	}
	
	/**
	 * Called when the connection with the server is established.
	 */
	public void onConnect() {
		//Wait until we have received the settings. If awaitSettings returns false,
		//we lost connection, so we simply return here.
		if (!awaitSettings()) {
			return;
		}
		
		//Get the settings we need.
		int width = (Integer) settings.getSetting("WIDTH");
		int height = (Integer) settings.getSetting("HEIGHT");
		
		//Create a new playing field
		MultiplayerGameController controller = Preloader.getControllerOrLoad("multiplayerGameScreen");
		MultiplayerClientPlayingField mcpf =
				new MultiplayerClientPlayingField(60, controller.getCanvas(), width, height);
		playingFieldProperty.set(mcpf);
		
		//Request a player fish
		queueMessage(new FishClientRequestPlayerMessage(), true);
		
		//Start the game
		mcpf.startGame();
	}
	
	/**
	 * Called when we are disconnected from the server.
	 */
	public void onDisconnect() {
		//Remove the current playing field.
		MultiplayerClientPlayingField mcpf = getPlayingField();
		playingFieldProperty.set(null);
		
		//Stop the game
		mcpf.stopGame();
		
		//Remove the settings
		this.settings = null;
		
		//TODO #169 Show message to client?
		
		//Switch back to main menu
		Util.onJavaFX(() -> Preloader.switchTo("mainMenu", 1000));
	}
	
	/**
	 * @param settings
	 * 		sets the settings to use for this FishIOClient.
	 */
	public void setSettings(FishServerSettingsMessage settings) {
		this.settings = settings;
	}
	
	/**
	 * Waits until we have received settings from the server.
	 * 
	 * @return
	 * 		<code>true</code> if the settings were received,
	 * 		<code>false</code> otherwise (e.g. connection closed).
	 */
	public boolean awaitSettings() {
		while (this.settings == null) {
			if (currentChannel == null || currentChannel.closeFuture().awaitUninterruptibly(100L)) {
				//We lost connection
				return false;
			}
		}
		
		return true;
	}
}
