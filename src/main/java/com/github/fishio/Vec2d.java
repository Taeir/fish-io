package com.github.fishio;

/**
 * Class to represent a 2D Vector.
 */
public class Vec2d {
	//We want these fields public for easy use.
	
	@SuppressWarnings("checkstyle:visibilitymodifier")
	public double x;
	@SuppressWarnings("checkstyle:visibilitymodifier")
	public double y;

	/**
	 * Creates a new vector with x and y set to 0.
	 */
	public Vec2d() { }

	/**
	 * @param x
	 * 		the x coordinate of the vector.
	 * @param y
	 * 		the y coordinate of the vector.
	 */
	public Vec2d(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Creates a new Vector with the same x and y as the given
	 * Vector.
	 * @param v
	 * 		the vector to copy the coordinates of.
	 */
	public Vec2d(Vec2d v) {
		this.x = v.x;
		this.y = v.y;
	}

	/**
	 * Sets the value of x to the given value.
	 * @param x
	 * 		the new value of x.
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Sets the value of y to the given value.
	 * @param y
	 * 		the new value of y.
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Sets the x and y of this vector to the given values.
	 * 
	 * @param x
	 * 		the new value of x.
	 * @param y
	 * 		the new value of y.
	 */
	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @param v
	 * 		the vector to calculate the distance to.
	 * @return
	 * 		the squared distance to the given vector.
	 */
	public double distanceSquared(Vec2d v) {
		final double vx = v.x - this.x;
		final double vy = v.y - this.y;
		return (vx * vx + vy * vy);
	}

	/**
	 * @param vx
	 * 		the x of the point.
	 * @param vy
	 * 		the y of the point.
	 * @return
	 * 		the distance to the given point.
	 */
	public double distance(double vx, double vy) {
		vx -= x;
		vy -= y;
		return Math.sqrt(vx * vx + vy * vy);
	}

	/**
	 * @param v
	 * 		the vector to calculate the distance to.
	 * @return
	 * 		the distance to the given vector.
	 */
	public double distance(Vec2d v) {
		final double vx = v.x - this.x;
		final double vy = v.y - this.y;
		return Math.sqrt(vx * vx + vy * vy);
	}

	/**
	 * @return
	 * 		the squared length of this vector.
	 */
	public double lengthSquared() {
		return this.x * this.x + this.y * this.y;
	}

	/**
	 * @return
	 * 		the length of this vector.
	 */
	public double length() {
		return Math.sqrt(lengthSquared()); 
	}

	/**
	 * @return
	 * 		a new vector that is normalized.
	 * @throws ArithmeticException
	 * 		if the length of this vector is 0.
	 */
	public Vec2d normalize() {
		double l = length();
		if (l == 0) {
			throw new ArithmeticException("Length is 0, cannot normalize!");
		}

		final double lInv = 1 / l;
		return new Vec2d(lInv * x, lInv * y);
	}

	@Override
	public int hashCode() {
		long bits = 7L;
		bits = 31L * bits + Double.doubleToLongBits(x);
		bits = 31L * bits + Double.doubleToLongBits(y);
		return (int) (bits ^ (bits >> 32));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Vec2d)) {
			return false;
		}

		Vec2d other = (Vec2d) obj;
		return (x == other.x) && (y == other.y);
	}

	@Override
	public String toString() {
		return "Vec2d [x=" + x + ", y=" + y + "]";
	}
}
