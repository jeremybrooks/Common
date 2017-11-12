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
