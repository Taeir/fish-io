package com.github.fishio.gui;

import static org.junit.Assert.fail;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.junit.After;

import com.github.fishio.FishIO;
import com.github.fishio.Preloader;
import com.github.fishio.control.MainMenuController;
import com.github.fishio.control.SinglePlayerController;
import com.github.fishio.control.SplashScreenController;

/**
 * Base class for GUI Tests.
 */
public class GuiTest extends AppTest {
	private static final FishIO FISH_IO = new FishIO();
	private Stage stage;
	
	private MainMenuController mainMenuController;
	private SinglePlayerController singleController;
	private SplashScreenController splashController;
	
	private Scene mainScene, singleScene, splashScene;
	
	private volatile boolean loaded = false;
	private volatile boolean loaded2 = false;
	
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		
		FISH_IO.start(stage);
		
		splashScene = Preloader.loadScreen("splashScreen");
		splashController = (SplashScreenController) splashScene.getProperties().get("Controller");
		
		mainScene = Preloader.loadScreen("mainMenu");
		mainMenuController = (MainMenuController) mainScene.getProperties().get("Controller");
		
		singleScene = Preloader.loadScreen("singlePlayer");
		singleController = (SinglePlayerController) singleScene.getProperties().get("Controller");
	}
	
	/**
	 * Called after every test.<br>
	 * <br>
	 * Allows the splash screen to be re skipped.
	 * 
	 * @throws Exception
	 * 		if an Exception occurs.
	 */
	@After
	public void breakDown() throws Exception {
		//Switch back to the splash screen.
		Platform.runLater(() -> {
			Preloader.switchTo("splashScreen", 0);
			
			loaded = true;
		});
		
		//Wait for the task to be executed.
		while (!loaded) {
			Thread.sleep(50L);
		}
		
		loaded = false;
	}
	
	/**
	 * @return
	 * 		the scene that is currently displayed.
	 */
	public Scene getCurrentScene() {
		return stage.getScene();
	}
	
	/**
	 * Checks if the given scene is the currently displayed scene.
	 * 
	 * @param scene
	 * 		the scene to check
	 * 
	 * @return
	 * 		if the given scene is the currently displayed scene.
	 */
	public boolean isCurrentScene(Scene scene) {
		return scene == getCurrentScene();
	}
	
	/**
	 * Sleeps for the given amount of milliseconds.<br>
	 * <br>
	 * If an InterruptedException is thrown from the sleep method, {@link org.junit.Assert#fail()} is called.
	 * 
	 * @param ms
	 * 		the amount of milliseconds to sleep for.
	 */
	public static void sleepFail(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException ex) {
			fail();
		}
	}
	
	/**
	 * Skips the splash screen.
	 */
	public void skipSplash() {
		//Reset the splash screen.
		getSplashScreenController().stopTransition();
		
		//Switch to the main menu.
		switchToScreen("mainMenu");
	}
	
	/**
	 * Switch to the given screen and wait for the switch to happen.
	 * 
	 * @param screen
	 * 		the screen to switch to.
	 */
	public void switchToScreen(final String screen) {
		//Switch to the given screen.
		Platform.runLater(() -> {
			Preloader.switchTo(screen, 0);
			
			loaded2 = true;
		});
		
		//Wait for the task to be executed.
		while (!loaded2) {
			sleepFail(50L);
		}
		
		loaded2 = false;
		
		//Sleep one more time
		sleepFail(50L);
	}
	
	/**
	 * @return
	 * 		the primary stage.
	 */
	public Stage getStage() {
		return stage;
	}
	
	/**
	 * @return
	 * 		the ScreenController for the Main Menu.
	 */
	public MainMenuController getMainMenuController() {
		return mainMenuController;
	}
	
	/**
	 * @return
	 * 		the ScreenController for the Single Player screen.
	 */
	public SinglePlayerController getSinglePlayerController() {
		return singleController;
	}
	
	/**
	 * @return
	 * 		the ScreenController for the splash screen.
	 */
	public SplashScreenController getSplashScreenController() {
		return splashController;
	}
	
	/**
	 * @return
	 * 		the scene for the Main Menu Screen.
	 */
	public Scene getMainMenuScene() {
		return mainScene;
	}
	
	/**
	 * @return
	 * 		the scene for the Single Player Screen.
	 */
	public Scene getSinglePlayerScene() {
		return singleScene;
	}
	
	/**
	 * @return
	 * 		the scene for the Splash Screen.
	 */
	public Scene getSplashScene() {
		return splashScene;
	}
}
