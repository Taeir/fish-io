package com.github.fishio.multiplayer;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javafx.scene.canvas.Canvas;

import com.github.fishio.PlayerFish;
import com.github.fishio.PlayingField;

/**
 * PlayingField for a multiplayer game.
 */
public abstract class MultiplayerPlayingField extends PlayingField {
	private Set<PlayerFish> players = Collections.newSetFromMap(new ConcurrentHashMap<PlayerFish, Boolean>());
	
	/**
	 * Creates a new MultiplayerPlayingField.
	 * 
	 * @param fps
	 * 		the fps of the renderer of this PlayingField.
	 * @param canvas
	 * 		the canvas to render on.
	 */
	public MultiplayerPlayingField(int fps, Canvas canvas) {
		super(fps, canvas, 50);
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
}
