/*
 * Copyright (c) 2013-2021, Jeremy Brooks
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

import java.net.Authenticator;
import java.net.PasswordAuthentication;


/**
 * This class contains various methods to help with Network usage.
 *
 * @author Jeremy Brooks
 */
public class NetUtil {


	/* Utility class. All methods are public static. */
	private NetUtil() {
	}


	/**
	 * Set up the system properties for HTTP proxy.
   *
	 * <p>This will set up the system properties for HTTP access via a proxy.
	 * This will not perform any authentication.</p>
	 *
	 * @param host the proxy host to use.
	 * @param port the proxy port to use.
	 * @throws NumberFormatException if the port is not a valid integer.
	 */
	public static void enableProxy(String host, String port) throws NumberFormatException {
		NetUtil.enableProxy(host, Integer.parseInt(port));
	}


	/**
	 * Set up the system properties for HTTP proxy.
	 *
	 * <p>This will set up the system properties for HTTP access via a proxy.
	 * This will not perform any authentication.</p>
	 *
	 * @param host the proxy host to use.
	 * @param port the proxy port to use.
	 */
	public static void enableProxy(String host, int port) {
		NetUtil.enableProxy(host, port, null, null);
	}


	/**
	 * Set up the system properties for HTTP proxy.
	 *
	 * <p>This will set up the system properties for HTTP access via a proxy.
	 * The specified username and password will be used for proxy
	 * authentication.</p>
	 *
	 * @param host     the proxy host to use.
	 * @param port     the proxy port to use.
	 * @param username the username for proxy authentication.
	 * @param password the password for proxy authentication.
	 * @throws NumberFormatException if the port is not a valid integer.
	 */
	public static void enableProxy(String host, String port, String username, char[] password)
			throws NumberFormatException {
		NetUtil.enableProxy(host, Integer.parseInt(port), username, password);
	}


	/**
	 * Set up the system properties for HTTP proxy.
   *
	 * <p>This will set up the system properties for HTTP access via a proxy.
	 * The specified username and password will be used for proxy
	 * authentication.</p>
	 *
	 * @param host     the proxy host to use.
	 * @param port     the proxy port to use.
	 * @param username the username for proxy authentication.
	 * @param password the password for proxy authentication.
	 */
	public static void enableProxy(String host, int port, final String username, final char[] password) {
		System.setProperty("http.proxyHost", host);
		System.setProperty("http.proxyPort", String.valueOf(port));

		if (username != null && !username.isEmpty()) {
			Authenticator.setDefault(new Authenticator() {

				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}

			});
		}
	}


	/**
	 * This will clear the system properties for HTTP proxy.
	 */
	public static void clearProxy() {
		System.clearProperty("http.proxyHost");
		System.clearProperty("http.proxyPort");
	}
}
