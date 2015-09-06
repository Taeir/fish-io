package com.github.fishio;

import static org.junit.Assert.assertEquals;
import javafx.stage.Stage;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Tests the PlayerFish class.
 */
public class TestPlayerFish {

	private PlayerFish pf;
	
	/**
	 * Creates a new PlayerFish before each test
	 * case.
	 */
	@Before
	public void setUp() {
		pf = new PlayerFish(Mockito.mock(BoundingBox.class), Mockito.mock(Stage.class));
	}
	
	/**
	 * Tests {@link PlayerFish#getSpeedVector()}.
	 */
	@Test
	public void testGetSpeedVector() {
		pf.setSpeedX(3.0);
		pf.setSpeedY(5.0);
		
		assertEquals(new Vec2d(3.0, 5.0), pf.getSpeedVector());
	}
	
}
