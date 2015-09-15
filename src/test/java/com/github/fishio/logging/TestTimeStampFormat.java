package com.github.fishio.logging;


import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test class for TimeStampFormat.
 *
 */
public class TestTimeStampFormat {

	private static TimeStampFormat formatter;
	
	/**
	 * Initialize a DefaultFormat. 
	 */
	@BeforeClass
	public static void setUp() {
		formatter = new TimeStampFormat();
	}
	
	/**
	 * Test String construction of the format method (1).
	 */
	@Test
	public void testFormat1() {
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
		assertEquals("[" + timeStamp + "] [" + LogLevel.ERROR.toString() + "]:- Test1.", 
				formatter.formatOutput(LogLevel.ERROR, "Test1."));
	}

	/**
	 * Test String construction of the format method (2).
	 */
	@Test
	public void testFormat2() {
		assertEquals("[" + LogLevel.WARNING.toString() + "]:- Test2.", 
				formatter.formatOutput(LogLevel.WARNING, "Test2."));
	}
}