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
	 * A method which gives back the minimal x coordinate of the Bounding Box.
	 * 
	 * @return the minimal x coordinate.
	 */
	double getMinX();
	
	/**
	 * A method which gives back the maximal x coordinate of the Bounding Box.
	 * 
	 * @return the maximal x coordinate.
	 */
	double getMaxX();
	
	/**
	 * A method which gives back the minimal y coordinate of the Bounding Box.
	 * 
	 * @return the minimal y coordinate.
	 */
	double getMinY();
	
	/**
	 * A method which gives back the maximal y coordinate of the Bounding Box.
	 * 
	 * @return the maximal y coordinate.
	 */
	double getMaxY();
	
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
}
