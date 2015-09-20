package com.github.fishio;

import java.util.ArrayList;

import com.github.fishio.achievements.Observer;
import com.github.fishio.achievements.Subject;
import com.github.fishio.control.SinglePlayerController;
import com.github.fishio.listeners.TickListener;

import javafx.scene.canvas.Canvas;

/**
 * Represents a playing field designed for single player.
 */
public class SinglePlayerPlayingField extends PlayingField implements Subject {

	private PlayerFish player;

	private final SinglePlayerController screenController;
	
	private ArrayList<Observer> observers = new ArrayList<Observer>();

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
					getGameThread().stop();

					//Stop the render thread after the animation is done.
					//This is in order to prevent the rendering from stopping prematurely.
					SinglePlayerPlayingField.this.screenController.showDeathScreen(true,
							event -> getRenderThread().stop());
							
					// Notify the achievement system that the player died.
					notifyObservers();
				}
			}
		});
	}

	/**
	 * Creates and adds the player fish.
	 */
	protected final void addPlayerFish() {
		ICollisionArea ca = new CollisionMask(new Vec2d(640, 335), 60, 30, 
				Preloader.getAlphaDataOrLoad("sprites/fish/playerFish.png"),
				Preloader.getSpriteAlphaRatioOrLoad("sprites/fish/playerFish.png"));
		this.player = new PlayerFish(ca, FishIO.getInstance().getPrimaryStage(), 
				Preloader.getImageOrLoad("sprites/fish/playerFish.png"));

		this.player.scoreProperty().addListener((observable, oldValue, newValue) -> {
			screenController.updateScoreDisplay(newValue.intValue());
		});

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
	
	@Override
	public void attach(Observer observer) {
		observers.add(observer);
		
	}
	
	@Override
	public void detach(Observer observer) {
		observers.remove(observer);
		
	}
	
	@Override
	public void notifyObservers() {
		for (Observer ob : observers) {
			ob.update();
		}
		
	}
}
