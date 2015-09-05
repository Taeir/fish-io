package com.github.fishio;

import java.io.IOException;
import com.github.fishio.view.ScreenController;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Main application class.
 */
public class FishIO extends Application {
	private Stage primaryStage;
	private static FishIO instance;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		instance = this;
		
		primaryStage.setTitle("Fish.io");

		loadScreen("view/mainMenu.fxml", false);
		primaryStage.setWidth(1280.0);
		primaryStage.setHeight(720.0);
		primaryStage.show();

	}

	/**
	 * Load a screen from a fxml-file. 
	 * This screen will replace the current screen
	 * 
	 * @param file
	 * 			Filepath of the fxml file.
	 * @param fadeIn
	 * 			If true, fade in the new screen, else just show it.
	 */
	public void loadScreen(String file, boolean fadeIn) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(file));

		try {
			Pane rootLayout = (Pane) loader.load();
			ScreenController controller = ((ScreenController) loader.getController());
			if (controller == null) {
				System.err.println("Screen controller not found for " + file);
				return;
			}
			controller.setMainApp(this);

			if (fadeIn) {
				FadeTransition fade = new FadeTransition(Duration.millis(400), rootLayout);
				fade.setFromValue(0.3);
				fade.setToValue(1.0);
				fade.play();
			}		    

			Scene scene = new Scene(rootLayout);

			primaryStage.setScene(scene);
		} catch (IOException e) {
			System.err.println("Error loading screen:" + file);
			e.printStackTrace();
		}

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
	 * Close the program.    
	 */
	public void closeApplication() {
		this.primaryStage.close();
	}

	/**
	 * @return primaryStage
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	/**
	 * Open the main menu when in another screen.
	 */
	public void openMainMenu() {
		loadScreen("view/mainMenu.fxml", true);
	}
	
	/**
	 * @return the instance of this application.
	 */
	public static FishIO getInstance() {
		return instance;
	}
}
