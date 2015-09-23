package com.github.fishio.achievements;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * This test class tests the Achievement class.
 *
 */
public class TestAchievement {
	
	/**
	 * Tests the {@link Achievement#Achievement(String, int)} constructor.
	 */
	@Test
	public void testAchievement1() {
		Achievement achievetest1 = new Achievement("Testachievement", 3);
	}
	
	/**
	 * Tests the {@link Achievement#Achievement(String)} constructor.
	 */
	@Test
	public void testAchievement2() {
		Achievement achievetest2 = new Achievement("Testachievement");
	}
	
	/**
	 * Tests the {@link Achievement#getName()} method.
	 */
	@Test
	public void testgetName() {
		Achievement achievetest1 = new Achievement("Testachievement", 3);
		assertEquals("Testachievement", achievetest1.getName());
		
		Achievement achievetest2 = new Achievement("Testachievement");
		assertEquals("Testachievement", achievetest2.getName());
	}
	
	/**
	 * Tests the {@link Achievement#getLevel()} method.
	 */
	@Test
	public void testgetLevel() {
		Achievement achievetest1 = new Achievement("Testachievement", 3);
		assertEquals(3, achievetest1.getLevel(), 0.0);
		
		Achievement achievetest2 = new Achievement("Testachievement");
		assertEquals(0, achievetest2.getLevel(), 0.0);
	}
	
	/**
	 * Tests the {@link Achievement#setLevel(int)} method.
	 */
	@Test
	public void testsetLevel() {
		Achievement achievetest1 = new Achievement("Testachievement", 3);
		achievetest1.setLevel(5);
		assertEquals(5, achievetest1.getLevel(), 0.0);
		
		Achievement achievetest2 = new Achievement("Testachievement");
		achievetest2.setLevel(5);
		assertEquals(5, achievetest2.getLevel(), 0.0);
	}

}
