package com.github.fishio.multiplayer.server;

import javafx.beans.property.SimpleObjectProperty;

import com.github.fishio.PlayerFish;
import com.github.fishio.Preloader;
import com.github.fishio.Util;
import com.github.fishio.control.MultiplayerGameController;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;
import com.github.fishio.multiplayer.FishMessage;
import com.github.fishio.multiplayer.RepeatingFishMessageSender;
import com.github.fishio.settings.Settings;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
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
	private static final FishIOServer INSTANCE = new FishIOServer();
	private int port;
	private ChannelGroup allChannels;
	private Channel channel;
	private boolean started;
	private SimpleObjectProperty<MultiplayerServerPlayingField> playingFieldProperty = new SimpleObjectProperty<>();
	
	private FishServerSettingsMessage settings;
	
	private FishIOServer() { }
	
	/**
	 * @return
	 * 		the FishIOServer instance.
	 */
	public static FishIOServer getInstance() {
		return INSTANCE;
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
		
		this.settings = new FishServerSettingsMessage();
		this.settings.setSetting("WIDTH", 10000);
		this.settings.setSetting("HEIGHT", 10000);
		this.settings.setSetting("MAX_ENEMIES", 200);
		this.settings.setSetting("PLAYER_ACCELERATION", PlayerFish.FISH_ACCELERATION);
		this.settings.setSetting("MAX_PLAYER_SPEED", Settings.getInstance().getDouble("MAX_PLAYER_SPEED"));
		
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
			
			//Call onStart
			onStart();
			
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
		//Remove the current playing field.
		MultiplayerServerPlayingField mspf = getPlayingField();
		playingFieldProperty.set(null);
		
		//Stop the old game and clear the field
		mspf.stopGame();
		mspf.clear();
		
		//Log that we have stopped
		Log.getLogger().log(LogLevel.INFO, "[Server] Server stopped");
		
		//Switch to the main menu
		Util.onJavaFX(() -> Preloader.switchTo("mainMenu", 1000));
	}
	
	/**
	 * Called when this server has started (is open to connections).
	 */
	public void onStart() {
		int width = (Integer) settings.getSetting("WIDTH");
		int height = (Integer) settings.getSetting("HEIGHT");
		int maxEnemies = (Integer) settings.getSetting("MAX_ENEMIES");
		
		//Create a new playing field
		MultiplayerGameController controller = Preloader.getControllerOrLoad("multiplayerGameScreen");
		MultiplayerServerPlayingField mspf =
				new MultiplayerServerPlayingField(60, controller.getCanvas(), width, height);
		mspf.getEnemyFishSpawner().setMaxEnemies(maxEnemies);
		
		this.playingFieldProperty.set(mspf);
		
		//Create own player and start the game
		mspf.respawnOwnPlayer();
		mspf.startGame();
	}
	
	/**
	 * @return
	 * 		the playingfield used by this server.
	 */
	public MultiplayerServerPlayingField getPlayingField() {
		return this.playingFieldProperty.get();
	}
	
	/**
	 * @return
	 * 		the playingfield property used by this server.
	 */
	public SimpleObjectProperty<MultiplayerServerPlayingField> getPlayingFieldProperty() {
		return this.playingFieldProperty;
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
	 * 		a ChannelGroupFuture, that can be used to determine if and when
	 * 		the message was actually sent.
	 * 		<code>null</code> is returned if the message could not be queued
	 * 		(e.g. server not running).
	 */
	public ChannelGroupFuture queueMessage(FishMessage message, boolean flush) {
		Log.getLogger().log(LogLevel.TRACE, "[Server] Queueing message " + message.getClass().getSimpleName());
		
		ChannelGroup cg = this.allChannels;
		if (cg == null) {
			return null;
		}
		
		ChannelGroupFuture cgf;
		if (flush) {
			cgf = cg.writeAndFlush(message);
		} else {
			cgf = cg.write(message);
		}
		
		return cgf;
	}
	
	/**
	 * Uses {@link RepeatingFishMessageSender#queueWriteFlushToAvailable(ChannelGroup)}
	 * to send the message (once) to all clients connected, excluding those
	 * still processing the last message.
	 * 
	 * @param sender
	 * 		the RepeatingFishMessageSender to queue.
	 * 
	 * @return
	 * 		<code>true</code> if sending was queued,
	 * 		<code>false</code> if not (e.g. server not running).
	 */
	public boolean queueMessage(RepeatingFishMessageSender sender) {
		ChannelGroup cg = this.allChannels;
		if (cg == null) {
			return false;
		}
		
		sender.queueWriteFlushToAvailable(cg);
		
		return true;
	}
	
	/**
	 * @return
	 * 		the settings of the FishIOServer.
	 */
	public FishServerSettingsMessage getSettings() {
		return this.settings;
	}
}
