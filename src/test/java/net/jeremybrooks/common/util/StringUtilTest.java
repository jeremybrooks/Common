package net.jeremybrooks.common.util;

import net.jeremybrooks.common.util.StringUtil;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * @author Jeremy Brooks
 */
public class StringUtilTest {
	@Test
	public void testIsNullOrEmpty() throws Exception {
		assertTrue(StringUtil.isNullOrEmpty(null));
		assertTrue(StringUtil.isNullOrEmpty(""));
		assertTrue(StringUtil.isNullOrEmpty("    "));
		assertFalse(StringUtil.isNullOrEmpty("not empty"));
	}
}
