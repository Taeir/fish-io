package com.github.fishio.multiplayer.client;

import java.util.Collection;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

import javafx.scene.canvas.Canvas;

import com.github.fishio.Entity;
import com.github.fishio.PlayerFish;
import com.github.fishio.game.GameThread;
import com.github.fishio.logging.LogLevel;
import com.github.fishio.multiplayer.MultiplayerPlayingField;
import com.github.fishio.multiplayer.server.FishServerEntitiesMessage;

/**
 * Represents a playing field for a client in a multiplayer game.
 */
public class MultiplayerClientPlayingField extends MultiplayerPlayingField {
	private GameThread gameThread;
	
	/**
	 * Creates a new MultiplayerClientPlayingField.
	 * 
	 * @param fps
	 * 		the fps of the renderer of this PlayingField.
	 * @param canvas
	 * 		the canvas to render on.
	 * @param width
	 * 		the width of the playing field
	 * @param height
	 * 		the height of the playing field
	 */
	public MultiplayerClientPlayingField(int fps, Canvas canvas, int width, int height) {
		super(fps, canvas, width, height);
		
		this.gameThread = new GameThread(this);
		logger.log(LogLevel.INFO, "Created GameThread");
	}
	
	@Override
	public GameThread getGameThread() {
		return this.gameThread;
	}
	
	@Override
	public void addEntities() {
		//The server handles adding entities.
	}

	@Override
	public void moveMovables() {
		//The server handles moving of enemyfish
		PlayerFish player = getOwnPlayer();
		if (player != null) {
			player.getBehaviour().preMove();
		}
	}
	
	@Override
	public void checkPlayerCollisions() {
		//The server handles collision checking
	}
	
	/**
	 * Update the entities list from the given server message.
	 * 
	 * @param message
	 * 		the server message to update with.
	 */
	public void updateEntities(FishServerEntitiesMessage message) {
		Set<Entity> deadEntities = message.getDeadEntities(getEntities());
		Set<Entity> newEntities = message.getNewEntities(getEntities());
		
		//First add the new entities
		for (Entity e : newEntities) {
			add(e);
		}
		
		//Then remove the dead entities.
		for (Entity e : deadEntities) {
			remove(e);
		}
		
		//Then update the entity information (position, rotation, etc.)
		Collection<Entity> updates = message.getEntities();
		
		Queue<Entity> current = getEntities();
		for (Entity currentEntity : current) {
			Optional<Entity> optionalEntity = updates
					.parallelStream()
					.filter((uentity) -> uentity.getEntityId() == currentEntity.getEntityId())
					.findAny();
			
			if (!optionalEntity.isPresent()) {
				logger.log(LogLevel.WARNING,
						"Cannot update entity " + currentEntity.getEntityId() + ": cannot find updated entity! "
					  + "This means there is a mistake in the getDeadEntities or getNewEntities method of "
					  + "FishServerEntitiesMessage!");
				continue;
			}
			
			updateEntity(currentEntity, optionalEntity.get());
		}
		
		if (getOwnPlayer() != null && !getEntities().contains(getOwnPlayer())) {
			getOwnPlayer().setDead();
		}
	}

	/**
	 * Updates the given current entity with the given updated entity.
	 * 
	 * @param current
	 * 		the entity to be updated
	 * @param updated
	 * 		the entity to update with
	 */
	private void updateEntity(Entity current, Entity updated) {
		if (current instanceof PlayerFish && current.equals(getOwnPlayer())) {
			//Dont update the speed and position of our own player, we handle that ourselves.
			if (updated.isDead()) {
				current.kill();
			}
			
			return;
		}
		
		//Update bounding area
		current.getBoundingArea().updateTo(updated.getBoundingArea());
		if (current.getBehaviour().getClass() == updated.getBehaviour().getClass()) {
			current.getBehaviour().updateTo(updated.getBehaviour());
		}
		//TODO Allow swapping behaviors
		
		//TODO Additional updating for playerfish of other players?
	}
}
