package com.github.fishio.control;

import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

import javafx.scene.Scene;

/**
 * A test controller that only logs the methods that are called.
 */
public class TestController implements ScreenController {
	private Log log = Log.getLogger();

	@Override
	public void init(Scene scene) {
		log.log(LogLevel.INFO, "Controller loaded");		
	}

	@Override
	public void onSwitchTo() {
		log.log(LogLevel.INFO, "Controller switched to");
	}
	
	@Override
	public void onSwitchAway() {
		log.log(LogLevel.INFO, "Controller switched away");
	}

}
