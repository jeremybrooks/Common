package net.jeremybrooks.common.util;

import org.apache.log4j.Logger;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * Utility class to help with I/O tasks.
 * <p/>
 * This class uses log4j to report any errors that occur.
 *
 * @author Jeremy Brooks
 */
public class IOUtil {

	/* Utility class. All methods are public static. */
	private static Logger logger = Logger.getLogger(IOUtil.class);


	public static void close(InputStream in) {
		try {
			if (in != null) {
				in.close();
			}
		} catch (Exception e) {
			logger.warn("ERROR CLOSING INPUT STREAM.", e);
		}
	}


	public static void close(OutputStream out) {
		try {
			if (out != null) {
				out.close();
			}
		} catch (Exception e) {
			logger.warn("ERROR CLOSING OUTPUT STREAM.", e);
		}
	}

	public static void close(Reader reader) {
		try {
			if (reader != null) {
				reader.close();
			}
		} catch (Exception e) {
			logger.warn("ERROR CLOSING READER.", e);
		}
	}

	public static void close(Writer writer) {
		try {
			if (writer != null) {
				writer.close();
			}
		} catch (Exception e) {
			logger.warn("ERROR CLOSING WRITER.", e);
		}
	}

	public static void flush(OutputStream out) {
		try {
			if (out != null) {
				out.flush();
			}
		} catch (Exception e) {
			logger.warn("ERROR FLUSHING OUTPUT STREAM.", e);
		}
	}

	public static void flush(Writer out) {
		try {
			if (out != null) {
				out.flush();
			}
		} catch (Exception e) {
			logger.warn("ERROR FLUSHING OUTPUT STREAM.", e);
		}
	}
}
