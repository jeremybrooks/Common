package net.jeremybrooks.common.util;

import java.io.File;
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
		FileUtil.getFilesRecursive(sourceList.toArray(new File[0]), destinationList, includeHiddenFiles);
	}

	public static void getFilesRecursive(List<File> sourceList, List<File> destinationList) {
		FileUtil.getFilesRecursive(sourceList.toArray(new File[0]), destinationList, false);
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
}
