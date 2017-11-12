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

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

/**
 * @author Jeremy Brooks
 */
public class FileUtil {

	/* Utility class. All methods are public static. */
	private FileUtil() {
	}


	public static void getFilesRecursive(File directory, List<File> list, boolean includeHiddenFiles) {
		FileUtil.getFilesRecursive(directory.listFiles(), list, includeHiddenFiles);
	}

	public static void getFilesRecursive(File directory, List<File> list) {
		FileUtil.getFilesRecursive(directory.listFiles(), list, false);
	}

	public static void getFilesRecursive(List<File> sourceList, List<File> destinationList, boolean includeHiddenFiles) {
		FileUtil.getFilesRecursive(sourceList.toArray(new File[sourceList.size()]), destinationList, includeHiddenFiles);
	}

	public static void getFilesRecursive(List<File> sourceList, List<File> destinationList) {
		FileUtil.getFilesRecursive(sourceList.toArray(new File[sourceList.size()]), destinationList, false);
	}


	public static void getFilesRecursive(File[] files, List<File> list) {
		FileUtil.getFilesRecursive(files, list, false);
	}


	/**
	 * Look for all files in all directories.
	 * <p>
	 * All File objects in the file array will be examined. If the object is
	 * a normal file and is not hidden it is added to the list. If the object is
	 * a normal file and is hidden, and the includeHiddenFiles parameter is true,
	 * it is added to the list. If the object is a directory, all files and
	 * directories in the directory will be examined as well.</p>
	 *
	 * @param files              the array of file objects to examine.
	 * @param list               the list which will hold all files that are found.
	 * @param includeHiddenFiles if true, hidden files will be included in the list.
	 */
	public static void getFilesRecursive(File[] files, List<File> list, boolean includeHiddenFiles) {
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					FileUtil.getFilesRecursive(file, list, includeHiddenFiles);
				} else if (file.isFile()) {
					if ((!file.isHidden()) || (file.isHidden() && includeHiddenFiles)) {
						list.add(file);
					}
				}
			}
		}
	}


	/**
	 * Copy a file.
	 *
	 * @param source the source file.
	 * @param dest   the destination file.
	 * @throws java.io.IOException if any errors occur during the copy.
	 */
	public static void copy(File source, File dest) throws IOException {
		Files.copy(source.toPath(), dest.toPath());
	}


	/**
	 * Recursively delete a local directory.
	 *
	 * @param directory the directory to delete.
	 * @throws Exception if directory is not a directory, or if there are any errors.
	 */
	public static void deleteLocalDirectory(File directory) throws Exception {
		if (!directory.isDirectory()) {
			throw new Exception("File " + directory.getAbsolutePath() + " is not a directory.");
		}
		Files.walkFileTree(directory.toPath(), new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
					throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException e)
					throws IOException {
				if (e == null) {
					Files.delete(dir);
					return FileVisitResult.CONTINUE;
				} else {
					// directory iteration failed
					throw e;
				}
			}
		});
	}


	/**
	 * Delete a local file or directory.
	 * Directories will be deleted recursively.
	 *
	 * @param file file or directory to delete.
	 * @throws Exception if there are any errors.
	 */
	public static void deleteLocalFileOrDirectory(File file) throws Exception {
		if (file.isFile()) {
			Files.delete(file.toPath());
		} else if (file.isDirectory()) {
			deleteLocalDirectory(file);
		}
	}
}
