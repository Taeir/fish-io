package com.github.fishio.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Test class for ConsoleHandler.
 *
 */
public class TestTxtFileHandler extends TestIHandler {

	private TxtFileHandler handler;
	
	//TODO add factory methods for get and set methods
	
	/**
	 * Set up handler and a buffered writer.
	 */
	@Before
	public void setUp() {
		handler = new TxtFileHandler(
				new File("src\\test\\java\\com\\github\\fishio\\logging\\TEMP\\test.txt"));
	}
	
	/**
	 * Remove Test file after test ran.
	 */
	@AfterClass
	public static void removeTestFile() {
		File file = new File("src\\test\\java\\com\\github\\fishio\\logging\\TEMP\\test.txt");
		boolean b = file.delete();
		System.out.println("Deleted file: " +  b);
	}
	
	/**
	 * Test initialization with default formatter.
	 * Test formatter.
	 */
	@Test
	public void testTxtFileHandlerDefault() {
		assertTrue(handler.getFormat() instanceof DefaultFormat);
	}
	
	
	
	/**
	 * Test the set and get method.
	 */
	@Test
	public void testSetGetBW() {
		BufferedWriter bw2 = null;
		try {
			bw2 = new BufferedWriter(new FileWriter(
					new File("src\\test\\java\\com\\github\\fishio\\logging\\TEMP\\test.txt")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		handler.setBufferedWriter(bw2);
		assertEquals(bw2, handler.getBufferedWriter());
	}
	
	/**
	 * Test if output calls correct method.
	 */
	@Test
	public void testOutput() {
		IFormatter formatter = Mockito.mock(DefaultFormat.class);
		when(formatter.formatOutput(LogLevel.ERROR, "Test")).thenReturn("Test Output");
		BufferedWriter mockedBW = Mockito.mock(BufferedWriter.class);
		
		handler.setFormat(formatter);
		handler.setBufferedWriter(mockedBW);
		handler.output(LogLevel.ERROR, "Test");
		Mockito.verify(formatter).formatOutput(LogLevel.ERROR, "Test");
		try {
			Mockito.verify(mockedBW).write("Test Output");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test Hashcode formatter and bw null.
	 */
	@Test
	public void testHashcodeBothNull() {
		handler.setFormat(null);
		handler.setBufferedWriter(null);
		assertEquals(31, handler.hashCode());
	}
	
	/**
	 * Test Hashcode formatter null.
	 */
	@Test
	public void testHashcodeFormatterNull() {
		handler.setFormat(null);
		assertEquals(31 + handler.getBufferedWriter().hashCode(), handler.hashCode());
	}
	
	/**
	 * Test Hashcode buffered writer null.
	 */
	@Test
	public void testHashcodeBWNull() {
		handler.setBufferedWriter(null);
		IFormatter format = handler.getFormat();
		assertEquals(31 + format.hashCode() , handler.hashCode());
	}
	
	/**
	 * Test Hashcode formatter with default.
	 */
	//@Test
	public void testHashcodeDefault() {
		assertEquals(31 + handler.getFormat().hashCode(), handler.hashCode());
	}
	
	/**
	 * Test equals with itself.
	 */
	@Test
	public void testEqualsItself() {
		assertTrue(handler.equals(handler));
	}
	
	/**
	 * Test equals with other class.
	 */
	@Test
	public void testEqualsNull() {
		assertFalse(handler.equals(null));
	}
	
	/**
	 * Test equals with other class.
	 */
	@Test
	public void testEqualsOtherClass() {
		assertFalse(handler.equals(new Double(1.0)));
	}
	
	
}
