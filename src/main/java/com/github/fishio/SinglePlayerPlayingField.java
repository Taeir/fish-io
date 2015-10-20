package com.github.fishio;

import java.util.ArrayList;

import com.github.fishio.factories.PowerUpSpawner;
import com.github.fishio.game.GameThread;
import com.github.fishio.logging.LogLevel;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;

/**
 * Represents a playing field designed for single player.
 */

public class SinglePlayerPlayingField extends PlayingField {
	
	private final SimpleObjectProperty<PlayerFish> player = new SimpleObjectProperty<PlayerFish>();
	private final ArrayList<PlayerFish> players = new ArrayList<PlayerFish>(1);
	
	private GameThread gameThread;
	private Scene scene;

	/**
	 * Creates the playing field for a single player.
	 * 
	 * @param fps
	 *      the (target) framerate.
	 * @param canvas
	 *      the canvas to use, can be <code>null</code> to create one.
	 * @param scene
	 *      the scene to use (for registering keylisteners)
	 * @param width
	 * 		the width of the playing field
	 * @param height
	 * 		the height of the playing field
	 */
	public SinglePlayerPlayingField(int fps, Canvas canvas, Scene scene, int width, int height) {
		super(fps, canvas, 50, width, height);
		
		this.scene = scene;
		
		gameThread = new GameThread(this);
		logger.log(LogLevel.INFO, "Created GameThread");

		//Add playerFish listeners (has to be the first method called in the constructor.)
		addPlayerFishListeners();
		
		//Adding the playerFish
		addPlayerFish();
	}
	
	/**
	 * Adds required listeners to the {@link #playerProperty()}. 
	 */
	protected final void addPlayerFishListeners() {
		playerProperty().addListener((observable, oldValue, newValue) -> {
			//If the player changes, update the player list.
			synchronized (this.players) {
				if (this.players.isEmpty()) {
					this.players.add(newValue);
				} else {
					this.players.set(0, newValue);
				}
			}
		});
		
		new PowerUpSpawner(this);
	}

	/**
	 * Creates and adds the player fish.
	 */
	protected final void addPlayerFish() {
		setPlayer(new PlayerFish(getStartCollisionMask(), scene,
				Preloader.getImageOrLoad("sprites/fish/playerFish.png")));

		add(getPlayer());
	}
	
	@Override
	public GameThread getGameThread() {
		return gameThread;
	}
	
	/**
	 * @return the starting collision mask of a playerfish.
	 */
	public CollisionMask getStartCollisionMask() {
		return new CollisionMask(new Vec2d(getWidth() / 2D, getHeight() / 2D), 60, 30, 
				Preloader.getAlphaDataOrLoad("sprites/fish/playerFish.png"),
				Preloader.getSpriteAlphaRatioOrLoad("sprites/fish/playerFish.png"));
	}
	
	/**
	 * @return
	 * 		the player property. Can be used to attach listeners to.
	 */
	public SimpleObjectProperty<PlayerFish> playerProperty() {
		return this.player;
	}
	
	/**
	 * @return
	 * 		the PlayerFish in this SinglePlayer game.
	 */
	public PlayerFish getPlayer() {
		return this.player.get();
	}
	
	/**
	 * Replaces the old PlayerFish in this SinglePlayer Game with a new
	 * one.
	 * 
	 * @param player
	 * 		the new PlayerFish to use.
	 */
	public void setPlayer(PlayerFish player) {
		this.player.set(player);
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

	@Override
	public void centerScreen() {
		ICollisionArea ba = player.get().getBoundingArea();
		getRenderer().setCenter(new Vec2d(ba.getCenterX(), ba.getCenterY()));
	}
}
