package com.github.fishio.achievements;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * This test class tests the Achievement class.
 */
public abstract class TestAchievement {
	
	/**
	 * @return
	 * 		a new achievement named test and level 0.
	 */
	public Achievement newAchievement() {
		return newAchievement("test", 0);
	}
	
	/**
	 * @param name
	 * 		the name the achievement should have.
	 * @param level
	 * 		the level the achievement should have.
	 * 
	 * @return
	 * 		a new achievement with the given name and level.
	 */
	public Achievement newAchievement(String name, int level) {
		return new Achievement(name, level) {
			@Override
			public void updateAchievement(AchievementObserver observer) { }
		};
	}
	
	/**
	 * Tests the {@link Achievement#Achievement(String, int)} constructor for
	 * its name field.
	 */
	@Test
	public void testAchievementconstructor_name() {
		Achievement a = newAchievement("hello", 0);
		assertEquals("hello", a.getName());
	}
	
	/**
	 * Tests the {@link Achievement#Achievement(String, int)} constructor for
	 * its level field.
	 */
	@Test
	public void testAchievementconstructor_level() {
		Achievement a = newAchievement("hello2", 3);
		assertEquals(3, a.getLevel());
	}
	
	/**
	 * Tests the {@link Achievement#getName()} method.
	 */
	@Test
	public void testgetName1() {
		Achievement a = newAchievement("hello3", 0);
		assertEquals("hello3", a.getName());
	}
	
	/**
	 * Tests the {@link Achievement#getLevel()} method.
	 */
	@Test
	public void testGetLevel() {
		Achievement a = newAchievement();
		assertEquals(0, a.getLevel());
	}

	/**
	 * Tests the {@link Achievement#setLevel(int)} method.
	 */
	@Test
	public void testSetLevel() {
		Achievement a = newAchievement();
		a.setLevel(5);
		assertEquals(5, a.getLevel());
	}
	
	/**
	 * Test the {@link Achievement#getLevelProperty()} method.
	 */
	@Test
	public void testGetLevelProperty() {
		Achievement a = newAchievement("test", 5);
		SimpleIntegerProperty levelProperty = new SimpleIntegerProperty(5);
		assertEquals(a.getLevelProperty(), levelProperty.get());
	}

	/**
	 * Test equals with itself.
	 */
	@Test
	public void testEqualsItself() {
		Achievement a = newAchievement();
		assertEquals(a, a);
	}
	
	/**
	 * Test equals with null.
	 */
	@Test
	public void testEqualsNull() {
		assertNotEquals(null, newAchievement());
	}
	
	/**
	 * Test equals with other class.
	 */
	@Test
	public void testEqualsOtherClass() {
		assertFalse(newAchievement().equals(new Double(1.0)));
	}
	
	/**
	 * Test equals with different achievement in second object.
	 */
	@Test
	public void testEqualsDifferentAchievement() {
		//These are different classes.
		assertNotEquals(newAchievement(), newAchievement());
	}
	
	/**
	 * Test for the hashCode method.
	 */
	@Test
	public void testHashCode() {
		Achievement a = newAchievement("a", 1);
		
		assertEquals("a".hashCode() * 31 + 1, a.hashCode());
	}
}
