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
	 * <p/>
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
	 * <p/>
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
	 * <p/>
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
	 * <p/>
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
