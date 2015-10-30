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

import com.github.fishio.logging.LogLevel;
import com.github.fishio.test.util.TestUtil;

/**
 * Test class for {@link HighScore}.
 */
public class TestHighScore {
	private static final String HIGH_SCORE_FILE = "testHighScores.yml";
	/**
	 * The player name that will be used by the tests.
	 */
	private static final String PLAYER_NAME = "bananas";

	/**
	 * Save the current highscores, and set up the logger.
	 */
	@BeforeClass
	public static void setUpClass() {
		//Setup the logger for testing
		TestUtil.setUpLoggerForTesting(LogLevel.ERROR);
		
		//Use test highscores file
		HighScore.setScoreFile(new File(HIGH_SCORE_FILE));
	}
	
	/**
	 * Restore the old highscore file and the logger.
	 */
	@AfterClass
	public static void tearDownClass() {
		//Restore the logger
		TestUtil.restoreLogger();
		
		//Restore the normal file
		HighScore.setScoreFile(new File("highScores.yml"));
		
		//Delete the test highscores file
		File file = new File(HIGH_SCORE_FILE);
		file.delete();
	}
	
	/**
	 * Reset all high scores and reset the logger.
	 */
	@Before
	public void setUp() {
		//Reset the file and the scores
		HighScore.setScoreFile(new File(HIGH_SCORE_FILE));
		HighScore.getAll().clear();
		
		//Reset the logger
		TestUtil.resetMockHandler();
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
		HighScore.addScore(10, PLAYER_NAME);
		assertEquals(1, HighScore.getAll().size());
	}
	
	/**
	 * Test for {@link HighScore#addScore(int, String)}.
	 * Overwrite an existing score.
	 */
	@Test
	public void testAddSameSize() {
		HighScore.addScore(7, PLAYER_NAME);
		HighScore.addScore(9, PLAYER_NAME);
		assertEquals(1, HighScore.getAll().size());
	}
	
	/**
	 * Test for {@link HighScore#addScore(int, String)}.
	 * Check the inserted value.
	 */
	@Test
	public void testAddValue() {
		HighScore.addScore(12, PLAYER_NAME);
		assertEquals(12, HighScore.getAll().get(PLAYER_NAME).intValue());
	}
	
	/**
	 * Test for {@link HighScore#addScore(int, String)}.
	 * Add a higher score.
	 */
	@Test
	public void testAddHigherValue() {
		HighScore.addScore(10, PLAYER_NAME);
		HighScore.addScore(12, PLAYER_NAME);
		assertEquals(12, HighScore.getAll().get(PLAYER_NAME).intValue());
	}
	
	/**
	 * Test for {@link HighScore#addScore(int, String)}.
	 * add a lower score.
	 */
	@Test
	public void testAddLowerValue() {
		HighScore.addScore(12, PLAYER_NAME);
		HighScore.addScore(6, PLAYER_NAME);
		assertEquals(12, HighScore.getAll().get(PLAYER_NAME).intValue());
	}
	
	/**
	 * Test for {@link HighScore#init()}.
	 * Test if the file is created.
	 */
	@Test
	public void testInit() {
		File file = new File(HIGH_SCORE_FILE);
		file.delete();
		assertFalse(file.exists());
		
		//Initialize the highscores
		HighScore.init();
		
		//It should have created the file and the highscores should be empty.
		assertTrue(file.exists());
		assertEquals(0, HighScore.getAll().size());
	}
	
	/**
	 * Test for {@link HighScore#init()}.
	 * Test exception handling.
	 * 
	 * @throws IOException
	 * 		When something goes wrong.
	 */
	@Test
	public void testInitException() throws IOException {
		File file = mock(File.class);
		when(file.createNewFile()).thenThrow(new IOException("Fake IOException on file creation"));
		
		HighScore.setScoreFile(file);

		HighScore.init();
		
		verify(TestUtil.getMockHandler()).output(LogLevel.ERROR, "Error creating file null");
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
		when(file.createNewFile()).thenThrow(new IOException("Fake IOException on file creation"));
		
		HighScore.setScoreFile(file);

		HighScore.loadHighScores();
		
		verify(TestUtil.getMockHandler()).output(LogLevel.ERROR, "Error loading file null");
	}
	
	/**
	 * Test for {@link HighScore#load()}.
	 * Test the loading of scores.
	 * @throws IOException
	 * 		When something goes wrong.
	 */
	@Test
	public void testload() throws IOException {
		try (FileWriter fw = new FileWriter(new File(HIGH_SCORE_FILE))) {
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
		
		try (BufferedReader br = new BufferedReader(new FileReader(new File(HIGH_SCORE_FILE)))) {
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
		//Create a completely invalid file
		File file = new File("\0\0/\\");
		HighScore.setScoreFile(file);
		
		HighScore.save();
		
		verify(TestUtil.getMockHandler()).output(LogLevel.ERROR, "Error writing file " + file.getAbsolutePath());
	}
	

}
