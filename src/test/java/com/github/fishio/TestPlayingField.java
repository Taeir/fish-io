package com.github.fishio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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
		assertEquals(PlayingField.WINDOW_X, field.getWidth());
	}
	
	/**
	 * Tests the getHeight method.
	 */
	@Test
	public void testGetHeight() {
		assertEquals(PlayingField.WINDOW_Y, field.getHeight());
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
	
}
