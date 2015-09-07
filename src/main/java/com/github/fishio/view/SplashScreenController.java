package com.github.fishio.view;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import com.github.fishio.FishIO;

/**
 * The splashScreenController class manages the splash screens.
 * Firstly it shows the company/group name and after that the name of the game. 
 * 
 * @author Chiel Bruin
 * @since 07-09-2015
 */
public class SplashScreenController implements ScreenController {

	// Reference to the main application.
	private FishIO mainApp;
	@FXML
	private VBox company;
	@FXML
	private VBox game;

	private boolean run = true;

	@Override
	public void setMainApp(FishIO mainApp) {
		this.mainApp = mainApp;	

		mainApp.getPrimaryStage().getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				event.consume();
				endSplash();          
			}        
		});

		showSplash(company, new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				event.consume();
				company.setVisible(false);
				showSplash(game, new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						event.consume();
						endSplash();
					}
				},  2000, 3000, 1700);

			}
		},  2000, 3000, 1700);
	}

	/**
	 * Shows an element by fading it in and out.
	 * @param element the element to display
	 * @param finishEvent eventHandler to trigger when finished
	 * @param in duration in milliseconds of the fade-in
	 * @param duration delay in milliseconds before starting the fade-out
	 * @param out duration in milliseconds of the fade-out
	 */
	public void showSplash(Node element, EventHandler<ActionEvent> finishEvent, 
			int in, int duration, int out) {

		FadeTransition fadeIn = new FadeTransition(Duration.millis(in), element);
		FadeTransition fadeOut = new FadeTransition(Duration.millis(out), element);

		fadeIn.setFromValue(0.0);
		fadeIn.setToValue(1.0);
		fadeIn.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				fadeOut.play();
			}
		});		

		fadeIn.play();
		try {
			Thread.sleep(100);	//without this sleep the splash will have a blink
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		element.setVisible(true);

		fadeOut.setFromValue(1.0);
		fadeOut.setToValue(0.0);
		fadeOut.setDelay(Duration.millis(duration));		
		fadeOut.setOnFinished(finishEvent);
	}

	/**
	 * Ends the splash screen and opens the menu.
	 */
	private void endSplash() {
		if (run) { //do only once
			run = false;
			mainApp.loadScreen("view/mainMenu.fxml", true);	
		}
	}

}