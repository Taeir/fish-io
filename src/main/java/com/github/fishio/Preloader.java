package com.github.fishio;

import java.io.IOException;
import java.util.HashMap;

import com.github.fishio.control.ScreenController;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * Class to preload sprites.
 */
public final class Preloader {
	
	private static Log log = Log.getLogger();
	private Preloader() { }
	
	/**
	 * A map which holds the loaded screens.
	 */
	public static final HashMap<String, Scene> SCREENS = new HashMap<String, Scene>();
	
	/**
	 * A map which holds the loaded images.
	 */
	public static final HashMap<String, Image> IMAGES = new HashMap<String, Image>();
	
	/**
	 * A map which holds the alpha map of an image.
	 */
	public static final HashMap<String, boolean[][]> IMAGE_DATA = new HashMap<String, boolean[][]>();
	
	/**
	 * A map which holds the relative size of images.
	 */
	public static final HashMap<String, Double> IMAGE_ALPHARATS = new HashMap<String, Double>();
	
	
	/**
	 * An empty scene for indicating that a screen is still being loaded.
	 */
	private static final Scene EMPTY_SCENE = new Scene(new HBox());
	
	/**
	 * Preload all the screens.
	 */
	public static void preloadScreens() {
		//Create a new thread to load the screens.
		Thread thread = new Thread(() -> {
			loadScreen("mainMenu");
			loadScreen("singlePlayer");
			loadScreen("helpScreen");
			loadScreen("achievementScreen");
			
			//We don't load the splash screen, because it is shown immediately.
		});
		
		thread.start();
	}
	
	/**
	 * Preload all images.
	 */
	public static void preloadImages() {
		Thread thread = new Thread(() -> {
			tryPreLoad("background.png", false);
			tryPreLoad("logo.png", false);
			
			//Load fish sprites
			tryPreLoad("sprites/fish/playerFish.png", true);
			for (int i = 0; i < 29; i++) {
				tryPreLoad("sprites/fish/fish" + i + ".png", true);
			}
			
			tryPreLoad("sprites/fish/special/barrelFish.png", true);
			tryPreLoad("sprites/fish/special/clownFish1.png", true);
			tryPreLoad("sprites/fish/special/clownFish2.png", true);
			tryPreLoad("sprites/fish/special/jellyfish.png", true);
			tryPreLoad("sprites/fish/special/submarine.png", true);
			tryPreLoad("sprites/fish/special/swordfish.png", true);
			tryPreLoad("sprites/fish/special/turtle.png", true);
			
			tryPreLoad("sprites/anchor1.png", false);
			tryPreLoad("sprites/anchor2.png", false);
			tryPreLoad("sprites/fishingPole.png", false);
			tryPreLoad("sprites/float.png", false);
			tryPreLoad("sprites/seaweed1.png", false);
			tryPreLoad("sprites/starfish.png", false);
			
			// Load achievement icons.
			for (int i = 1; i < 8; i++) {
				tryPreLoad("sprites/chieveIcon/Achievesmall" + i + ".png", true);
			}
			
			// Load large achievements.
			for (int i = 1; i < 8; i++) {
				tryPreLoad("sprites/chieveLarge/Achieve" + i + ".png", true);
			}
			
			// Load PowerUp sprites.
			for (int i = 0; i < 3; i++) {
				tryPreLoad("sprites/powerup/pu" + i + ".png", true);
			}
		});
		
		thread.start();
	}
	
	/**
	 * Attempts to preload an image.<br>
	 * <br>
	 * If loading the image causes an Exception, an error message is output to System.err.
	 * 
	 * @param file
	 * 		the file of the image.
	 */
	private static void tryPreLoad(String file, boolean pixelData) {
		synchronized (IMAGES) {
			if (IMAGES.containsKey(file)) {
				return;
			}
		}
		
		Image image;
		try {
			image = new Image(file);
		} catch (Exception ex) {
			log.log(LogLevel.ERROR, "Error while trying to load image: " + file);
			return;
		}
		if (pixelData) {
			boolean[][] data = CollisionMask.buildData(image);
			double alphaRatio = CollisionMask.getAlphaRatio(data);
			
			synchronized (IMAGE_ALPHARATS) {
				IMAGE_ALPHARATS.put(file, alphaRatio);
			}
			
			synchronized (IMAGE_DATA) {
				IMAGE_DATA.put(file, data);
			}			
		}
		synchronized (IMAGES) {
			IMAGES.put(file, image);
		}
	}
	
	/**
	 * Gets an Image for the given filepath.<br>
	 * If it is not loaded, it loads the Image.
	 * 
	 * @param file
	 * 		the file of the Image.
	 * 
	 * @return
	 * 		the image
	 */
	public static Image getImageOrLoad(String file) {
		synchronized (IMAGES) {
			Image image = IMAGES.get(file);
			if (image != null) {
				return image;
			}
		}
		
		Image image = new Image(file);
		synchronized (IMAGES) {
			IMAGES.put(file, image);
		}
		return image;
	}
	
	/**
	 * Gets the alpha data of an Image for the given filepath.<br>
	 * If it is not loaded, it builds the data.
	 * 
	 * @param file
	 * 		the file of the Image.
	 * 
	 * @return
	 * 		the alpha data of the image
	 */
	public static boolean[][] getAlphaDataOrLoad(String file) {
		boolean[][] data;
		synchronized (IMAGE_DATA) {
			data = IMAGE_DATA.get(file);
			if (data != null) {
				return data;
			}
		}
		
		Image image = getImageOrLoad(file);
		data = CollisionMask.buildData(image);
		synchronized (IMAGE_DATA) {
			IMAGE_DATA.put(file, data);
		}
		return data;
	}

	/**
	 * Gets the ratio of opaque and transparent pixels of an image with the given filepath.<br>
	 * If it is not loaded, it calculates the ratio.
	 * 
	 * @param file
	 * 		the file of the Image.
	 * 
	 * @return
	 * 		the ratio.
	 */
	public static double getSpriteAlphaRatioOrLoad(String file) {
		synchronized (IMAGE_ALPHARATS) {
		Double temp = IMAGE_ALPHARATS.get(file);
			if (temp != null) {
				return temp.doubleValue();
			}
		}
		
		boolean[][] data = getAlphaDataOrLoad(file);
		double alphaRatio = CollisionMask.getAlphaRatio(data);
		synchronized (IMAGE_ALPHARATS) {
			IMAGE_ALPHARATS.put(file, alphaRatio);
		}
		return alphaRatio;
	}
	
	/**
	 * Gets the preloaded image from the given file, if it is loaded.<br>
	 * If not, this method throws an IllegalArgumentException.
	 * 
	 * @param file
	 * 		the filepath of the image to get.
	 * 
	 * @return
	 * 		the image at the given filepath.
	 * 
	 * @throws IllegalArgumentException
	 * 		if the image for this filepath is not yet loaded.
	 */
	public static Image getImage(String file) {
		synchronized (IMAGES) {
			Image image = IMAGES.get(file);
			if (image != null) {
				return image;
			} else {
				log.log(LogLevel.ERROR, "No image loaded for " + file + "!");
				throw new IllegalArgumentException("No image loaded for " + file + "!");
			}
		}
	}
	
	/**
	 * Load the screen from the given file, and store it for later use.
	 * 
	 * @param filename
	 * 		the name of the file to load (without extension).
	 * 
	 * @return
	 * 		the scene that has been loaded.
	 */
	public static Scene loadScreen(String filename) {
		Scene oldScene;
		
		sync:
			synchronized (SCREENS) {
				//Check if this screen is already being loaded
				oldScene = SCREENS.get(filename);
				
				if (oldScene == EMPTY_SCENE) {
					//The screen is being loaded.
					//We break out of the synchronized block and start waiting below.
					break sync;
				} else if (oldScene != null) {
					//The screen has already been loaded, so we return it.
					return oldScene;
				}
	
				//Indicate that we are loading the screen by putting the EMPTY_SCENE in the map.
				SCREENS.put(filename, EMPTY_SCENE);
			}
		
		//While the scene is the EMPTY_SCENE (indicates that the scene is being loaded), we wait.
		if (oldScene == EMPTY_SCENE) {
			do {
				try {
					Thread.sleep(50L);
					
					synchronized (SCREENS) {
						oldScene = SCREENS.get(filename);
					}
				} catch (InterruptedException ex) {
					log.log(LogLevel.ERROR, "Interrupted while waiting for screen to get loaded!");
					throw new LoaderException("Interrupted while waiting for screen to get loaded!", ex);
				}
			} while (oldScene == EMPTY_SCENE);
			
			return oldScene;
		}
		
		//Otherwise, we load the scene.
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Preloader.class.getResource("view/" + filename + ".fxml"));

		try {
			Pane rootLayout = (Pane) loader.load();
			ScreenController controller = ((ScreenController) loader.getController());
			if (controller == null) {
				log.log(LogLevel.ERROR, "Screen controller not found for " + filename);
				return null;
			}

			Scene scene = new Scene(rootLayout);
			
			//Set the controller as userdata for the scene.
			scene.getProperties().put("Controller", controller);
			
			//Initialize the controller
			try {
				controller.init(scene);
			} catch (Exception ex) {
				log.log(LogLevel.ERROR, "Error while initializing controller for " 
						+ filename + " Exeception: " + ex.getMessage());
			}
			
			synchronized (SCREENS) {
				SCREENS.put(filename, scene);
			}
			
			return scene;
		} catch (IOException e) {
			log.log(LogLevel.ERROR, "Error loading screen: " 
					+ " Exeception: " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Changes the screen to the screen with the given filename (without extension),
	 * with a FadeTransition of length milliseconds.
	 * 
	 * @param filename
	 * 		The filename of the screen to switch to.
	 * @param length
	 * 		The length in milliseconds of the FadeTransition.
	 * 
	 * @throws LoaderException
	 * 		if a screen is still being loaded, and while waiting for it to be done loaded,
	 * 		we get interrupted.
	 * 
	 * @return
	 * 		the Scene of the screen that was switched to.
	 */
	public static Scene switchTo(String filename, int length) {
		Scene scene;
		synchronized (SCREENS) {
			scene = SCREENS.get(filename);
		}
		
		if (scene == null) {
			log.log(LogLevel.ERROR, "No screen with name " + filename + " is loaded!");
			throw new IllegalArgumentException("No screen with name " + filename + " is loaded!");
		} else if (scene == EMPTY_SCENE) {
			//Screen is being loaded, so sleep for a bit and try again
			while (scene == EMPTY_SCENE) {
				try {
					Thread.sleep(50L);
					
					synchronized (SCREENS) {
						scene = SCREENS.get(filename);
					}
				} catch (InterruptedException ex) {
					log.log(LogLevel.ERROR, "Interrupted while waiting for screen to get loaded!");
					throw new LoaderException("Interrupted while waiting for screen to get loaded!", ex);
				}
			}
		}
		
		showScreen(scene, length);
		return scene;
	}
	
	/**
	 * Load a screen from a fxml-file and show it.
	 * 
	 * @param filename
	 * 			filename of the fxml file.
	 * @param length
	 * 			If &gt; 0, fade in the new screen, else just show it.
	 * 
	 * @return
	 * 		the Scene of the screen that is being shown.
	 */
	public static Scene loadAndShowScreen(String filename, int length) {
		Scene scene;
		synchronized (SCREENS) {
			scene = SCREENS.get(filename);
		}
		
		if (scene == null) {
			scene = loadScreen(filename);
		}
		
		showScreen(scene, length);
		return scene;
	}
	
	/**
	 * Shows the given scene on the screen.
	 * 
	 * @param scene
	 * 		the scene to show.
	 * @param length
	 * 		the length of the FadeTransition for this screen.
	 */
	private static void showScreen(Scene scene, int length) {
		ScreenController controller = (ScreenController) scene.getProperties().get("Controller");
		controller.onSwitchTo();
		
		//DoubleProperty opacity = scene.getRoot().opacityProperty();
		
		if (length > 0) {
			scene.getRoot().setOpacity(0);
			FishIO.getInstance().getPrimaryStage().setScene(scene);
			
			FadeTransition fade = new FadeTransition(Duration.millis(length), scene.getRoot());
			fade.setFromValue(0.0);
			fade.setToValue(1.0);
			fade.play();
		} else {
			FishIO.getInstance().getPrimaryStage().setScene(scene);
		}
	}
}
