package com.github.fishio.multiplayer.client;

import com.github.fishio.PlayerFish;

/**
 * Client message for sending player information.
 */
public class FishClientPlayerFishMessage implements FishClientMessage {
	private static final long serialVersionUID = 5736090385818144039L;

	private PlayerFish player;
	
	/**
	 * @param player
	 * 		the player to use for this message.
	 */
	public FishClientPlayerFishMessage(PlayerFish player) {
		this.player = player;
	}
	
	/**
	 * @return
	 * 		the player in this message.
	 */
	public PlayerFish getPlayer() {
		return player;
	}
}
