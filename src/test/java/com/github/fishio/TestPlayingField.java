package com.github.fishio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.github.fishio.behaviours.VerticalBehaviour;
import com.github.fishio.game.GameState;
import com.github.fishio.gui.GuiTest;

import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

/**
 * Tests the PlayingField class.
 */
public class TestPlayingField extends GuiTest {

	private PlayingField field;
	private Canvas canvas;
	
	/**
	 * Initialises the PlayingField field.
	 */
	@Before
	public void setUp() {
		this.canvas = Mockito.mock(Canvas.class);
		field = new SinglePlayerPlayingField(60, canvas);
	}
	
	/**
	 * Tests the getFPS method.
	 */
	@Test
	public void testGetFPS() {
		assertEquals(60, field.getFPS());
	}
	
	/**
	 * Tests the setFPS method.
	 */
	@Test
	public void testSetFPS() {
		field.setFPS(80);
		
		assertEquals(80, field.getFPS());
	}
	
	/**
	 * Tests the getWidth method.
	 */
	@Test
	public void testGetWidth() {
		assertEquals(1280, field.getWidth(), 0.0D);
	}
	
	/**
	 * Tests the getHeight method.
	 */
	@Test
	public void testGetHeight() {
		assertEquals(670.0, field.getHeight(), 0.0D);
	}
	
	/**
	 * Tests the getEntities method.
	 */
	@Test
	public void testGetEntities() {
		assertEquals(1, field.getEntities().size());
		assertTrue(field.getEntities().get(0) instanceof PlayerFish);
	}
	
	/**
	 * Tests the getRenderer method.
	 */
	@Test
	public void testGetRenderer() {
		assertEquals(canvas, field.getRenderer().getCanvas());
		assertEquals(60, field.getRenderer().getFps());
	}
	
	/**
	 * Tests the getGameThread method.
	 */
	@Test
	public void testGetGameThread() {
		assertEquals(GameState.STOPPED, field.getGameThread().getState());
	}
	
	/**
	 * Tests the startGame method.
	 */
	@Test
	public void testStartGame() {
		field.startGame();
		
		assertTrue(field.getRenderer().isRendering());
		
		GameState state = field.getGameThread().getState();
		assertTrue(state == GameState.RUNNING || state == GameState.STARTING);
	}
	
	/**
	 * Tests the startGameAndWait method.
	 */
	@Test
	public void testStartGameAndWait() {
		try {
			field.startGameAndWait();
		} catch (InterruptedException e) {
			fail();
		}
		
		assertTrue(field.getRenderer().isRendering());
		assertEquals(GameState.RUNNING, field.getGameThread().getState());
	}
	
	/**
	 * Tests the stopGame method.
	 */
	@Test
	public void testStopGame() {
		try {
			field.startGameAndWait();
		} catch (InterruptedException e) {
			fail();
		}
		field.stopGame();
		
		assertFalse(field.getRenderer().isRendering());
		GameState state = field.getGameThread().getState();
		assertTrue(state == GameState.STOPPED || state == GameState.STOPPING);
	}
	
	/**
	 * Tests the stopGameAndWait method.
	 */
	@Test
	public void testStopGameAndWait() {
		try {
			field.startGameAndWait();
			field.stopGameAndWait();
		} catch (InterruptedException e) {
			fail();
		}
		
		assertFalse(field.getRenderer().isRendering());
		assertEquals(GameState.STOPPED, field.getGameThread().getState());
	}
	
	/**
	 * Tests the add method.
	 */
	@Test
	public void testAdd() {
		Entity e = Mockito.mock(Entity.class);
		
		field.add(e);
		
		assertSame(e, field.getEntities().get(1));
		assertSame(e, field.getDrawables().getFirst());
		assertEquals(2, field.getDrawables().size());
	}
	
	/**
	 * Tests the remove method.
	 */
	@Test
	public void testRemove() {
		Entity e = Mockito.mock(Entity.class);
		
		field.add(e);
		field.remove(e);
		
		assertEquals(1, field.getEntities().size());
	}
	
	/**
	 * Tests the clear method.
	 */
	@Test
	public void testClear() {
		for (int i = 0; i < 10; i++) {
			field.add(Mockito.mock(Entity.class));
		}
		
		field.clear();
		
		// Everything is gone but the PlayerFish.
		assertEquals(1, field.getEntities().size());
		assertEquals(1, field.getDrawables().size());
	}
	
	/**
	 * Tests the clearEnemies method.
	 */
	@Test
	public void clearEnemies() {
		for (int i = 0; i < 10; i++) {
			field.add(Mockito.mock(Entity.class));
			field.add(new PlayerFish(null, Mockito.mock(Stage.class), null));
		}
		
		field.clearEnemies();
		
		assertEquals(1, field.getEntities().size());
		assertEquals(1, field.getDrawables().size());
	}
	
	/**
	 * Tests the getDrawables method.
	 */
	@Test
	public void testGetDrawables() {
		IDrawable d = Mockito.mock(Entity.class);
		field.add(d);
		
		assertEquals(d, field.getDrawables().getFirst());
		assertEquals(2, field.getDrawables().size());
	}
	
	/**
	 * Tests the isPlayerAlive method using one living PlayerFish.
	 */
	@Test
	public void testIsPlayerAlive1() {
		assertTrue(field.isPlayerAlive());
	}
	
	/**
	 * Tests the isPlayerAlive method using one dead PlayerFish.
	 */
	@Test
	public void testIsPlayerAlive2() {
		//Replacing the current PlayerFish with our own mocked PlayerFish.
		field.getPlayers().clear();
		PlayerFish pf = Mockito.mock(PlayerFish.class);
		field.getPlayers().add(pf);
		Mockito.when(pf.isDead()).thenReturn(true);
		
		assertFalse(field.isPlayerAlive());
	}
	
	/**
	 * Tests the checkPlayerCollisions method.
	 */
	@Test
	public void testCheckPlayerCollisions1() {
		Entity[] entities = new Entity[3];
		for (int i = 0; i < 3; i++) {
			entities[i] = Mockito.mock(Entity.class);
			field.add(entities[i]);
		}
		
		//Replacing the current PlayerFish with our own mocked PlayerFish.
		field.getPlayers().clear();
		PlayerFish pf = Mockito.mock(PlayerFish.class);
		field.getPlayers().add(pf);
		
		Mockito.when(pf.doesCollides(entities[0])).thenReturn(true);
		Mockito.when(pf.doesCollides(entities[1])).thenReturn(true);
		Mockito.when(pf.doesCollides(entities[2])).thenReturn(false);
		
		field.checkPlayerCollisions();
		
		Mockito.verify(entities[0]).onCollide(pf);
		Mockito.verify(entities[1]).onCollide(pf);
		Mockito.verify(entities[2], Mockito.never()).onCollide(pf);
		Mockito.verify(pf).onCollide(entities[0]);
		Mockito.verify(pf).onCollide(entities[1]);
		Mockito.verify(pf, Mockito.never()).onCollide(entities[2]);
		Mockito.verify(entities[0], Mockito.never()).onCollide(entities[1]);
	}
	
	/**
	 * Tests the cleanUpDead method.
	 */
	@Test
	public void testCleanUpDead() {
		Entity e1 = Mockito.mock(Entity.class);
		Entity e2 = Mockito.mock(Entity.class);	
		
		field.add(e1);
		field.add(e2);
		
		Mockito.when(e1.isDead()).thenReturn(true);
		
		field.cleanupDead();
		
		assertEquals(2, field.getEntities().size());
		assertTrue(field.getEntities().contains(e2));
		assertFalse(field.getEntities().contains(e1));
	}
	
	/**
	 * Tests the addEntities method.
	 */
	@Test
	public void testAddEntities() {
		field.addEntities();
		
		assertEquals(PlayingField.MAX_ENEMY_COUNT + 1, field.getEntities().size());
		
		for (int i = 5; i < 11; i++) {
			field.getEntities().get(i).kill();
		}
		field.cleanupDead();
		
		field.addEntities();
		
		assertEquals(PlayingField.MAX_ENEMY_COUNT + 1, field.getEntities().size());
	}
	
	/**
	 * Tests the moveMovables method.
	 */
	@Test
	public void testMoveMovables() {
		BoundingBox ca = Mockito.mock(BoundingBox.class);
		Entity e = new EnemyFish(ca, null, 0, 0);
		VerticalBehaviour b = Mockito.spy(new VerticalBehaviour(3));
		e.setBehaviour(b);
		
		field.add(e);
		
		field.moveMovables();
		
		Mockito.verify(b).preMove();
		Mockito.verify(ca).move(new Vec2d(0, -3));
		
	}
}
