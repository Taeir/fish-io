package com.github.fishio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Iterator;

import org.junit.Test;

import com.github.fishio.behaviours.KeyListenerBehaviour;
import com.github.fishio.behaviours.VerticalBehaviour;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;

/**
 * Test class for the SinglePlayerPlayingField.
 */
public class TestSinglePlayerPlayingField extends TestPlayingField {

	@Override
	public PlayingField getPlayingField(int fps, Canvas canvas) {
		return new SinglePlayerPlayingField(fps, canvas, mock(Scene.class), 1280, 720);
	}
	
	@Override
	public SinglePlayerPlayingField getField() {
		return (SinglePlayerPlayingField) super.getField();
	}
	
	@Override
	public int getDefaultAmount() {
		return 1;
	}
	
	@Override
	public int getInitialAmount() {
		return 1;
	}
	
	/**
	 * Tests the isPlayerAlive method using one living PlayerFish.
	 */
	@Test
	public void testIsPlayerAlive2() {
		assertTrue(getField().isPlayerAlive());
	}
	
	/**
	 * Tests the getEntities method.
	 */
	@Test
	public void testGetEntities() {
		assertEquals(1, getField().getEntities().size());
		assertTrue(getField().getEntities().iterator().next() instanceof PlayerFish);
	}
	
	/**
	 * Tests the addEntities method.
	 */
	@Test
	public void testAddEntities() {
		getField().addEntities();
		
		assertEquals(getField().getEnemyFishSpawner().getMaxEnemies() + 1, getField().getEntities().size());
		
		Iterator<Entity> it = getField().getEntities().iterator();
		int i = 0;
		while (it.hasNext() && i < 5) {
			it.next().kill();
			i++;
		}
		
		getField().cleanupDead();
		
		getField().addEntities();
		
		assertEquals(getField().getEnemyFishSpawner().getMaxEnemies() + 1, getField().getEntities().size());
	}
	
	/**
	 * Tests the checkPlayerCollisions method.
	 */
	@Test
	public void testCheckPlayerCollisions1() {
		Entity[] entities = new Entity[3];
		for (int i = 0; i < 3; i++) {
			entities[i] = mock(Entity.class);
			getField().add(entities[i]);
		}
		
		//Replacing the current PlayerFish with our own mocked PlayerFish.
		getField().getPlayers().clear();
		PlayerFish pf = mock(PlayerFish.class);
		getField().getPlayers().add(pf);
		
		when(pf.doesCollides(entities[0])).thenReturn(true);
		when(pf.doesCollides(entities[1])).thenReturn(true);
		when(pf.doesCollides(entities[2])).thenReturn(false);
		
		getField().checkPlayerCollisions();
		
		verify(entities[0]).onCollide(pf);
		verify(entities[1]).onCollide(pf);
		verify(entities[2], never()).onCollide(pf);
		verify(pf).onCollide(entities[0]);
		verify(pf).onCollide(entities[1]);
		verify(pf, never()).onCollide(entities[2]);
		verify(entities[0], never()).onCollide(entities[1]);
	}
	
	/**
	 * Tests the moveMovables method.
	 */
	@Test
	public void testMoveMovables() {
		CollisionMask ca = mock(CollisionMask.class);
		Entity e = new EnemyFish(ca, null, 0, 0);
		VerticalBehaviour b = spy(new VerticalBehaviour(3));
		e.setBehaviour(b);
		
		getField().add(e);
		
		getField().moveMovables();
		
		verify(b).preMove();
		verify(ca).move(new Vec2d(0, -3));
		
	}
	
	/**
	 * Test for {@link SinglePlayerPlayingField#revivePlayer()}.
	 */
	public void testRevivePlayer() {
		PlayerFish player = getField().getPlayer();
		
		//Give the player a speed
		((KeyListenerBehaviour) player.getBehaviour()).setSpeedVector(new Vec2d(10, 10));
		//Give the player a rotation
		player.getBoundingArea().setRotation(20);
		
		//Call the revive
		getField().revivePlayer();
		
		//The speed and position of the player should have been reset.
		assertEquals(0, player.getBehaviour().getSpeed(), 0);
		assertEquals(0, player.getBoundingArea().getRotation(), 0);
	}

}
