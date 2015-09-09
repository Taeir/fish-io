package com.github.fishio;

import java.util.HashSet;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

/**
 * Class for checking collisions of sprites.
 */
public class CollisionMask implements ICollisionArea {

	private Vec2d center;
	private double width;
	private double height;
	
	private double rotation;
	
	private double size;
	
	private boolean[][] data;
	
	/**
	 * Constructor for a CollisionMask.
	 * 
	 * @param center
	 * 		The center of the collisionMask.
	 * @param width
	 * 		The width of the collisionMask.
	 * @param height
	 * 		The height of the collisionMask.
	 * @param data
	 * 		The collision data.
	 */
	public CollisionMask(Vec2d center, double width, double height, boolean[][] data) {
		this.center = center;
		this.width = width;
		this.height = height;
		
		rotation = 0;
		this.size = width * height;
		
		this.data = data;
	}
	
	/**
	 * Build the data used for checking collisions.
	 * 
	 * @param img
	 * 		The image to build the data from.
	 * 
	 * @return
	 * 		The generated data set.
	 */
	public static boolean[][] buildData(Image img) {
		boolean[][] res = new boolean[(int) img.getWidth()][(int) img.getHeight()];
		PixelReader pr = img.getPixelReader();
		for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                res[x][y] = pr.getColor(x, y).getOpacity() < 0.1;
            }
        }
		return res;
	}

	/**
	 * @see <a href="http://forum.codecall.net/topic/65950-pixel-perfect-collision-detection-use-for-your-java-games/">
	 * 		pixel-perfect sprite collision</a>
	 */
	@Override
	public boolean intersects(ICollisionArea other) {
		// TODO get the pseudo code working
		
		// check normal bounding
		// if(boxColliding)
			// if(instanceOf BoundingBox)
			//		return true;
			// if (instanceOf CollisionMask)
			//		cast other
			//		return other.getMask().retainAll(this.getMask()).size() > 0
		return false;
	}

	@Override
	public double getMinX() {
		return center.x - 0.5 * width; //TODO use rotation
	}

	@Override
	public double getMaxX() {
		return center.x + 0.5 * width; //TODO use rotation
	}

	@Override
	public double getMinY() {
		return center.y - 0.5 * height; //TODO use rotation
	}

	@Override
	public double getMaxY() {
		return center.y + 0.5 * height; //TODO use rotation
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
	public double getSize() {
		return size;
	}

	@Override
	public void increaseSize(double delta) {
		double w = getWidth();
		double h = getHeight();
		double c = w / h;

		width = Math.sqrt((w * h + size) / c) - h;
		height = c * (h + width) - w;
	}

	@Override
	public void move(Vec2d speedVector) {
		center.add(speedVector);
	}

	@Override
	public double setRotation(IMovable m) {
		return rotation;
	}
	
	@Override
	public double getRotation() {
		return rotation;
	}
	
	/**
	 * Builds a hashSet containing all the (screen) pixels filled by the sprite.
	 * @return
	 * 		The built hashSet.
	 */
	public HashSet<String> getMask() {
		//TODO
		return null;
		
	}

}
