package com.github.fishio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.fishio.logging.ConsoleHandler;
import com.github.fishio.logging.Log;
import com.github.fishio.logging.LogLevel;

/**
 * Test class for {@link HighScore}.
 */
public class HighScoreTest {
	
	/**
	 * Save the current highscores before running the test.
	 */
	@BeforeClass
	public static void copyScores() {
		File file = new File("highScores.yml");
		File newFile = new File("highScores.tmp");
		newFile.delete();
		file.renameTo(newFile);
	}
	
	/**
	 * Restore the old highscore file.
	 */
	@AfterClass
	public static void restoreScores() {
		File file = new File("highScores.yml");
		File tempFile = new File("highScores.tmp");
		file.delete();
		tempFile.renameTo(file);
	}
	
	/**
	 * Reset all high scores.
	 */
	@Before
	public void before() {
		HighScore.getAll().clear();
	}
	
	/**
	 * Test for {@link HighScore#addScore(int, String)}.
	 * add a score without a name and check that it isn't inserted.
	 */
	@Test
	public void testAddEmpty() {
		HighScore.addScore(10, "");
		assertEquals(0, HighScore.getAll().size());
	}
	
	/**
	 * Test for {@link HighScore#addScore(int, String)}.
	 */
	@Test
	public void testAddSize() {
		HighScore.addScore(10, "bananas");
		assertEquals(1, HighScore.getAll().size());
	}
	
	/**
	 * Test for {@link HighScore#addScore(int, String)}.
	 * Overwrite an existing score.
	 */
	@Test
	public void testAddSameSize() {
		HighScore.addScore(7, "bananas");
		HighScore.addScore(9, "bananas");
		assertEquals(1, HighScore.getAll().size());
	}
	
	/**
	 * Test for {@link HighScore#addScore(int, String)}.
	 * Check the inserted value.
	 */
	@Test
	public void testAddValue() {
		HighScore.addScore(12, "bananas");
		assertEquals(12, HighScore.getAll().get("bananas").intValue());
	}
	
	/**
	 * Test for {@link HighScore#addScore(int, String)}.
	 * Add a higher score.
	 */
	@Test
	public void testAddHigherValue() {
		HighScore.addScore(10, "bananas");
		HighScore.addScore(12, "bananas");
		assertEquals(12, HighScore.getAll().get("bananas").intValue());
	}
	
	/**
	 * Test for {@link HighScore#addScore(int, String)}.
	 * add a lower score.
	 */
	@Test
	public void testAddLowerValue() {
		HighScore.addScore(12, "bananas");
		HighScore.addScore(6, "bananas");
		assertEquals(12, HighScore.getAll().get("bananas").intValue());
	}
	
	/**
	 * Test for {@link HighScore#init()}.
	 * Test if the file is created.
	 */
	@Test
	public void testInit() {
		File file = new File("highScores.yml");
		file.delete();
		assertFalse(file.exists());
		HighScore.init();
		assertTrue(file.exists());
		HighScore.init();
		assertEquals(0, HighScore.getAll().size());
	}
	
	/**
	 * Test for {@link HighScore#init()}.
	 * Test exception handling.
	 * @throws IOException
	 * 		When something goes wrong.
	 */
	@Test
	public void testInitException() throws IOException {
		File file = mock(File.class);
		HighScore.setScoreFile(file);
		Log log = Log.getLogger();
		ConsoleHandler mock = mock(ConsoleHandler.class);
		log.removeAllHandlers();
		log.addHandler(mock);
		when(file.createNewFile()).thenThrow(new IOException("test"));
		HighScore.init();
		
		verify(mock).output(LogLevel.ERROR, "Error creating file null");
		HighScore.setScoreFile(new File("highScores.yml"));	//reset
	}
	
	/**
	 * Test for {@link HighScore#load()}.
	 * Test exception handling.
	 * @throws IOException
	 * 		When something goes wrong.
	 */
	@Test
	public void testLoadException() throws IOException {
		File file = mock(File.class);
		HighScore.setScoreFile(file);
		Log log = Log.getLogger();
		ConsoleHandler mock = mock(ConsoleHandler.class);
		log.removeAllHandlers();
		log.addHandler(mock);
		when(file.createNewFile()).thenThrow(new IOException("test"));
		HighScore.loadHighScores();
		
		verify(mock).output(LogLevel.ERROR, "Error loading file null");
		HighScore.setScoreFile(new File("highScores.yml"));	//reset
	}
	
	/**
	 * Test for {@link HighScore#load()}.
	 * Test the loading of scores.
	 * @throws IOException
	 * 		When something goes wrong.
	 */
	@Test
	public void testload() throws IOException {
		try (FileWriter fw = new FileWriter(new File("highScores.yml"))) {
			fw.write("test: 0 \ntest2: 11");
			fw.flush();
		}
		HighScore.loadHighScores();
		HashMap<String, Integer> map = HighScore.getAll();
		assertEquals(2, map.size());
		assertEquals(0, map.get("test").intValue());
		assertEquals(11, map.get("test2").intValue());
	}
	
	/**
	 * Test for {@link HighScore#save()}.
	 * Test saving of scores.
	 * @throws IOException
	 * 		When something goes wrong.
	 */
	@Test
	public void testsave() throws IOException {
		HighScore.addScore(12, "test");
		HighScore.addScore(23, "test2");
		HighScore.save();
		
		try (BufferedReader br = new BufferedReader(new FileReader(new File("highScores.yml")))) {
			assertEquals("test2: 23", br.readLine());
			assertEquals("test: 12", br.readLine());
			assertNull(br.readLine());
		}
	}
	
	/**
	 * Test for {@link HighScore#save()}.
	 * Test exception handling.
	 * @throws IOException
	 * 		When something goes wrong.
	 */
	@Test
	public void testWriteException() throws IOException {
		File file = new File("highScores.yml");
		file.setWritable(false);
		Log log = Log.getLogger();
		ConsoleHandler mock = mock(ConsoleHandler.class);
		log.removeAllHandlers();
		log.addHandler(mock);
		HighScore.save();
		
		verify(mock).output(LogLevel.ERROR, "Error writing file " + file.getAbsolutePath());
		file.setWritable(true);	//reset
	}
	

}
