package com.github.fishio;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import javafx.scene.canvas.Canvas;

import com.github.fishio.behaviours.IMoveBehaviour;
import com.github.fishio.factories.EnemyFishSpawner;
import com.github.fishio.game.GameThread;
import com.github.fishio.gui.Renderer;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

/**
 * Represents the PlayingField.
 */
public abstract class PlayingField {
	private static final int ENEMY_COUNT = 10;
	
	protected Log logger = Log.getLogger();
	
	private Renderer renderer;

	private ConcurrentLinkedDeque<IDrawable> drawables = new ConcurrentLinkedDeque<>();
	private Set<Entity> entities = Collections.newSetFromMap(new ConcurrentHashMap<Entity, Boolean>());
	private Set<ICollidable> collidables = Collections.newSetFromMap(new ConcurrentHashMap<ICollidable, Boolean>());

	private int width;
	private int height;
	private EnemyFishSpawner enemyFishSpawner;

	/**
	 * Creates the playing field with a set framerate and canvas.
	 * 
	 * @param fps
	 *            the (target) framerate.
	 * @param canvas
	 *            the canvas to use, can be <code>null</code> to create one.
	 * @param yBorder
	 * 			  the vertical border to be applied.
	 * @param width
	 * 		the width of the playing field
	 * @param height
	 * 		the height of the playing field
	 */
	public PlayingField(int fps, Canvas canvas, int yBorder, int width, int height) {
		this.height = height;
		this.width = width;
		
		//Create the enemy fish spawner
		enemyFishSpawner = new EnemyFishSpawner(this, ENEMY_COUNT);
		
		renderer = new Renderer(this, canvas, fps, yBorder);
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
	public int getWidth() {
		return width;
	}

	/**
	 * Gives back the height of the field.
	 * 
	 * @return the height of the field.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Checks for player collisions.
	 */
	public void checkPlayerCollisions() {
		//Iterate over the players
		for (PlayerFish player : getPlayers()) {
			//Get collidables parallel.
			collidables.parallelStream()
			
			//We only want the collidables we actually collide with
			.filter(collidable -> player != collidable && player.doesCollides(collidable))
			
			//We want to do the for each sequentially, otherwise we get parallelism issues
			.sequential()
			
			//Iterate over the elements
			.forEach(collidable -> {
				player.onCollide(collidable);
				collidable.onCollide(player);
			});
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
			
			//Log action.
			logger.log(LogLevel.DEBUG, "Removed entity");
		}
	}

	/**
	 * Adds new entities.
	 */
	public void addEntities() {
		enemyFishSpawner.spawnEnemyFish();
	}
	
	/**
	 * @return
	 * 		the EnemyFishSpawner this PlayingField is using.
	 */
	public EnemyFishSpawner getEnemyFishSpawner() {
		return enemyFishSpawner;
	}
	
	/**
	 * Sets the EnemyFishSpawner used by this PlayingField.
	 * 
	 * @param spawner
	 * 		the new enemyFishSpawner to use.
	 */
	public void setEnemyFishSpawner(EnemyFishSpawner spawner) {
		this.enemyFishSpawner = spawner;
	}

	/**
	 * Gives back the different players in the field.
	 * 
	 * @return all the players in this field.
	 */
	public abstract Collection<PlayerFish> getPlayers();
	
	/**
	 * @return
	 * 		the entities queue used by this PlayingField.
	 */
	public Set<Entity> getEntities() {
		return entities;
	}

	/**
	 * @return
	 * 		the collidables in this field
	 */
	public Set<ICollidable> getCollidables() {
		return collidables;
	}
	
	/**
	 * Moves Movable items.
	 */
	public void moveMovables() {
		entities.parallelStream().forEach(e -> moveEntity(e));
	}

	/**
	 * Moves the given entity.
	 * 
	 * @param entity
	 * 		the entity to move.
	 */
	public void moveEntity(Entity entity) {
		IMoveBehaviour b = entity.getBehaviour();
		b.preMove();
		
		CollisionMask mask = entity.getBoundingArea();
		if (hitsWall(entity, mask)) {
			entity.hitWall();
		}

		mask.move(b.getSpeedVector());

		if (!entity.canMoveThroughWall()) {
			moveWithinScreen(mask);
		}
	}

	/**
	 * Center the screen at the correct location.
	 * This location should be the player, but can be something different.
	 */
	public abstract void centerScreen();

	/**
	 * Check if a the given IMovable hits a wall or not.
	 * 
	 * @param e
	 * 		the entity to check.
	 * @param box
	 * 		the ICollisionArea to check.
	 * 
	 * @return
	 * 		true if the given Movable with the given box hits a wall.
	 */
	public boolean hitsWall(Entity e, ICollisionArea box) {
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
	public void moveWithinScreen(ICollisionArea box) {
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
	 * @param obj
	 * 		the object to add.
	 */
	public void add(Object obj) {
		if (obj instanceof Entity) {
			entities.add((Entity) obj);
		}
		
		if (obj instanceof ICollidable) {
			collidables.add((ICollidable) obj);
		}
		
		if (obj instanceof IDrawable) {
			drawables.addFirst((IDrawable) obj);
		}
	}

	/**
	 * Removes the given object from this playing field.
	 * 
	 * @param obj
	 * 		the object to remove.
	 */
	public void remove(Object obj) {
		if (obj instanceof Entity) {
			entities.remove(obj);
		}
		
		if (obj instanceof ICollidable) {
			collidables.remove(obj);
		}

		if (obj instanceof IDrawable) {
			drawables.remove(obj);
		}
	}
	
	/**
	 * Clear this PlayingField.<br>
	 * <br>
	 * This removes all Entities and Drawables.
	 */
	public void clear() {
		//Kill all entities
		entities.parallelStream().forEach(e -> e.kill());

		//Clear the lists
		entities.clear();
		drawables.clear();
		collidables.clear();
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
		
		//Remove all non playerfish from collidables
		collidables.removeIf(c -> !(c instanceof PlayerFish));
		drawables.removeIf(c -> !(c instanceof PlayerFish));
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
