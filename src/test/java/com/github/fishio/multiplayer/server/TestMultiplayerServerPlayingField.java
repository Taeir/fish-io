package com.github.fishio.multiplayer.server;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import javafx.scene.canvas.Canvas;

import com.github.fishio.CollisionMask;
import com.github.fishio.PlayerFish;
import com.github.fishio.TestPlayingField;
import com.github.fishio.behaviours.IMoveBehaviour;

/**
 * Test class for the MultiplayerServerPlayingField.
 */
public class TestMultiplayerServerPlayingField extends TestPlayingField {
	
	@Override
	public MultiplayerServerPlayingField getPlayingField(int fps, Canvas canvas) {
		return new MultiplayerServerPlayingField(fps, canvas);
	}

	@Override
	public int getDefaultAmount() {
		return 0;
	}
	
	/**
	 * Test for {@link MultiplayerServerPlayingField#updatePlayer(PlayerFish)}.
	 */
	@Test
	public void testUpdatePlayer() {
		MultiplayerServerPlayingField mspf = (MultiplayerServerPlayingField) getField();
		
		//Create a player to update
		CollisionMask cm1 = mock(CollisionMask.class);
		IMoveBehaviour imb1 = mock(IMoveBehaviour.class);
		PlayerFish pf1 = mock(PlayerFish.class);
		when(pf1.getEntityId()).thenReturn(10);
		when(pf1.getBoundingArea()).thenReturn(cm1);
		when(pf1.getBehaviour()).thenReturn(imb1);
		
		//Add the player to the field
		mspf.add(pf1);
		
		//Create a second player to update with
		CollisionMask cm2 = mock(CollisionMask.class);
		IMoveBehaviour imb2 = mock(IMoveBehaviour.class);
		PlayerFish pf2 = mock(PlayerFish.class);
		when(pf2.getEntityId()).thenReturn(10);
		when(pf2.getBoundingArea()).thenReturn(cm2);
		when(pf2.getBehaviour()).thenReturn(imb2);
		
		//Call the update method
		mspf.updatePlayer(pf2);
		
		//The collision mask and move behaviour of the first entity should have been updated
		verify(cm1).updateTo(cm2);
		verify(imb1).updateTo(imb2);
	}

}
