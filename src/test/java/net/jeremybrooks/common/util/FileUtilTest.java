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

package net.jeremybrooks.common.util;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * @author Jeremy Brooks
 */
public class FileUtilTest {

	@Test
	public void testCopy() throws Exception {
		File tempDir = new File(System.getProperty("java.io.tmpdir"));
		File testDir = new File(tempDir, "testdir");
		File sourceFile = new File(testDir, "source");
		File destFile = new File(testDir, "dest");

		assertTrue(testDir.mkdirs());

		Random random = new Random(System.currentTimeMillis());
		FileOutputStream out = new FileOutputStream(sourceFile);
		// create source
		for (int i = 0; i < 10000; i++) {
			out.write(random.nextInt());
		}
		IOUtil.flush(out);
		IOUtil.close(out);

		FileUtil.copy(sourceFile, destFile);

		// compare
		assertEquals(sourceFile.length(), destFile.length());
		FileInputStream source = new FileInputStream(sourceFile);
		FileInputStream dest = new FileInputStream(destFile);
		int a;
		int b;
		while ((a = source.read()) != -1) {
			b = dest.read();
			assertEquals(a, b);
		}
		IOUtil.close(dest);
		IOUtil.close(source);

		FileUtil.deleteLocalDirectory(testDir);
	}


	@Test
	public void testDeleteLocalFileOrDirectory() throws Exception {
		File tempFile = new File(System.getProperty("java.io.tmpdir"), "testdeleteme");

		Random random = new Random(System.currentTimeMillis());
		FileOutputStream out = new FileOutputStream(tempFile);
		for (int i = 0; i < 10000; i++) {
			out.write(random.nextInt());
		}
		IOUtil.flush(out);
		IOUtil.close(out);

		assertTrue(tempFile.exists());
		assertEquals(10000, tempFile.length());

		FileUtil.deleteLocalFileOrDirectory(tempFile);
		assertFalse(tempFile.exists());

		File tempDir = new File(System.getProperty("java.io.tmpdir"), "testdeletedir");
		assertTrue(tempDir.mkdirs());
		FileUtil.deleteLocalFileOrDirectory(tempDir);
		assertFalse(tempDir.exists());
	}
}
