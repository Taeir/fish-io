package com.github.fishio;

public interface IBoundingArea {
	
	public boolean intersects(IBoundingArea other);
	
	public double getMinX();
	public double getMaxX();
	
	public double getMinY();
	public double getMaxY();
	
	public double getWidth();
	public double getHeight();
	
	public double getSize();
	
	public void increaseSize(double delta);
	
	public void move(Vec2d speedVector);
	
	public double getRotation();
	
	@Deprecated
	public void move(Direction dir, double amount);

}
