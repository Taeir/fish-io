package com.github.fishio;

/**
 * Class to represent an (Axis Aligned) Bounding Box.
 */

public class BoundingBox implements ICollisionArea {
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

	@Override
	public double getMinX() {
		return xmin;
	}

	@Override
	public double getMaxX() {
		return xmax;
	}

	@Override
	public double getMinY() {
		return ymin;
	}

	@Override
	public double getMaxY() {
		return ymax;
	}

	@Override
	public double getCenterX() {
		return (xmax + xmin) / 2;
	}

	@Override
	public double getCenterY() {
		return (ymax + ymin) / 2;
	}

	@Override
	public double getWidth() {
		return xmax - xmin;
	}

	@Override
	public double getHeight() {
		return ymax - ymin;
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

	@Override
	public void move(Vec2d v) {
		xmin += v.x;
		xmax += v.x;
		ymin -= v.y;
		ymax -= v.y;
	}

	@Override
	public double getSize() {
		return getWidth() * getHeight();
	}

	@Override
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

	@Override
	public boolean intersects(ICollisionArea other) {		
		return this.xmin + this.getWidth() > other.getMinX()
				&& this.xmin < other.getMinX() + other.getWidth()
				&& this.ymin + this.getHeight() > other.getMinY()
				&& this.ymin < other.getMinY() + other.getHeight();
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
