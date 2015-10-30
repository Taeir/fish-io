package com.github.fishio.multiplayer.client;

import io.netty.channel.ChannelFuture;

import java.util.Optional;
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
	private ChannelFuture lastPlayerUpdate;
	
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
	public void setOwnPlayer(PlayerFish player) {
		super.setOwnPlayer(player);
		
		//For client side, we need an additional check for setting our own player.
		
		//Check if there is a fake player present
		Optional<PlayerFish> fakePlayer = getPlayers()
				.stream()
				.filter(p -> p != player && p.equals(player))
				.findAny();
		
		if (!fakePlayer.isPresent()) {
			return;
		}
		
		//Replace the behaviour of the fake with the new (correct) behaviour
		fakePlayer.get().setBehaviour(player.getBehaviour());
		
		//Set our player to the fake one.
		getOwnPlayerProperty().set(fakePlayer.get());
	}
	
	@Override
	public void addEntities() {
		//The server handles adding entities.
	}
	
	@Override
	public void moveMovables() {
		//The server handles moving of enemyfish
		PlayerFish player = getOwnPlayer();
		if (player == null || player.isDead()) {
			return;
		}
		
		//Move the player
		moveEntity(player);
		
		//If the last update was not yet sent, we don't send a new one
		if (lastPlayerUpdate != null && !lastPlayerUpdate.isDone()) {
			return;
		}
		
		//Send the update
		lastPlayerUpdate = FishIOClient.getInstance().queueMessage(new FishClientPlayerFishMessage(player), true);
	}
	
	@Override
	public void checkPlayerCollisions() {
		//The server handles collision checking
	}
	
	@Override
	public void clear() {
		getEntities().clear();
		getDrawables().clear();
		getCollidables().clear();
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
		
		//1) add the new entities
		newEntities.parallelStream().forEach(e -> add(e));
		
		//2) remove the dead entities
		deadEntities.parallelStream().forEach(e -> remove(e));
		
		//3) update the entity information (position, rotation, etc.)
		message.getEntities().parallelStream().forEach(updated -> {
			//Find the corresponding current entity
			for (Entity current : getEntities()) {
				if (current.getEntityId() == updated.getEntityId()) {
					//Update the entity
					updateEntity(current, updated);
					break;
				}
			}
		});
	}

	/**
	 * Updates the given current entity with the given updated entity.
	 * 
	 * @param current
	 * 		the entity to be updated
	 * @param updated
	 * 		the entity to update with
	 */
	public void updateEntity(Entity current, Entity updated) {
		//If current and updated are the same object, we don't need to update it.
		if (current == updated) {
			return;
		}
		
		//Update death state
		if (updated.isDead()) {
			current.kill();
		}
		
		//If the current entity is our own player
		if (current instanceof PlayerFish && current.equals(getOwnPlayer())) {
			//We need to only update our size
			current.getBoundingArea().setSize(updated.getBoundingArea().getSize());
		} else {
			//Update bounding area
			current.getBoundingArea().updateTo(updated.getBoundingArea());
			
			//Update behaviour
			if (current.getBehaviour().getClass() == updated.getBehaviour().getClass()) {
				current.getBehaviour().updateTo(updated.getBehaviour());
			} else {
				current.setBehaviour(updated.getBehaviour());
			}
		}
	}
}
