package com.github.fishio;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.github.fishio.gui.GuiTest;

import javafx.scene.canvas.Canvas;

/**
 * Tests the PlayingField class.
 */
public class TestPlayingField extends GuiTest {

	private PlayingField field;
	
	/**
	 * Initialises the PlayingField field.
	 */
	@Before
	public void setUp() {
		field = new SinglePlayerPlayingField(60, Mockito.mock(Canvas.class));
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
	
}
