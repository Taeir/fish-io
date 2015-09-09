package com.github.fishio;

import java.util.ArrayList;

import com.github.fishio.listeners.TickListener;
import com.github.fishio.view.SinglePlayerController;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

/**
 * Represents a playing field designed for single player.
 */
public class SinglePlayerPlayingField extends PlayingField {

	private PlayerFish player;
	
	/**
	 * Creates the playing field for a single player.
	 * 
	 * @param fps
	 *            the (target) framerate.
	 * @param canvas
	 *            the canvas to use, can be <code>null</code> to create one.
	 * @param screenController
	 *            the screenController on which this playing field is located
	 *            on.
	 */
	public SinglePlayerPlayingField(int fps, Canvas canvas, SinglePlayerController screenController) {
		super(fps, canvas);
		
		//Adding the playerFish
		player = new PlayerFish(new BoundingBox(new Vec2d(640, 335), 30, 15), 
				FishIO.getInstance().getPrimaryStage(), new Image("sprites/fish/playerFish.png"));
		add(player);
	
		player.scoreProperty().addListener((observable, oldValue, newValue) -> {
			screenController.updateScoreDisplay(newValue.intValue());
		});
		
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

	@Override
	public ArrayList<PlayerFish> getPlayers() {
		ArrayList<PlayerFish> res = new ArrayList<>();
		res.add(player);
		return res;
	}

}
