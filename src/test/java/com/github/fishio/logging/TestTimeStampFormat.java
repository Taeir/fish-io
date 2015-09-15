package com.github.fishio.logging;


import static org.junit.Assert.assertEquals;
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
	 * Test String construction of the format method (1).<br>
	 * 
	 * Note: It is very important to know why the string output is only partially tested.
	 * This must me done because otherwise the moment when the test is run is important and
	 * the amount time it takes to finish the test also is important. This is because the
	 * test retrieves a current time stamp, so results may differ on different computers,
	 * which is desirable. To make sure this does not happen, the time stamp
	 * relevant part is cut out. This ensures the test will pass when the rest of the format
	 * is correct.
	 */
	@Test
	public void testFormat1() {
		String testString = formatter.formatOutput(LogLevel.ERROR, "Test1.");
		testString = testString.substring(0, 1) + testString.substring(20, testString.length());
		assertEquals("[] [" + LogLevel.ERROR.toString() + "]:- 	Test1.", 
				testString);
	}

	/**
	 * Test String construction of the format method (2).<br>
	 * 
	 * Note: It is very important to know why the string output is only partially tested.
	 * This must me done because otherwise the moment when the test is run is important and
	 * the amount time it takes to finish the test also is important. This is because the
	 * test retrieves a current time stamp, so results may differ on different computers,
	 * which is not desirable. To make sure this does not happen, the time stamp
	 * relevant part is cut out. This ensures the test will pass when the rest of the format
	 * is correct.
	 */
	@Test
	public void testFormat2() {
		String testString = formatter.formatOutput(LogLevel.WARNING, "Test2.");
		testString = testString.substring(0, 1) + testString.substring(20, testString.length());
		assertEquals("[] [" + LogLevel.WARNING.toString() + "]:- 	Test2.", 
				testString);
	}
}