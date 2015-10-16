package com.github.fishio.multiplayer.server;

import com.github.fishio.PlayerFish;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;
import com.github.fishio.multiplayer.client.FishClientPlayerFishMessage;
import com.github.fishio.multiplayer.client.FishClientRequestPlayerMessage;
import com.github.fishio.multiplayer.client.FishClientMessage;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Handler used by the server for messages sent from the client.
 */
public class FishServerHandler extends SimpleChannelInboundHandler<FishClientMessage> {

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, FishClientMessage msg) throws Exception {
		Log.getLogger().log(LogLevel.INFO,
				"[Server] [" + ctx.channel().remoteAddress() + "] Received " + msg.getClass().getSimpleName());
		
		if (msg instanceof FishClientRequestPlayerMessage) {
			handlePlayerRequest((FishClientRequestPlayerMessage) msg, ctx);
		}
		
		if (msg instanceof FishClientPlayerFishMessage) {
			handleClientUpdate((FishClientPlayerFishMessage) msg);
		}
	}

	/**
	 * Handles a player request message from the client.
	 * 
	 * @param msg
	 * 		the message from the client.
	 * @param ctx
	 * 		the ChannelHandlerContext that can be used to send messages back.
	 */
	public void handlePlayerRequest(FishClientRequestPlayerMessage msg, ChannelHandlerContext ctx) {
		//A client is requesting a new playerfish, so we need to create one.
		MultiplayerServerPlayingField mspf = FishIOServer.getInstance().getPlayingField();
		PlayerFish player = mspf.createClientPlayer();
		
		//Send a message back to the client with the newly spawned player fish
		FishServerPlayerMessage fspm = new FishServerPlayerMessage(player);
		ctx.writeAndFlush(fspm);
	}
	
	/**
	 * Handles a player update message from the client.
	 * 
	 * @param msg
	 * 		the message from the client.
	 */
	public void handleClientUpdate(FishClientPlayerFishMessage msg) {
		MultiplayerServerPlayingField mspf = FishIOServer.getInstance().getPlayingField();
		
		mspf.updatePlayer(msg.getPlayer());
	}
}
