package com.github.fishio;

import java.util.ArrayList;

import com.github.fishio.listeners.TickListener;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.util.Duration;

/**
 * Represents the PlayingField.
 */
public class PlayingField {
	private static final int WINDOW_X = 1280;
	private static final int WINDOW_Y = 670;

	private Timeline gameThread;
	private int fps;

	private Canvas canvas;

	private ArrayList<TickListener> listeners = new ArrayList<>();
	private ArrayList<IDrawable> drawables = new ArrayList<>();
	private ArrayList<Entity> entities = new ArrayList<>();

	private Image background;

	/**
	 * @param fps
	 * 		the (target) framerate.
	 */
	public PlayingField(int fps) {
		this(fps, null);
	}
	
	/**
	 * @param fps
	 * 		the (target) framerate.
	 * @param canvas
	 * 		the canvas to use, can be <code>null</code> to create one.
	 */
	public PlayingField(int fps, Canvas canvas) {
		this.fps = fps;

		if (canvas == null) {
			this.canvas = new Canvas(1280, 670);
		} else {
			this.canvas = canvas;
		}
		
		createGameThread();
	}

	/**
	 * @return
	 * 		the (target) framerate in frames per second.
	 */
	public int getFPS() {
		return fps;
	}

	/**
	 * @return
	 * 		the width of the field.
	 */
	public int getWidth() {
		return WINDOW_X;
	}

	/**
	 * @return
	 * 		the height of the field.
	 */
	public int getHeigth() {
		return WINDOW_Y;
	}

	/**
	 * Creates the canvas.
	 * 
	 * @param c
	 * 		the canvas to use. If <code>null</code>, a canvas is created.
	 */
	protected final void createCanvas(Canvas c) {
		if (c == null) {
			canvas = new Canvas(1280, 670);
		} else {
			canvas = c;
		}
	}

	/**
	 * Creates the game thread.
	 */
	protected final void createGameThread() {
		Duration dur = Duration.millis(1000.0 / getFPS());

		KeyFrame frame = new KeyFrame(dur, event -> {
			//Call listeners pretick
			preListeners();

			//Add new entities
			addEntities();

			//Re-render items
			redraw();

			//Check for collisions
			checkCollisions();

			//Cleanup dead entities.
			cleanupDead();

			//Call listeners posttick
			postListeners();
		}, new KeyValue[0]);

		Timeline tl = new Timeline(frame);
		tl.setCycleCount(-1);

		gameThread = tl;
	}

	/**
	 * Called to redraw the screen.
	 */
	public void redraw() {
		GraphicsContext gc = canvas.getGraphicsContext2D();

		//Clear screen
		gc.clearRect(0, 0, WINDOW_X, WINDOW_Y);

		//draw background image
		gc.drawImage(background, 0, 0);

		//Render all drawables
		for (IDrawable d : drawables) {
			d.render(gc);
		}
	}

	/**
	 * Checks for collisions.
	 */
	public void checkCollisions() {
		//TODO
	}

	/**
	 * Cleans up dead entities.
	 */
	public void cleanupDead() {
		ArrayList<Entity> tbr = new ArrayList<Entity>();
		for (Entity e : entities) {
			if (e.isDead()) {
				tbr.add(e);
			}
		}

		for (Entity e : tbr) {
			remove(e);
		}
	}

	/**
	 * Adds new entities.
	 */
	public void addEntities() {
		EnemyFish eFish = LevelBuilder.randomizedFish(null);
		add(eFish);
	}

	/**
	 * Calls all listeners pre tick.
	 */
	public void preListeners() {
		//TODO Concurrency + try catch
		for (TickListener tl : listeners) {
			try {
				tl.preTick();
			} catch (Exception ex) {
				//TODO Handle exception differently
				System.out.println("Error in preTick!");
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Calls all listeners post tick.
	 */
	public void postListeners() {
		//TODO Concurrency + try catch
		for (TickListener tl : listeners) {
			try {
				tl.postTick();
			} catch (Exception ex) {
				//TODO Handle exception differently
				System.out.println("Error in postTick!");
				ex.printStackTrace();
			}
		}
	}

	/**
	 * @return
	 * 		the gamethread.
	 */
	public Timeline getGameThread() {
		return gameThread;
	}

	/**
	 * Starts the game.
	 */
	public void startGame() {
		gameThread.play();
	}

	/**
	 * Stops the game.
	 */
	public void stopGame() {
		gameThread.stop();
		//gameThread.pause();
	}
	
	/**
	 * @return
	 * 		the canvas that is the PlayingField.
	 */
	public Canvas getCanvas() {
		return canvas;
	}

	/**
	 * Adds the given object to this Playing Field.
	 * 
	 * @param o
	 * 		the object to add.
	 */
	public void add(Object o) {
		if (o instanceof IDrawable) {
			drawables.add((IDrawable) o);
		}

		if (o instanceof Entity) {
			entities.add((Entity) o);
		}
	}

	/**
	 * Removes the given object from this playing field.
	 * 
	 * @param o
	 * 		the object to remove.
	 */
	public void remove(Object o) {
		if (o instanceof IDrawable) {
			drawables.remove(o);
		}

		if (o instanceof Entity) {
			entities.remove(o);
		}
	}

	/**
	 * Clear this PlayingField.<br>
	 * <br>
	 * This removes all Entities and Drawables.
	 */
	public void clear() {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		for (Entity e : entities) {
			e.setDead();
		}

		for (IDrawable d : drawables) {
			d.drawDeath(gc);
		}

		entities.clear();
		drawables.clear();
	}

	/**
	 * Registers the given TickListener.
	 * 
	 * @param tl
	 * 		the TickListener to register.
	 */
	public void registerListener(TickListener tl) {
		listeners.add(tl);
	}

	/**
	 * Unregisters the given TickListener.
	 * 
	 * @param tl
	 * 		the TickListener to unregister.
	 */
	public void unregisterListener(TickListener tl) {
		listeners.remove(tl);
	}

	/**
	 * Set the background image of the level.
	 * @param image
	 * 			The background image.
	 */
	public void setBackground(Image image) {
		if (image.isError()) {
			System.err.println("Error loading the new background!\nUsing old one instead");
			return;
		}
		background = image;

	}
}
