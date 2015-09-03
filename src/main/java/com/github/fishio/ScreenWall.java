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
		if (other instanceof IMovable) {
			((IMovable) other).hitWall();
		}
		
		return false;
	}

	@Override
	public BoundingBox getBoundingBox() {
		return bb;
	}

}
