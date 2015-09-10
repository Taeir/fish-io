package com.github.fishio;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

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

	/**
	 * @return 
	 * 		The offsets to the top right and bottom left corner as seen from the center 
	 */
	private Vec2d getTRBLCornerOffsets() {
		double tempX = 0.5 * width;
		double tempY = 0.5 * height;

		double a = Math.toRadians(rotation);
		double rx = tempX * Math.cos(a) - tempY * Math.sin(a);
		double ry = tempX * Math.sin(a) + tempY * Math.cos(a);
		return new Vec2d(rx, ry);
	}

	/**
	 * @return 
	 * 		The offsets to the top left and bottom right corner as seen from the center 
	 */
	private Vec2d getTLBRCornerOffsets() {
		double tempX = 0.5 * width;
		double tempY = 0.5 * height;

		double a = Math.toRadians(rotation);
		double rx = tempX * Math.cos(a) + tempY * Math.sin(a);
		double ry = tempX * Math.sin(a) - tempY * Math.cos(a);
		return new Vec2d(rx, ry);
	}

	@Override
	public Vec2d getTopLeft() {
		Vec2d d = getTLBRCornerOffsets();
		return new Vec2d(center.x - d.x, center.y + d.y);
	}

	@Override
	public Vec2d getTopRight() {
		Vec2d d = getTRBLCornerOffsets();
		return new Vec2d(center.x + d.x, center.y - d.y);
	}	

	@Override
	public Vec2d getBottomLeft() {
		Vec2d d = getTRBLCornerOffsets();
		return new Vec2d(center.x - d.x, center.y + d.y);
	}

	@Override
	public Vec2d getBottomRight() {
		Vec2d d = getTLBRCornerOffsets();
		return new Vec2d(center.x + d.x, center.y - d.y);
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

		Shape rect1 = new Rectangle((int) (getCenterX() - 0.5 * width), 
				(int) (getCenterY() - 0.5 * height), 
				(int) width, (int) height);
		Rectangle rect2 = new Rectangle((int) (other.getCenterX() - 0.5 * other.getWidth()), 
				(int) (other.getCenterY() - 0.5 * other.getHeight()), 
				(int) other.getWidth(), (int) other.getHeight());

		AffineTransform t1 = new AffineTransform();
		AffineTransform t2 = new AffineTransform();
		
		t1.rotate(Math.toRadians(this.getRotation()), 
				this.getCenterX(), this.getCenterY()); //rotate self
		t2.rotate(Math.toRadians(other.getRotation()), 
				other.getCenterX(), other.getCenterY()); //rotate around other
		
		rect1 = t1.createTransformedShape(rect1);
		rect1 = t2.createTransformedShape(rect1);

		return rect1.intersects(rect2);
	}


	@Override
	public double setRotation(IMovable m) {
		Vec2d sv = m.getSpeedVector();
		if (sv.x == 0) {
			if (sv.y > 0) {
				rotation = 270;
			} else if (sv.y < 0) {
				rotation = 90;
			} else {
				rotation = 0;
			}
		} else if (sv.y == 0) {
			if (sv.x >= 0) {
				rotation = 0;
			} else {
				rotation = 180;
			}
		} else {
			rotation = Math.toDegrees(Math.atan(sv.y / sv.x));
		}

		rotation %= 180;	//get rid of upside down boxes
		
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
