package net.jeremybrooks.common.util;

/**
 * @author Jeremy Brooks
 */
public class MacUtil {

	/**
	 * Creates a new instance of MacUtil
	 */
	private MacUtil() {
	}


	/**
	 * If running on a Mac, this method will set the
	 * system property which tells the menu bar to
	 * display at the top of the screen.
	 */
	public static void setMacMenuBar() {
		// SET SYSTEM PROPERTY FOR MAC-STYLE MENU BAR
		if (System.getProperty("mrj.version") != null) {
			System.setProperty("apple.laf.useScreenMenuBar", "true");
		}
	}
}
