package com.github.fishio.multiplayer.client;

import com.github.fishio.PlayerFish;
import com.github.fishio.Preloader;
import com.github.fishio.behaviours.IMoveBehaviour;
import com.github.fishio.behaviours.KeyListenerBehaviour;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;
import com.github.fishio.multiplayer.server.FishServerEntitiesMessage;
import com.github.fishio.multiplayer.server.FishServerPlayerMessage;
import com.github.fishio.multiplayer.server.FishServerMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Handler used by the client for messages sent from the server.
 */
public class FishClientHandler extends SimpleChannelInboundHandler<FishServerMessage> {
	
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, FishServerMessage msg) throws Exception {
		Log.getLogger().log(LogLevel.INFO,
				"[Client] [" + ctx.channel().remoteAddress() + "] Received " + msg.getClass().getSimpleName()
				+ " from server");
		
		if (msg instanceof FishServerEntitiesMessage) {
			handleEntitiesMessage((FishServerEntitiesMessage) msg);
		}
		
		if (msg instanceof FishServerPlayerMessage) {
			handleSpawnMessage((FishServerPlayerMessage) msg);
		}
	}
	
	/**
	 * Handles FishServerEntities messages.
	 * 
	 * @param msg
	 * 		the message from the server.
	 */
	public void handleEntitiesMessage(FishServerEntitiesMessage msg) {
		FishIOClient.getInstance().getPlayingField().updateEntities(msg);
	}

	/**
	 * Handles FishServerPlayerFish messages.
	 * 
	 * @param msg
	 * 		the message from the server.
	 */
	public void handleSpawnMessage(FishServerPlayerMessage msg) {
		//Unregister the key handlers of the old player fish
		PlayerFish old = FishIOClient.getInstance().getPlayingField().getOwnPlayer();
		if (old != null) {
			IMoveBehaviour imb = old.getBehaviour();
			if (imb instanceof KeyListenerBehaviour) {
				((KeyListenerBehaviour) imb).unregisterKeyHandlers();
			}
		}
		
		//Set the new player fish
		FishIOClient.getInstance().getPlayingField().setOwnPlayer(msg.getPlayer());
		
		//Create a new key listener
		KeyListenerBehaviour klb = KeyListenerBehaviour.createWithDefaultSettings(
				Preloader.loadScreen("multiplayerGame"));
		
		//Update the new key listener to the correct behaviour.
		klb.updateTo(msg.getPlayer().getBehaviour());
		msg.getPlayer().setBehaviour(klb);
	}
}
