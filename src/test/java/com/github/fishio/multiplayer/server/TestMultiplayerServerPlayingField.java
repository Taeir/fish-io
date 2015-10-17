package com.github.fishio.multiplayer.server;

import org.junit.Test;
import org.mockito.Mockito;

import javafx.scene.canvas.Canvas;

import com.github.fishio.CollisionMask;
import com.github.fishio.PlayerFish;
import com.github.fishio.behaviours.IMoveBehaviour;
import com.github.fishio.multiplayer.TestMultiplayerPlayingField;

/**
 * Test class for the MultiplayerServerPlayingField.
 */
public class TestMultiplayerServerPlayingField extends TestMultiplayerPlayingField {
	
	@Override
	public MultiplayerServerPlayingField getPlayingField(int fps, Canvas canvas) {
		return new MultiplayerServerPlayingField(fps, canvas);
	}
	
	/**
	 * Test for {@link MultiplayerServerPlayingField#updatePlayer(PlayerFish)}.
	 */
	@Test
	public void testUpdatePlayer() {
		MultiplayerServerPlayingField mspf = (MultiplayerServerPlayingField) getField();
		
		//Create a player to update
		CollisionMask cm1 = Mockito.mock(CollisionMask.class);
		IMoveBehaviour imb1 = Mockito.mock(IMoveBehaviour.class);
		PlayerFish pf1 = Mockito.mock(PlayerFish.class);
		Mockito.when(pf1.getEntityId()).thenReturn(10);
		Mockito.when(pf1.getBoundingArea()).thenReturn(cm1);
		Mockito.when(pf1.getBehaviour()).thenReturn(imb1);
		
		//Add the player to the field
		mspf.add(pf1);
		
		//Create a second player to update with
		CollisionMask cm2 = Mockito.mock(CollisionMask.class);
		IMoveBehaviour imb2 = Mockito.mock(IMoveBehaviour.class);
		PlayerFish pf2 = Mockito.mock(PlayerFish.class);
		Mockito.when(pf2.getEntityId()).thenReturn(10);
		Mockito.when(pf2.getBoundingArea()).thenReturn(cm2);
		Mockito.when(pf2.getBehaviour()).thenReturn(imb2);
		
		//Call the update method
		mspf.updatePlayer(pf2);
		
		//The collision mask and move behaviour of the first entity should have been updated
		Mockito.verify(cm1).updateTo(cm2);
		Mockito.verify(imb1).updateTo(imb2);
	}

}
