package com.github.fishio;

import com.github.fishio.listeners.TickListener;
import com.github.fishio.view.SinglePlayerController;

import javafx.scene.canvas.Canvas;

/**
 * Represents a playing field designed for single player.
 */
public class SinglePlayerPlayingField extends PlayingField {

	private SinglePlayerController screenController;
	private PlayerFish player;
	
	/**
	 * @param fps
	 * 		the (target) framerate.
	 * @param canvas
	 * 		the canvas to use, can be <code>null</code> to create one.
	 * @param screenController
	 * 		the screenController on which this playing field is located on.
	 */
	public SinglePlayerPlayingField(int fps, Canvas canvas, SinglePlayerController screenController) {
		super(fps, canvas);
		
		this.screenController = screenController;
		
		//Adding the playerFish
		player = new PlayerFish(new BoundingBox(100, 150, 200, 200), 
				FishIO.getInstance().getPrimaryStage());
		add(player);
		
		//Checking if the playerFish died
		registerGameListener(new TickListener() {
			@Override
			public void preTick() { }

			@Override
			public void postTick() {
				if (player.isDead()) {
					screenController.showDeathScreen(true);
					stopGame();
					clear();
				}
			}
		});
	}

}
