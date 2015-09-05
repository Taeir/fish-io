package com.github.fishio;

/**
 * Class to represent an (Axis Aligned) Bounding Box.
 */
public class BoundingBox {
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
	 * @return
	 * 		the minimal x coordinate.
	 */
	public double getMinX() {
		return xmin;
	}
	
	/**
	 * @return
	 * 		the maximal x coordinate.
	 */
	public double getMaxX() {
		return xmax;
	}
	
	/**
	 * @return
	 * 		the minimal y coordinate.
	 */
	public double getMinY() {
		return ymin;
	}
	
	/**
	 * @return
	 * 		the maximal y coordinate.
	 */
	public double getMaxY() {
		return ymax;
	}
	
	/**
	 * @return
	 * 		the x coordinate of the centre of this bounding box
	 */
	public double getCenterX() {
		return (xmax + xmin) / 2;
	}
	
	/**
	 * @return
	 * 		the y coordinate of the centre of this bounding box
	 */
	public double getCenterY() {
		return (ymax + ymin) / 2;
	}
	
	/**
	 * @return
	 * 		the width of this Bounding Box.
	 */
	public double getWidth() {
		return (xmax - xmin);
	}
	
	/**
	 * @return
	 * 		the height of this Bounding Box.
	 */
	public double getHeight() {
		return (ymax - ymin);
	}
	
	/**
	 * Moves this bounding box in the specified direction.
	 * 
	 * @param dir
	 * 		the direction to move in.
	 * @param amount
	 * 		the amount to move (speed).
	 */
	public void move(Direction dir, double amount) {
		Vec2d v = dir.getNormalVector();
		
		v.x *= amount;
		v.y *= amount;
		
		move(v);
	}
	
	/**
	 * Move this bounding box in the specified direction.
	 * 
	 * @param rad
	 * 		the radians of the direction to move in.
	 * @param amount
	 * 		the amount to move (speed).
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
	 * Moves this bounding box in the specified direction.
	 * 
	 * @param v
	 * 		The direction the BoundingBox should move at.
	 * 		The length of the vector is the speed.
	 */
	public void move(Vec2d v) {
		xmin += v.x;
		xmax += v.x;
		ymin += v.y;
		ymax += v.y;
	}

	/**
	 * @param other
	 * 		the bounding box to check with.
	 * @return
	 * 		if this bounding box collides with the given Bounding Box.
	 */
	public boolean intersects(BoundingBox other) {
		//TODO Collision detection
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
	
}
