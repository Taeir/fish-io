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
import com.github.fishio.multiplayer.server.FishServerSettingsMessage;

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
		
		if (msg instanceof FishServerSettingsMessage) {
			handleSettingsMessage((FishServerSettingsMessage) msg);
		}
	}
	
	/**
	 * Handles FishServerEntities messages.
	 * 
	 * @param msg
	 * 		the message from the server.
	 */
	public void handleEntitiesMessage(FishServerEntitiesMessage msg) {
		MultiplayerClientPlayingField mcpf = FishIOClient.getInstance().getPlayingField();
		if (mcpf != null) {
			mcpf.updateEntities(msg);
		}
	}

	/**
	 * Handles FishServerPlayerFish messages.
	 * 
	 * @param msg
	 * 		the message from the server.
	 */
	public void handleSpawnMessage(FishServerPlayerMessage msg) {
		Log.getLogger().log(LogLevel.DEBUG, "[Client] Received spawn message. Creating KeyListener.");
		//Unregister the key handlers of the old player fish
		MultiplayerClientPlayingField mcpf = FishIOClient.getInstance().getPlayingField();
		PlayerFish old = mcpf.getOwnPlayer();
		if (old != null) {
			IMoveBehaviour imb = old.getBehaviour();
			if (imb instanceof KeyListenerBehaviour) {
				((KeyListenerBehaviour) imb).unregisterKeyHandlers();
			}
		}
		
		//We need to create a new behaviour.
		KeyListenerBehaviour klb = KeyListenerBehaviour.createWithDefaultSettings(
				Preloader.loadScreen("multiplayerGameScreen"));
		
		//Update the new key listener to the correct behaviour.
		klb.updateTo(msg.getPlayer().getBehaviour());
		msg.getPlayer().setBehaviour(klb);
		
		//Set the new player fish
		mcpf.setOwnPlayer(msg.getPlayer());
	}
	
	/**
	 * Handles FishServerSettings messages.
	 * 
	 * @param msg
	 * 		the message from the server.
	 */
	public void handleSettingsMessage(FishServerSettingsMessage msg) {
		FishIOClient.getInstance().setSettings(msg);
	}
}
