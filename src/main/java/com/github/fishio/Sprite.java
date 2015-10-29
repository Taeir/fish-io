package com.github.fishio;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Image with collision mask data.
 */
public class Sprite extends Image {
	private double alphaRatio;
	private boolean[][] pixelData;
	
	/**
	 * Constructs a MaskedImage from the file at the given url.
	 * 
	 * @param url
	 * 		the string representing the URL to use in fetching the pixel data
	 */
	public Sprite(String url) {
		super(url);
		
		this.pixelData = buildPixelData();
		this.alphaRatio = calculateAlphaRatio();
	}
	
	/**
	 * Constructs a MaskedImage from the file at the given url and gives the
	 * sprite a certain predefined color. This is used for multiplayer mode.
	 * 
	 * @param url
	 *            the string representing the URL to use in fetching the pixel
	 *            data
	 * @param color
	 *            the color the sprite should have.
	 */
	public Sprite(String url, Color color) {
		super(url);

		this.pixelData = buildPixelData(color);
		this.alphaRatio = calculateAlphaRatio();
	}

	/**
	 * Build the data used for the collision mask.
	 * 
	 * @return
	 * 		the generated pixel data matrix.
	 */
	private boolean[][] buildPixelData() {
		int width = (int) getWidth();
		int heigth = (int) getHeight();
		
		boolean[][] res = new boolean[width][heigth];
		
		PixelReader pr = getPixelReader();
		for (int y = 0; y < heigth; y++) {
			for (int x = 0; x < width; x++) {
				res[x][y] = pr.getColor(x, y).getOpacity() > 0.5;
			}
		}
		
		return res;
	}
	
	/**
	 * Build the data used for the collision mask, with a given color.<br>
	 * This is used to generate different colored sprites in multiplayer mode.
	 * 
	 * @return the generated pixel data matrix.
	 */
	private boolean[][] buildPixelData(Color color) {
		int width = (int) getWidth();
		int heigth = (int) getHeight();

		boolean[][] res = new boolean[width][heigth];

		WritableImage wrimg = new WritableImage(width, heigth);
		PixelWriter pw = wrimg.getPixelWriter();

		PixelReader pr = getPixelReader();
		for (int y = 0; y < heigth; y++) {
			for (int x = 0; x < width; x++) {
				res[x][y] = pr.getColor(x, y).getOpacity() > 0.5;
				if (pr.getColor(x, y).getOpacity() > 0.5) {
					pw.setColor(x, y, color);
				}
			}
		}
		return res;
	}

	/**
	 * Calculates the alpha ration of this image.
	 * 
	 * @return the ratio between opaque and non-opaque pixels.
	 */
	private double calculateAlphaRatio() {
		int res = 0;
		for (boolean[] row : pixelData) {
			for (boolean pixel : row) {
				if (pixel) {
					res++;
				}
			}
		}

		return ((double) res) / (pixelData.length * pixelData[0].length);
	}
	
	/**
	 * @return
	 * 		the alpha ration of this image.
	 */
	public double getAlphaRatio() {
		return this.alphaRatio;
	}
	
	/**
	 * @return
	 * 		a boolean matrix with true for pixels that are part of the mask,
	 * 		and false for all pixels that are not.
	 */
	public boolean[][] getPixelData() {
		return this.pixelData;
	}

}