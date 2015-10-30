package com.github.fishio.multiplayer.server;

import java.util.Optional;

import com.github.fishio.CollisionMask;
import com.github.fishio.PlayerFish;
import com.github.fishio.Preloader;
import com.github.fishio.Sprite;
import com.github.fishio.SpriteStore;
import com.github.fishio.Vec2d;
import com.github.fishio.game.GameThread;
import com.github.fishio.logging.LogLevel;
import com.github.fishio.multiplayer.MultiplayerPlayingField;
import com.github.fishio.multiplayer.RepeatingFishMessageSender;

import javafx.scene.canvas.Canvas;

/**
 * PlayingField for server side multiplayer.
 */
public class MultiplayerServerPlayingField extends MultiplayerPlayingField {
	private static final long SPAWN_INVINCIBILITY = 6_000L;
	private ServerGameThread gameThread;
	private double startX, startY;
	private RepeatingFishMessageSender entityUpdateSender;
	
	/**
	 * Creates a new MultiplayerServerPlayingField.
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
	public MultiplayerServerPlayingField(int fps, Canvas canvas, int width, int height) {
		super(fps, canvas, width, height);
		startX = width / 2D;
		startY = height / 2D;
		
		this.gameThread = new ServerGameThread(this);
		this.entityUpdateSender = new RepeatingFishMessageSender(new FishServerEntitiesMessage(this));
	}

	@Override
	public GameThread getGameThread() {
		return this.gameThread;
	}
	
	/**
	 * Sends an entities update to all connected clients.
	 */
	public void sendEntitiesUpdate() {
		FishIOServer.getInstance().queueMessage(entityUpdateSender);
	}
	
	/**
	 * Called when the client sends a player update message.
	 * 
	 * @param updated
	 * 		the updated PlayerFish corresponding to the client.
	 */
	public void updatePlayer(PlayerFish updated) {
		//Find the player by entity id
		Optional<PlayerFish> oPlayer = getPlayers()
				.stream()
				.filter(player -> player.getEntityId() == updated.getEntityId())
				.findAny();
		
		if (!oPlayer.isPresent()) {
			logger.log(LogLevel.DEBUG, "[MSPF] A player update was received, but that player is not in the game...");
			return;
		}
		
		PlayerFish player = oPlayer.get();
		
		//Add synchronization here to prevent the size from getting lost.
		synchronized (player) {
			//Store the size, because we need to restore it after the update
			double size = player.getBoundingArea().getSize();
			
			//Update the bounding area
			player.getBoundingArea().updateTo(updated.getBoundingArea());
			//Restore the size
			player.getBoundingArea().setSize(size);
		}
		
		//Update the behaviour
		if (player.getBehaviour().getClass() == updated.getBehaviour().getClass()) {
			player.getBehaviour().updateTo(updated.getBehaviour());
		}
	}
	
	/**
	 * Creates a new playerfish for a client.
	 * 
	 * @return
	 * 		a new PlayerFish without any keys registered.
	 */
	public PlayerFish createClientPlayer() {
		Sprite sprite = SpriteStore.getSpriteOrLoad(PlayerFish.SPRITE_LOCATION);

		CollisionMask cm = new CollisionMask(new Vec2d(startX, startY), 60, 30, sprite);
		PlayerFish tbr = new PlayerFish(cm, sprite);

		tbr.setInvincible(System.currentTimeMillis() + SPAWN_INVINCIBILITY);
		
		add(tbr);
		
		return tbr;
	}
	
	/**
	 * Respawn the player of the server.
	 */
	public void respawnOwnPlayer() {
		Sprite sprite = SpriteStore.getSpriteOrLoad(PlayerFish.SPRITE_LOCATION);
		CollisionMask cm = new CollisionMask(new Vec2d(startX, startY), 60, 30, sprite);

		PlayerFish nplayer = new PlayerFish(cm, Preloader.loadScreen("multiplayerGameScreen"), sprite);
		
		nplayer.setInvincible(System.currentTimeMillis() + SPAWN_INVINCIBILITY);
		
		setOwnPlayer(nplayer);
	}
}
