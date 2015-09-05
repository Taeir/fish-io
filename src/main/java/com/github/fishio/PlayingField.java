package com.github.fishio;

import java.util.ArrayList;

import com.github.fishio.listeners.TickListener;

import javafx.animation.Animation.Status;
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

	public static final int WINDOW_X = 1280;
	public static final int WINDOW_Y = 670;
	public static final double GAME_TPS = 60;


	private Timeline gameThread;
	private Timeline renderThread;
	private int fps;

	private Canvas canvas;

	private ArrayList<TickListener> gameListeners = new ArrayList<>();
	private ArrayList<TickListener> renderListeners = new ArrayList<>();
	private ArrayList<IDrawable> drawables = new ArrayList<>();
	private ArrayList<IMovable> movables = new ArrayList<>();
	private ArrayList<Entity> entities = new ArrayList<>();


	private Image background;
	private int enemyCount;
	private final int enemyCountMax = 20;

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
			this.canvas = new Canvas(WINDOW_X, WINDOW_Y);
		} else {
			this.canvas = canvas;
		}

		//Adding a (temporary) playerFish
		PlayerFish fish = new PlayerFish(new BoundingBox(100, 150, 200, 200), 
				FishIO.getInstance().getPrimaryStage());
		add(fish);
		registerGameListener(fish);

		//count enemies
		enemyCount = 0;

		createGameThread();
		createRenderThread();
	}

	/**
	 * @return
	 * 		the (target) framerate in frames per second.
	 */
	public int getFPS() {
		return fps;
	}

	/**
	 * Sets the (target) framerate for the render thread in
	 * frames per second.
	 * 
	 * @param fps
	 * 		the new framerate
	 */
	public void setFPS(int fps) {
		this.fps = fps;

		Timeline oldRenderThread = renderThread;
		createRenderThread();

		//If render thread was running, start the new one and stop the old one.
		if (oldRenderThread.getStatus() == Status.RUNNING) {
			renderThread.play();
			oldRenderThread.stop();
		}

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
	 * Creates the rendering thread.
	 */
	protected final void createRenderThread() {
		Duration dur = Duration.millis(1000.0 / getFPS());

		KeyFrame frame = new KeyFrame(dur, event -> {
			//Call listeners pretick
			preListeners(true);

			//Re-render items
			redraw();

			//Call listeners posttick
			postListeners(true);
		}, new KeyValue[0]);

		Timeline tl = new Timeline(frame);
		tl.setCycleCount(-1);

		renderThread = tl;
	}

	/**
	 * Creates the game thread.
	 */
	protected final void createGameThread() {
		Duration dur = Duration.millis(1000.0 / GAME_TPS);

		KeyFrame frame = new KeyFrame(dur, event -> {
			//Call listeners pretick
			preListeners(false);

			//Move all movables
			moveMovables();

			//Add new entities
			addEntities();

			//Check for collisions
			checkCollisions();

			//Cleanup dead entities.
			cleanupDead();

			//Call listeners posttick
			postListeners(false);
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

		//add enemy entities
		while (enemyCount <= enemyCountMax) {
			//TODO add scalible enemyFish
			EnemyFish eFish = LevelBuilder.randomizedFish(null);
			add(eFish);
			enemyCount++;
		}
	}

	/**
	 * Moves Movable items.
	 */
	public void moveMovables() {
		for (IMovable m : movables) {
			m.preMove();
			m.getBoundingBox().move(m.getSpeedVector());
		}
	}

	/**
	 * Calls all listeners pre tick.
	 * 
	 * @param render
	 * 		if true, calls the render listeners.
	 * 		if false, calls the game listeners.
	 */
	public void preListeners(boolean render) {
		ArrayList<TickListener> list;
		if (render) {
			list = renderListeners;
		} else {
			list = gameListeners;
		}

		//TODO Concurrency
		for (TickListener tl : list) {
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
	 * 
	 * @param render
	 * 		if true, calls the render listeners.
	 * 		if false, calls the game listeners.
	 */
	public void postListeners(boolean render) {
		ArrayList<TickListener> list;
		if (render) {
			list = renderListeners;
		} else {
			list = gameListeners;
		}

		//TODO Concurrency
		for (TickListener tl : list) {
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
	 * 		the renderthread.
	 */
	public Timeline getRenderThread() {
		return renderThread;
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
		renderThread.play();
		gameThread.play();
	}

	/**
	 * Stops (pauses) the game.
	 */
	public void stopGame() {
		gameThread.stop();
		renderThread.stop();
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

		if (o instanceof IMovable) {
			movables.add((IMovable) o);
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

		if (o instanceof IMovable) {
			movables.remove(o);
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
		movables.clear();
	}

	/**
	 * Registers the given TickListener for the game thread.
	 * 
	 * @param tl
	 * 		the TickListener to register.
	 */
	public void registerGameListener(TickListener tl) {
		gameListeners.add(tl);
	}

	/**
	 * Unregisters the given TickListener from the game thread.
	 * 
	 * @param tl
	 * 		the TickListener to unregister.
	 */
	public void unregisterGameListener(TickListener tl) {
		gameListeners.remove(tl);
	}

	/**
	 * Registers the given TickListener for the render thread.
	 * 
	 * @param tl
	 * 		the TickListener to register.
	 */
	public void registerRenderListener(TickListener tl) {
		gameListeners.add(tl);
	}

	/**
	 * Unregisters the given TickListener from the render thread.
	 * 
	 * @param tl
	 * 		the TickListener to unregister.
	 */
	public void unregisterRenderListener(TickListener tl) {
		gameListeners.remove(tl);
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
