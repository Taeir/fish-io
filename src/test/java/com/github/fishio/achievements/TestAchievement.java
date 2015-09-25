package com.github.fishio.achievements;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * This test class tests the Achievement class.
 *
 */
public class TestAchievement {
	
	private Achievement achievetest1;
	
	/**
	 * Sets up an Achievement object before the tests.
	 */
	@Before
	public void setUp() {
		achievetest1 = new Achievement("Testachievement", 3);
	}
	
	/**
	 * Tests the {@link Achievement#Achievement(String, int)} constructor for
	 * two seperately defined Achievements.
	 */
	@Test
	public void testAchievementconstructor1_twoAchievements() {
		Achievement achievetest2 = new Achievement("Testachievement", 3);
		assertEquals(achievetest1, achievetest2);
	}
	
	/**
	 * Tests the {@link Achievement#Achievement(String, int)} constructor for
	 * its name field.
	 */
	@Test
	public void testAchievementconstructor1_name() {
		assertEquals(achievetest1.getName(), "Testachievement");
	}
	
	/**
	 * Tests the {@link Achievement#Achievement(String, int)} constructor for
	 * its level field.
	 */
	@Test
	public void testAchievementconstructor1_level() {
		assertEquals(3, achievetest1.getLevel(), 0.0);
	}
	
	/**
	 * Tests the {@link Achievement#Achievement(String)} constructor for its
	 * name field.
	 */
	@Test
	public void testAchievementconstructor2_name() {
		assertEquals(achievetest1.getName(), "Testachievement");
	}
	
	/**
	 * Tests the {@link Achievement#Achievement(String)} constructor for its
	 * level field.
	 */
	@Test
	public void testAchievementconstructor2_level() {
		Achievement achievetest2 = new Achievement("Testachievment");
		assertEquals(0, achievetest2.getLevel(), 0.0);
	}
	

	/**
	 * Tests the {@link Achievement#getName()} method.
	 */
	@Test
	public void testgetName1() {
		assertEquals("Testachievement", achievetest1.getName());
		
		Achievement achievetest2 = new Achievement("Testachievement");
		assertEquals("Testachievement", achievetest2.getName());
	}
	
	/**
	 * Tests the {@link Achievement#getName()} method.
	 */
	@Test
	public void testgetName2() {
		Achievement achievetest2 = new Achievement("Testachievement");
		assertEquals("Testachievement", achievetest2.getName());
	}
	
	/**
	 * Tests the {@link Achievement#getLevel()} method.
	 */
	@Test
	public void testgetLevel1() {
		assertEquals(3, achievetest1.getLevel(), 0.0);
	}
	
	/**
	 * Tests the {@link Achievement#getLevel()} method.
	 */
	@Test
	public void testgetLevel2() {
		Achievement achievetest2 = new Achievement("Testachievement");
		assertEquals(0, achievetest2.getLevel(), 0.0);
	}
	
	/**
	 * Tests the {@link Achievement#setLevel(int)} method.
	 */
	@Test
	public void testsetLevel1() {
		achievetest1.setLevel(5);
		assertEquals(5, achievetest1.getLevel(), 0.0);
	}
	
	/**
	 * Tests the {@link Achievement#setLevel(int)} method.
	 */
	@Test
	public void testsetLevel2() {
		Achievement achievetest2 = new Achievement("Testachievement");
		achievetest2.setLevel(5);
		assertEquals(5, achievetest2.getLevel(), 0.0);
	}
	
	/**
	 * Test equals with itself.
	 */
	@Test
	public void testEqualsItself() {
		assertEquals(achievetest1, achievetest1);
	}
	
	/**
	 * Test equals with null.
	 */
	@Test
	public void testEqualsNull() {
		assertFalse(achievetest1.equals(null));
	}
	
	/**
	 * Test equals with other class.
	 */
	@Test
	public void testEqualsOtherClass() {
		assertFalse(achievetest1.equals(new Double(1.0)));
	}
	
	/**
	 * Test equals with different achievement in second object.
	 */
	@Test
	public void testEqualsDifferentAchievement() {
		assertFalse(achievetest1.equals(new Achievement("Testother", 0)));
	}
	
	/**
	 * Test equals with different achievement in second object.
	 */
	@Test
	public void testEqualsSameAchievement() {
		assertTrue(achievetest1.equals(new Achievement("Testachievement", 3)));
	}
}
