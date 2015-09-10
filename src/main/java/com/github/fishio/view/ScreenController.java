package com.github.fishio.view;

import javafx.scene.Scene;

/**
 * Interface ScreenController.
 * Must be implemented by all screencontrollers!
 * 
 * @author Chiel Bruin
 * @since 03-09-2015
 */
public interface ScreenController {
	/**
	 * Called after the screen is loaded from fxml.
	 * 
	 * @param scene
	 * 		The scene corresponding to this screen.
	 */
	void init(Scene scene);
	
	/**
	 * Called right before the screen is switched to.
	 */
	void onSwitchTo();
}
