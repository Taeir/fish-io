package com.github.fishio.gui;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.github.fishio.FishIO;
import com.github.fishio.Preloader;
import com.github.fishio.control.AchievementScreenController;
import com.github.fishio.control.HelpScreenController;
import com.github.fishio.control.MainMenuController;
import com.github.fishio.control.SinglePlayerController;
import com.github.fishio.control.SplashScreenController;
import com.github.fishio.settings.Settings;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Base class for GUI Tests.
 */
public class GuiTest extends AppTest {
	private static final FishIO FISH_IO = new FishIO();
	public static final String DEFAULT_STARTSCREEN = "splashScreen";
	
	private Stage stage;
	
	private MainMenuController mainMenuController;
	private SinglePlayerController singleController;
	private SplashScreenController splashController;
	private HelpScreenController helpController;
	private AchievementScreenController achievementController;
	
	private Scene mainScene, singleScene, splashScene, helpScene, achievementScene;
	
	private String startScreen;
	
	/**
	 * Creates a new GuiTest with the default start screen,
	 * {@link #DEFAULT_STARTSCREEN}.
	 */
	public GuiTest() {
		this(DEFAULT_STARTSCREEN);
	}
	
	/**
	 * Creates a new GuiTest with the given startScreen.
	 * 
	 * @param startScreen
	 * 		the screen that should be switched to before every
	 * 		test.
	 */
	public GuiTest(String startScreen) {
		this.startScreen = startScreen;
	}
	
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
		
		achievementScene = Preloader.loadScreen("achievementScreen");
		achievementController = (AchievementScreenController) achievementScene.getProperties().get("Controller");
	}
	
	/**
	 * Called before every tests.<br>
	 * <br>
	 * Resets the GUI to the start screen.
	 * 
	 * @throws Exception
	 * 		if an Exception occurs while switching to the start screen.
	 */
	@Before
	public void setUpGuiTest() throws Exception {
		//Stop any running splash screen transition
		getSplashScreenController().stopTransition();
		
		//Switch to the splash screen.
		ScreenSwitcher switcher = new ScreenSwitcher(startScreen, 0);
		
		//We wait until we have switched to the given screen.
		if (switcher.waitUntilDone()) {
			//We rethrow the exception of the screen switch.
			throw switcher.getException();
		}
	}
	
	/**
	 * Deletes the settings file before running the GuiTest.
	 */
	@BeforeClass
	public static void setUpGuiTestClass() {
		new File("settings.yml").delete();
	}
	
	/**
	 * Deletes the settings file after running the GuiTest.
	 */
	@AfterClass
	public static void breakDownGuiTestClass() {
		new File("settings.yml").delete();
	}

	/**
	 * @return
	 * 		the screen that should be switched to before each test.
	 */
	public String getStartScreen() {
		return startScreen;
	}
	
	/**
	 * Sets the screen that will be switched to before every test.
	 * 
	 * @param screen
	 * 		the screen that should be switched to before every test.
	 */
	public void setStartScreen(String screen) {
		this.startScreen = screen;
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
	 * @return the ScreenController for the Single Player Screen.
	 */
	public SinglePlayerController getSinglePlayerController() {
		return singleController;
	}
	
	/**
	 * @return the ScreenController for the Splash Screen.
	 */
	public SplashScreenController getSplashScreenController() {
		return splashController;
	}
	
	/**
	 * @return the ScreenController for the Help Screen.
	 */
	public HelpScreenController getHelpScreenController() {
		return helpController;
	}
	
	/**
	 * @return the ScreenController for the Achievement Screen.
	 */
	public AchievementScreenController getAchievementScreenController() {
		return achievementController;
	}
	
	/**
	 * @return the scene for the Main Menu Screen.
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
	
	/**
	 * @return the scene for the Achievement Screen.
	 */
	public Scene getAchievementScene() {
		return achievementScene;
	}
}
