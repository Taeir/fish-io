package com.github.fishio;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;

import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;
import com.github.fishio.settings.Settings;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.shape.Rectangle;

/**
 * Class for checking collisions of sprites.
 */
public class CollisionMask implements ICollisionArea, Serializable {
	private static final long serialVersionUID = -2752164590884475330L;

	private Vec2d center;

	private double width;
	private double height;

	private double rotation;

	private double alphaRatio;
	private transient boolean[][] data;

	private transient volatile Rectangle box;

	/**
	 * Creates a new CollisionMask.
	 * 
	 * @param center
	 *            The center of the collisionMask.
	 * @param width
	 *            The width of the collisionMask.
	 * @param height
	 *            The height of the collisionMask.
	 * @param maskedImage
	 *            The {@link Sprite} to get the pixeldata and alpha ratio from.
	 */
	public CollisionMask(Vec2d center, double width, double height, Sprite maskedImage) {
		this(center, width, height, maskedImage.getPixelData(), maskedImage.getAlphaRatio());
	}
	
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
	public CollisionMask(Vec2d center, double width, double height, boolean[][] data, double alphaRatio) {
		this.center = center;
		this.width = width;
		this.height = height;

		this.rotation = 0;

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
		int width = (int) img.getWidth();
		int heigth = (int) img.getHeight();
		
		boolean[][] res = new boolean[width][heigth];
		
		PixelReader pr = img.getPixelReader();
		for (int y = 0; y < heigth; y++) {
			for (int x = 0; x < width; x++) {
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
		int res = 0;
		for (boolean[] row : data) {
			for (boolean pixel : row) {
				if (pixel) {
					res++;
				}
			}
		}

		return ((double) res) / (data.length * data[0].length);
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
		return (width * height) * alphaRatio;
	}
	
	@Override
	public void setSize(double size) {
		double r = width / height;
		height = Math.sqrt(size / (alphaRatio * r));
		width = height * r;
	}
	
	@Override
	public void increaseSize(double delta) {
		double r = width / height;
		height = Math.sqrt((getSize() + delta) / (alphaRatio * r));
		width = height * r;
	}
	
	@Override
	public double getRotation() {
		return rotation;
	}
	
	@Override
	public double setRotation(double angle) {
		rotation = angle % 360;
		return rotation;
	}

	/**
	 * Builds a hashSet containing all the (screen) pixels filled by the sprite.
	 * 
	 * @return The built hashSet.
	 */
	public HashSet<Vec2d> getMask() {
		double width = this.width;
		double height = this.height;
		double rotation = this.rotation;
		
		HashSet<Vec2d> mask = new HashSet<Vec2d>();
		int lx, ly; // location of the pixel in the image
		double cosa, sina;
		double rx, ry; // relative positions after rotation
		int px, py; // positions after translating from center

		cosa = Math.cos(Math.toRadians(360 - rotation));
		sina = Math.sin(Math.toRadians(360 - rotation));
		
		double scalex = data.length / width;
		double scaley = data[0].length / height;

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int datax = (int) (x * scalex);
				int datay = (int) (y * scaley);
				
				//Flip y
				if (isReversed()) {
					datay = data[0].length - 1 - datay;
				}
				
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
		
		if (width != this.width || height != this.height) {
			Log.getLogger().log(LogLevel.DEBUG, "[CollisionMask] Size changed while creating mask!");
		}
		
		return mask;
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

	/**
	 * @return
	 * 		the offsets to the top left and bottom right corner as seen
	 * 		from the center.
	 */
	private Vec2d getTLBRCornerOffsets() {
		double tempX = 0.5 * width;
		double tempY = 0.5 * height;

		double a = Math.toRadians(rotation % 180);
		double rx = tempX * Math.cos(a) + tempY * Math.sin(a);
		double ry = tempX * Math.sin(a) - tempY * Math.cos(a);
		return new Vec2d(rx, ry);
	}

	/**
	 * @return
	 * 		the offsets to the top right and bottom left corner as seen
	 * 		from the center
	 */
	private Vec2d getTRBLCornerOffsets() {
		double tempX = 0.5 * width;
		double tempY = 0.5 * height;

		double a = Math.toRadians(rotation % 180);
		double rx = tempX * Math.cos(a) - tempY * Math.sin(a);
		double ry = tempX * Math.sin(a) + tempY * Math.cos(a);
		return new Vec2d(rx, ry);
	}

	/**
	 * Performs a few checks to find out whether the CollisionMask has any
	 * overlap with an ICollisionArea object.
	 * 
	 * @param other
	 *            the ICollisionArea to check with.
	 * @return true if this collision mask collides with the given ICollisionArea,
	 *         false if not.
	 * @see <a href=
	 *      "http://forum.codecall.net/topic/65950-pixel-perfect-collision-detection-use-for-your-java-games/">
	 *      pixel-perfect sprite collision</a>
	 */
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
	public void updateTo(ICollisionArea area) {
		if (!(area instanceof CollisionMask)) {
			throw new IllegalArgumentException("Cannot update to different type!");
		}
		
		CollisionMask mask = (CollisionMask) area;
		this.center.x = mask.center.x;
		this.center.y = mask.center.y;
		this.width = mask.width;
		this.height = mask.height;
		this.rotation = mask.rotation;
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeObject(this.center);
		out.writeDouble(this.height);
		out.writeDouble(this.width);
		out.writeDouble(this.rotation);
		out.writeDouble(this.alphaRatio);
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		this.center = (Vec2d) in.readObject();
		this.height = in.readDouble();
		this.width = in.readDouble();
		this.rotation = in.readDouble();
		this.alphaRatio = in.readDouble();
	}
}
