package com.github.fishio;

import java.io.File;
import java.io.IOException;

import com.github.fishio.achievements.AchievementIO;
import com.github.fishio.audio.AudioEngine;
import com.github.fishio.logging.ConsoleHandler;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;
import com.github.fishio.logging.TimeStampFormat;
import com.github.fishio.logging.TxtFileHandler;
import com.github.fishio.multiplayer.client.FishIOClient;
import com.github.fishio.multiplayer.server.FishIOServer;
import com.github.fishio.settings.Settings;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main application class.
 */
public final class FishIO extends Application {
	private Stage primaryStage;
	private static FishIO instance;
	
	private Log log = Log.getLogger();
	private ConsoleHandler consoleHandler = new ConsoleHandler(new TimeStampFormat());
	private TxtFileHandler textFileHandler =
			new TxtFileHandler(new TimeStampFormat(), new File("logs" +  File.separator + "log.txt"));

	private Settings settings = Settings.getInstance();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		
		instance = this;
		
		//Initialize Logger
		initiateLogger();
		
		log.log(LogLevel.INFO, "Starting up Fish.io.");
		
		//Preload the images
		Preloader.preloadImages();
		
		//Preload the screens
		Preloader.preloadScreens();
		
		//Start loading the sprites
		SpriteStore.loadSprites();
		
		//Set settings
		primaryStage.setTitle("Fish.io");
		primaryStage.setWidth(settings.getDouble("SCREEN_WIDTH"));
		primaryStage.setHeight(settings.getDouble("SCREEN_HEIGHT"));
		primaryStage.setMinHeight(480);
		primaryStage.setMinWidth(640);
		
		log.log(LogLevel.DEBUG, "Primary stage set.");
		//Load and show the splash screen.
		Preloader.loadAndShowScreen("splashScreen", 0);
		primaryStage.show();
		
		//track changes in screen size
		primaryStage.heightProperty().addListener((o, old, height) -> 
			settings.setDouble("SCREEN_HEIGHT", height.doubleValue()));
		primaryStage.widthProperty().addListener((o, old, width) ->
			settings.setDouble("SCREEN_WIDTH", width.doubleValue()));
		
		settings.getDoubleProperty("SCREEN_HEIGHT").addListener((o, old, height) -> {
			primaryStage.setHeight(height.doubleValue());
		});
		settings.getDoubleProperty("SCREEN_WIDTH").addListener((o, old, width) -> {
			primaryStage.setWidth(width.doubleValue());
		});

		//Start loading audio and start background music
		AudioEngine.getInstance().getAudioFactory().startLoading();
		AudioEngine.getInstance().startBackgroundMusicWhenLoaded();
		
		HighScore.init();
		AchievementIO.load();
	}
	
	@Override
	public void stop() throws Exception {
		closeApplication();
	}

	/**
	 * Startup method.
	 * @param args
	 * 		program arguments.
	 */
	public static void main(String[] args) {
		launch();
	}

	/**
	 * Closes the program.
	 */
	public void closeApplication() {
		try {
			log.log(LogLevel.INFO, "Game shutting Down.");
			
			//Disconnect the client and stop the server
			FishIOClient.getInstance().disconnect();
			FishIOServer.getInstance().stop();
			
			//Close the window
			Preloader.switchAway();
			this.primaryStage.close();
			
			//Shutdown AudioEngine
			AudioEngine.getInstance().shutdown();
			
			//Save the settings
			settings.save();
			AchievementIO.save();
			HighScore.save();
		} finally {
			//Unregister logger handlers
			log.removeAllHandlers();
			
			//Close the log handler
			try {
				textFileHandler.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method returns the Primary Stage.
	 * 
	 * @return primaryStage
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	/**
	 * Returns the instance parameter of the class.
	 * 
	 * @return the instance of this application.
	 */
	public static FishIO getInstance() {
		return instance;
	}
	
	/**
	 * Set up new logger.
	 */
	protected void initiateLogger() {
		//Remove any Handlers if, for GUI tests.
		log.removeAllHandlers();
		
		//Set Handlers with formatters
		log.addHandler(consoleHandler);
		log.addHandler(textFileHandler);
		
		//Set Log level
		log.setLogLevel(LogLevel.fromInt(settings.getInteger("LOG_LEVEL")));
		
		//Log that logger has been setup
		log.log(LogLevel.INFO, "Logger has initialized. Ready to Start Logging!");
	}
}
