package com.github.fishio.multiplayer.client;

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;

import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import com.github.fishio.PlayerFish;
import com.github.fishio.Preloader;
import com.github.fishio.behaviours.KeyListenerBehaviour;
import com.github.fishio.gui.SlimGuiTest;
import com.github.fishio.multiplayer.server.FishServerEntitiesMessage;
import com.github.fishio.multiplayer.server.FishServerPlayerMessage;
import com.github.fishio.multiplayer.server.FishServerSettingsMessage;

/**
 * Test class for the {@link FishClientHandler}.
 */
public class TestFishClientHandler extends SlimGuiTest {
	private MultiplayerClientPlayingField playingField;
	
	/**
	 * Fakes that we are connected to a server and creates a playingField
	 * for the client.
	 */
	@Before
	public void setUp() {
		//Set the multiplayerGameScreen to a mock screen.
		Preloader.setScreen("multiplayerGameScreen", mock(Scene.class));
		
		//Fake that we are connected
		Whitebox.setInternalState(FishIOClient.getInstance(), "connected", true);
		
		//Create a playingfield and spy on it
		playingField = spy(new MultiplayerClientPlayingField(60, new Canvas(), 10000, 10000));
		
		//Set the playingField in the FishIOClient
		SimpleObjectProperty<MultiplayerClientPlayingField> playingFieldProperty =
				Whitebox.getInternalState(FishIOClient.getInstance(), "playingFieldProperty");
		playingFieldProperty.set(playingField);
	}
	
	/**
	 * @return
	 * 		a mocked ChannelHandlerContext.
	 */
	private ChannelHandlerContext getChannelHandlerContext() {
		Channel channel = mock(Channel.class);
		ChannelHandlerContext ctx = mock(ChannelHandlerContext.class);
		when(ctx.channel()).thenReturn(channel);
		
		return ctx;
	}

	/**
	 * Test method for {@link FishClientHandler#handleEntitiesMessage(FishServerEntitiesMessage)}.
	 * 
	 * @throws Exception
	 * 		if an exception occurs in messageReceived.
	 */
	@Test
	public void testHandleEntitiesMessage() throws Exception {
		FishClientHandler fch = new FishClientHandler();
		ChannelHandlerContext ctx = getChannelHandlerContext();
		FishServerEntitiesMessage fsem = new FishServerEntitiesMessage(new ArrayList<>());
		
		//Call message received
		fch.messageReceived(ctx, fsem);
		
		//UpdateEntities should have been called on the playing field.
		verify(playingField).updateEntities(fsem);
	}

	/**
	 * Test method for {@link FishClientHandler#handleSpawnMessage(FishServerPlayerMessage)}.
	 * 
	 * @throws Exception
	 * 		if an exception occurs in messageReceived.
	 */
	@Test
	public void testHandleSpawnMessage() throws Exception {
		FishClientHandler fch = new FishClientHandler();
		ChannelHandlerContext ctx = getChannelHandlerContext();
		FishServerPlayerMessage fspm = getPlayerMessage();
		PlayerFish player = fspm.getPlayer();
		
		//Call the method
		fch.messageReceived(ctx, fspm);
		
		//Our own player should now be the one in the message.
		assertSame(fspm.getPlayer(), playingField.getOwnPlayer());
		
		//The behaviour of the player should have been set
		verify(player).setBehaviour(any(KeyListenerBehaviour.class));
	}

	/**
	 * Test method for {@link FishClientHandler#handleSettingsMessage(FishServerSettingsMessage)}.
	 * 
	 * @throws Exception
	 * 		if an exception occurs in messageReceived.
	 */
	@Test
	public void testHandleSettingsMessage() throws Exception {
		FishClientHandler fch = new FishClientHandler();
		ChannelHandlerContext ctx = getChannelHandlerContext();
		FishServerSettingsMessage fssm = new FishServerSettingsMessage();
		
		//Call the method
		fch.messageReceived(ctx, fssm);
		
		//The settings should have been set.
		assertSame(fssm, FishIOClient.getInstance().getSettings());
	}
	
	/**
	 * @return
	 * 		a FishServerPlayerMessage with a mocked player.
	 */
	private FishServerPlayerMessage getPlayerMessage() {
		PlayerFish player = mock(PlayerFish.class);
		KeyListenerBehaviour klb = mock(KeyListenerBehaviour.class);
		when(player.getBehaviour()).thenReturn(klb);
		
		FishServerPlayerMessage tbr = new FishServerPlayerMessage(player);
		
		return tbr;
	}
}
