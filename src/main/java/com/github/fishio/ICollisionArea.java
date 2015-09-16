package com.github.fishio;

/**
 * Interface used to represent collision areas of entities.
 */
public interface ICollisionArea {
	
	/**
	 * Performs a few checks to find out whether the Bounding Box has any
	 * overlap with an IBoundingAres object.
	 * 
	 * @param other
	 *            the boundingArea to check with.
	 * @return true if this bounding box collides with the given BoundingArea,
	 *         false if not.
	 */
	boolean intersects(ICollisionArea other);
	
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
	double setRotation(IMovable m);
	
	/**
	 * @return
	 * 		the rotation of the boundingArea in degrees.
	 */
	double getRotation();
	
	/**
	 * @return
	 * 		the largest x coordinate of this ICollisionArea.
	 */
	default double getMaxX() {
		return Math.max(getTopRight().x, getBottomRight().x);
	}
	
	/**
	 * @return
	 * 		the smallest x coordinate of this ICollisionArea.
	 */
	default double getMinX() {
		return Math.min(getTopLeft().x, getBottomLeft().x);
	}
	
	/**
	 * @return
	 * 		the largest y coordinate of this ICollisionArea.
	 */
	default double getMaxY() {
		return Math.max(getBottomLeft().y, getBottomRight().y);
	}
	
	/**
	 * @return
	 * 		the smallest y coordinate of this ICollisionArea.
	 */
	default double getMinY() {
		return Math.min(getTopLeft().y, getTopRight().y);
	}
}
