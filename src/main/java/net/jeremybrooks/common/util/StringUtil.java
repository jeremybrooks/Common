package net.jeremybrooks.common.util;

/**
 * Utility methods to deal with strings.
 * <p>The methods in this class are all public static.
 *
 * @author Jeremy Brooks
 */
public class StringUtil {


	/* Utility class. All methods are public static. */
	private StringUtil() {
	}


	/**
	 * Check to see if a <code>String</code> is empty.
	 * <p>A string is considered empty if it is null or if it's length after
	 * a <code>trim()</code> is equal to 0.
	 *
	 * @param string the string to check.
	 * @return true if the string is null or empty, false otherwise.
	 */
	public static boolean isNullOrEmpty(String string) {
		return (string == null || string.trim().length() == 0);
	}
}
