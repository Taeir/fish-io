package com.github.fishio;


import java.io.File;
import java.io.IOException;

import com.github.fishio.audio.AudioEngine;
import com.github.fishio.logging.ConsoleHandler;
import com.github.fishio.logging.TxtFileHandler;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;
import com.github.fishio.logging.TimeStampFormat;
import com.github.fishio.settings.Settings;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main application class.
 */
public class FishIO extends Application {
	private Stage primaryStage;
	private static FishIO instance;
	
	private Log log = Log.getLogger();
	private Settings settings = Settings.getInstance();
	private ConsoleHandler consoleHandler = new ConsoleHandler(new TimeStampFormat());
	private TxtFileHandler textFileHandler =
			new TxtFileHandler(new TimeStampFormat(), new File("logs" +  File.separator + "log.txt"));

	@Override
	public void start(Stage primaryStage) throws Exception {
		instance = this;
		
		//Initialize Logger
		initiateLogger();
		
		log.log(LogLevel.INFO, "Starting up Fish.io.");
		//Preload the screens
		Preloader.preloadScreens();
		log.log(LogLevel.DEBUG, "Preloaded the screens.");
		//Preload the images
		Preloader.preloadImages();
		log.log(LogLevel.DEBUG, "Preloaded the images.");
		
		this.primaryStage = primaryStage;
		
		primaryStage.setTitle("Fish.io");
		primaryStage.setWidth(settings.getDouble("SCREEN_WIDTH"));
		primaryStage.setHeight(settings.getDouble("SCREEN_HEIGHT"));
		
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

		//Start background music
		AudioEngine.getInstance().startBackgroundMusicWhenLoaded();
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
		log.log(LogLevel.INFO, "Game shutting Down.");
		settings.save();
		
		//Shutdown AudioEngine
		AudioEngine.getInstance().shutdown();
		try {
			textFileHandler.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.primaryStage.close();
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
	protected final void initiateLogger() {
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
