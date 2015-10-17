package com.github.fishio.multiplayer.client;

import javafx.scene.canvas.Canvas;

import com.github.fishio.multiplayer.TestMultiplayerPlayingField;

/**
 * Test class for the MultiplayerClientPlayingField.
 */
public class TestMultiplayerClientPlayingField extends TestMultiplayerPlayingField {
	
	@Override
	public MultiplayerClientPlayingField getPlayingField(int fps, Canvas canvas) {
		return new MultiplayerClientPlayingField(fps, canvas);
	}
}
