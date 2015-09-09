package com.github.fishio;

/**
 * Class to represent an (Axis Aligned) Bounding Box.
 */

public class BoundingBox implements ICollisionArea {
	private Vec2d center;
	private double height;
	private double width;
	private double rotation;
	
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
		this.width = xmax - xmin;
		this.height = ymax - ymin;
		this.center = new Vec2d(xmin + 0.5 * width, ymin + 0.5 * height);
		this.rotation = 0;
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
		this.center = position;
		this.width = width;
		this.height = height;
		this.rotation = 0;
	}

	@Override
	public double getMinX() {
		return center.x - 0.5 * width;
	}

	@Override
	public double getMaxX() {
		return center.x + 0.5 * width;
	}

	@Override
	public double getMinY() {
		return center.y - 0.5 * height;
	}

	@Override
	public double getMaxY() {
		return center.y + 0.5 * height;
	}

	@Override
	public double getCenterX() {
		return center.x;
	}

	@Override
	public double getCenterY() {
		return center.y;
	}

	@Override
	public double getWidth() {
		return width;
	}

	@Override
	public double getHeight() {
		return height;
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

		center.add(v);
	}

	@Override
	public void move(Vec2d v) {
		v.y = -v.y;
		center.add(v);
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

		width += a;
		height += b;
	}

	@Override
	public boolean intersects(ICollisionArea other) {
		double xmin = getMinX();
		double ymin = getMinY();
		
		if (xmin + this.getWidth() > other.getMinX()
				&& xmin < other.getMinX() + other.getWidth()
				&& ymin + this.getHeight() > other.getMinY()
				&& ymin < other.getMinY() + other.getHeight()) {
			return true;
		}

		return false;
	}


	@Override
	public double setRotation(IMovable m) {
		Vec2d sv = m.getSpeedVector();
		if (sv.x != 0) {
			rotation = 360 - Math.toDegrees(Math.atan(sv.y / sv.x));
		} else {
			if (sv.y > 0) {
				rotation = 270;
			} else if (sv.y < 0) {
				rotation = 90;
			} else {
				rotation = 0;
			}
		}		
		return rotation;
	}
	
	@Override
	public double getRotation() {
		return rotation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		if (center == null) {
			result *= prime;
		} else {
			result = prime * result + center.hashCode();
		}
		long temp;
		temp = Double.doubleToLongBits(height);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(rotation);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(width);
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
		if (center == null) {
			if (other.center != null) {
				return false;
			}
		} else if (!center.equals(other.center)) {
			return false;
		}
		if (Double.doubleToLongBits(height) != Double.doubleToLongBits(other.height)) {
			return false;
		}
		if (Double.doubleToLongBits(rotation) != Double.doubleToLongBits(other.rotation)) {
			return false;
		}
		if (Double.doubleToLongBits(width) != Double.doubleToLongBits(other.width)) {
			return false;
		}
		return true;
	}

	
}
