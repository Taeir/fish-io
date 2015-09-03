package com.github.fishio;

import javafx.scene.canvas.GraphicsContext;

/**
 * Represents a drawable object.
 */
public interface IDrawable {
	/**
	 * Called when this object dies.
	 * 
	 * @param gc
	 * 		the graphicscontext to render on.
	 */
	void drawDeath(GraphicsContext gc);
	
	/**
	 * Called when this object should be rendered.
	 * 
	 * @param gc
	 * 		the graphicscontext to render on.
	 */
	void render(GraphicsContext gc);
}
