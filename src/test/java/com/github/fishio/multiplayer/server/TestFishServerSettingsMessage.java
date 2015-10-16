package com.github.fishio.multiplayer.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Test class for {@link FishServerSettingsMessage}.
 */
public class TestFishServerSettingsMessage {

	/**
	 * Test method for {@link FishServerSettingsMessage#setSetting(String, Object)}.
	 */
	@Test
	public void testSetSetting() {
		FishServerSettingsMessage fssm = new FishServerSettingsMessage();
		fssm.setSetting("Hello", 100);
		
		assertEquals(100, fssm.getSetting("Hello"));
	}

	/**
	 * Test method for {@link FishServerSettingsMessage#getSetting(String)}.
	 */
	@Test
	public void testGetSetting() {
		FishServerSettingsMessage fssm = new FishServerSettingsMessage();
		
		assertNull(fssm.getSetting("Hello"));
	}

}
