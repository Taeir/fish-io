package com.github.fishio;

import java.util.ArrayList;

import com.github.fishio.game.GameThread;

import javafx.scene.canvas.Canvas;

/**
 * Extended class of playingfield that renders fish on the background of the main manu.
 */
public class MenuField extends PlayingField {
	private GameThread gameThread;
	
	/**
	 * Constructor for the menuField.
	 * @param fps
	 * 		the amount of frames per second of the render thread.
	 * @param canvas
	 * 		the canvas to render on.
	 */
	public MenuField(int fps, Canvas canvas) {
		super(fps, canvas, 0, 1280, 720);
		
		this.gameThread = new GameThread(this);
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

	@Override
	public GameThread getGameThread() {
		return gameThread;
	}

	@Override
	public void centerScreen() {
		getRenderer().setCenter(new Vec2d(640, 360));
	}

}
