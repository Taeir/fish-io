package com.github.fishio;

import com.github.fishio.behaviours.IMoveBehaviour;

import javafx.geometry.Bounds;
import javafx.scene.shape.Rectangle;

/**
 * Interface used to represent collision areas of entities.
 */
public interface ICollisionArea {
	
	/**
	 * Calculates if a box intersection occurred between this and another CollisionArea.
	 * @param other 
	 * 		The other collisionArea
	 * @return
	 * 		True if they collide, false otherwise.
	 */
	default boolean boxIntersects(ICollisionArea other) {
		Rectangle myBox = getBox();
		Rectangle otherBox = other.getBox();
		
		return myBox.intersects(otherBox.getBoundsInParent());
	}
	
	/**
	 * @return
	 * 		a rectangular box around this ICollisionArea.
	 */
	default Rectangle getBox() {
		Rectangle tbr = new Rectangle(
				getCenterX() - 0.5 * getWidth(),
				getCenterY() - 0.5 * getHeight(),
				getWidth(),
				getHeight());
		
		tbr.setRotate(getRotation());
		return tbr;
	}
	
	/**
	 * @param minX
	 * 		the minimal X coordinate.
	 * @param minY
	 * 		the minimal Y coordinate.
	 * @param maxX
	 * 		the maximal X coordinate.
	 * @param maxY
	 * 		the maximal Y coordinate.
	 * 
	 * @return
	 * 		<code>true</code> if the center of this ICollisionArea is
	 * 		outside the given coordinates. <code>false</code> otherwise.
	 */
	default boolean isOutside(double minX, double minY, double maxX, double maxY) {
		Bounds b = getBox().getBoundsInParent();
		return b.getMinX() <= minX || b.getMaxX() >= maxX || b.getMinY() <= minY || b.getMaxY() >= maxY;
	}
	
	/**
	 * @return
	 * 		if the collision area should be flipped upside down.
	 */
	default boolean isReversed() {
		return getRotation() > 90 && getRotation() < 270;
	}

	
	/**
	 * A method which gives back the top left coordinate of the Bounding Box.
	 * 
	 * @return the top left coordinate.
	 */
	Vec2d getTopLeft();
	
	/**
	 * A method which gives back the to right coordinate of the Bounding Box.
	 * 
	 * @return the top right coordinate.
	 */
	Vec2d getTopRight();
	
	/**
	 * A method which gives back the bottom left coordinate of the Bounding Box.
	 * 
	 * @return the bottom left coordinate.
	 */
	Vec2d getBottomLeft();
	
	/**
	 * A method which gives back the bottom right coordinate of the Bounding Box.
	 * 
	 * @return the bottom right coordinate.
	 */
	Vec2d getBottomRight();
	
	/**
	 * A method which returns the x coordinate of the centre of the Bounding
	 * Box.
	 * 
	 * @return the x coordinate of the centre of this bounding box
	 */
	double getCenterX();
	
	/**
	 * A method which returns the y coordinate of the centre of the Bounding
	 * Box.
	 * 
	 * @return the y coordinate of the centre of this bounding box
	 */
	double getCenterY();
	
	/**
	 * A method which gives the width of the bounding box. The width is given
	 * along the length of the fish.
	 * 
	 * @return the width of this Bounding Box.
	 */
	double getWidth();
	
	/**
	 * A method which gives the height of the bounding box. The height is given
	 * along the height of the fish.
	 * 
	 * @return the height of this Bounding Box.
	 */
	double getHeight();
	
	/**
	 * Method which returns the area or size of the Bounding Box.
	 * 
	 * @return the size (width times height) of the BoundingBox
	 */
	double getSize();
	
	/**
	 * Set the size of the collisionArea.
	 * 
	 * @param size
	 * 		The new size.
	 */
	void setSize(double size);
	
	/**
	 * Increases the size (area) of the fish without
	 * affecting the width/height (shape stays the same).
	 * 
	 * @param delta
	 * 		The size to increase the current size with.
	 */
	void increaseSize(double delta);
	
	/**
	 * Moves this Bounding Box in the specified direction.
	 * 
	 * @param speedVector
	 *            The vector which specifies the direction the Bounding Box
	 *            should move at. The length of the vector is the speed.
	 */
	void move(Vec2d speedVector);
	
	/**
	 * Calculates and sets the rotation of a  IMovable object.
	 * 
	 * @param m
	 * 		the movable that has this collisionArea
	 * 
	 * @return
	 * 		the rotation of the boundingArea.
	 */
	default double setRotation(IMoveBehaviour m) {
		Vec2d sv = m.getSpeedVector();
		if (sv.x == 0) {
			if (sv.y > 0) {
				//Set rotation to 90 (UP)
				return setRotation(90);
			} else if (sv.y < 0) {
				//Set rotation to 270 (DOWN)
				return setRotation(270);
			} else {
				//not moving, so not updating rotation.
				return getRotation();
			}
		} else if (sv.y == 0) {
			if (sv.x >= 0) {
				//Set rotation to 0 (RIGHT)
				return setRotation(0);
			} else {
				//Set rotation to 180 (LEFT)
				return setRotation(180);
			}
		} else {
			if (sv.x < 0) {
				return setRotation(Math.toDegrees(Math.atan(sv.y / sv.x)) + 180);
			} else {
				return setRotation(Math.toDegrees(Math.atan(sv.y / sv.x)) + 360);
			}
		}
	}
	
	/**
	 * @return
	 * 		the rotation of the boundingArea in degrees.
	 */
	double getRotation();
	
	/**
	 * Sets the rotation of the CollisionArea.
	 * @param angle
	 * 		The new angle in degrees.
	 * @return
	 * 		The set value of the angle.
	 */
	double setRotation(double angle);

	/**
	 * @return
	 * 		the largest x coordinate of this ICollisionArea.
	 */
	default double getMaxX() {
		return getBox().getBoundsInParent().getMaxX();
	}
	
	/**
	 * @return
	 * 		the smallest x coordinate of this ICollisionArea.
	 */
	default double getMinX() {
		return getBox().getBoundsInParent().getMinX();
	}
	
	/**
	 * @return
	 * 		the largest y coordinate of this ICollisionArea.
	 */
	default double getMaxY() {
		return getBox().getBoundsInParent().getMaxY();
	}
	
	/**
	 * @return
	 * 		the smallest y coordinate of this ICollisionArea.
	 */
	default double getMinY() {
		return getBox().getBoundsInParent().getMinY();
	}
	
	/**
	 * Update the parameters of this ICollisionArea to those of the given
	 * area.
	 * 
	 * @param area
	 * 		the ICollisionArea to update the parameters to.
	 * 
	 * @throws IllegalArgumentException
	 * 		if the type of the given area and this area do not match.
	 */
	void updateTo(ICollisionArea area);
}
