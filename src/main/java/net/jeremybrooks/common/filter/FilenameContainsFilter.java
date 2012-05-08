package net.jeremybrooks.common.filter;

import net.jeremybrooks.common.util.StringUtil;

import java.io.File;
import java.io.FilenameFilter;

/**
 * An implementation of {@link FilenameFilter} that will match specified text anywhere in the filename.
 *
 * @author Jeremy Brooks
 */
public class FilenameContainsFilter implements FilenameFilter {

	private String matchText;
	private boolean caseSensitive;

	/* Disallow default constructor. */
	private FilenameContainsFilter() {
	}


	/**
	 * Create an instance of FilenameContainsFilter that will match the specified text.
	 * <p/>
	 * This is equivalent to calling <code>new FilenameContainsFilter(matchText, true)</code>
	 *
	 * @param matchText text to match.
	 */
	public FilenameContainsFilter(String matchText) {
		this(matchText, true);
	}


	/**
	 * Create an instance of FilenameContainsFilter that will match the specified text, specifying
	 * the case sensitivity.
	 *
	 * @param matchText     text to match.
	 * @param caseSensitive specifies if the match should be case sensitive.
	 */
	public FilenameContainsFilter(String matchText, boolean caseSensitive) {
		if (StringUtil.isNullOrEmpty(matchText)) {
			this.matchText = null;
		} else {
			this.matchText = matchText;
		}
		this.caseSensitive = caseSensitive;
	}


	/**
	 * Gets the text that will be matched.
	 *
	 * @return text to be matched.
	 */
	public String getMatchText() {
		return this.matchText;
	}


	/**
	 * Indicates if this filter is case sensitive.
	 *
	 * @return true if case sensitive, false otherwise.
	 */
	public boolean isCaseSensitive() {
		return this.caseSensitive;
	}


	/**
	 * Return true if the filename contains the text specified when this class was created.
	 *
	 * @param dir  the directory in which the file was found.
	 * @param name the name of the file.
	 * @return true if the filename contained the text to match.
	 */
	@Override
	public boolean accept(File dir, String name) {
		boolean accept = false;

		if (this.caseSensitive) {
			if (dir != null && name != null && name.contains(this.matchText)) {
				accept = true;
			}

		} else {
			if (dir != null && name != null && name.toLowerCase().contains((this.matchText.toLowerCase()))) {
				accept = true;
			}
		}

		return accept;
	}


}
