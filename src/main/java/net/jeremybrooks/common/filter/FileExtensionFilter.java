/*
 * Copyright (c) 2013, Jeremy Brooks
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

import net.jeremybrooks.common.util.StringUtil;

import java.io.File;
import java.io.FilenameFilter;

/**
 * An implementation of {@link FilenameFilter} that matches file extensions.
 * <p/>
 * A file extension is defined as a dot followed by characters. When this class is created, a file extension must
 * be specified. If the specified extension does not start with a dot, the dot is added. If the specified extension
 * is empty, the accept method will always return false when called.
 *
 * @author Jeremy Brooks
 * @author jeremyb@whirljack.net
 */
public class FileExtensionFilter implements FilenameFilter {

	private String extension;

	/* Disallow default constructor. */
	private FileExtensionFilter() {
	}


	/**
	 * Create an instance of FilenameFilter specifying the extension to match.
	 * <p/>
	 * If the extension does not start with '.', the '.' will be added.
	 *
	 * @param extension extension to match.
	 */
	public FileExtensionFilter(String extension) {
		if (StringUtil.isNullOrEmpty(extension)) {
			this.extension = null;
		} else {
			if (extension.trim().startsWith(".")) {
				this.extension = extension;
			} else {
				this.extension = "." + extension;
			}
		}
	}


	/**
	 * Create an instance of FilenameFilter specifying a file whose extension to match.
	 * <p/>
	 * If the file is null, or the file does not end with '.' followed by some text, the filter will always return false.
	 *
	 * @param file the file whose extension should be used as the extension to match.
	 */
	public FileExtensionFilter(File file) {
		if (file == null) {
			this.extension = null;
		} else {
			int index = file.getName().lastIndexOf('.');
			if (index == -1) {
				this.extension = null;
			} else {
				this.extension = file.getName().substring(index);
			}
		}
	}


	/**
	 * Get the extension that will be matched.
	 *
	 * @return extension that will be matched.
	 */
	public String getExtension() {
		return this.extension;
	}


	/**
	 * Return true if the filename matches the extension specified when the class was created.
	 *
	 * @param dir  the directory in which the file was found.
	 * @param name the name of the file.
	 * @return true if the extension of the named file matches.
	 */
	@Override
	public boolean accept(File dir, String name) {
		boolean accept = false;

		if (this.extension != null && dir != null && name != null && name.endsWith(this.extension)) {
			accept = true;
		}

		return accept;
	}
}
