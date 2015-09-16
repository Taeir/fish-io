package com.github.fishio.gui;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;

import com.github.fishio.FishIO;
import com.github.fishio.Preloader;
import com.github.fishio.control.HelpScreenController;
import com.github.fishio.control.MainMenuController;
import com.github.fishio.control.SinglePlayerController;
import com.github.fishio.control.SplashScreenController;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Base class for GUI Tests.
 */
public class GuiTest extends AppTest {
	private static final FishIO FISH_IO = new FishIO();
	private Stage stage;
	
	private MainMenuController mainMenuController;
	private SinglePlayerController singleController;
	private SplashScreenController splashController;
	private HelpScreenController helpController;
	
	private Scene mainScene, singleScene, splashScene, helpScene;
	
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
		
		helpScene = Preloader.loadScreen("helpScreen");
		helpController = (HelpScreenController) helpScene.getProperties().get("Controller");
	}
	
	/**
	 * Called after every test.<br>
	 * <br>
	 * Resets the GUI to the splash screen.
	 * 
	 * @throws Exception
	 * 		if an Exception occurs.
	 */
	@After
	public void breakDownGuiTest() throws Exception {
		//Stop any running splash screen transition
		getSplashScreenController().stopTransition();
		
		//Switch to the splash screen.
		ScreenSwitcher switcher = new ScreenSwitcher("splashScreen", 0);
		
		//We wait until we have switched to the given screen.
		if (switcher.waitUntilDone()) {
			//We rethrow the exception of the screen switch.
			throw switcher.getException();
		}

		//Wait until the screen is the actually displayed screen.
		while (!isCurrentScene(switcher.getScene())) {
			Thread.sleep(50L);
		}
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
	 * Asserts if the given scene is the currently displayed scene, using {@link org.junit.Assert#assertTrue(boolean)}.
	 * 
	 * @param scene
	 * 		the scene to check
	 * 
	 * @see #isCurrentScene(Scene)
	 */
	public void assertCurrentScene(Scene scene) {
		assertTrue(isCurrentScene(scene));
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
		ScreenSwitcher switcher = new ScreenSwitcher(screen, 0);
		
		//We wait until we have switched to the given screen.
		//If an exception occurs, we call fail.
		try {
			if (switcher.waitUntilDone()) {
				fail();
			}
		} catch (InterruptedException ex) {
			fail();
		}

		//Wait until the screen is the actually displayed screen.
		while (!isCurrentScene(switcher.getScene())) {
			sleepFail(50L);
		}
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
	 * 		the ScreenController for the splash screen.
	 */
	public HelpScreenController getHelpScreenController() {
		return helpController;
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
	
	/**
	 * @return
	 * 		the scene for the Help Screen.
	 */
	public Scene getHelpScene() {
		return helpScene;
	}
}
