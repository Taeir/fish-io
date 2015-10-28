package com.github.fishio.control;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import com.github.fishio.audio.AudioEngine;
import com.github.fishio.gui.NoSoundSlimGuiTest;
import com.github.fishio.logging.LogLevel;
import com.github.fishio.multiplayer.client.FishIOClient;
import com.github.fishio.multiplayer.client.MultiplayerClientPlayingField;
import com.github.fishio.multiplayer.server.FishIOServer;
import com.github.fishio.multiplayer.server.MultiplayerServerPlayingField;
import com.github.fishio.test.util.TestUtil;

/**
 * Test class for {@link MultiplayerGameController}.
 */
public class MultiplayerGameControllerTest extends NoSoundSlimGuiTest {
	private MultiplayerGameController controller;
	private Scene scene;
	private Canvas canvas;
	
	/**
	 * Initialize the logger for testing.
	 */
	@BeforeClass
	public static void setUpClass() {
		TestUtil.setUpLoggerForTesting(LogLevel.TRACE);
	}
	
	/**
	 * When all tests are done, this method will restore the FishIOClient
	 * and FishIOServer to a working state.
	 */
	@AfterClass
	public static void tearDownClass() {
		Whitebox.setInternalState(FishIOClient.getInstance(), "connected", false);
		Whitebox.setInternalState(FishIOServer.getInstance(), "started", false);
		
		//Restore the logger
		TestUtil.restoreLogger();
	}
	
	/**
	 * Creates a new controller before every test.
	 */
	@Before
	public void setUp() {
		//Reset client and server
		Whitebox.setInternalState(FishIOClient.getInstance(), "connected", false);
		Whitebox.setInternalState(FishIOServer.getInstance(), "started", false);
		
		canvas = spy(new Canvas());
		scene = mock(Scene.class);
		
		//Create the controller
		controller = new MultiplayerGameController();
		
		//Initialize the fields
		controller.initFXMLForTest();
		
		controller.setCanvas(canvas);
		
		//Initialize the controller
		controller.init(scene);
		
		//Reset the mocked logger
		TestUtil.resetMockHandler();
	}
	
	/**
	 * Sets up the controller as if we are connected as a Client.
	 */
	private void initClient() {
		//Fake that we are connected
		Whitebox.setInternalState(FishIOClient.getInstance(), "connected", true);
		
		//Create a playingfield and set it
		MultiplayerClientPlayingField mcpf =
				new MultiplayerClientPlayingField(60, controller.getCanvas(), 10000, 10000);
		
		SimpleObjectProperty<MultiplayerClientPlayingField> playingFieldProperty =
				Whitebox.getInternalState(FishIOClient.getInstance(), "playingFieldProperty");
		
		playingFieldProperty.set(mcpf);
	}
	
	/**
	 * Sets up the controller as if we are hosting as a Server.
	 */
	private void initServer() {
		//Fake that the server has started
		Whitebox.setInternalState(FishIOServer.getInstance(), "started", true);
		
		//Create a plyingfield and set it
		MultiplayerServerPlayingField mspf =
				new MultiplayerServerPlayingField(60, controller.getCanvas(), 10000, 10000);
		
		SimpleObjectProperty<MultiplayerServerPlayingField> playingFieldProperty =
				Whitebox.getInternalState(FishIOServer.getInstance(), "playingFieldProperty");
		
		playingFieldProperty.set(mspf);
	}
	
	/**
	 * Test method for {@link MultiplayerGameController#onSwitchTo()},
	 * when we are a client.
	 */
	@Test
	public void testOnSwitchToClient() {
		initClient();
		
		controller.onSwitchTo();
		
		//The end buttons should display disconnect when we are a client.
		assertEquals("Disconnect", controller.getEndButton().getText());
		assertEquals("Disconnect", controller.getDeathScreenEndButton().getText());
	}
	
	/**
	 * Test method for {@link MultiplayerGameController#onSwitchTo()},
	 * when we are a server.
	 */
	@Test
	public void testOnSwitchToServer() {
		initServer();
		
		controller.onSwitchTo();
		
		//The end buttons should display stop server when we are a server.
		assertEquals("Stop server", controller.getEndButton().getText());
		assertEquals("Stop server", controller.getDeathScreenEndButton().getText());
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
	 * Test method for {@link MultiplayerGameController#onMute()}.
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
	 * Test method for {@link MultiplayerGameController#onMute()}.
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
	 * Test method for {@link MultiplayerGameController#onMute()}.
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
	 * Test method for {@link MultiplayerGameController#onEnd()}.
	 */
	@Test
	public void testOnEnd() {
		controller.onEnd();
		
		//No errors should occur
		verify(TestUtil.getMockHandler(), times(0)).output(any(), any());
	}

}
