package com.github.fishio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.github.fishio.game.GameState;
import com.github.fishio.gui.SlimGuiTest;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;

/**
 * Abstract test class for the PlayingField class.
 */
public abstract class TestPlayingField extends SlimGuiTest {

	/**
	 * @param fps
	 * 		the fps of the playing field.
	 * @param canvas
	 * 		the canvas to use

	 * @return
	 * 		a new PlayingField.
	 */
	public abstract PlayingField getPlayingField(int fps, Canvas canvas);
	
	/**
	 * @return
	 * 		the amount of entities that is in the field by default.
	 */
	public abstract int getDefaultAmount();
	
	/**
	 * @return
	 * 		the amount of entities that is in the field initially.
	 */
	public abstract int getInitialAmount();
	
	private PlayingField field;
	private Canvas canvas;
	
	/**
	 * Initialises the PlayingField field.
	 */
	@Before
	public void setUp() {
		this.canvas = Mockito.spy(new Canvas(1280, 670));
		this.field = getPlayingField(60, canvas);
	}
	
	/**
	 * Stops the game after we're done testing.
	 */
	@After
	public void demolish() {
		try {
			field.stopGameAndWait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
		int amount = field.getEntities().size();
		Entity e = mock(Entity.class);
		
		field.add(e);
		
		assertTrue(field.getEntities().contains(e));
		assertTrue(field.getDrawables().contains(e));
		assertEquals(amount + 1, field.getDrawables().size());
	}
	
	/**
	 * Tests the remove method.
	 */
	@Test
	public void testRemove() {
		int amount = field.getEntities().size();
		Entity e = mock(Entity.class);
		
		field.add(e);
		field.remove(e);
		
		assertFalse(field.getEntitiesList().contains(e));
		assertFalse(field.getDrawables().contains(e));
		assertEquals(amount, field.getEntitiesList().size());
	}
	
	/**
	 * Tests the clear method.
	 */
	@Test
	public void testClear() {
		for (int i = 0; i < 10; i++) {
			field.add(mock(Entity.class));
		}
		
		field.clear();
		
		// Everything is gone but the PlayerFish.
		assertEquals(getDefaultAmount(), field.getEntities().size());
		assertEquals(getDefaultAmount(), field.getDrawables().size());
	}
	
	/**
	 * Tests the clearEnemies method.
	 */
	@Test
	public void clearEnemies() {
		for (int i = 0; i < 10; i++) {
			field.add(mock(Entity.class));
			field.add(new PlayerFish(null, mock(Scene.class), null));
		}
		
		field.clearEnemies();
		
		assertEquals(getInitialAmount() + 10, field.getEntities().size());
		assertEquals(getInitialAmount() + 10, field.getDrawables().size());
	}
	
	/**
	 * Tests the getDrawables method.
	 */
	@Test
	public void testGetDrawables() {
		int amount = field.getEntities().size();
		IDrawable d = mock(Entity.class);
		field.add(d);
		
		assertTrue(field.getDrawables().contains(d));
		assertEquals(amount + 1, field.getDrawables().size());
	}
	
	/**
	 * Tests the isPlayerAlive method using one dead PlayerFish.
	 */
	@Test
	public void testIsPlayerAlive() {
		//Replacing the current PlayerFish with our own mocked PlayerFish.
		field.getPlayers().clear();
		PlayerFish pf = mock(PlayerFish.class);
		field.getPlayers().add(pf);
		Mockito.when(pf.isDead()).thenReturn(true);
		
		assertFalse(field.isPlayerAlive());
	}
	
	/**
	 * Tests the cleanUpDead method.
	 */
	@Test
	public void testCleanUpDead() {
		Entity e1 = mock(Entity.class);
		Entity e2 = mock(Entity.class);	
		
		field.add(e1);
		field.add(e2);
		
		Mockito.when(e1.isDead()).thenReturn(true);
		
		field.cleanupDead();
		
		assertEquals(getInitialAmount() + 1, field.getEntities().size());
		assertTrue(field.getEntities().contains(e2));
		assertFalse(field.getEntities().contains(e1));
	}

	/**
	 * @return
	 * 		the PlayingField created. Can be used by extending classes.
	 */
	protected PlayingField getField() {
		return field;
	}
}
