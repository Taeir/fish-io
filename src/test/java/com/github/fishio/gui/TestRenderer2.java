package com.github.fishio.gui;

import static org.mockito.Mockito.mock;

import javafx.scene.canvas.Canvas;

import com.github.fishio.SinglePlayerPlayingField;
import com.github.fishio.listeners.Listenable;
import com.github.fishio.listeners.TestListenable;

/**
 * Test for the {@link Renderer} class.<br>
 * <br>
 * Since {@link TestRenderer} has to extend {@link GuiTest}, it is
 * unable to also extend the interface test class {@link TestListenable},
 * so that is done here.
 */
public class TestRenderer2 extends TestListenable {

	@Override
	public Listenable getListenable() {
		return new Renderer(mock(SinglePlayerPlayingField.class), new Canvas(), 60, 0);
	}

}
