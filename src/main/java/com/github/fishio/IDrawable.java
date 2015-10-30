package com.github.fishio;

import java.util.HashSet;

import com.github.fishio.settings.Settings;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

/**
 * Represents a drawable object.
 */
public interface IDrawable {

	/**
	 * Called when this object should be rendered.
	 * 
	 * @param gc
	 * 		the {@link GraphicsContext} to render on.
	 */
	void render(GraphicsContext gc);

	/**
	 * Draw a rotated image on the given {@link GraphicsContext}.
	 * 
	 * @param gc
	 * 		The {@link GraphicsContext} to draw on.
	 * @param image
	 * 		The image to draw.
	 * @param collisionArea
	 * 		The CollisionArea of the sprite
	 */
	default void drawRotatedImage(GraphicsContext gc, Image image, ICollisionArea collisionArea) {
		double angle = collisionArea.getRotation();
		double cx = collisionArea.getCenterX();
		double cy = collisionArea.getCenterY();
		double width = collisionArea.getWidth();
		double height = collisionArea.getHeight();
		if (collisionArea.isReversed()) {
			height = -collisionArea.getHeight();
		} else {
			height = collisionArea.getHeight();
		}

		gc.save();
		Rotate r = new Rotate(360 - angle, cx, cy);
		gc.transform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());

		gc.drawImage(image, cx - 0.5 * width, cy - 0.5 * height, width, height);
		gc.restore();

		if (Settings.getInstance().getBoolean("DEBUG_DRAW")) {
			debugDraw(gc, collisionArea);
		}
	}

	/**
	 * Method for debug drawing. Debug drawing shows collision masks over
	 * entities and shows the angle and size.
	 * 
	 * @param gc
	 * 		the {@link GraphicsContext} to render on.
	 * @param collisionArea
	 * 		the collision area to render.
	 */
	default void debugDraw(GraphicsContext gc, ICollisionArea collisionArea) {
		double angle = collisionArea.getRotation();
		double cx = collisionArea.getCenterX();
		double cy = collisionArea.getCenterY();
		double height = collisionArea.getHeight();
		
		gc.setFill(Color.RED);
		
		gc.fillText("angle: " + angle, cx, cy - (0.5 * height + 10)); //Angle display
		gc.fillText("size: " + collisionArea.getSize(),	cx, cy - (0.5 * height + 25)); //Size display
		
		if (collisionArea instanceof CollisionMask) {
			gc.setFill(Color.FUCHSIA);
			HashSet<Vec2d> mask = ((CollisionMask) collisionArea).getMask();
			for (Vec2d v : mask) {
				gc.fillOval(v.x, v.y, 1, 1);
			}
		}

		gc.setFill(Color.CYAN);
		gc.fillOval(cx, cy, 2, 2);	//draw sprite center

		//draw CollisionArea box corners
		Vec2d tl = collisionArea.getTopLeft();
		Vec2d tr = collisionArea.getTopRight();
		Vec2d bl = collisionArea.getBottomLeft();
		Vec2d br = collisionArea.getBottomRight();

		gc.setFill(Color.RED);	
		gc.fillOval(tl.x, tl.y, 2, 2);
		gc.fillOval(tr.x, tr.y, 2, 2);
		gc.fillOval(bl.x, bl.y, 2, 2);
		gc.fillOval(br.x, br.y, 2, 2);
	}
}
