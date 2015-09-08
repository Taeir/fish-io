package com.github.fishio;

/**
 * Class to represent an (Axis Aligned) Bounding Box.
 */

public class BoundingBox implements IBoundingArea {
	private double xmin;
	private double ymin;
	private double xmax;
	private double ymax;

	/**
	 * Creates a new Bounding Box with the given coordinates.
	 * 
	 * @param xmin
	 * 		the minimal x coordinate
	 * @param ymin
	 * 		the minimal y coordinate
	 * @param xmax
	 * 		the largest x coordinate
	 * @param ymax
	 * 		the largest y coordinate
	 */
	public BoundingBox(double xmin, double ymin, double xmax, double ymax) {
		this.xmin = xmin;
		this.ymin = ymin;
		this.xmax = xmax;
		this.ymax = ymax;
	}

	/**
	 * Creates a new Bounding Box with the given size and center.
	 * 
	 * @param position
	 *            the position of the center of the box.
	 * @param width
	 *            the width of the box along the length of the fish.
	 * @param height
	 *            the height of the box along the height of the fish.
	 */
	public BoundingBox(Vec2d position, double width, double height) {
		this.xmin = position.x - 0.5 * width;
		this.ymin = position.y - 0.5 * height;
		this.xmax = position.x + 0.5 * width;
		this.ymax = position.y + 0.5 * height;
	}

	/**
	 * A method which gives back the minimal x coordinate of the Bounding Box.
	 * 
	 * @return the minimal x coordinate.
	 */
	public double getMinX() {
		return xmin;
	}

	/**
	 * A method which gives back the maximal x coordinate of the Bounding Box.
	 * 
	 * @return the maximal x coordinate.
	 */
	public double getMaxX() {
		return xmax;
	}

	/**
	 * A method which gives back the minimal y coordinate of the Bounding Box.
	 * 
	 * @return the minimal y coordinate.
	 */
	public double getMinY() {
		return ymin;
	}

	/**
	 * A method which gives back the maximal y coordinate of the Bounding Box.
	 * 
	 * @return the maximal y coordinate.
	 */
	public double getMaxY() {
		return ymax;
	}

	/**
	 * A method which returns the x coordinate of the centre of the Bounding
	 * Box.
	 * 
	 * @return the x coordinate of the centre of this bounding box
	 */
	public double getCenterX() {
		return (xmax + xmin) / 2;
	}

	/**
	 * A method which returns the y coordinate of the centre of the Bounding
	 * Box.
	 * 
	 * @return the y coordinate of the centre of this bounding box
	 */
	public double getCenterY() {
		return (ymax + ymin) / 2;
	}

	/**
	 * A method which gives the width of the bounding box. The width is given
	 * along the length of the fish.
	 * 
	 * @return the width of this Bounding Box.
	 */
	public double getWidth() {
		return xmax - xmin;
	}

	/**
	 * A method which gives the height of the bounding box. The height is given
	 * along the height of the fish.
	 * 
	 * @return the height of this Bounding Box.
	 */
	public double getHeight() {
		return ymax - ymin;
	}

	/**
	 * Moves this bounding box in the specified direction.
	 * 
	 * @param dir
	 *            the vector which specifies the direction to move in.
	 * @param amount
	 *            the amount to move (speed).
	 */
	public void move(Direction dir, double amount) {
		Vec2d v = dir.getNormalVector();

		v.x *= amount;
		v.y *= amount;

		move(v);
	}

	/**
	 * Move this Bounding Box in the specified direction.
	 * 
	 * @param rad
	 *            the radians of the direction to move in.
	 * @param amount
	 *            the amount to move (speed).
	 */
	@Deprecated
	public void move(double rad, double amount) {
		double dx = Math.cos(rad);
		double dy = Math.sin(rad);

		Vec2d v = new Vec2d(dx, dy);
		if (v.lengthSquared() > 0) {
			v = v.normalize();
		}

		v.x *= amount;
		v.y *= amount;

		xmin += v.x;
		xmax += v.x;
		ymin += v.y;
		ymax += v.y;
	}

	/**
	 * Moves this Bounding Box in the specified direction.
	 * 
	 * @param v
	 *            The vector which specifies the direction the Bounding Box
	 *            should move at. The length of the vector is the speed.
	 */
	public void move(Vec2d v) {
		xmin += v.x;
		xmax += v.x;
		ymin -= v.y;
		ymax -= v.y;
	}

	/**
	 * Method which returns the area or size of the Bounding Box.
	 * 
	 * @return the size (width times height) of the BoundingBox
	 */
	public double getSize() {
		return getWidth() * getHeight();
	}

	/**
	 * Increases the size (area) of the fish without
	 * affecting the width/height (shape stays the same).
	 * 
	 * @param size
	 * 		The size to increase the current size with.
	 */
	public void increaseSize(double size) {
		double w = getWidth();
		double h = getHeight();
		double c = w / h;

		double b = Math.sqrt((w * h + size) / c) - h;
		double a = c * (h + b) - w;

		xmax += 0.5 * a;
		xmin -= 0.5 * a;

		ymax += 0.5 * b;
		ymin -= 0.5 * b;
	}

	/**
	 * Performs a few checks to find out whether the Bounding Box has any
	 * overlap with an IBoundingAres object.
	 * 
	 * @param other
	 *            the boundingArea to check with.
	 * @return true if this bounding box collides with the given BoundingArea,
	 *         false if not.
	 */
	public boolean intersects(IBoundingArea other) {
		if (other instanceof BoundingBox) {
			BoundingBox obb = (BoundingBox) other;
			
			return this.xmin + this.getWidth() > obb.xmin
					&& this.xmin < obb.xmin + obb.getWidth()
					&& this.ymin + this.getHeight() > obb.ymin
					&& this.ymin < obb.ymin + obb.getHeight();
		}
		
		/*if (other instanceof CollisionMask) {
			return false; //TODO
		}*/
		
		return false;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(xmax);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(xmin);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(ymax);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(ymin);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BoundingBox)) {
			return false;
		}
		BoundingBox other = (BoundingBox) obj;
		if (Double.doubleToLongBits(xmax) != Double.doubleToLongBits(other.xmax)) {
			return false;
		}
		if (Double.doubleToLongBits(xmin) != Double.doubleToLongBits(other.xmin)) {
			return false;
		}
		if (Double.doubleToLongBits(ymax) != Double.doubleToLongBits(other.ymax)) {
			return false;
		}
		if (Double.doubleToLongBits(ymin) != Double.doubleToLongBits(other.ymin)) {
			return false;
		}
		return true;
	}

	@Override
	public double getRotation() {
		return 0;	//TODO (maybe)
	}

}
