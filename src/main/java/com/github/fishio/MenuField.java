package com.github.fishio;

import java.util.ArrayList;

import javafx.scene.canvas.Canvas;

/**
 * Extended class of playingfield that renders fish on the background of the main manu.
 */
public class MenuField extends PlayingField {

	/**
	 * Constructor for the menuField.
	 * @param fps
	 * 		the amount of frames per second of the render thread.
	 * @param canvas
	 * 		the canvas to render on.
	 */
	public MenuField(int fps, Canvas canvas) {
		super(fps, canvas, 0);
		}

	@Override
	public ArrayList<PlayerFish> getPlayers() {
		return null;
	}
	
	@Override
	public void checkPlayerCollisions() { }
	
	@Override
	public void addEntities() {

		//add enemy entities
		while (getEntities().size()  < 5) {
			EnemyFish eFish = EnemyFishFactory.randomizedFish(new BoundingBox(null, 60, 60));
			add(eFish);
		}
	}

}
