package com.github.fishio.gui;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javafx.scene.image.Image;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.github.fishio.PlayingField;

/**
 * Tests powermock requiring methods of {@link Renderer}.
 */
@PrepareForTest(Image.class)
@RunWith(PowerMockRunner.class)
public class TestRenderer2 {
	/**
	 * Test for {@link Renderer#setBackground(Image)} with error image.
	 */
	@Test
	public void testSetBackgroundError() {
		Renderer renderer = new Renderer(mock(PlayingField.class), null, 60, 0);
		
		//Set a good image as the background.
		Image goodImage = mock(Image.class);
		renderer.setBackground(goodImage);
		
		//Try set a bad image as the background.
		Image badImage = PowerMockito.mock(Image.class);
		when(badImage.isError()).thenReturn(true);
		renderer.setBackground(badImage);
		
		//The old image should still be in place.
		assertSame(goodImage, renderer.getBackground());
	}
}
