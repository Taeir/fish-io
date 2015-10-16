package com.github.fishio.multiplayer.server;

import com.github.fishio.PlayerFish;

/**
 * Represents a message from the server to tell a client which PlayerFish
 * is the one controlled by that client.
 */
public class FishServerPlayerMessage implements FishServerMessage {
	private static final long serialVersionUID = 2155427623841668282L;

	private PlayerFish player;
	
	/**
	 * Creates a new Server handshake message with the given PlayerFish.
	 * 
	 * @param player
	 * 		the player the client receiving this message should use.
	 */
	public FishServerPlayerMessage(PlayerFish player) {
		this.player = player;
	}
	
	/**
	 * @return
	 * 		the player in this message
	 */
	public PlayerFish getPlayer() {
		return player;
	}
}
