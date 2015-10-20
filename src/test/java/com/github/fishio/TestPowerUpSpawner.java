package com.github.fishio;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.github.fishio.factories.PowerUpSpawner;
import com.github.fishio.game.GameThread;
import com.github.fishio.gui.SlimGuiTest;
import com.github.fishio.power_ups.ExtraLifePowerUp;
import com.github.fishio.power_ups.FreezePowerUp;
import com.github.fishio.power_ups.PowerUp;
import com.github.fishio.power_ups.SuperSpeedPowerUp;
import com.github.fishio.settings.Settings;

/**
 * Tests the PowerUpSpawer class.
 */
public class TestPowerUpSpawner extends SlimGuiTest {

	private PowerUpSpawner pus;
	
	private PlayingField pf;
	
	/**
	 * Sets the PowerUpSpawner attribute in this class
	 * before every test.
	 */
	@Before
	public void setUp() {
		this.pf = mock(SinglePlayerPlayingField.class);
		
		GameThread gt = Mockito.spy(new GameThread(pf));
		when(pf.getGameThread()).thenReturn(gt); //Preventing nullPointerExceptions from the gameThread
		when(pf.getFPS()).thenReturn(60); //Making sure our PowerUpSpawner doesn't think the FPS is 0.
		when(pf.getWidth()).thenReturn(100); //Same as above
		
		this.pus = Mockito.spy(new PowerUpSpawner(pf));
	}
	
	/**
	 * Tests the postTick method.
	 */
	@Test
	public void testPostTick() {
		int intervalTicks = pf.getFPS() * pus.getInterval();
		
		//Making sure that no PowerUp is added after when the interval hasn't passed yet
		for (int i = 0; i < intervalTicks - 1; i++) {
			pus.postTick();
			verify(pf, never()).add(anyObject());
		}
		
		//Now that the interval has passed, a random PowerUp should have been added.
		pus.postTick();
		verify(pf).add(Mockito.any(PowerUp.class));
		
		//Making sure that it resets after an interval has passed
		for (int i = 0; i < intervalTicks - 1; i++) {
			pus.postTick();
			verify(pf).add(anyObject());
		}
		
		//Again a random powerUp should have been added
		pus.postTick();
		verify(pf, times(2)).add(Mockito.any(PowerUp.class));
	}
}
