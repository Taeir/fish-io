package com.github.fishio.multiplayer.server;

import java.util.Optional;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

import com.github.fishio.CollisionMask;
import com.github.fishio.Entity;
import com.github.fishio.PlayerFish;
import com.github.fishio.Preloader;
import com.github.fishio.SinglePlayerPlayingField;
import com.github.fishio.Vec2d;
import com.github.fishio.game.GameThread;
import com.github.fishio.logging.LogLevel;
import com.github.fishio.multiplayer.MultiplayerPlayingField;

/**
 * PlayingField for server side multiplayer.
 */
public class MultiplayerServerPlayingField extends MultiplayerPlayingField {
	private static final long SPAWN_INVINCIBILITY = 6_000L;
	private ServerGameThread gameThread;
	
	/**
	 * Creates a new MultiplayerServerPlayingField.
	 * 
	 * @param fps
	 * 		the fps of the renderer of this PlayingField.
	 * @param canvas
	 * 		the canvas to render on.
	 */
	public MultiplayerServerPlayingField(int fps, Canvas canvas) {
		super(fps, canvas);
		
		this.gameThread = new ServerGameThread(this);
	}

	@Override
	public GameThread getGameThread() {
		return this.gameThread;
	}
	
	/**
	 * Sends an entities update to all connected clients.
	 */
	public void sendEntitiesUpdate() {
		FishServerEntitiesMessage fsem = new FishServerEntitiesMessage(this);
		FishIOServer.getInstance().queueMessage(fsem);
		FishIOServer.getInstance().flush();
	}
	
	/**
	 * Called when the client sends a player update message.
	 * 
	 * @param updated
	 * 		the updated PlayerFish corresponding to the client.
	 */
	public void updatePlayer(PlayerFish updated) {
		Optional<Entity> oEntity = getEntities()
				.parallelStream()
				.filter((entity) -> entity instanceof PlayerFish && entity.getEntityId() == updated.getEntityId())
				.findAny();
		
		if (!oEntity.isPresent()) {
			logger.log(LogLevel.WARNING, "[MSPF] A player update was received, but that player is not in the game...");
			return;
		}
		
		Entity entity = oEntity.get();
		entity.getBoundingArea().updateTo(updated.getBoundingArea());
		if (entity.getBehaviour().getClass() == updated.getBehaviour().getClass()) {
			entity.getBehaviour().updateTo(updated.getBehaviour());
		}
	}
	
	/**
	 * Creates a new playerfish for a client.
	 * 
	 * @return
	 * 		a new PlayerFish without any keys registered.
	 */
	public PlayerFish createClientPlayer() {
		Image sprite = Preloader.getImageOrLoad("sprites/fish/playerFish.png");
		CollisionMask cm = new CollisionMask(
				new Vec2d(SinglePlayerPlayingField.START_X, SinglePlayerPlayingField.START_Y), 60, 30, 
				Preloader.getAlphaDataOrLoad("sprites/fish/playerFish.png"),
				Preloader.getSpriteAlphaRatioOrLoad("sprites/fish/playerFish.png"));
		PlayerFish tbr = new PlayerFish(cm, sprite);
		
		//TODO #168 Make setting for this "spawn invincibility"
		tbr.setInvincible(System.currentTimeMillis() + SPAWN_INVINCIBILITY);
		
		add(tbr);
		
		return tbr;
	}
	
	/**
	 * Respawn the player of the server.
	 */
	public void respawnOwnPlayer() {
		Image sprite = Preloader.getImageOrLoad("sprites/fish/playerFish.png");
		CollisionMask cm = new CollisionMask(
				new Vec2d(SinglePlayerPlayingField.START_X, SinglePlayerPlayingField.START_Y), 60, 30, 
				Preloader.getAlphaDataOrLoad("sprites/fish/playerFish.png"),
				Preloader.getSpriteAlphaRatioOrLoad("sprites/fish/playerFish.png"));
		PlayerFish nplayer = new PlayerFish(cm, Preloader.loadScreen("multiplayerGameScreen"), sprite);
		//TODO #168 Make setting for this "spawn invincibility"
		nplayer.setInvincible(System.currentTimeMillis() + SPAWN_INVINCIBILITY);
		
		setOwnPlayer(nplayer);
		add(nplayer);
	}
}
