package com.github.fishio;

import java.io.Serializable;

import javafx.scene.shape.Rectangle;

/**
 * Class to represent an (Axis Aligned) Bounding Box.
 */

public class BoundingBox implements ICollisionArea, Serializable {
	private static final long serialVersionUID = 8596897445938544587L;
	
	private Vec2d center;
	private double height;
	private double width;
	private double rotation;
	
	private transient Rectangle box;

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
		this.width = Math.abs(xmax - xmin);
		this.height = Math.abs(ymax - ymin);
		
		double xxMin = Math.min(xmin, xmax);
		double yyMin = Math.min(ymin, ymax);
		this.center = new Vec2d(xxMin + 0.5 * width, yyMin + 0.5 * height);
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
	public Rectangle getBox() {
		//Don't recreate the box every time, simply update it.
		if (box == null) {
			box = new Rectangle();
		}
		
		box.setX(getCenterX() - 0.5 * getWidth());
		box.setY(getCenterY() - 0.5 * getHeight());
		box.setWidth(getWidth());
		box.setHeight(getHeight());
		box.setRotate(getRotation());
		
		return box;
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

	@Override
	public double setRotation(double angle) {
		rotation = angle % 180;	//get rid of upside down boxes
		return rotation;
	}

	@Override
	public void setSize(double size) {
		double r = width / height;
		height = Math.sqrt(size / r);
		width = height * r;
	}
	
	@Override
	public void updateTo(ICollisionArea area) {
		if (!(area instanceof BoundingBox)) {
			throw new IllegalArgumentException("Cannot update to different type!");
		}
		
		BoundingBox box = (BoundingBox) area;
		this.center.x = box.center.x;
		this.center.y = box.center.y;
		this.width = box.width;
		this.height = box.height;
		this.rotation = box.rotation;
	}
	
	/**
	 * Setter for the height of the box.
	 * @param newValue
	 * 		The new height
	 */
	public void setHeight(double newValue) {
		height = newValue;
	}
	
	/**
	 * Setter for the width of the box.
	 * @param newValue
	 * 		The new width
	 */
	public void setWidth(double newValue) {
		width = newValue;
	}
	
	/**
	 * @return
	 * 		The center of the box.
	 */
	public Vec2d getCenter() {
		return center;
	}

	/**
	 * Move the center of the box to the specified position.
	 * @param center
	 * 		The new center of the box.
	 */
	public void moveTo(Vec2d center) {
		this.center = center;
	}
}
