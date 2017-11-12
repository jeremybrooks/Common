/*
 * Copyright (c) 2013, 2017, Jeremy Brooks
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.jeremybrooks.common.filter;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


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
