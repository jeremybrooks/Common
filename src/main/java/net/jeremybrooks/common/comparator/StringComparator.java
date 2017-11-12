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

package net.jeremybrooks.common.comparator;

import java.util.Comparator;

/**
 * Comparator to compare two Strings.
 *
 * By default, this class is not case sensitive. To get a case-sensitive {@code StringComparator},
 * use the constructor which accepts a boolean.
 */
public class StringComparator implements Comparator<String> {

	private boolean caseSensitive;

	/**
	 * Create a case-insensitive StringComparator.
	 */
	public StringComparator() {
		this(false);
	}

	/**
	 * Create a StringComparator, specifying the case-sensitivity.
	 *
	 * @param caseSensitive indicates if this instance of StringComparator should be case-sensitive.
	 */
	public StringComparator(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	 @Override
	 public int compare(String o1, String o2) {
		 if (this.caseSensitive) {
			 return o1.compareTo(o2);
		 } else {
			 return o1.compareToIgnoreCase(o2);
		 }
	 }
 }