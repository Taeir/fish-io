package com.github.fishio;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests {@link BoundingBox#intersects(BoundingBox)}.
 */
@RunWith (Parameterized.class)
public class TestBoundingBoxIntersects {
	
	private BoundingBox bb1;
	private BoundingBox bb2;
	
	private boolean expected;
	
	/**
	 * @param xmin
	 * 		The smallest x-coordinate of the second BoundingBox.
	 * @param ymin
	 * 		The smallest y-coordinate of the second BoundingBox.
	 * @param xmax
	 * 		The largest x-coordinate of the second BoundingBox.
	 * @param ymax
	 * 		The largest y-coordinate of the second BoundingBox.
	 * @param expected
	 * 		Whether the BoundingBox should collide with the other
	 * 		BoundingBox.
	 */
	public TestBoundingBoxIntersects(double xmin, double ymin, double xmax, double ymax, boolean expected) {
		this.bb2 = new BoundingBox(xmin, ymin, xmax, ymax);
		this.expected = expected;
	}
	
	/**
	 * Creates the default BoundingBox b1 before each test case
	 * execution.
	 */
	@Before
	public void createBb1() {
		bb1 = new BoundingBox(0.0, 0.0, 10.0, 10.0);
	}
	
	/**
	 * @return a collection of inputs and expected output from
	 * 		the intersects method.
	 */
	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{0.0, 0.0, 5.0, 5.0, true},
				{4.0, 4.0, 5.0, 5.0, true},
				{5.0, 10.0, 5.0, 5.0, true},
				{0.0, 5.0, 5.0, 9.9, true},
				{5.0, 5.0, 0.0, 5.0, true},
				{20.0, 20.0, 25.0, 25.0, false},
				{-5.0, -5.0, 5.0, 10.0, true},
				{-5.0, -5.0, -1.0, -1.0, false}
		});
	}
	
	/**
	 * Tests the intersects method using the given parameters.
	 */
	@Test
	public void test() {
		assertEquals(expected, bb2.intersects(bb1));
		assertEquals(expected, bb1.intersects(bb2));
	}
}
