package com.github.fishio;

import com.github.fishio.logging.ConsoleHandler;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;
import com.github.fishio.logging.TimeStampFormat;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main application class.
 */
public class FishIO extends Application {
	private Stage primaryStage;
	private static FishIO instance;
	
	private Log log = Log.getLogger();
	private ConsoleHandler consoleHandler = new ConsoleHandler(new TimeStampFormat());
	private LogLevel logLevel = LogLevel.DEBUG;

	@Override
	public void start(Stage primaryStage) throws Exception {
		instance = this;
		
		// Initialize Logger
		initiateLogger();
		
		//Preload the screens
		Preloader.preloadScreens();
		//Preload the images
		Preloader.preloadImages();
		
		this.primaryStage = primaryStage;
		
		primaryStage.setTitle("Fish.io");
		primaryStage.setWidth(1280.0);
		primaryStage.setHeight(720.0);
		
		//Load and show the splash screen.
		Preloader.loadAndShowScreen("splashScreen", 0);
		primaryStage.show();
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
	 * Open the main menu when in another screen.
	 */
	public void openMainMenu() {
		Preloader.switchTo("mainMenu", 400);
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
		//Set Handlers with formatters
		log.addHandler(consoleHandler);
		
		//Set Log level
		log.setLogLevel(logLevel);
		
		//Log that logger has been setup
		log.log(LogLevel.INFO, "Logger has initialized. Ready to Start Logging!");
	}
}
