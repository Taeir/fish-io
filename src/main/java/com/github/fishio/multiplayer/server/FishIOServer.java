package com.github.fishio.multiplayer.server;

import com.github.fishio.Preloader;
import com.github.fishio.Util;
import com.github.fishio.control.MultiplayerGameController;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;
import com.github.fishio.multiplayer.FishMessage;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * Singleton class that represents this program in a multiplayer game
 * (that is, this FishIO application is the singleton server).<br>
 * <br>
 * This class is only used server side.
 */
public final class FishIOServer implements Runnable {
	private static FishIOServer instance;
	private int port;
	private ChannelGroup allChannels;
	private Channel channel;
	private boolean started;
	private MultiplayerServerPlayingField playingField;
	
	private FishIOServer() {
		MultiplayerGameController controller = Preloader.getControllerOrLoad("multiplayerGameScreen");
		playingField = new MultiplayerServerPlayingField(60, controller.getCanvas(), 1280 ,720);
	}
	
	/**
	 * @return
	 * 		the FishIOServer instance.
	 */
	public static FishIOServer getInstance() {
		synchronized (FishIOServer.class) {
			if (instance == null) {
				instance = new FishIOServer();
			}
		}
		
		return instance;
	}
	
	/**
	 * Starts this FishIOServer on the given port.
	 * 
	 * @param port
	 * 		the port to use.
	 */
	public void start(int port) {
		if (this.started) {
			throw new IllegalStateException("You cannot start a FishIOServer twice!");
		}
		
		this.port = port;
		
		new Thread(this).start();
	}
	
	@SuppressWarnings("resource")
	@Override
	public void run() {
		synchronized (this) {
			if (this.started) {
				throw new IllegalStateException("You cannot start a FishIOServer twice!");
			}
			
			this.started = true;
		}
		
		Log.getLogger().log(LogLevel.INFO, "[Server] Starting server on port " + port);
		
		//Handles accepting connections
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		//Handles sending/receiving messages
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		allChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
		
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast("decoder",
							new ObjectDecoder(ClassResolvers.softCachingResolver(ClassLoader.getSystemClassLoader())));
					ch.pipeline().addLast("encoder", new ObjectEncoder());
					ch.pipeline().addLast("handler", new FishServerHandler(allChannels));
				}
			})
			.option(ChannelOption.SO_BACKLOG, 128)
			.childOption(ChannelOption.SO_KEEPALIVE, true);
			
			//Bind and start to accept incoming connections.
			ChannelFuture f = b.bind(port).sync();
			
			synchronized (this) {
				this.channel = f.channel();
			}

			//Call onStop when we stop
			f.channel().closeFuture().addListener((future) -> onStop());
			
			//Create own player and start the game
			this.playingField.respawnOwnPlayer();
			this.playingField.startGame();
			
			//Wait until the server socket is closed.
			f.channel().closeFuture().sync();
		} catch (InterruptedException ex) {
			//Wrap the exception in a runtime exception
			throw new RuntimeException(ex);
		} finally {
			//Close all connected channels.
			allChannels.close();
			
			//Set started to false and the channel to null
			synchronized (this) {
				this.started = false;
				this.channel = null;
				this.allChannels = null;
			}
			
			//Shutdown the groups
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
    }
	
	/**
	 * @return
	 * 		the channel used by this server.
	 */
	public synchronized Channel getChannel() {
		return this.channel;
	}
	
	/**
	 * @return
	 * 		<code>true</code> if this server is running,
	 * 		<code>false</code> otherwise.
	 */
	public boolean isRunning() {
		return this.started;
	}
	
	/**
	 * If this FishIOServer is running, this method will tell it to
	 * terminate (non-blocking).<br>
	 * If this server is not running, this method has no effect.
	 * 
	 * @see #stopAndWait(long)
	 */
	public synchronized void stop() {
		if (this.channel != null) {
			this.channel.close();
		}
	}
	
	/**
	 * Stops this FishIOServer and waits for it to terminate for a
	 * maximum of maxTime milliseconds.<br>
	 * <br>
	 * When this method is called when the server is not running, this
	 * method has no effect.
	 * 
	 * @param maxTime
	 * 		the maximum amount of time to wait for the server to stop,
	 * 		in milliseconds.
	 * 
	 * @return
	 * 		<code>false</code> if maxTime ran out before the server was
	 * 		stopped, <code>true</code> otherwise.
	 * 
	 * @throws InterruptedException
	 * 		if we are interrupted while waiting.
	 */
	public boolean stopAndWait(long maxTime) throws InterruptedException {
		Future<Void> closeFuture;
		synchronized (this) {
			if (channel == null) {
				return true;
			}
			
			closeFuture = this.channel.close();
		}
		
		return closeFuture.await(maxTime);
	}
	
	/**
	 * Called when the server is stopped.
	 */
	public void onStop() {
		//Stop the game and clear the field
		getPlayingField().stopGame();
		getPlayingField().clear();
		
		//TODO #169 show a message to the user?
		
		//Log that we have stopped
		Log.getLogger().log(LogLevel.INFO, "[Server] Server stopped");
		
		//Switch to the main menu
		Util.onJavaFX(() -> Preloader.switchTo("mainMenu", 1000));
	}
	
	/**
	 * @return
	 * 		the playingfield used by this server.
	 */
	public MultiplayerServerPlayingField getPlayingField() {
		return this.playingField;
	}
	
	/**
	 * Queues up the given message to send it to all clients.
	 * 
	 * @param message
	 * 		the message to queue up for sending.
	 * @param flush
	 * 		if <code>true</code>, the queue is flushed after this call.
	 * 
	 * @return
	 * 		<code>true</code> if the message was queued,
	 * 		<code>false</code> otherwise (e.g. server not running).
	 */
	public boolean queueMessage(FishMessage message, boolean flush) {
		Log.getLogger().log(LogLevel.TRACE, "[Server] Queueing message " + message.getClass().getSimpleName());
		
		ChannelGroup cg = this.allChannels;
		if (cg == null) {
			return false;
		}
		
		if (flush) {
			cg.writeAndFlush(message);
		} else {
			cg.write(message);
		}
		return true;
	}
	
	/**
	 * If this server is running, this method flushes all queued messages
	 * to all connected clients.
	 */
	public void flush() {
		Log.getLogger().log(LogLevel.TRACE, "[Server] Flushing messages");
		
		ChannelGroup cg = this.allChannels;
		
		if (cg != null) {
			cg.flush();
		}
	}
}
