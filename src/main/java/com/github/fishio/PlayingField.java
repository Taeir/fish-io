package com.github.fishio;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

import javafx.beans.value.ObservableDoubleValue;
import javafx.scene.canvas.Canvas;

import com.github.fishio.behaviours.IMoveBehaviour;
import com.github.fishio.game.GameThread;
import com.github.fishio.gui.Renderer;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;
import com.github.fishio.settings.Settings;

/**
 * Represents the PlayingField.
 */
public abstract class PlayingField {

	protected Log logger = Log.getLogger();
	
	private Renderer renderer;

	private ConcurrentLinkedDeque<IDrawable> drawables = new ConcurrentLinkedDeque<>();
	private ConcurrentLinkedDeque<IDrawable> deadDrawables = new ConcurrentLinkedDeque<>();
	private ConcurrentLinkedQueue<Entity> entities = new ConcurrentLinkedQueue<>();
	private ConcurrentLinkedQueue<ICollidable> collidables = new ConcurrentLinkedQueue<>();
	
	
	private ObservableDoubleValue widthProperty;
	private ObservableDoubleValue heightProperty;

	private int enemyCount;
	public static final int MAX_ENEMY_COUNT = 10;

	/**
	 * Creates the playing field with a set framerate and canvas.
	 * 
	 * @param fps
	 *            the (target) framerate.
	 * @param canvas
	 *            the canvas to use, can be <code>null</code> to create one.
	 */
	public PlayingField(int fps, Canvas canvas) {
		//Get width and height properties.
		widthProperty = Settings.getInstance().getDoubleProperty("SCREEN_WIDTH");
		heightProperty = Settings.getInstance().getDoubleProperty("SCREEN_HEIGHT").subtract(50.0);
		
		//count enemies
		enemyCount = 0;
		
		renderer = new Renderer(this, canvas, fps);
		logger.log(LogLevel.INFO, "Created Renderer");
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
	public double getWidth() {
		return widthProperty.doubleValue();
	}

	/**
	 * Gives back the height of the field.
	 * 
	 * @return the height of the field.
	 */
	public double getHeight() {
		return heightProperty.doubleValue();
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
			if (e instanceof EnemyFish) {
				enemyCount--;
			}
			
			//Log action.
			logger.log(LogLevel.DEBUG, "Removed entity. Enemycount: " + enemyCount + ".");
		}
	}

	/**
	 * Adds new entities.
	 */
	public void addEntities() {

		//add enemy entities
		while (enemyCount < MAX_ENEMY_COUNT) {
			//TODO add scalible enemyFish
			EnemyFish eFish = EnemyFishFactory.randomizedFish(getPlayers());
			add(eFish);
			
			enemyCount++;
			logger.log(LogLevel.DEBUG, "Added enemy fish. Enemycount: " +  enemyCount + ".");
		}
	}

	/**
	 * Gives back the different players in the field.
	 * 
	 * @return all the players in this field.
	 */
	public abstract Collection<PlayerFish> getPlayers();
	
	/**
	 * @return
	 * 		All the entities in this field.
	 */
	public List<Entity> getEntitiesList() {
		return new ArrayList<Entity>(entities);
	}
	
	/**
	 * @return
	 * 		the entities queue used by this PlayingField.
	 */
	public Queue<Entity> getEntities() {
		return entities;
	}

	/**
	 * Moves Movable items.
	 */
	public void moveMovables() {
		for (Entity e : entities) {
			IMoveBehaviour b = e.getBehaviour();
			b.preMove();
			
			ICollisionArea box = e.getBoundingArea();
			if (hitsWall(e, box)) {
				e.hitWall();
			}

			box.move(b.getSpeedVector());

			if (!e.canMoveThroughWall()) {
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
	private boolean hitsWall(Entity e, ICollisionArea box) {
		// prevent playerfish from leaving the screen
		if (e instanceof PlayerFish) {
			return box.isOutside(0, 0, getWidth(), getHeight());
		} else {
			double dw = 2.0 * box.getWidth() + 1;
			double dh = 2.0 * box.getHeight() + 1;
			return box.isOutside(-dw, -dh, getWidth() + dw, getHeight() + dh);
		}
	}

	/**
	 * If the ICollisionArea is off the screen, it is moved back within
	 * the screen boundaries.
	 * 
	 * @param box
	 * 		the ICollisionArea to move.
	 */
	private void moveWithinScreen(ICollisionArea box) {
		if (box.isOutside(0, 0, getWidth(), getHeight())) {			
			if (box.getMaxX() > getWidth()) {
				box.move(new Vec2d(-(box.getMaxX() - getWidth()), 0));
			}
			
			if (box.getMinX() < 0) {
				box.move(new Vec2d(-box.getMinX(), 0));
			}
			
			if (box.getMaxY() > getHeight()) {
				box.move(new Vec2d(0, box.getMaxY() - getHeight()));
			}
			
			if (box.getMinY() < 0) {
				box.move(new Vec2d(0, box.getMinY()));
			}
		}
	}
	
	/**
	 * @return
	 * 		the Renderer for this PlayingField.
	 */
	public Renderer getRenderer() {
		return renderer;
	}
	
	/**
	 * @return
	 * 		the GameThread for this PlayingField.
	 */
	public abstract GameThread getGameThread();

	/**
	 * Starts the game.<br>
	 * <br>
	 * If the render thread has not started, it is started right before
	 * the game is started.
	 */
	public void startGame() {
		//Start the rendering first
		getRenderer().startRendering();
		
		//Start the game thread after that.
		getGameThread().start();
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
		getRenderer().startRendering();
		
		getGameThread().startAndWait();
	}

	/**
	 * Stops (pauses) the game and the rendering.
	 */
	public void stopGame() {
		getGameThread().stop();
		getRenderer().stopRendering();
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
		getGameThread().stopAndWait();
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
			e.kill();
		}

		entities.clear();
		drawables.clear();
		collidables.clear();
		
		enemyCount = 0;
	}
	
	/**
	 * Clears everything but player fish from this PlayingField.
	 */
	public void clearEnemies() {
		Iterator<Entity> it = entities.iterator();
		while (it.hasNext()) {
			Entity e = it.next();
			//Skip playerfish
			if (e instanceof PlayerFish) {
				continue;
			}
			
			//Kill and remove the entity
			e.kill();
			it.remove();
		}
		
		Iterator<IDrawable> it2 = drawables.iterator();
		while (it2.hasNext()) {
			IDrawable d = it2.next();
			
			//Skip playerfish
			if (d instanceof PlayerFish) {
				continue;
			}
			
			//Add to deadDrawables and remove
			deadDrawables.add(d);
			it2.remove();
		}

		//Remove all non playerfish from collidables
		collidables.removeIf((c) -> !(c instanceof PlayerFish));
		
		enemyCount = 0;
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
	
}
