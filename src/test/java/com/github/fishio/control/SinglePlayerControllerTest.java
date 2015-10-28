package com.github.fishio.control;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.fishio.PlayerFish;
import com.github.fishio.SinglePlayerPlayingField;
import com.github.fishio.audio.AudioEngine;
import com.github.fishio.game.GameState;
import com.github.fishio.game.GameThread;
import com.github.fishio.gui.NoSoundSlimGuiTest;
import com.github.fishio.gui.Renderer;

/**
 * Test class for {@link SinglePlayerController}.
 */
public class SinglePlayerControllerTest extends NoSoundSlimGuiTest {
	private SinglePlayerController controller;
	private Scene scene;
	private Canvas canvas;
	
	/**
	 * Creates a new controller before every test, so that tests do not
	 * interfere.
	 */
	@Before
	public void setUp() {
		canvas = spy(new Canvas());
		scene = mock(Scene.class);
		
		controller = new SinglePlayerController();
		
		//Initialize the fields
		controller.initFXMLForTest();
		
		controller.setGameCanvas(canvas);
		
		//Initialize the controller
		controller.init(scene);
	}
	
	/**
	 * Stop any running game after every test.
	 * 
	 * @throws Exception
	 * 		if an exception occurs while trying to stop the game.
	 */
	@After
	public void tearDown() throws Exception {
		controller.getPlayingField().stopGameAndWait();
	}

	/**
	 * Test method for {@link SinglePlayerController#init(Scene)}.
	 */
	@Test
	public void testInit() {
		//Create a new controller and initialize it.
		controller = new SinglePlayerController();
		controller.initFXMLForTest();
		controller.setGameCanvas(canvas);
		controller.init(scene);
		
		//The playingfield should have been initialized
		assertNotNull(controller.getPlayingField());
	}

	/**
	 * Test method for {@link SinglePlayerController#onSwitchTo()}.
	 * 
	 * When the user switches to the Single player screen,
	 * the game should be started.
	 */
	@Test
	public void testOnSwitchTo() {
		controller.onSwitchTo();
		
		//The game should now be starting or running
		GameState state = controller.getPlayingField().getGameThread().getState();
		assertTrue(state == GameState.STARTING || state == GameState.RUNNING);
	}

	/**
	 * Test method for {@link SinglePlayerController#onPause()}, when the
	 * game is running.
	 */
	@Test
	public void testOnPause_Running() {
		//Start the game
		try {
			controller.getPlayingField().startGameAndWait();
		} catch (Exception ex) {
			fail("Error while waiting for game to start");
		}
		
		//Pause
		controller.onPause();
		
		//The game should not be running
		assertFalse(controller.getPlayingField().getGameThread().isRunning());
		
		//The pause button should now display Unpause
		assertEquals("Unpause", controller.getPauseButton().getText());
	}
	
	/**
	 * Test method for {@link SinglePlayerController#onPause()}, when the
	 * game is paused.
	 */
	@Test
	public void testOnPause_Paused() {
		//Ensure that the game is not running.
		assertFalse(controller.getPlayingField().getGameThread().isRunning());
		
		//Unpause
		controller.onPause();
		
		//The game should now be running (or starting)
		GameState state = controller.getPlayingField().getGameThread().getState();
		assertTrue(state == GameState.STARTING || state == GameState.RUNNING);
		
		//The pause button should now display Pause
		assertEquals("Pause", controller.getPauseButton().getText());
	}
	
	/**
	 * Switches to the given mute state.
	 * 
	 * @param state
	 * 		the state to switch to.
	 */
	private void switchMuteState(int state) {
		AudioEngine.getInstance().getMuteStateProperty().set(state);
	}

	/**
	 * Test method for {@link SinglePlayerController#onMute()}.
	 */
	@Test
	public void testOnMute() {
		//Set the mute state to no mute
		switchMuteState(AudioEngine.NO_MUTE);
		
		//"Press" the mute button
		controller.onMute();
		
		//The button text should now be "Mute all sounds"
		assertEquals("Mute all sounds", controller.getMuteButton().getText());
		
		//The new mute state should be MUTE_MUSIC
		assertEquals(AudioEngine.MUTE_MUSIC, AudioEngine.getInstance().getMuteState());
	}
	
	/**
	 * Test method for {@link SinglePlayerController#onMute()}.
	 */
	@Test
	public void testOnMute2() {
		//Set the mute state to MUTE_MUSIC
		switchMuteState(AudioEngine.MUTE_MUSIC);
		
		//"Press" the mute button
		controller.onMute();
		
		//The button text should now be "Unmute all sounds"
		assertEquals("Unmute all sounds", controller.getMuteButton().getText());
		
		//The new mute state should be MUTE_ALL
		assertEquals(AudioEngine.MUTE_ALL, AudioEngine.getInstance().getMuteState());
	}
	
	/**
	 * Test method for {@link SinglePlayerController#onMute()}.
	 */
	@Test
	public void testOnMute3() {
		//Set the mute state to MUTE_MUSIC
		switchMuteState(AudioEngine.MUTE_ALL);
		
		//"Press" the mute button
		controller.onMute();
		
		//The button text should now be "Mute music"
		assertEquals("Mute music", controller.getMuteButton().getText());
		
		//The new mute state should be NO_MUTE
		assertEquals(AudioEngine.NO_MUTE, AudioEngine.getInstance().getMuteState());
	}

	/**
	 * Test method for {@link SinglePlayerController#revive()}.
	 */
	@Test
	public void testRevive() {
		//Mock the playing field
		SinglePlayerPlayingField sppf = mock(SinglePlayerPlayingField.class);
		Renderer renderer = mock(Renderer.class);
		GameThread gameThread = mock(GameThread.class);
		
		when(sppf.getRenderer()).thenReturn(renderer);
		when(sppf.getGameThread()).thenReturn(gameThread);
		
		controller.setPlayingField(sppf);

		//"Press" the revive button
		controller.revive();
		
		//The player should have been revived
		verify(sppf).revivePlayer();
		
		//The renderer should have been started
		verify(renderer).startRendering();
	}

	/**
	 * Test method for {@link SinglePlayerController#restartGame()}.
	 */
	@Test
	public void testRestartGame() {
		//Mock an integer property for the player
		SimpleIntegerProperty sip = mock(SimpleIntegerProperty.class);
		when(sip.intValue()).thenReturn(1);
		
		//Mock the player
		PlayerFish player = mock(PlayerFish.class);
		when(player.livesProperty()).thenReturn(sip);
		when(player.scoreProperty()).thenReturn(sip);
		when(player.getLives()).thenReturn(1);
		
		//Create a players list
		ArrayList<PlayerFish> list = new ArrayList<>();
		list.add(player);
		
		//Mock the playing field
		SinglePlayerPlayingField sppf = mock(SinglePlayerPlayingField.class);
		Renderer renderer = mock(Renderer.class);
		GameThread gameThread = mock(GameThread.class);
		when(sppf.getRenderer()).thenReturn(renderer);
		when(sppf.getGameThread()).thenReturn(gameThread);
		when(sppf.getPlayer()).thenReturn(player);
		when(sppf.getPlayers()).thenReturn(list);
		
		//Set the playing field
		controller.setPlayingField(sppf);
		
		//Restart the game
		controller.restartGame();
		
		//All entities should have been removed.
		verify(sppf).clear();
	}

	/**
	 * Test method for {@link SinglePlayerController#updatePauseButton()},
	 * when the game is running and a player is alive.
	 */
	@Test
	public void testUpdatePauseButton() {
		//Mock the playing field
		GameThread gameThread = mock(GameThread.class);
		when(gameThread.isRunning()).thenReturn(true);
		
		SinglePlayerPlayingField sppf = mock(SinglePlayerPlayingField.class);
		when(sppf.getGameThread()).thenReturn(gameThread);
		when(sppf.isPlayerAlive()).thenReturn(true);
		
		controller.setPlayingField(sppf);
		
		//Call the method
		controller.updatePauseButton();
		
		//If there is a player alive the pause button should be enabled
		assertFalse(controller.getPauseButton().isDisabled());
		//If the game is running, the pause button should display "Pause"
		assertEquals("Pause", controller.getPauseButton().getText());
	}
	
	/**
	 * Test method for {@link SinglePlayerController#updatePauseButton()},
	 * when the game is NOT running and a player is alive.
	 */
	@Test
	public void testUpdatePauseButton2() {
		//Mock the playing field
		GameThread gameThread = mock(GameThread.class);
		when(gameThread.isRunning()).thenReturn(false);
		
		SinglePlayerPlayingField sppf = mock(SinglePlayerPlayingField.class);
		when(sppf.getGameThread()).thenReturn(gameThread);
		when(sppf.isPlayerAlive()).thenReturn(true);
		
		controller.setPlayingField(sppf);
		
		//Call the method
		controller.updatePauseButton();
		
		//If there is a player alive the pause button should be enabled
		assertFalse(controller.getPauseButton().isDisabled());
		//If the game is not running, the pause button should display "Unpause"
		assertEquals("Unpause", controller.getPauseButton().getText());
	}
	
	/**
	 * Test method for {@link SinglePlayerController#updatePauseButton()},
	 * when the game is NOT running and a player is alive.
	 */
	@Test
	public void testUpdatePauseButton3() {
		//Mock the playing field
		SinglePlayerPlayingField sppf = mock(SinglePlayerPlayingField.class);
		GameThread gameThread = mock(GameThread.class);
		when(sppf.getGameThread()).thenReturn(gameThread);
		
		controller.setPlayingField(sppf);
		
		//Call the method
		controller.updatePauseButton();
		
		//If there is no player alive the pause button should be disabled
		assertTrue(controller.getPauseButton().isDisabled());
	}

}
