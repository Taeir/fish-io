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

import javafx.scene.Scene;

/**
 * Tests the adjustSpeedX method in the KeyListenerBehaviour class 
 * using a Parameterized test.
 */
@RunWith(Parameterized.class)
public class TestKeyListenerBehaviourAdjustSpeedX {

	private KeyListenerBehaviour behaviour;
	private double expectedSpeedX;
	
	private static double acceleration = 0.1;

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
	public TestKeyListenerBehaviourAdjustSpeedX(boolean leftPressed, boolean rightPressed, 
			double speedX, double expectedSpeedX) {
		
		behaviour = new KeyListenerBehaviour(Mockito.mock(Scene.class), 
				null, null, null, null, acceleration, 4);
		
		behaviour.setLeftPressed(leftPressed);
		behaviour.setRightPressed(rightPressed);
		
		behaviour.setSpeedVector(new Vec2d(speedX, 0));
		
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
				{false, false, 1.0, 1.0 - acceleration},
				{false, false, -1.0, acceleration - 1.0},
				{false, true, 1.0, 1.0 + acceleration},
				{false, true, -1.0, -1.0 + 2 * acceleration},
				{true, false, -1.0, -1.0 - acceleration},
				{true, false, 1.0, 1.0 - 2 * acceleration},
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
		behaviour.adjustXSpeed();
		assertEquals(expectedSpeedX, behaviour.getSpeedVector().x, 0.0000001D);
	}
	
}
