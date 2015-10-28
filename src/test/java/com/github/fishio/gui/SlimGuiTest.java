package com.github.fishio.gui;

import javafx.stage.Stage;

/**
 * A GuiTest that only starts a "slim" version of the GUI: no window, no
 * FishIO instance, just JavaFX.
 */
public abstract class SlimGuiTest extends AppTest {
	@Override
	public void start(Stage stage) throws Exception { }
}
