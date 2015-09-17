package com.github.fishio.logging;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Test class for TimeStampFormat.
 *
 */
public class TestTimeStampFormat {

	private TimeStampFormat mockedFormatter;
	
	/**
	 * Setup Mockito to replace the getTimeStampMethod.
	 */
	@Before
	public void setUp() {
		mockedFormatter = Mockito.spy(new TimeStampFormat());
	}
	
	/**
	 * Test the getTimeStamp method. <br>
	 * Note: Since there are a lot of factors that can influence
	 * either the speed of the tests and method execution a different
	 * approach then normal is taken: The date before and after the test 
	 * are recorded. The date of test itself should lie between these two dates,
	 * at least. Checking this gives some confidence that the time stamp that the
	 * method creates is correct.
	 */
	@Test
	public void testGetTimeStamp() {
		TimeStampFormat timeStampFormat = new TimeStampFormat();
		
		// Date before test
		Date beforeTest = new Date();
		
		Date test = new Date();
		try {
			// Parse date returned from getTimeStamp method
			test = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
				.parse(timeStampFormat.getTimeStamp());
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		}
		
		// Date after test
		Date afterTest = new Date();
		
		// Check if date is after the beginning of the test
		assertFalse(beforeTest.after(test));
		// Check if date is before the end of the test
		assertFalse(afterTest.before(test));
	}
	
	/**
	 * Test the formatOutput method with some inputs(1).
	 */
	@Test
	public void testFormatOutPut1() {
		String mockedDate = "MockedTimeStamp";
		when(mockedFormatter.getTimeStamp()).thenReturn(mockedDate);
		
		assertEquals("[" + mockedDate + "] [" + LogLevel.ERROR.toString() + "]:-\tTest1.",
				mockedFormatter.formatOutput(LogLevel.ERROR, "Test1."));
	}
	
	/**
	 * Test the formatOutput method with some inputs(2).
	 */
	@Test
	public void testFormatOutPut2() {
		String mockedDate = "MockedTimeStamp";
		when(mockedFormatter.getTimeStamp()).thenReturn(mockedDate);
		
		assertEquals("[" + mockedDate + "] [" + LogLevel.WARNING.toString() + "]:-\tTest2.",
				mockedFormatter.formatOutput(LogLevel.WARNING, "Test2."));
	}
}