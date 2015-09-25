package com.github.fishio;

import java.util.ArrayList;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.Canvas;

/**
 * Represents a playing field designed for single player.
 */

public class SinglePlayerPlayingField extends PlayingField {

	public static final int START_X = 640;
	public static final int START_Y = 335;
	
	private final SimpleObjectProperty<PlayerFish> player = new SimpleObjectProperty<PlayerFish>();
	private final ArrayList<PlayerFish> players = new ArrayList<PlayerFish>(1);

	/**
	 * Creates the playing field for a single player.
	 * 
	 * @param fps
	 *            the (target) framerate.
	 * @param canvas
	 *            the canvas to use, can be <code>null</code> to create one.
	 */
	public SinglePlayerPlayingField(int fps, Canvas canvas) {
		super(fps, canvas);

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
		setPlayer(new PlayerFish(getStartCollisionArea(),
				FishIO.getInstance().getPrimaryStage(), 
				Preloader.getImageOrLoad("sprites/fish/playerFish.png")));

		add(getPlayer());
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
}
