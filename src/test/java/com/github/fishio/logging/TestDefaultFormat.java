package com.github.fishio.logging;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test class for DefaultFormater.
 *
 */
public class TestDefaultFormat {

	private static DefaultFormat formatter;
	
	/**
	 * Initialize a DefaultFormat. 
	 */
	@BeforeClass
	public static void setUp() {
		formatter = new DefaultFormat();
	}
	
	/**
	 * Test String construction of the format method (1).
	 */
	@Test
	public void testFormat1() {
		assertEquals("[" + LogLevel.ERROR.toString() + "]:- 	Test1.",
				formatter.formatOutput(LogLevel.ERROR, "Test1."));
	}

	/**
	 * Test String construction of the format method (2).
	 */
	@Test
	public void testFormat2() {
		assertEquals("[" + LogLevel.WARNING.toString() + "]:- 	Test2.", 
				formatter.formatOutput(LogLevel.WARNING, "Test2."));
	}
}
