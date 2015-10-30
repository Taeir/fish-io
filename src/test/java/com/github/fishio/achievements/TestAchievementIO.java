package com.github.fishio.achievements;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
 * Test class for {@link AchievementIO}.
 *
 */
public class TestAchievementIO {

	private static final String ACHIEVEMENT_FILE = "testAchievements.yml";

	/**
	 * The achievement name that will be used by the tests.
	 */
	private static final String ACHIEVEMENT_NAME = "achievetest";

	/**
	 * Save the current achievement file, and set up the logger.
	 */
	@BeforeClass
	public static void setUpClass() {
		// Setup the logger for testing
		TestUtil.setUpLoggerForTesting(LogLevel.ERROR);

		// Use test achievements file
		AchievementIO.setAchievementFile(new File(ACHIEVEMENT_FILE));
	}

	/**
	 * Restore the old a file and the logger.
	 */
	@AfterClass
	public static void tearDownClass() {
		// Restore the logger
		TestUtil.restoreLogger();

		// Restore the normal file
		AchievementIO.setAchievementFile(new File("achievements.yml"));

		// Delete the test achievement file
		File file = new File(ACHIEVEMENT_FILE);
		file.delete();
	}

	/**
	 * Reset all achievements and reset the logger.
	 */
	@Before
	public void setUp() {
		// Reset the file and the scores
		AchievementIO.setAchievementFile(new File(ACHIEVEMENT_FILE));
		AchievementIO.getAll().clear();

		// Reset the logger
		TestUtil.resetMockHandler();
	}

	/**
	 * Test for {@link AchievementIO#addObserverCounter(String, int)}.
	 */
	@Test
	public void testAddSize() {
		AchievementIO.addObserverCounter(ACHIEVEMENT_NAME, 3);
		assertEquals(1, AchievementIO.getAll().size());
	}

	/**
	 * Test for {@link AchievementIO#addObserverCounter(String, int)}. Overwrite
	 * an existing score.
	 */
	@Test
	public void testAddSameSize() {
		AchievementIO.addObserverCounter(ACHIEVEMENT_NAME, 10);
		AchievementIO.addObserverCounter(ACHIEVEMENT_NAME, 65);
		assertEquals(1, AchievementIO.getAll().size());
	}

	/**
	 * Test for {@link AchievementIO#addObserverCounter(String, int)}. Check the
	 * inserted value.
	 */
	@Test
	public void testAddValue() {
		AchievementIO.addObserverCounter(ACHIEVEMENT_NAME, 65);
		assertEquals(65, AchievementIO.getAll().get(ACHIEVEMENT_NAME).intValue());
	}

	/**
	 * Test for {@link AchievementIO#addObserverCounter(String, int)}. Add a
	 * higher counter.
	 */
	@Test
	public void testAddHigherValue() {
		AchievementIO.addObserverCounter(ACHIEVEMENT_NAME, 60);
		AchievementIO.addObserverCounter(ACHIEVEMENT_NAME, 65);
		assertEquals(65, AchievementIO.getAll().get(ACHIEVEMENT_NAME).intValue());
	}

	/**
	 * Test for {@link AchievementIO#load()}. Test exception handling.
	 * 
	 * @throws IOException
	 *             When something goes wrong.
	 */
	@Test
	public void testLoadException() throws IOException {
		File file = mock(File.class);
		when(file.createNewFile()).thenThrow(new IOException("Fake IOException on file creation"));

		AchievementIO.setAchievementFile(file);

		AchievementIO.load();

		verify(TestUtil.getMockHandler()).output(LogLevel.ERROR, "Error loading file null");
	}

	/**
	 * Test for {@link AchievementIO#load()}. Test the loading of scores.
	 * 
	 * @throws IOException
	 *             When something goes wrong.
	 */
	@Test
	public void testload() throws IOException {
		try (FileWriter fw = new FileWriter(new File(ACHIEVEMENT_FILE))) {
			fw.write("test: 0 \ntest2: 11");
			fw.flush();
		}
		AchievementIO.load();
		HashMap<String, Integer> map = AchievementIO.getAll();
		assertEquals(2, map.size());
		assertEquals(0, map.get("test").intValue());
		assertEquals(11, map.get("test2").intValue());
	}

	/**
	 * Test for {@link AchievementIO#save()}. Test saving of scores.
	 * 
	 * @throws IOException
	 *             When something goes wrong.
	 */
	@Test
	public void testsave() throws IOException {
		AchievementIO.addObserverCounter("test", 12);
		AchievementIO.addObserverCounter("test2", 23);
		AchievementIO.save();

		try (BufferedReader br = new BufferedReader(new FileReader(new File(ACHIEVEMENT_FILE)))) {
			assertEquals("test2: 23", br.readLine());
			assertEquals("test: 12", br.readLine());
			assertNull(br.readLine());
		}
	}

	/**
	 * Test for {@link AchievementIO#save()}. Test exception handling.
	 * 
	 * @throws IOException
	 *             When something goes wrong.
	 */
	@Test
	public void testWriteException() throws IOException {
		// Create a completely invalid file
		File file = new File("\0\0/\\");
		AchievementIO.setAchievementFile(file);

		AchievementIO.save();

		verify(TestUtil.getMockHandler()).output(LogLevel.ERROR, "Error writing file " + file.getAbsolutePath());
	}

}

