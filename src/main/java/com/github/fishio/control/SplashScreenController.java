package com.github.fishio.control;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Animation.Status;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import com.github.fishio.Preloader;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

/**
 * The splashScreenController class manages the splash screens.
 * Firstly it shows the company/group name and after that the name of the game. 
 * 
 * @author Chiel Bruin
 * @since 07-09-2015
 */
public class SplashScreenController implements ScreenController {

	private Log log = Log.getLogger();
	
	@FXML
	private VBox company;
	@FXML
	private VBox game;
	@FXML
	private Label slogan;
	
	private SequentialTransition transition;
	
	private String[] slogans = {"Not even squidding!", "Krilling it!"
		, "This game is brill-iant!", "In cod we trust!", "Dolphinitely a great game!"
		, "Any fin is possible!", "Seariously the best game ever!"
		, "Don't leave it to salmon else!"};

	@Override
	public void init(Scene scene) {
		slogan.setText(slogans[(int) (Math.random() * slogans.length)]);
		
		//If you press any key, we skip the splash.
		scene.setOnKeyPressed(event -> {
			//Consume the event, so that it doesn't get passed along.
			event.consume();
			log.log(LogLevel.INFO, "Player pressed key, skipping splashScreen.");
			endSplash();
		});
		
		//Show the splash for the company.
		SequentialTransition companyFade = createFade(company, null, 2000, 3000, 1700);
		
		//Shows the splash for the game.
		SequentialTransition gameFade = createFade(game, null, 2000, 3000, 1700);
		
		this.transition = new SequentialTransition(companyFade, gameFade);
		this.transition.setOnFinished(event -> {
			endSplash();
		});
	}
	
	@Override
	public void onSwitchTo() {
		this.transition.playFromStart();
	}

	/**
	 * Creates a fade in and fade out for the given Node.
	 * 
	 * @param element
	 * 		the element to display
	 * @param finishEvent
	 * 		eventHandler to trigger when finished
	 * @param in
	 * 		duration in milliseconds of the fade-in
	 * @param duration
	 * 		delay in milliseconds before starting the fade-out
	 * @param out
	 * 		duration in milliseconds of the fade-out
	 * 
	 * @return
	 * 		a {@link SequentialTransition} consisting of a fade-in and fade-out.
	 */
	public SequentialTransition createFade(Node element, EventHandler<ActionEvent> finishEvent, int in, int duration,
			int out) {
		FadeTransition fadeIn = new FadeTransition(Duration.millis(in), element);
		fadeIn.setFromValue(0.0);
		fadeIn.setToValue(1.0);

		FadeTransition fadeOut = new FadeTransition(Duration.millis(out), element);
		fadeOut.setFromValue(1.0);
		fadeOut.setToValue(0.0);
		fadeOut.setDelay(Duration.millis(duration));		
		
		SequentialTransition st = new SequentialTransition(fadeIn, fadeOut);
		if (finishEvent != null) {
			st.setOnFinished(finishEvent);
		}
		return st;
	}

	/**
	 * Ends the splash screen and opens the menu.
	 */
	private void endSplash() {
		//Stop the current animation
		stopTransition();
		
		//Switch to the main menu.
		Preloader.switchTo("mainMenu", 1000);
	}

	/**
	 * Stops the transition, if it is running.
	 */
	public void stopTransition() {
		if (transition.getStatus() == Status.RUNNING) {
			transition.pause();
		}
	}
}