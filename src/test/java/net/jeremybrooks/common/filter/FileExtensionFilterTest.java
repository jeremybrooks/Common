package net.jeremybrooks.common.filter;

import org.junit.Test;

import java.io.File;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * @author Jeremy Brooks
 */
public class FileExtensionFilterTest {
	@Test
	public void testGetExtension() throws Exception {
		FileExtensionFilter fileExtensionFilter = new FileExtensionFilter(".tmp");
		assertEquals(".tmp", fileExtensionFilter.getExtension());
	}

	@Test
	public void testAccept() throws Exception {
		FileExtensionFilter fileExtensionFilter = new FileExtensionFilter(".tmp");
		File tempFile = File.createTempFile("prefix", ".tmp");
		assertTrue(fileExtensionFilter.accept(tempFile.getCanonicalFile(), tempFile.getName()));

		fileExtensionFilter = new FileExtensionFilter(tempFile);
		assertEquals(".tmp", fileExtensionFilter.getExtension());

		fileExtensionFilter = new FileExtensionFilter("tmp");
		assertTrue(fileExtensionFilter.accept(tempFile.getCanonicalFile(), tempFile.getName()));
	}

	@Test
	public void testEmptyString() throws Exception {
		FileExtensionFilter fileExtensionFilter = new FileExtensionFilter("");
		assertFalse(fileExtensionFilter.accept(new File(System.getProperty("user.home")), "someFilename"));
	}
}
