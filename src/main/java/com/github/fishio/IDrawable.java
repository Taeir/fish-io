package com.github.fishio;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

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

	/**
	 * Draw a rotated image on the given GraphicsContext.
	 * 
	 * @param gc
	 * 		The graphicsContext to draw on.
	 * @param image
	 * 		The image to draw.
	 * @param ca
	 * 		The CollisionArea of the sprite
	 * @param reverse
	 * 		when true reverse left/right orientation of the sprite.
	 * 
	 */
	default void drawRotatedImage(GraphicsContext gc, Image image, ICollisionArea ca, boolean reverse) {
		double angle = ca.getRotation();
		double cx = ca.getCenterX();
		double cy = ca.getCenterY();
		double width;
		if (reverse) {
			width = -ca.getWidth();
		} else {
			width = ca.getWidth();
		}
		double height = ca.getHeight();

		gc.save();
		Rotate r = new Rotate(angle, cx, cy);
		gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());

		gc.drawImage(image, cx - 0.5 * width, cy - 0.5 * height, width, height);
		gc.restore();

		// debug options
		if (true) {
			gc.fillRect(cx - 0.5 * ca.getWidth(), cy - 0.5 * height, ca.getWidth(), height);
			gc.setFill(Color.CYAN);
			gc.fillOval(cx, cy, 2, 2);	//draw sprite center (usefull for debug)
			gc.setFill(Color.RED);
			double d = Math.sqrt(0.25 * width * width + 0.25 * height * height);
			double dx = d * Math.cos(angle);
			double dy = d * Math.sin(angle);
			gc.fillOval(cx - dx, cy - dy, 2, 2);
			gc.fillOval(cx - dx, cy + dy, 2, 2);
			gc.fillOval(cx + dx, cy - dy, 2, 2);
			gc.fillOval(cx + dx, cy + dy, 2, 2);
		}
	}
}
