package com.github.fishio.multiplayer.server;

import com.github.fishio.game.GameThread;

public class ServerGameThread extends GameThread {

	/**
	 * Creates a new ServerGameThread, a game thread that also calls the
	 * correct multiplayer update messages.
	 * 
	 * @param playingField
	 * 		the playingfield to use.
	 */
	public ServerGameThread(MultiplayerServerPlayingField playingField) {
		super(playingField);
	}

	@Override
	protected void gameTick() {
		super.gameTick();
		
		//Send the entities update
		getPlayingField().sendEntitiesUpdate();
	}
	
	@Override
	public MultiplayerServerPlayingField getPlayingField() {
		return (MultiplayerServerPlayingField) super.getPlayingField();
	}

}
