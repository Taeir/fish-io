package com.github.fishio;

import java.util.ArrayList;
import java.util.ListIterator;

import com.github.fishio.listeners.TickListener;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

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
	private Log log = Log.getLogger();

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
		log.log(LogLevel.INFO, "Set Playing field fps to: " + fps + ".");
		
		if (canvas == null) {
			this.canvas = new Canvas(WINDOW_X, WINDOW_Y);
		} else {
			this.canvas = canvas;
		}

		//count enemies
		enemyCount = 0;

		createGameThread();
		log.log(LogLevel.INFO, "Created GameThread().");
		createRenderThread();
		log.log(LogLevel.INFO, "Created RenderThread().");
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
			checkPlayerCollisions();

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

		//Render all drawables, in reverse order
		ListIterator<IDrawable> li = drawables.listIterator(drawables.size());
		while (li.hasPrevious()) {
			li.previous().render(gc);
		}
	}

	/**
	 * Checks for player collisions.
	 */
	public void checkPlayerCollisions() {
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
			log.log(LogLevel.DEBUG, "Removed enemy fish. Enemycount: " + enemyCount + ".");
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
			log.log(LogLevel.DEBUG, "Added enemy fish. Enemycount: " +  enemyCount + ".");
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
			if (hitsWall(m, box)) {
				m.hitWall();
			}

			box.move(m.getSpeedVector());

			if (!m.canMoveThroughWall()) {
				moveWithinScreen(box);
			}
		}
	}

	/**
	 * Check if a the given IMovable hits a wall or not.
	 * 
	 * @param m
	 * 		the movable to check.
	 * @param box
	 * 		the ICollisionArea to check.
	 * 
	 * @return
	 * 		true if the given Movable with the given box hits a wall.
	 */
	private boolean hitsWall(IMovable m, ICollisionArea box) {
		// prevent playerfish from leaving the screen
		if (m instanceof PlayerFish) {	
			if (box.getMaxX() >= WINDOW_X
					|| box.getMinX() <= 0
					|| box.getMaxY() >= WINDOW_Y
					|| box.getMinY() <= 0) {
				return true;
			}
		} else {
			if (box.getMaxX() >= WINDOW_X + 2.0 * box.getWidth()
					|| box.getMinX() <= -(2.0 * box.getWidth()) - 1
					|| box.getMaxY() >= WINDOW_Y + 2.0 * box.getHeight() + 1
					|| box.getMinY() <= -(2.0 * box.getHeight()) - 1) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * If the ICollisionArea is off the screen, it is moved back within
	 * the screen boundaries.
	 * 
	 * @param box
	 * 		the ICollisionArea to move.
	 */
	private void moveWithinScreen(ICollisionArea box) {
		if (box.getMaxX() > WINDOW_X) {
			box.move(new Vec2d(-(box.getMaxX() - WINDOW_X), 0));
		}
		
		if (box.getMinX() < 0) {
			box.move(new Vec2d(-box.getMinX(), 0));
		}
		
		if (box.getMaxY() > WINDOW_Y) {
			box.move(new Vec2d(0, box.getMaxY() - WINDOW_Y));
		}
		
		if (box.getMinY() < 0) {
			box.move(new Vec2d(0, box.getMinY()));
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
				log.log(LogLevel.ERROR, "Error in preTick:\t" + ex.getMessage());
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
		if (renderThread.getStatus() != Status.RUNNING) {
			renderThread.play();
		}
		
		if (!isRunning()) {
			gameThread.play();
		}
	}
	
	/**
	 * Starts the game and waits for it to be running.
	 * 
	 * @throws InterruptedException
	 * 		if the waiting is interrupted.
	 * 
	 * @see #startGame()
	 */
	public void startGameAndWait() throws InterruptedException {
		Duration position = gameThread.getCurrentTime();
		
		startGame();
		
		//If the play head is at the same position, the game thread has not
		//ran a cycle yet, so we wait a bit and check again.
		while (position.equals(gameThread.getCurrentTime())) {
			Thread.sleep(25L);
		}
	}

	/**
	 * Stops (pauses) the game.
	 */
	public void stopGame() {
		gameThread.stop();
		renderThread.stop();
	}
	
	/**
	 * Stops (pauses) the game, and waits for the game to fully stop.
	 * 
	 * @throws InterruptedException
	 * 		if the waiting is interrupted.
	 * 
	 * @see #stopGame()
	 */
	public void stopGameAndWait() throws InterruptedException {
		stopGame();
		
		//While we are still running, we wait.
		while (isRunning()) {
			Thread.sleep(25L);
		}
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
