package com.github.fishio.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

/**
 * Test class for ConsoleHandler.
 *
 */
public class TestTxtFileHandler extends TestIHandler {

	private TxtFileHandler handler;
	private String filename = "test.txt";
	
	/**
	 * Set up handler and a buffered writer.
	 * @throws Exception 
	 * 		if an error occurs when creating the file.
	 */
	@Before
	public void setUp() throws Exception {
		handler = new TxtFileHandler(folder.newFile(filename));
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
	 * Test initialization with custom formatter.
	 * Test formatter.
	 */
	@Test
	public void testTxtFileHandlerCustom() {
		TxtFileHandler handler2 = new TxtFileHandler(new TimeStampFormat(),
				new File(folder.getRoot(), filename));
		assertTrue(handler2.getFormat() instanceof TimeStampFormat);
		try {
			handler2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test the set and get method.
	 */
	@Test
	public void testSetGetBW() {
		BufferedWriter bw2 = null;
		try {
			bw2 = new BufferedWriter(new FileWriter(new File(folder.getRoot(), filename)));
		} catch (IOException e) {
			fail("Exception while trying to write to file.");
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
		when(formatter.formatOutput(LogLevel.ERROR, filename)).thenReturn("Test Output");
		BufferedWriter mockedBW = Mockito.mock(BufferedWriter.class);
		
		handler.setFormat(formatter);
		handler.setBufferedWriter(mockedBW);
		handler.output(LogLevel.ERROR, filename);
		Mockito.verify(formatter).formatOutput(LogLevel.ERROR, filename);
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
	 * Test equals with itself.
	 */
	@Test
	public void testEqualsItself() {
		assertEquals(handler, handler);
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
	
	/**
	 * Test equals with original format is null.
	 * Other has not null format.
	 */
	@Test
	public void testEqualsFormatNull() {
		TxtFileHandler handler2 = new TxtFileHandler(new TimeStampFormat(),
				new File(folder.getRoot(), filename));
		
		handler.setFormat(null);
		assertFalse(handler.equals(handler2));
		try {
			handler2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test equals with original format is null.
	 * Other has null format.
	 */
	@Test
	public void testEqualsFormatNullBoth() {
		TxtFileHandler handler2 = new TxtFileHandler(new TimeStampFormat(),
				new File(folder.getRoot(), filename));
		
		handler.setFormat(null);
		handler2.setFormat(null);
		assertEquals(handler, handler2);
		try {
			handler2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test equals with original format is not null.
	 * Other has null format.
	 */
	@Test
	public void testEqualsFormatNullOther() {
		TxtFileHandler handler2 = new TxtFileHandler(new TimeStampFormat(),
				new File(folder.getRoot(), "test.txt"));
		
		handler2.setFormat(null);
		assertFalse(handler.equals(handler2));
		try {
			handler2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test equals with equal TxtFileHandler.
	 */
	@Test
	public void testEqualsEqual() {
		TxtFileHandler handler2 = new TxtFileHandler(new TimeStampFormat(),
				new File(folder.getRoot(), filename));
		DefaultFormat df = new DefaultFormat();
		handler.setFormat(df);
		handler2.setFormat(df);
		
		assertEquals(handler, handler2);
		try {
			handler2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public IHandler getIHandler() {
		return new TxtFileHandler(new File(folder.getRoot(), filename));
	}
}
