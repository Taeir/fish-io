package com.github.fishio.view;

import com.github.fishio.FishIO;

/**
 * Interface ScreenController.
 * Must be implemented by all screencontrollers!
 * 
 * @author Chiel Bruin
 * @since 03-09-2015
 */
public interface ScreenController {
	/**
	 * Set the mainApp reference of the screencontroller.
	 * 
	 * @param mainApp
	 * 			The reference to the main app
	 */
	void setMainApp(FishIO mainApp);
}
