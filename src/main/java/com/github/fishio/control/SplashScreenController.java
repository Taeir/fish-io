package com.github.fishio.control;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import com.github.fishio.Preloader;

/**
 * The splashScreenController class manages the splash screens.
 * Firstly it shows the company/group name and after that the name of the game. 
 * 
 * @author Chiel Bruin
 * @since 07-09-2015
 */
public class SplashScreenController implements ScreenController {

	@FXML
	private VBox company;
	@FXML
	private VBox game;
	@FXML
	private Label slogan;
	
	private String[] slogans = {"Not even squidding!", "Krilling it!"
		, "This game is brill-iant!", "In cod we trust!", "Dolphinitely a great game!"
		, "Any fin is possible!", "Seariously the best game ever!"
		, "Don't leave it to salmon else!"};

	private boolean run;

	@Override
	public void init(Scene scene) {
		slogan.setText(slogans[(int) (Math.random() * slogans.length)]);
		scene.setOnKeyPressed(event -> {
			event.consume();
			endSplash();
		});
	}
	
	@Override
	public void onSwitchTo() {
		//Show the splash for the company.
		showSplash(company, event -> {
			//Consume the event, so that it doesn't get passed along.
			event.consume();
			
			company.setVisible(false);
			
			//Show the splash for the game screen.
			showSplash(game, event2 -> {
				event2.consume();
				endSplash();
			}, 2000, 3000, 1700);
		}, 2000, 3000, 1700);
	}

	/**
	 * Shows an element by fading it in and out.
	 * @param element the element to display
	 * @param finishEvent eventHandler to trigger when finished
	 * @param in duration in milliseconds of the fade-in
	 * @param duration delay in milliseconds before starting the fade-out
	 * @param out duration in milliseconds of the fade-out
	 */
	public void showSplash(Node element, EventHandler<ActionEvent> finishEvent, int in, int duration, int out) {
		FadeTransition fadeOut = new FadeTransition(Duration.millis(out), element);
		fadeOut.setFromValue(1.0);
		fadeOut.setToValue(0.0);
		fadeOut.setDelay(Duration.millis(duration));		
		fadeOut.setOnFinished(finishEvent);
		
		FadeTransition fadeIn = new FadeTransition(Duration.millis(in), element);
		fadeIn.setFromValue(0.0);
		fadeIn.setToValue(1.0);
		fadeIn.setOnFinished(event -> fadeOut.play());

		//Start the fade in. It will automatically call the fadeout when it is done.
		fadeIn.play();
	}

	/**
	 * Ends the splash screen and opens the menu.
	 */
	private void endSplash() {
		if (!run) { //do only once
			run = true;
			Preloader.switchTo("mainMenu", 1000);
		}
	}
	
	/**
	 * Used by the tests, allows the animation to be ran again.
	 */
	public void allowRerun() {
		run = false;
	}

}