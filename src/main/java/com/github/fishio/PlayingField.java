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
public abstract class PlayingField {

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
	private ArrayList<ICollidable> collidables = new ArrayList<>();

	private Image background;
	private int enemyCount;
	private static final int MAX_ENEMY_COUNT = 10;

	/**
	 * Creates the playing field with a set framerate.
	 * 
	 * @param fps
	 *            the (target) framerate.
	 */
	public PlayingField(int fps) {
		this(fps, null);
	}

	/**
	 * Creates the playing field with a set framerate and canvas.
	 * 
	 * @param fps
	 *            the (target) framerate.
	 * @param canvas
	 *            the canvas to use, can be <code>null</code> to create one.
	 */
	public PlayingField(int fps, Canvas canvas) {
		this.fps = fps;

		if (canvas == null) {
			this.canvas = new Canvas(WINDOW_X, WINDOW_Y);
		} else {
			this.canvas = canvas;
		}

		//count enemies
		enemyCount = 0;

		createGameThread();
		createRenderThread();
	}

	/**
	 * Gives back the framerate of the playing field.
	 * 
	 * @return the (target) framerate in frames per second.
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
	 * Gives back the width of the field.
	 * 
	 * @return the width of the field.
	 */
	public int getWidth() {
		return WINDOW_X;
	}

	/**
	 * Gives back the height of the field.
	 * 
	 * @return the height of the field.
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
	 * Only needed for the players on movables
	 */
	public void checkCollisions() {
		for (int i = 0; i < getPlayers().size(); i++) {
			for (int j = 0; j < collidables.size(); j++) {
				ICollidable c1 = getPlayers().get(i);
				ICollidable c2 = collidables.get(j);
				if (c1 != c2 && c1.doesCollides(c2)) {
					c1.onCollide(c2);
					c2.onCollide(c1);
				}
			}
		}
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
			enemyCount--;
		}
	}

	/**
	 * Adds new entities.
	 */
	public void addEntities() {

		//add enemy entities
		while (enemyCount < MAX_ENEMY_COUNT) {
			//TODO add scalible enemyFish
			EnemyFish eFish = LevelBuilder.randomizedFish(getPlayers().get(0).getBoundingArea());
			add(eFish);
			enemyCount++;
		}
	}

	/**
	 * Gives back the different players in the field.
	 * 
	 * @return all the players in this field.
	 */
	public abstract ArrayList<PlayerFish> getPlayers();

	/**
	 * Moves Movable items.
	 */
	public void moveMovables() {
		for (IMovable m : movables) {
			m.preMove();
			
			ICollisionArea box = m.getBoundingArea();
			double maxx = Math.max(box.getTopRight().x, box.getBottomRight().x);
			double minx = Math.min(box.getTopLeft().x, box.getBottomLeft().x);
			double maxy = Math.max(box.getBottomLeft().y, box.getBottomRight().y);
			double miny = Math.min(box.getTopLeft().y, box.getTopRight().y);
			if (m instanceof PlayerFish) {	// prevent playerfish from leaving the screen
				if (maxx >= WINDOW_X
						|| minx <= 0
						|| maxy >= WINDOW_Y
						|| miny <= 0) {
					m.hitWall();
				}
			} else {
				if (maxx >= WINDOW_X + 2.0 * box.getWidth()
						|| minx <= -1 - 2.0 * box.getWidth()
						|| maxy >= WINDOW_Y + 2.0 * box.getHeight() + 1
						|| miny <= 0 - 2.0 * box.getHeight() - 1) {
					m.hitWall();
				}
			}

			box.move(m.getSpeedVector());

			if (!m.canMoveThroughWall()) {

				if (maxx > WINDOW_X) {
					box.move(new Vec2d(-(maxx - WINDOW_X), 0));
				} if (minx < 0) {
					box.move(new Vec2d(-minx, 0));
				} if (maxy > WINDOW_Y) {
					box.move(new Vec2d(0, maxy - WINDOW_Y));
				} if (miny < 0) {
					box.move(new Vec2d(0, miny));
				}

			}
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
				System.err.println("Error in preTick!");
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
				System.err.println("Error in postTick!");
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Gives back the renderthread.
	 * 
	 * @return the renderthread.
	 */
	public Timeline getRenderThread() {
		return renderThread;
	}

	/**
	 * Returns the gamethread.
	 * 
	 * @return the gamethread.
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
	 * 		if the game is running or not (stopped / paused)
	 */
	public boolean isRunning() {
		return gameThread.getStatus() == Status.RUNNING;
	}

	/**
	 * Gives back the canvas of the Playing Field.
	 * 
	 * @return the canvas that is the PlayingField.
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

		if (o instanceof ICollidable) {
			collidables.add((ICollidable) o);
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
		
		if (o instanceof ICollidable) {
			collidables.remove(o);
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
		collidables.clear();
		
		enemyCount = 0;
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
