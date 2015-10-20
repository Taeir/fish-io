package com.github.fishio.multiplayer;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import javafx.scene.canvas.Canvas;

import com.github.fishio.CollisionMask;
import com.github.fishio.PlayerFish;
import com.github.fishio.TestPlayingField;
import com.github.fishio.behaviours.IMoveBehaviour;

/**
 * Abstract test class for {@link MultiplayerPlayingField}.
 */
public abstract class TestMultiplayerPlayingField extends TestPlayingField {
	@Override
	public abstract MultiplayerPlayingField getPlayingField(int fps, Canvas canvas);
	
	@Override
	public MultiplayerPlayingField getField() {
		return (MultiplayerPlayingField) super.getField();
	}
	
	@Override
	public int getDefaultAmount() {
		return 0;
	}
	
	@Override
	public int getInitialAmount() {
		return 1;
	}
	
	/**
	 * Sets a player before every test.
	 */
	@Before
	public void setUpMultiplayerPlayingField() {
		PlayerFish player = mock(PlayerFish.class);
		when(player.getBoundingArea()).thenReturn(mock(CollisionMask.class));
		when(player.getBehaviour()).thenReturn(mock(IMoveBehaviour.class));
		
		getField().setOwnPlayer(player);
	}
	
	/**
	 * Test for {@link MultiplayerPlayingField#getOwnPlayer()}.
	 */
	@Test
	public void testGetOwnPlayer() {
		PlayerFish player = getField().getOwnPlayerProperty().get();
		
		assertSame(getField().getOwnPlayer(), player);
	}
	
	/**
	 * Test for {@link MultiplayerPlayingField#setOwnPlayer(PlayerFish)}.
	 */
	@Test
	public void testSetOwnPlayer() {
		PlayerFish nplayer = mock(PlayerFish.class);
		
		getField().setOwnPlayer(nplayer);
		
		assertSame(getField().getOwnPlayer(), nplayer);
	}
	
	/**
	 * Test for {@link MultiplayerPlayingField#setOwnPlayer(PlayerFish)},
	 * when setting the player to what it was before. This is a test for
	 * when a PlayerFish is received and needs to be removed from the lists
	 * before the new one is readded.
	 */
	@Test
	public void testSetOwnPlayer2() {
		PlayerFish player = getField().getOwnPlayerProperty().get();
		
		getField().setOwnPlayer(player);
		
		assertSame(getField().getOwnPlayer(), player);
	}
}
