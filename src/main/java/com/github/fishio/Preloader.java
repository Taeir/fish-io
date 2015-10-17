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
	
	private static Log logger = Log.getLogger();
	private Preloader() { }
	
	/**
	 * A map which holds the loaded screens.
	 */
	private static final HashMap<String, Scene> SCREENS = new HashMap<String, Scene>();
	
	/**
	 * A map which holds the loaded images.
	 */
	private static final HashMap<String, Image> IMAGES = new HashMap<String, Image>();
	
	/**
	 * An empty scene for indicating that a screen is still being loaded.
	 */
	private static final Scene EMPTY_SCENE = new Scene(new HBox());
	

	private static final String INTERRUPTED_MESSAGE 
		= "Interrupted while waiting for screen to get loaded!";
	
	/**
	 * Preload all the screens.
	 */
	public static void preloadScreens() {
		Log.getLogger().log(LogLevel.DEBUG, "[Preloader] Preloading screens...");
		
		//Order matters here. We first load the mainMenu, since that screen will be shown directly after
		//the spash screen.
		MultiThreadedUtility.submitTask(() -> loadScreen("mainMenu"), false);
		MultiThreadedUtility.submitTask(() -> loadScreen("singlePlayer"), false);
		MultiThreadedUtility.submitTask(() -> loadScreen("helpScreen"), false);
		MultiThreadedUtility.submitTask(() -> loadScreen("achievementScreen"), false);
		MultiThreadedUtility.submitTask(() -> loadScreen("settingsScreen"), false);
		MultiThreadedUtility.submitTask(() -> loadScreen("multiplayerScreen"), false);
		MultiThreadedUtility.submitTask(() -> loadScreen("multiplayerGameScreen"), false);
	}
	
	/**
	 * Preload all images.
	 */
	public static void preloadImages() {
		Log.getLogger().log(LogLevel.DEBUG, "[Preloader] Preloading images...");
		
		//Load the background and the logo (short tasks)
		MultiThreadedUtility.submitTask(() -> tryPreLoad("background.png"), true);
		MultiThreadedUtility.submitTask(() -> tryPreLoad("logo.png"), true);
		
		//Load achievement sprites (short task)
		MultiThreadedUtility.submitTask(() -> {
			for (int i = 1; i < 8; i++) {
				tryPreLoad("sprites/chieveIcon/Achievesmall" + i + ".png");
				tryPreLoad("sprites/chieveLarge/Achieve" + i + ".png");
			}
		}, true);
	}
	
	/**
	 * Attempts to preload an image.<br>
	 * <br>
	 * If loading the image causes an Exception, an error message is output to System.err.
	 * 
	 * @param file
	 * 		the file of the image.
	 */
	private static void tryPreLoad(String file) {
		synchronized (IMAGES) {
			if (IMAGES.containsKey(file)) {
				return;
			}
		}
		
		Image image;
		try {
			image = new Image(file);
		} catch (Exception ex) {
			logger.log(LogLevel.ERROR, "Error while trying to load image: " + file);
			return;
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
			}
		}
		
		logger.log(LogLevel.ERROR, "No image loaded for " + file + "!");
		throw new IllegalArgumentException("No image loaded for " + file + "!");
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
					logger.log(LogLevel.ERROR, INTERRUPTED_MESSAGE);
					throw new LoaderException(INTERRUPTED_MESSAGE, ex);
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
				logger.log(LogLevel.ERROR, "Screen controller not found for " + filename);
				return null;
			}

			Scene scene = new Scene(rootLayout);
			
			//Set the controller as userdata for the scene.
			scene.getProperties().put("Controller", controller);
			
			//Initialize the controller
			try {
				controller.init(scene);
			} catch (Exception ex) {
				logger.log(LogLevel.ERROR, "Error while initializing controller for " + filename);
				logger.log(LogLevel.DEBUG, ex);
			}
			
			//Add the scene
			synchronized (SCREENS) {
				SCREENS.put(filename, scene);
			}
			
			return scene;
		} catch (IOException e) {
			logger.log(LogLevel.ERROR, "Error loading screen " + filename);
			logger.log(LogLevel.DEBUG, e);
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
			logger.log(LogLevel.ERROR, "No screen with name " + filename + " is loaded!");
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
					logger.log(LogLevel.ERROR, INTERRUPTED_MESSAGE);
					throw new LoaderException(INTERRUPTED_MESSAGE, ex);
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
		switchAway();
		ScreenController controller = getController(scene);
		controller.onSwitchTo();
		
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
	
	/**
	 * Gets the controller of the screen with the given filename. If that
	 * screen is not yet loaded, it is loaded first.
	 * 
	 * @param filename
	 * 		the filename (without extension) of the screen to get the
	 * 		controller of.
	 * @param <T>
	 * 		the ScreenController implementation that is expected.
	 * 
	 * @return
	 * 		the controller of the screen with the given filename, or 
	 * 		<code>null</code> if no such screen exists.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends ScreenController> T getControllerOrLoad(String filename) {
		Scene scene;
		synchronized (SCREENS) {
			scene = SCREENS.get(filename);
		}
		
		if (scene == null || scene == EMPTY_SCENE) {
			scene = loadScreen(filename);
		}
		
		if (scene == null || scene == EMPTY_SCENE) {
			return null;
		}
		
		return (T) scene.getProperties().get("Controller");
	}
	
	/**
	 * @param scene
	 * 		the scene to get the controller of.
	 * @param <T>
	 * 		the ScreenController implementation that is expected.
	 * 
	 * @return
	 * 		the controller of the given Scene.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends ScreenController> T getController(Scene scene) {
		if (scene == null) {
			return null;
		}
		
		return (T) scene.getProperties().get("Controller");
	}

	/**
	 * Switch away from the current screen.
	 */
	public static void switchAway() {
		Scene current = FishIO.getInstance().getPrimaryStage().getScene();
		if (current != null) {
			getController(current).onSwitchAway();
		}		
	}
}
