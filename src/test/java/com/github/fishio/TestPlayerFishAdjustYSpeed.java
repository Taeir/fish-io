package com.github.fishio;


import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import javafx.scene.Scene;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

/**
 * Tests the adjustXSpeed method in the PlayerFish class.
 * {@link PlayerFish#adjustXSpeed()}
 */
@RunWith(Parameterized.class)
public class TestPlayerFishAdjustYSpeed {

	private PlayerFish fish;
	private double expectedSpeedY;
	
	private static double acc = 
			new PlayerFish(Mockito.mock(BoundingBox.class), Mockito.mock(Scene.class)).getAcceleration();
	
	/**
	 * @param upPressed
	 * 		Whether the PlayerFish believes if the up key is pressed.
	 * @param downPressed
	 * 		Whether the PlayerFish believes if the down key is pressed.
	 * @param speedY
	 * 		The initial vertical speed of the PlayerFish.
	 * @param expectedSpeedY
	 * 		The expected vertical speed of the PlayerFish once adjustYSpeed()
	 * 		is called.
	 */
	public TestPlayerFishAdjustYSpeed(boolean upPressed, boolean downPressed, 
			double speedY, double expectedSpeedY) {
		
		fish = new PlayerFish(Mockito.mock(BoundingBox.class), Mockito.mock(Scene.class));
	
		fish.setUpPressed(upPressed);
		fish.setDownPressed(downPressed);
		fish.setSpeedY(speedY);
		
		this.expectedSpeedY = expectedSpeedY;
	}
	
	/**
	 * @return a collection of inputs and expected output from
	 * 		the PlayerFish class.
	 */
	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{false, false, 0.0, 0.0},
				{false, false, 1.0, 1.0 - acc},
				{false, false, -1.0, acc - 1.0},
				{true, false, 1.0, 1.0 + acc},
				{true, false, -1.0, -1.0 + 2 * acc},
				{false, true, -1.0, -1.0 - acc},
				{false, true, 1.0, 1.0 - 2 * acc},
				{true, true, 0.0, 0.0},
				{true, true, 1.0, 1.0 - acc},
				{true, true, -1.0, acc - 1.0}
		});
	}
	
	/**
	 * Tests the PlayerFish using the given parameters.
	 */
	@Test
	public void test() {
		fish.adjustYSpeed();
		assertEquals(expectedSpeedY, fish.getSpeedY(), 0.0000001D);
	}

}
