package net.jeremybrooks.common.filter;

import java.io.File;
import java.io.FileFilter;

/**
 * An instance of java.io.FileFilter that only allows directories.
 *
 * @author Jeremy Brooks
 */
public class DirectoryFilter implements FileFilter {

	/**
	 * Filters for directories.
	 *
	 * @param pathname pathname to check.
	 * @return true if the pathname is a directory, false otherwise.
	 */
	@Override
	public boolean accept(File pathname) {
		return (pathname != null && pathname.isDirectory());
	}
}
