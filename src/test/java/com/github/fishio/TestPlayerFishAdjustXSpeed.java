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
public class TestPlayerFishAdjustXSpeed {

	private PlayerFish fish;
	private double expectedSpeedX;
	
	private static double acc = 
			new PlayerFish(Mockito.mock(BoundingBox.class), Mockito.mock(Scene.class)).getAcceleration();
	
	/**
	 * @param leftPressed
	 * 		Whether the PlayerFish believes if the left key is pressed.
	 * @param rightPressed
	 * 		Whether the PlayerFish believes if the right key is pressed.
	 * @param speedX
	 * 		The initial horizontal speed of the PlayerFish.
	 * @param expectedSpeedX
	 * 		The expected horizontal speed of the PlayerFish once adjustXSpeed()
	 * 		is called.
	 */
	public TestPlayerFishAdjustXSpeed(boolean leftPressed, boolean rightPressed, 
			double speedX, double expectedSpeedX) {
		
		fish = new PlayerFish(Mockito.mock(BoundingBox.class), Mockito.mock(Scene.class));
	
		fish.setLeftPressed(leftPressed);
		fish.setRightPressed(rightPressed);
		fish.setSpeedX(speedX);
		
		this.expectedSpeedX = expectedSpeedX;
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
				{false, true, 1.0, 1.0 + acc},
				{false, true, -1.0, -1.0 + 2 * acc},
				{true, false, -1.0, -1.0 + acc},
				{true, false, 1.0, 1.0 - 2 * acc},
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
		fish.adjustXSpeed();
		assertEquals(expectedSpeedX, fish.getSpeedX(), 0.0000001D);
	}

}
