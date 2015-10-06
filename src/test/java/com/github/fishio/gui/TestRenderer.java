package com.github.fishio.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.ConcurrentLinkedDeque;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.github.fishio.SinglePlayerPlayingField;

/**
 * Tests the {@link Renderer} class.
 */
public class TestRenderer {

	private static SinglePlayerPlayingField sppf;
	private static Canvas canvas;
	private Renderer renderer;
	
	/**
	 * Creates a new PlayingField, Canvas and Renderer before every test,
	 * so that tests do not interfere with each other.
	 * @throws InterruptedException 
	 * 		if we are interrupted while waiting for the game to stop.
	 */
	@Before
	public void setUp() throws InterruptedException {
		//Create a new SinglePlayerPlayingField mock.
		sppf = Mockito.mock(SinglePlayerPlayingField.class);
		when(sppf.getDrawables()).thenReturn(new ConcurrentLinkedDeque<>());
		when(sppf.getDeadDrawables()).thenReturn(new ConcurrentLinkedDeque<>());
		
		//Create a new Canvas and spy on it.
		canvas = spy(new Canvas());
		
		//Create the renderer
		renderer = new Renderer(sppf, canvas, 60);
	}

	/**
	 * Stops the renderer after every test.
	 * 
	 * @throws Exception
	 * 		if we are interrupted while waiting for the renderer to stop.
	 */
	@After
	public void breakDown() throws Exception {
		//Stop the renderer and wait for it to stop.
		renderer.stopRendering();
		
		while (renderer.isRendering()) {
			Thread.sleep(25L);
		}
	}

	/**
	 * Test for {@link Renderer#redraw()}.
	 */
	@Test
	public void testRedraw() {
		renderer.redraw();
		
		//The redraw method should get the graphics context at least once.
		verify(canvas, times(1)).getGraphicsContext2D();
	}

	/**
	 * Test for {@link Renderer#getFps()}.
	 */
	@Test
	public void testGetFps() {
		assertEquals(60, renderer.getFps());
	}

	/**
	 * Test for {@link Renderer#setFps(int)}.
	 */
	@Test
	public void testSetFps() {
		renderer.setFps(30);
		assertEquals(30, renderer.getFps());
	}

	/**
	 * Test for {@link Renderer#getCanvas()}.
	 */
	@Test
	public void testGetCanvas() {
		assertSame(canvas, renderer.getCanvas());
	}

	/**
	 * Test for {@link Renderer#setCanvas(Canvas)}.
	 */
	@Test
	public void testSetCanvas() {
		Canvas canvas2 = new Canvas();
		renderer.setCanvas(canvas2);
		
		assertSame(canvas2, renderer.getCanvas());
	}
	
	/**
	 * Test for {@link Renderer#getBackground()}.
	 */
	@Test
	public void testGetBackground() {
		//Background should initially be null
		assertNull(renderer.getBackground());
	}

	/**
	 * Test for {@link Renderer#setBackground(Image)}.
	 */
	@Test
	public void testSetBackground() {
		Image image = new Image("AlphaDataTest.png");
		renderer.setBackground(image);
		
		assertSame(image, renderer.getBackground());
	}

	/**
	 * Test for {@link Renderer#startRendering()}.
	 * When startRendering is called, rendering should start within 5
	 * seconds.
	 * 
	 * @throws InterruptedException
	 * 		if we are interrupted while waiting.
	 */
	@Test
	public void testStartRendering() throws InterruptedException {
		//We aren't rendering yet.
		assertFalse(renderer.isRendering());
		
		//We start rendering
		renderer.startRendering();
		
		//We wait for a maximum of 5 seconds
		for (int i = 0; i < 100 && !renderer.isRendering(); i++) {
			Thread.sleep(50L);
		}
		
		//We should now be rendering.
		assertTrue(renderer.isRendering());
	}

	/**
	 * Test for {@link Renderer#stopRendering()}.
	 * When stopRendering is called, rendering should stop within 5
	 * seconds.
	 * 
	 * @throws InterruptedException
	 * 		if we are interrupted while waiting.
	 */
	@Test
	public void testStopRendering() throws InterruptedException {
		//We aren't rendering yet.
		assertFalse(renderer.isRendering());
		
		//We start rendering
		renderer.startRendering();
		
		//Wait for rendering to start (max 5 seconds)
		for (int i = 0; i < 100 && !renderer.isRendering(); i++) {
			Thread.sleep(50L);
		}
		
		//Stop rendering
		renderer.stopRendering();
		
		//We wait for a maximum of 5 seconds for rendering to stop.
		for (int i = 0; i < 100 && renderer.isRendering(); i++) {
			Thread.sleep(50L);
		}
		
		//We should now no longer be rendering.
		assertFalse(renderer.isRendering());
	}

	/**
	 * Test for {@link Renderer#isRendering()}, when not rendering.
	 */
	@Test
	public void testIsRendering() {
		assertFalse(renderer.isRendering());
	}
	
	/**
	 * Test for {@link Renderer#isRendering()}, when rendering.
	 * 
	 * @throws InterruptedException
	 * 		if we are interrupted while waiting.
	 */
	@Test
	public void testIsRendering2() throws InterruptedException {
		//Begin rendering
		renderer.startRendering();
		
		//Wait for the renderer to start, for a maximum of 5 seconds.
		for (int i = 0; i < 100 && !renderer.isRendering(); i++) {
			Thread.sleep(50L);
		}
		
		//We should now be rendering
		assertTrue(renderer.isRendering());
	}

}
