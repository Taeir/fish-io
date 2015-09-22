package com.github.fishio;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

import com.github.fishio.gui.Renderer;
import com.github.fishio.listeners.TickListener;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

/**
 * Represents the PlayingField.
 */
public abstract class PlayingField {
	public static final int WINDOW_X = 1280;
	public static final int WINDOW_Y = 670;
	public static final double GAME_TPS = 60;

	private GameRunnable gameRunnable;
	private Renderer renderer;

	private ConcurrentLinkedQueue<TickListener> gameListeners = new ConcurrentLinkedQueue<>();
	private ConcurrentLinkedQueue<TickListener> renderListeners = new ConcurrentLinkedQueue<>();
	private ConcurrentLinkedDeque<IDrawable> drawables = new ConcurrentLinkedDeque<>();
	private ConcurrentLinkedDeque<IDrawable> deadDrawables = new ConcurrentLinkedDeque<>();
	private ConcurrentLinkedQueue<IMovable> movables = new ConcurrentLinkedQueue<>();
	private ConcurrentLinkedQueue<Entity> entities = new ConcurrentLinkedQueue<>();
	private ConcurrentLinkedQueue<ICollidable> collidables = new ConcurrentLinkedQueue<>();
	private Log log = Log.getLogger();

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
		//count enemies
		enemyCount = 0;

		gameRunnable = new GameRunnable(true);
		log.log(LogLevel.INFO, "Created GameThread().");
		
		renderer = new Renderer(this, canvas, fps);
		log.log(LogLevel.INFO, "Created Renderer");
	}

	/**
	 * Gives back the framerate of the playing field.
	 * 
	 * @return the (target) framerate in frames per second.
	 */
	public int getFPS() {
		return renderer.getFps();
	}

	/**
	 * Sets the (target) framerate for the render thread in
	 * frames per second.
	 * 
	 * @param fps
	 * 		the new framerate
	 */
	public void setFPS(int fps) {
		renderer.setFps(fps);
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
	 * Creates a new gamerunnable.
	 */
	protected final void createGameRunnable() {
		gameRunnable = new GameRunnable();
	}

	/**
	 * Checks for player collisions.
	 */
	public void checkPlayerCollisions() {
		//Iterate over the players
		for (PlayerFish player : getPlayers()) {
			//Iterate over the collidables
			for (ICollidable c : collidables) {
				if (player != c && player.doesCollides(c)) {
					player.onCollide(c);
					c.onCollide(player);
				}
			}
		}
	}

	/**
	 * Cleans up dead entities.
	 */
	public void cleanupDead() {
		Iterator<Entity> it = entities.iterator();
		while (it.hasNext()) {
			Entity e = it.next();
			
			if (!e.isDead()) {
				continue;
			}
			
			//Remove from entities list
			it.remove();
			
			//Remove from other lists.
			remove(e);
			
			//Decrease enemy count.
			enemyCount--;
			
			//Log action.
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
		ConcurrentLinkedQueue<TickListener> list;
		if (render) {
			list = renderListeners;
		} else {
			list = gameListeners;
		}

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
		ConcurrentLinkedQueue<TickListener> list;
		if (render) {
			list = renderListeners;
		} else {
			list = gameListeners;
		}

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
	 * Starts rendering (updating the screen).
	 * The game itself will be unaffected by this call.
	 */
	public void startRendering() {
		renderer.startRendering();
	}
	
	/**
	 * Stops rendering (updating the screen).
	 * The game itself will be unaffected by this call.
	 */
	public void stopRendering() {
		renderer.stopRendering();
	}
	
	/**
	 * @return
	 * 		if the render thread is running or not.
	 */
	public boolean isRendering() {
		return renderer.isRendering();
	}
	
	/**
	 * Starts the game thread.<br>
	 * <br>
	 * If the game thread is not running, it is started.<br>
	 * If the game thread is stopping, this method waits for it to stop and
	 * starts it again after that.<br>
	 * Otherwise, this method will have no effect.
	 */
	public void startGameThread() {
		//If already running
		if (isRunning()) {
			//If we have not called stop yet, we can simply return.
			if (!isStopping()) {
				return;
			} else {
				//We have to stop the old game thread.
				try {
					gameRunnable.stopAndWait();
				} catch (InterruptedException ex) {
					log.log(LogLevel.ERROR,
							"Error while starting game thread: interrupted while waiting for gameRunnable to stop.");
				}
			}
		}
		
		createGameRunnable();
		new Thread(gameRunnable).start();
	}
	
	/**
	 * Sets the stop status of the game thread to true.<br>
	 * <br>
	 * If the game thread was running at the time of this call, it will
	 * stop sometime in the future. Otherwise, this method has no effect.
	 * 
	 * @see #stopGameThreadAndWait()
	 */
	public void stopGameThread() {
		gameRunnable.stop();
	}
	
	/**
	 * Sets the stop status of the game thread to true, and waits for the
	 * game thread to stop.<br>
	 * <br>
	 * If the game thread was not running at the time of this call, this 
	 * method has no effect.
	 * 
	 * @throws InterruptedException
	 * 		if we are interrupted while waiting for the game thread to stop.
	 * 
	 * @see #stopGameThread()
	 */
	public void stopGameThreadAndWait() throws InterruptedException {
		gameRunnable.stopAndWait();
	}
	
	/**
	 * @return
	 * 		if the game thread is running or not (stopped / paused)
	 */
	public boolean isRunning() {
		return !gameRunnable.isStopped();
	}
	
	/**
	 * @return
	 * 		<code>true</code> if the game is stopping,
	 * 		<code>false</code> otherwise.
	 */
	public boolean isStopping() {
		return gameRunnable.isStopping();
	}

	/**
	 * Starts the game.<br>
	 * <br>
	 * If the render thread has not started, it is started right before
	 * the game is started.
	 */
	public void startGame() {
		//Start the rendering first
		startRendering();
		
		//Start the game thread after that.
		startGameThread();
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
		startGame();
		
		while (!isRunning()) {
			Thread.sleep(25L);
		}
	}

	/**
	 * Stops (pauses) the game and the rendering.
	 */
	public void stopGame() {
		stopGameThread();
		stopRendering();
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
		//Stop the game and render thread
		stopGame();
		
		//Wait until the game thread has stopped.
		stopGameThreadAndWait();
	}

	/**
	 * Adds the given object to this Playing Field.
	 * 
	 * @param o
	 * 		the object to add.
	 */
	public void add(Object o) {
		if (o instanceof IDrawable) {
			drawables.addFirst((IDrawable) o);
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
		//Add all drawables to the drawDeaths.
		deadDrawables.addAll(drawables);

		for (Entity e : entities) {
			e.setDead();
		}

		entities.clear();
		drawables.clear();
		movables.clear();
		collidables.clear();
		
		enemyCount = 0;
	}
	
	/**
	 * Clears everything but player fish from this PlayingField.
	 */
	public void clearEnemies() {
		for (Entity e : entities) {
			if (e instanceof PlayerFish) {
				continue;
			}
			
			e.setDead();
		}

		for (IDrawable d : drawables) {
			if (d instanceof PlayerFish) {
				continue;
			}
			
			deadDrawables.add(d);
		}
		
		entities.clear();
		drawables.clear();
		movables.clear();
		collidables.clear();
		
		enemyCount = 0;
		
		//Re-add the players
		for (PlayerFish player : getPlayers()) {
			add(player);
		}
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
		renderer.registerListener(tl);
	}

	/**
	 * Unregisters the given TickListener from the render thread.
	 * 
	 * @param tl
	 * 		the TickListener to unregister.
	 */
	public void unregisterRenderListener(TickListener tl) {
		renderer.unregisterListener(tl);
	}
	
	/**
	 * @return
	 * 		the drawables on this playing field.
	 */
	public ConcurrentLinkedDeque<IDrawable> getDrawables() {
		return drawables;
	}
	
	/**
	 * @return
	 * 		the drawables that died last game tick.
	 */
	public ConcurrentLinkedDeque<IDrawable> getDeadDrawables() {
		return deadDrawables;
	}

	/**
	 * Set the background image of the level.
	 * @param image
	 * 			The background image.
	 */
	public void setBackground(Image image) {
		renderer.setBackground(image);
	}
	
	/**
	 * @return
	 * 		<code>true</code> if there is a player fish alive.
	 * 		<code>false</code> otherwise.
	 */
	public boolean isPlayerAlive() {
		for (PlayerFish pf : getPlayers()) {
			if (!pf.isDead()) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Special Runnable class for the GameThread.
	 */
	private class GameRunnable implements Runnable {
		private volatile boolean stop;
		private volatile boolean done;
		
		public GameRunnable() { }
		
		public GameRunnable(boolean fake) {
			this.done = fake;
			this.stop = fake;
		}

		/**
		 * Set the stop status of this GameRunnable to true.
		 */
		public void stop() {
			this.stop = true;
		}
		
		/**
		 * @return
		 * 		true if this game runnable is stopping.
		 */
		public boolean isStopping() {
			return this.stop;
		}
		
		/**
		 * @return
		 * 		true if this game runnable has stopped.
		 */
		public boolean isStopped() {
			return this.done;
		}
		
		/**
		 * Sets the stop status of this GameRunnable to true, and waits
		 * until it has actually stopped.
		 * 
		 * @throws InterruptedException
		 * 		if we are interrupted while waiting.
		 */
		public void stopAndWait() throws InterruptedException {
			//We have already stopped.
			if (isStopped()) {
				return;
			}
			
			//Mark that we want to stop
			stop();
			
			//Wait until we have stopped.
			while (!isStopped()) {
				Thread.sleep(25L);
			}
		}
		
		@Override
		public void run() {
			//If we have already ran before, we throw an exception.
			if (isStopped()) {
				throw new IllegalStateException("This GameRunnable has already been stopped!");
			}
			
			try {
				long start, end;
				double waitTime = 1000.0 / GAME_TPS;
				
				while (!stop) {
					start = System.currentTimeMillis();
	
					//Call listeners pretick
					preListeners(false);
	
					//Move all movables
					moveMovables();
	
					//Add new entities
					addEntities();
	
					//Check for stop again halfway.
					if (stop) {
						break;
					}
					
					//Check for collisions
					checkPlayerCollisions();
	
					//Cleanup dead entities.
					cleanupDead();
	
					//Call listeners posttick
					postListeners(false);
					
					end = System.currentTimeMillis();
					
					//Sleep if we have time to spare.
					long dur = end - start;
					if (dur < waitTime) {
						try {
							Thread.sleep(Math.round(waitTime - dur));
						} catch (InterruptedException ex) { }
					}
				}
			} finally {
				//Reset stopping
				done = true;
			}
		}
	}
}
