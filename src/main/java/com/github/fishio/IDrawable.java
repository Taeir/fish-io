package com.github.fishio;

import java.util.HashSet;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

/**
 * Represents a drawable object.
 */
public interface IDrawable {
	boolean DEBUG = false;

	/**
	 * Called when this object dies.
	 * 
	 * @param gc
	 * 		the graphicscontext to rendpSrcer on.
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
	 * 
	 */
	default void drawRotatedImage(GraphicsContext gc, Image image, ICollisionArea ca) {
		double angle = ca.getRotation();
		double cx = ca.getCenterX();
		double cy = ca.getCenterY();
		double width = ca.getWidth();
		double height = ca.getHeight();
		if (ca.isReversed()) {
			height = -ca.getHeight();
		} else {
			height = ca.getHeight();
		}

		gc.save();
		Rotate r = new Rotate(360 - angle, cx, cy);
		gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());

		gc.drawImage(image, cx - 0.5 * width, cy - 0.5 * height, width, height);
		gc.restore();

		// debug rendering
		if (DEBUG) {
			gc.setFill(Color.RED);			
			gc.fillText("angle: " + angle,			//angle display 
					cx, cy - (0.5 * ca.getHeight() + 10));
			gc.fillText("size: " + ca.getSize(),	//size display
					cx, cy - (0.5 * ca.getHeight() + 25));

			if (ca instanceof CollisionMask) {
				gc.setFill(Color.FUCHSIA);
				HashSet<Vec2d> mask = ((CollisionMask) ca).getMask();
				for (Vec2d v : mask) {
					gc.fillOval(v.x, v.y, 1, 1);
				}
			}

			gc.setFill(Color.CYAN);
			gc.fillOval(cx, cy, 2, 2);	//draw sprite center

			// draw CollisionArea box corners
			Vec2d tl = ca.getTopLeft();
			Vec2d tr = ca.getTopRight();
			Vec2d bl = ca.getBottomLeft();
			Vec2d br = ca.getBottomRight();

			gc.setFill(Color.RED);	
			gc.fillOval(tl.x, tl.y, 2, 2);
			gc.fillOval(tr.x, tr.y, 2, 2);
			gc.fillOval(bl.x, bl.y, 2, 2);
			gc.fillOval(br.x, br.y, 2, 2);
		}
	}
}
