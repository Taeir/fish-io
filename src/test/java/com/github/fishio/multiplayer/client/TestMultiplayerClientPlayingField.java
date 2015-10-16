package com.github.fishio.multiplayer.client;

import javafx.scene.canvas.Canvas;

import com.github.fishio.TestPlayingField;

/**
 * Test class for the MultiplayerClientPlayingField.
 */
public class TestMultiplayerClientPlayingField extends TestPlayingField {
	
	@Override
	public MultiplayerClientPlayingField getPlayingField(int fps, Canvas canvas) {
		return new MultiplayerClientPlayingField(fps, canvas);
	}

	@Override
	public int getDefaultAmount() {
		return 0;
	}
}
