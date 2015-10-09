package com.github.fishio.behaviours;

import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

import com.github.fishio.Vec2d;

import javafx.stage.Stage;

/**
 * Tests the adjustSpeedY method in the KeyListenerBehaviour class 
 * using a Parameterized test.
 */
@RunWith(Parameterized.class)
public class TestKeyListenerBehaviourAdjustSpeedY {

	private KeyListenerBehaviour behaviour;
	private double expectedSpeedY;
	
	private static double acceleration = 0.1;

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
	public TestKeyListenerBehaviourAdjustSpeedY(boolean upPressed, boolean downPressed, 
			double speedY, double expectedSpeedY) {
		
		behaviour = new KeyListenerBehaviour(Mockito.mock(Stage.class), 
				null, null, null, null, acceleration, 4);
		
		behaviour.setUpPressed(upPressed);
		behaviour.setDownPressed(downPressed);
		
		behaviour.setSpeedVector(new Vec2d(0, speedY));
		
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
			{false, false, 1.0, 1.0 - acceleration},
			{false, false, -1.0, acceleration - 1.0},
			{true, false, 1.0, 1.0 + acceleration},
			{true, false, -1.0, -1.0 + 2 * acceleration},
			{false, true, -1.0, -1.0 - acceleration},
			{false, true, 1.0, 1.0 - 2 * acceleration},
			{true, true, 0.0, 0.0},
			{true, true, 1.0, 1.0 - acceleration},
			{true, true, -1.0, acceleration - 1.0}
		});
	}
	
	/**
	 * Tests the PlayerFish using the given parameters.
	 */
	@Test
	public void test() {
		behaviour.adjustYSpeed();
		assertEquals(expectedSpeedY, behaviour.getSpeedVector().y, 0.0000001D);
	}
	
}

