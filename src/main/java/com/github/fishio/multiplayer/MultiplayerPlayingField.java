package com.github.fishio.multiplayer;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.Canvas;

import com.github.fishio.ICollisionArea;
import com.github.fishio.PlayerFish;
import com.github.fishio.PlayingField;
import com.github.fishio.Vec2d;

/**
 * PlayingField for a multiplayer game.
 */
public abstract class MultiplayerPlayingField extends PlayingField {
	private Set<PlayerFish> players = Collections.newSetFromMap(new ConcurrentHashMap<PlayerFish, Boolean>());
	
	private ObjectProperty<PlayerFish> playerFishProperty = new SimpleObjectProperty<>();
	
	/**
	 * Creates a new MultiplayerPlayingField.
	 * 
	 * @param fps
	 * 		the fps of the renderer of this PlayingField.
	 * @param canvas
	 * 		the canvas to render on.
	 * @param width
	 * 		the width of the playing field
	 * @param height
	 * 		the height of the playing field
	 */
	public MultiplayerPlayingField(int fps, Canvas canvas, int width, int height) {
		super(fps, canvas, 50, width, height);
	}
	
	/**
	 * @return
	 * 		a property holding the playerfish owned by this client/server.
	 */
	public ObjectProperty<PlayerFish> getOwnPlayerProperty() {
		return this.playerFishProperty;
	}
	
	/**
	 * Sets the player corresponding to this client/server.
	 * 
	 * @param player
	 * 		the player to set.
	 */
	public void setOwnPlayer(PlayerFish player) {
		this.playerFishProperty.set(player);
		
		//Remove our new entity from the list and add a new one.
		//This is to prevent client side problems
		if (getEntities().contains(player)) {
			remove(player);
		}
		
		add(player);
	}
	
	/**
	 * @return
	 * 		the player owned by this client/server.
	 */
	public PlayerFish getOwnPlayer() {
		return this.playerFishProperty.get();
	}

	@Override
	public Set<PlayerFish> getPlayers() {
		return players;
	}
	
	@Override
	public void add(Object o) {
		super.add(o);
		
		if (o instanceof PlayerFish) {
			players.add((PlayerFish) o);
		}
	}
	
	@Override
	public void remove(Object o) {
		super.remove(o);
		
		if (o instanceof PlayerFish) {
			players.remove(o);
		}
	}
	
	@Override
	public void centerScreen() {
		PlayerFish p = getOwnPlayer();
		if (p == null) {
			return;
		}
		ICollisionArea ca = p.getBoundingArea();
		getRenderer().setCenter(new Vec2d(ca.getCenterX(), ca.getCenterY()));		
	}
}
