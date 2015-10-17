package com.github.fishio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.Mockito;

import com.github.fishio.behaviours.VerticalBehaviour;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;

/**
 * Test class for the SinglePlayerPlayingField.
 */
public class TestSinglePlayerPlayingField extends TestPlayingField {

	@Override
	public PlayingField getPlayingField(int fps, Canvas canvas) {
		return new SinglePlayerPlayingField(fps, canvas, Mockito.mock(Scene.class));
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
		assertEquals(1, getField().getEntitiesList().size());
		assertTrue(getField().getEntitiesList().get(0) instanceof PlayerFish);
	}
	
	/**
	 * Tests the addEntities method.
	 */
	@Test
	public void testAddEntities() {
		getField().addEntities();
		
		assertEquals(PlayingField.MAX_ENEMY_COUNT + 1, getField().getEntities().size());
		
		for (int i = 5; i < 11; i++) {
			getField().getEntitiesList().get(i).kill();
		}
		getField().cleanupDead();
		
		getField().addEntities();
		
		assertEquals(PlayingField.MAX_ENEMY_COUNT + 1, getField().getEntities().size());
	}
	
	/**
	 * Tests the checkPlayerCollisions method.
	 */
	@Test
	public void testCheckPlayerCollisions1() {
		Entity[] entities = new Entity[3];
		for (int i = 0; i < 3; i++) {
			entities[i] = Mockito.mock(Entity.class);
			getField().add(entities[i]);
		}
		
		//Replacing the current PlayerFish with our own mocked PlayerFish.
		getField().getPlayers().clear();
		PlayerFish pf = Mockito.mock(PlayerFish.class);
		getField().getPlayers().add(pf);
		
		Mockito.when(pf.doesCollides(entities[0])).thenReturn(true);
		Mockito.when(pf.doesCollides(entities[1])).thenReturn(true);
		Mockito.when(pf.doesCollides(entities[2])).thenReturn(false);
		
		getField().checkPlayerCollisions();
		
		Mockito.verify(entities[0]).onCollide(pf);
		Mockito.verify(entities[1]).onCollide(pf);
		Mockito.verify(entities[2], Mockito.never()).onCollide(pf);
		Mockito.verify(pf).onCollide(entities[0]);
		Mockito.verify(pf).onCollide(entities[1]);
		Mockito.verify(pf, Mockito.never()).onCollide(entities[2]);
		Mockito.verify(entities[0], Mockito.never()).onCollide(entities[1]);
	}
	
	/**
	 * Tests the moveMovables method.
	 */
	@Test
	public void testMoveMovables() {
		CollisionMask ca = Mockito.mock(CollisionMask.class);
		Entity e = new EnemyFish(ca, null, 0, 0);
		VerticalBehaviour b = Mockito.spy(new VerticalBehaviour(3));
		e.setBehaviour(b);
		
		getField().add(e);
		
		getField().moveMovables();
		
		Mockito.verify(b).preMove();
		Mockito.verify(ca).move(new Vec2d(0, -3));
		
	}

}
