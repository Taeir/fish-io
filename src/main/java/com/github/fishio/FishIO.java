package com.github.fishio;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main application class.
 */
public class FishIO extends Application {
	private Stage primaryStage;
	private static FishIO instance;

	@Override
	public void start(Stage primaryStage) throws Exception {
		instance = this;
		
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
}
