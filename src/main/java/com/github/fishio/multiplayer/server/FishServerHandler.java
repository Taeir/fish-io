package com.github.fishio.multiplayer.server;

import com.github.fishio.PlayerFish;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;
import com.github.fishio.multiplayer.client.FishClientPlayerFishMessage;
import com.github.fishio.multiplayer.client.FishClientRequestPlayerMessage;
import com.github.fishio.multiplayer.client.FishClientMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

/**
 * Handler used by the server for messages sent from the client.
 */
public class FishServerHandler extends SimpleChannelInboundHandler<FishClientMessage> {
	
	private ChannelGroup allChannels;
	
	/**
	 * Creates a new FishServerHandler.
	 * 
	 * @param channelGroup
	 * 		the channelgroup to add new channels to.
	 */
	public FishServerHandler(ChannelGroup channelGroup) {
		allChannels = channelGroup;
	}
	
	@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //Add channels to the group.
        allChannels.add(ctx.channel());
        
        //Send the settings
        ctx.writeAndFlush(FishIOServer.getInstance().getSettings());
        
        super.channelActive(ctx);
    }
	
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
		//If the playing field is null, the server must have stopped, so we return.
		MultiplayerServerPlayingField mspf = FishIOServer.getInstance().getPlayingField();
		if (mspf == null) {
			return;
		}

		//A client is requesting a new playerfish, so we need to create one.
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
		if (mspf == null) {
			return;
		}
		
		mspf.updatePlayer(msg.getPlayer());
	}
}
