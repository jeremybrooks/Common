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

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;

/**
 * Utility class to help with I/O tasks.
 *
 * <p>This class uses log4j to report any errors that occur.</p>
 *
 * @author Jeremy Brooks
 */
public class IOUtil {

	public static void close(InputStream in) {
		try {
			if (in != null) {
				in.close();
			}
		} catch (Exception e) {
		  e.printStackTrace();
		}
	}


	public static void close(OutputStream out) {
		try {
			if (out != null) {
				out.close();
			}
		} catch (Exception e) {
		  e.printStackTrace();
		}
	}

	public static void close(Reader reader) {
		try {
			if (reader != null) {
				reader.close();
			}
		} catch (Exception e) {
		  e.printStackTrace();
		}
	}

	public static void close(Writer writer) {
		try {
			if (writer != null) {
				writer.close();
			}
		} catch (Exception e) {
		  e.printStackTrace();
		}
	}

	public static void flush(OutputStream out) {
		try {
			if (out != null) {
				out.flush();
			}
		} catch (Exception e) {
		  e.printStackTrace();
		}
	}

	public static void flush(Writer out) {
		try {
			if (out != null) {
				out.flush();
			}
		} catch (Exception e) {
		  e.printStackTrace();
		}
	}

	public static void close(Socket s) {
		try {
			if (s != null) {
				s.close();
			}
		} catch (Exception e) {
		  e.printStackTrace();
		}
	}
}
