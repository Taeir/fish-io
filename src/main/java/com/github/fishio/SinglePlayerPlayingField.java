package com.github.fishio;

import java.util.ArrayList;

import com.github.fishio.listeners.TickListener;
import com.github.fishio.view.SinglePlayerController;

import javafx.scene.canvas.Canvas;

/**
 * Represents a playing field designed for single player.
 */
public class SinglePlayerPlayingField extends PlayingField {

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
		
		//Adding the playerFish
		addPlayerFish();
		
		//Checking if the playerFish died
		registerGameListener(new TickListener() {
			@Override
			public void preTick() { }

			@Override
			public void postTick() {
				if (player.isDead()) {
					//Stop the game thread.
					getGameThread().stop();
					
					//Stop the render thread after the animation is done.
					//This is in order to prevent the rendering from stopping prematurely.
					screenController.showDeathScreen(true, event -> getRenderThread().stop());
				}
			}
		});
	}
	
	/**
	 * Creates and adds the player fish.
	 */
	protected final void addPlayerFish() {
		this.player = new PlayerFish(
				new BoundingBox(new Vec2d(640, 335), 30, 15), 
				FishIO.getInstance().getPrimaryStage(),
				Preloader.getImageOrLoad("sprites/fish/playerFish.png"));
		add(this.player);
	}
	
	@Override
	public void clear() {
		super.clear();
		
		//Also add the playerfish again.
		addPlayerFish();
	}

	@Override
	public ArrayList<PlayerFish> getPlayers() {
		ArrayList<PlayerFish> res = new ArrayList<>();
		res.add(player);
		return res;
	}
}
