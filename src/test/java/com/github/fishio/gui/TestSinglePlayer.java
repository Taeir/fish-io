package com.github.fishio.gui;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import javafx.scene.input.MouseButton;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.fishio.PlayerFish;

/**
 * Test for the Single Player Screen.
 * 
 * @see SinglePlayerController
 */
public class TestSinglePlayer extends GuiTest {
	
	/**
	 * Skip the splash screen, switch to the single player screen and
	 * restart the game before every test.
	 */
	@Before
	public void setUpSinglePlayer() {
		//Skip the splash screen.
		skipSplash();
		
		//Switch to the single player screen
		switchToScreen("singlePlayer");
		
		//Check that we are on the single player screen.
		isCurrentScene(getSinglePlayerScene());
		
		//Restart the game.
		getSinglePlayerController().getPlayingField().stopGame();
		getSinglePlayerController().getPlayingField().clear();
		getSinglePlayerController().getPlayingField().startGame();
		
		//Sleep a bit
		sleepFail(20L);
	}
	
	/**
	 * Stop the game after every test.
	 */
	@After
	public void breakDownSinglePlayer() {
		//Stop the game
		getSinglePlayerController().getPlayingField().stopGame();
	}

	/**
	 * @return
	 * 		the PlayerFish, or null if there isn't one in the current game.
	 */
	private PlayerFish getPlayer() {
		ArrayList<PlayerFish> players = getSinglePlayerController().getPlayingField().getPlayers();
		if (players.isEmpty()) {
			return null;
		}
		
		return players.get(0);
	}
	
	/**
	 * Test for the following scenario:<br>
	 * <br>
	 * Scenario S3.1: Pause<br>
	 * Given the user is on the Single Player Screen,<br>
	 * and   the game is running,<br>
	 * and   the Player Fish is not dead;<br>
	 * When  the user presses the "Pause" button;<br>
	 * Then  the game should be paused.
	 */
	@Test
	public void testPause() {
		//The game should be running.
		assertTrue(getSinglePlayerController().getPlayingField().isRunning());
		
		//The PlayerFish must be present and not dead.
		assertTrue(getPlayer() != null && !getPlayer().isDead());
		
		//Click on the pause button
		clickOn(getSinglePlayerController().getBtnPause(), MouseButton.PRIMARY);
		
		//The game should not be running.
		assertFalse(getSinglePlayerController().getPlayingField().isRunning());
		
		//The PlayerFish should still be present and not dead.
		assertTrue(getPlayer() != null && !getPlayer().isDead());
	}
	
	/**
	 * Test for the following scenario:<br>
	 * <br>
	 * Scenario S3.2: unpause<br>
	 * Given the user is on the Single Player Screen,<br>
	 * and   the game is paused,<br>
	 * and   the Player Fish is not dead;<br>
	 * When  the user presses the "Pause" button;<br>
	 * Then  the game should be resumed (unpaused).
	 */
	@Test
	public void testUnpause() {
		//The game should be running.
		assertTrue(getSinglePlayerController().getPlayingField().isRunning());
		
		//The PlayerFish must be present and not dead.
		assertTrue(getPlayer() != null && !getPlayer().isDead());
		
		//Pause the game
		getSinglePlayerController().getPlayingField().stopGame();
		
		//Click on the pause button
		clickOn(getSinglePlayerController().getBtnPause(), MouseButton.PRIMARY);
		
		//The game should be running again.
		assertTrue(getSinglePlayerController().getPlayingField().isRunning());
		
		//The PlayerFish should still be present and not dead.
		assertTrue(getPlayer() != null && !getPlayer().isDead());
	}
	
	/**
	 * Test for the following scenario:<br>
	 * <br>
	 * Scenario S3.3: mute<br>
	 * Given the user is on the Single Player Screen;<br>
	 * When  the user presses the "Mute" button;<br>
	 * Then  nothing should happen.
	 */
	@Test
	public void testMute() {
		//The game should be running.
		assertTrue(getSinglePlayerController().getPlayingField().isRunning());
		
		//The PlayerFish must be present and not dead.
		assertTrue(getPlayer() != null && !getPlayer().isDead());
		
		//Pause the game
		getSinglePlayerController().getPlayingField().stopGame();
		
		//Click on the pause button
		clickOn(getSinglePlayerController().getBtnPause(), MouseButton.PRIMARY);
		
		//The game should be running again.
		assertTrue(getSinglePlayerController().getPlayingField().isRunning());
		
		//The PlayerFish should still be present and not dead.
		assertTrue(getPlayer() != null && !getPlayer().isDead());
	}
}
