package com.github.fishio;

import java.util.ArrayList;

import com.github.fishio.control.SinglePlayerController;
import com.github.fishio.listeners.TickListener;

import javafx.scene.canvas.Canvas;

/**
 * Represents a playing field designed for single player.
 */
public class SinglePlayerPlayingField extends PlayingField {
	public static final int START_X = 640;
	public static final int START_Y = 335;
	
	private PlayerFish player;
	private final ArrayList<PlayerFish> players = new ArrayList<PlayerFish>(1);
	private final SinglePlayerController screenController;

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

		this.screenController = screenController;

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
					stopGameThread();

					//Stop the render thread after the animation is done.
					//This is in order to prevent the rendering from stopping prematurely.
					SinglePlayerPlayingField.this.screenController.showDeathScreen(true, event -> stopRendering());
				}
			}
		});
	}

	/**
	 * Creates and adds the player fish.
	 */
	protected final void addPlayerFish() {
		ICollisionArea ca = getStartCollisionArea();
		this.player = new PlayerFish(ca, FishIO.getInstance().getPrimaryStage(), 
				Preloader.getImageOrLoad("sprites/fish/playerFish.png"));

		this.player.scoreProperty().addListener((observable, oldValue, newValue) -> {
			screenController.updateScoreDisplay(newValue.intValue());
		});
		
		//Listen for changes in the lives.
		this.player.livesProperty().addListener((observable, oldValue, newValue) -> {
			//Update lives display
			screenController.updateLivesDisplay(newValue.intValue());
			
			//Death is handled elsewhere
			if (newValue.intValue() == 0) {
				return;
			}
			
			//If we lost a life
			if (newValue.intValue() < oldValue.intValue()) {
				stopGame();
				screenController.showDeathScreen(true, null);
			}
		});
		
		if (this.players.isEmpty()) {
			this.players.add(this.player);
		} else {
			this.players.set(0, this.player);
		}

		add(this.player);
	}
	
	/**
	 * @return
	 * 		the starting collision area of a playerfish.
	 */
	public ICollisionArea getStartCollisionArea() {
		return new CollisionMask(new Vec2d(START_X, START_Y), 60, 30, 
				Preloader.getAlphaDataOrLoad("sprites/fish/playerFish.png"),
				Preloader.getSpriteAlphaRatioOrLoad("sprites/fish/playerFish.png"));
	}

	@Override
	public void clear() {
		super.clear();

		//Also add the playerfish again.
		addPlayerFish();
	}

	@Override
	public ArrayList<PlayerFish> getPlayers() {
		return players;
	}
}
