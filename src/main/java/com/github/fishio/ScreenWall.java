package com.github.fishio;

/**
 * Represents a ScreenWall, a boundary of the visible screen.
 */
public class ScreenWall implements ICollidable, IPositional {
	private Direction side;
	private BoundingBox bb;
	
	/**
	 * @param side
	 * 		the side of the screen this wall is supposed to be.
	 */
	public ScreenWall(Direction side) {
		this.side = side;
		//TODO set boundingbox?
	}
	
	@Override
	public double getX() {
		//TODO
		return 0;
	}

	@Override
	public double getY() {
		//TODO
		return 0;
	}
	
	/**
	 * @return
	 * 		the side of the screen this wall is at.
	 */
	public Direction getSide() {
		return side;
	}

	@Override
	public boolean onCollide(ICollidable other) {
		//TODO Collision detection
		if (other instanceof IMoving) {
			((IMoving) other).hitWall();
		}
		
		return false;
	}

	@Override
	public BoundingBox getBoundingBox() {
		return bb;
	}

	@Override
	public boolean doesCollides(ICollidable other) {
		return false;
	}

}
