package com.github.fishio;

import java.util.HashSet;

import com.github.fishio.settings.Settings;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;

/**
 * Class for checking collisions of sprites.
 */
public class CollisionMask implements ICollisionArea {

	private Vec2d center;

	private double width;

	private double height;

	private double rotation;

	private double alphaRatio;

	private boolean[][] data;

	/**
	 * Constructor for a CollisionMask.
	 * 
	 * @param center
	 *            The center of the collisionMask.
	 * @param width
	 *            The width of the collisionMask.
	 * @param height
	 *            The height of the collisionMask.
	 * @param data
	 *            The collision data.
	 * @param alphaRatio
	 *            The ratio between opaque and transparent pixels in the sprite
	 */
	public CollisionMask(Vec2d center, double width, double height,
			boolean[][] data, double alphaRatio) {
		this.center = center;
		this.width = width;
		this.height = height;

		rotation = 0;

		this.data = data;
		this.alphaRatio = alphaRatio;
	}
	
	/**
	 * Build the data used for checking collisions.
	 * 
	 * @param img
	 *            The image to build the data from.
	 * 
	 * @return The generated data set.
	 */
	public static boolean[][] buildData(Image img) {
		boolean[][] res = new boolean[(int) img.getWidth()][(int) img
				.getHeight()];
		PixelReader pr = img.getPixelReader();
		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				res[x][y] = pr.getColor(x, y).getOpacity() > 0.5;
			}
		}
		return res;
	}
	
	/**
	 * Calculate the relSize.
	 * 
	 * @param data
	 *            The alpha data of a sprite
	 * 
	 * @return The amount of opaque pixels
	 */
	public static double getAlphaRatio(boolean[][] data) {
		double res = 0;
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				if (data[i][j]) {
					res++;
				}
			}
		}
		return res / (data.length * data[0].length);
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
	public double getHeight() {
		return height;
	}

	/**
	 * Builds a hashSet containing all the (screen) pixels filled by the sprite.
	 * 
	 * @return The built hashSet.
	 */
	public HashSet<Vec2d> getMask() {
		HashSet<Vec2d> mask = new HashSet<Vec2d>();
		double x, y;
		int lx, ly; // location of the pixel in the image
		double cosa, sina;
		double rx, ry; // relative positions after rotation
		int px, py; // positions after translating from center

		cosa = Math.cos(Math.toRadians(360 - rotation));
		sina = Math.sin(Math.toRadians(360 - rotation));

		for (x = 0; x < width; x++) {
			for (y = 0; y < height; ++y) {
				int datax = (int) (x * (data.length / width));
				int datay = (int) (y * (data[0].length / height));
				//TODO get this to trigger (this line works): if(reverse) datax = data.length - 1 - datax;
				if (data[datax][datay]) {
					lx = (int) (x - width * 0.5);
					ly = (int) (y - height * 0.5);

					rx = cosa * lx - sina * ly;
					ry = sina * lx + cosa * ly;

					px = (int) (rx + center.x);
					py = (int) (ry + center.y);
					mask.add(new Vec2d(px, py));
				}
			}
		}
		return mask;
	}

	@Override
	public double getRotation() {
		return rotation;
	}

	@Override
	public double getSize() {
		return (width * height) * alphaRatio;
	}

	/**
	 * @return The offsets to the top left and bottom right corner as seen from
	 *         the center
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

	/**
	 * @return The offsets to the top right and bottom left corner as seen from
	 *         the center
	 */
	private Vec2d getTRBLCornerOffsets() {
		double tempX = 0.5 * width;
		double tempY = 0.5 * height;

		double a = Math.toRadians(rotation);
		double rx = tempX * Math.cos(a) - tempY * Math.sin(a);
		double ry = tempX * Math.sin(a) + tempY * Math.cos(a);
		return new Vec2d(rx, ry);
	}

	@Override
	public double getWidth() {
		return width;
	}

	@Override
	public void increaseSize(double delta) {
		double r = width / height;
		height = Math.sqrt((getSize() + delta) / (alphaRatio * r));
		width = height * r;
	}

	/**
	 * @see <a
	 *      href="http://forum.codecall.net/topic/65950-pixel-perfect-collision-detection-use-for-your-java-games/">
	 *      pixel-perfect sprite collision</a>
	 */
	@Override
	public boolean intersects(ICollisionArea other) {
		// check normal bounding
		if (boxIntersects(other)) { // use normal
			if (other instanceof BoundingBox) {
				return true;
			}
			if (other instanceof CollisionMask) { // do pixel perfect
				if (!Settings.getInstance().getBoolean("PIXEL_PERFECT_COLLISIONS")) {
					return true;
				}
				CollisionMask o = (CollisionMask) other;
				HashSet<Vec2d> mask = o.getMask();
				mask.retainAll(this.getMask());
				return mask.size() > 0;
			}
		}
		return false;
	}

	@Override
	public void move(Vec2d speedVector) {
		speedVector.y *= -1;
		center.add(speedVector);
	}

	@Override
	public double setRotation(double angle) {
		rotation = angle % 180;
		return rotation;
	}
	
	@Override
	public void setSize(double size) {
		double r = width / height;
		height = Math.sqrt(size / (alphaRatio * r));
		width = height * r;
	}

}
