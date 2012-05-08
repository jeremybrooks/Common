package net.jeremybrooks.common.filter;

import org.junit.Test;

import java.io.File;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * @author Jeremy Brooks
 */
public class FilenameContainsFilterTest {
	@Test
	public void testGetMatchText() throws Exception {
		FilenameContainsFilter filenameContainsFilter = new FilenameContainsFilter("test");
		assertEquals("test", filenameContainsFilter.getMatchText());
	}

	@Test
	public void testIsCaseSensitive() throws Exception {
		FilenameContainsFilter filenameContainsFilter = new FilenameContainsFilter("test");

		assertTrue(filenameContainsFilter.isCaseSensitive());

		filenameContainsFilter = new FilenameContainsFilter("test", false);
		assertFalse(filenameContainsFilter.isCaseSensitive());

		filenameContainsFilter = new FilenameContainsFilter("test", true);
		assertTrue(filenameContainsFilter.isCaseSensitive());
	}


	@Test
	public void testAccept() throws Exception {
		// case sensitive filter
		FilenameContainsFilter filenameContainsFilter = new FilenameContainsFilter("test");
		File testFile = File.createTempFile("ThisIsATest", ".tmp");
		assertFalse(filenameContainsFilter.accept(testFile.getCanonicalFile(), testFile.getName()));

		testFile = File.createTempFile("thisisatestfile", ".tmp");
		assertTrue(filenameContainsFilter.accept(testFile.getCanonicalFile(), testFile.getName()));

		// non case sensitive filter
		filenameContainsFilter = new FilenameContainsFilter("test", false);
		testFile = File.createTempFile("ThisIsATest", ".tmp");

		assertTrue(filenameContainsFilter.accept(testFile.getCanonicalFile(), testFile.getName()));

		testFile = File.createTempFile("thisisatestfile", ".tmp");
		assertTrue(filenameContainsFilter.accept(testFile.getCanonicalFile(), testFile.getName()));

	}
}
