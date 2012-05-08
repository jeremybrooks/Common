package net.jeremybrooks.common;

import net.jeremybrooks.common.util.IOUtil;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * Implements a way to load and store application settings in a properties file.
 *
 * @author Jeremy Brooks
 * @author jeremyb@whirljack.net
 */
public class PropertyStore {

	private Logger logger = Logger.getLogger(PropertyStore.class);

	private Properties props;
	private File propsFile;


	/**
	 * Create a new instance of PropertyStore.
	 * <p/>
	 * <p>Properties will be saved in the user's home directory, in a file
	 * named default.properties. This is probably not what you want,
	 * so use the other constructors.</p>
	 *
	 * @throws Exception
	 */
	public PropertyStore() throws Exception {
		this(new File(System.getProperty("user.home")), null);
	}


	/**
	 * Create a new instance of PropertyStore.
	 * <p/>
	 * <p>Properties will be saved in the file specified in the constructor
	 * arguments. If the directory is null or empty, the user's home
	 * directory will be used. If the filename is null or empty, the filename
	 * will be "default.properties". If the directory does not exist,
	 * directory.mkdirs() will be called in an attempt to create it.</p>
	 *
	 * @param directory the directory the properties file will be stored in.
	 * @param filename  the name of the properties file.
	 * @throws Exception if the directory does not exist and cannot be created,
	 *                   or if there is an error creating the properties file.
	 */
	public PropertyStore(String directory, String filename) throws Exception {
		this(new File(directory), filename);
	}


	/**
	 * Create a new instance of PropertyStore.
	 * <p/>
	 * <p>Properties will be saved in the file specified in the constructor
	 * arguments. If the directory is null, the user's home
	 * directory will be used. If the filename is null or empty, the filename
	 * will be "default.properties". If the directory does not exist,
	 * directory.mkdirs() will be called in an attempt to create it.</p>
	 *
	 * @param directory the directory the properties file will be stored in.
	 * @param filename  the name of the properties file.
	 * @throws Exception if the directory does not exist and cannot be created,
	 *                   or if there is an error creating the properties file.
	 */
	public PropertyStore(File directory, String filename) throws Exception {
		if (directory == null) {
			directory = new File(System.getProperty("user.home"));
		}
		if (filename == null || filename.isEmpty()) {
			filename = "default.properties";
		}

		if (!directory.exists()) {
			if (!directory.mkdirs()) {
				throw new Exception("Unable to create directory " + directory.getAbsolutePath());
			}
		}
		this.props = new Properties();
		this.propsFile = new File(directory, filename);

		if (!propsFile.exists()) {
			if (!propsFile.createNewFile()) {
				throw new Exception("Unable to create properties file " + propsFile.getAbsolutePath());
			}
		}
		FileInputStream in = null;
		try {
			in = new FileInputStream(this.propsFile);
			this.props.load(in);
		} catch (Exception e) {
			throw e;
		} finally {
			IOUtil.close(in);
		}
	}


	/**
	 * Get the property for the specified key.
	 *
	 * @param key the name of the key to look up the value for.
	 * @return value corresponding to the key, or null if the key does not exist.
	 */
	public String getProperty(String key) {
		if (key == null || key.isEmpty()) {
			return null;
		}

		return props.getProperty(key);
	}


	/**
	 * Get the value for the key as an int.
	 * <p/>
	 * <p>If the key does not exist, 0 is returned. If you need to know if the
	 * key is missing, use getProperty(key) and check for null.</p>
	 *
	 * @param key the name of the key to look up the value for.
	 * @return value for the key as an int.
	 */
	public int getPropertyAsInt(String key) {
		int retVal = 0;

		try {
			retVal = Integer.parseInt(getProperty(key));
		} catch (Exception e) {
			// invalid, so will return zero
		}

		return retVal;
	}


	/**
	 * Get the value for the key as a long.
	 * <p/>
	 * <p>If the key does not exist, 0 is returned. If you need to know if the
	 * key is missing, use getProperty(key) and check for null.</p>
	 *
	 * @param key the name of the key to look up the value for.
	 * @return value for the key as a long.
	 */
	public long getPropertyAsLong(String key) {
		long retVal = 0;

		try {
			retVal = Long.parseLong(getProperty(key));
		} catch (Exception e) {
			// invalid, so will return zero
		}

		return retVal;
	}


	/**
	 * Get the value for the key as a float.
	 * <p/>
	 * <p>If the key does not exist, 0.0 is returned. If you need to know if the
	 * key is missing, use getProperty(key) and check for null.</p>
	 *
	 * @param key the name of the key to look up the value for.
	 * @return value for the key as a float.
	 */
	public float getPropertyAsFloat(String key) {
		float retVal = 0.0f;

		try {
			retVal = Float.parseFloat(getProperty(key));
		} catch (Exception e) {
			// invalid, so will return zero
		}

		return retVal;
	}


	/**
	 * Get the value for the key as a double.
	 * <p/>
	 * <p>If the key does not exist, 0.0 is returned. If you need to know if the
	 * key is missing, use getProperty(key) and check for null.</p>
	 *
	 * @param key the name of the key to look up the value for.
	 * @return value for the key as a double.
	 */
	public double getPropertyAsDouble(String key) {
		double retVal = 0.0;

		try {
			retVal = Double.parseDouble(getProperty(key));
		} catch (Exception e) {
			// invalid, so will return zero
		}

		return retVal;
	}


	/**
	 * Get the value for the key as a boolean.
	 * <p/>
	 * <p>This method will return true if the key value is any of "true", "yes", "y",
	 * or "1". The match is not case sensitive. If the key does not exist, false
	 * is returned. If you need to know if the key is missing, use
	 * getProperty(key) and check for null.</p>
	 *
	 * @param key the name of the key to look up the value for.
	 * @return value for the key as a boolean.
	 */
	public boolean getPropertyAsBoolean(String key) {
		boolean retVal = false;

		try {
			String value = getProperty(key);
			if (value != null) {
				if (value.equalsIgnoreCase("true") ||
						value.equalsIgnoreCase("y") ||
						value.equalsIgnoreCase("yes") ||
						value.equalsIgnoreCase("1")) {
					retVal = true;
				}
			}

		} catch (Exception e) {
			// invalid, will return false
		}

		return retVal;
	}


	/**
	 * Set the value of the specified key.
	 * <p/>
	 * <p>This method will set the key/value pair in the Properties object
	 * that is backing this PropertyStore, and write the Properties object
	 * out to disk. If your application needs to know for sure if the save was
	 * successful, check the return value.</p>
	 *
	 * @param key   the key to be placed in the properties object.
	 * @param value the value to associate with the key.
	 * @return true if the properties object is successfully stored, false if
	 *         there are errors.
	 */
	public boolean setProperty(String key, String value) {
		boolean success = false;

		if (key == null || key.isEmpty()) {
			logger.error("Cannot store a null or empty key.");
			return success;
		}
		if (value == null) {
			value = "";
		}

		this.props.setProperty(key, value);

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(this.propsFile);
			this.props.store(out, "Saved by " + this.getClass().getName());
			success = true;
		} catch (Exception e) {
			logger.error("Unable to store properties in file " + this.propsFile.getAbsolutePath(), e);
		} finally {
			IOUtil.close(out);
		}

		return success;
	}


	/**
	 * Set the value of the specified key.
	 * <p/>
	 * <p>This is equivalent to calling setProperty(key, Integer.toString(value));</p>
	 *
	 * @param key   the key to be placed in the properties object.
	 * @param value the value to associate with the key.
	 * @return true if the properties object is successfully stored, false if
	 *         there are errors.
	 */
	public boolean setProperty(String key, int value) {
		return setProperty(key, Integer.toString(value));
	}


	/**
	 * Set the value of the specified key.
	 * <p/>
	 * <p>This is equivalent to calling setProperty(key, Long.toString(value));</p>
	 *
	 * @param key   the key to be placed in the properties object.
	 * @param value the value to associate with the key.
	 * @return true if the properties object is successfully stored, false if
	 *         there are errors.
	 */
	public boolean setProperty(String key, long value) {
		return setProperty(key, Long.toString(value));
	}


	/**
	 * Set the value of the specified key.
	 * <p/>
	 * <p>This is equivalent to calling setProperty(key, Double.toString(value));</p>
	 *
	 * @param key   the key to be placed in the properties object.
	 * @param value the value to associate with the key.
	 * @return true if the properties object is successfully stored, false if
	 *         there are errors.
	 */
	public boolean setProperty(String key, double value) {
		return setProperty(key, Double.toString(value));
	}


	/**
	 * Set the value of the specified key.
	 * <p/>
	 * <p>This is equivalent to calling setProperty(key, Float.toString(value));</p>
	 *
	 * @param key   the key to be placed in the properties object.
	 * @param value the value to associate with the key.
	 * @return true if the properties object is successfully stored, false if
	 *         there are errors.
	 */
	public boolean setProperty(String key, float value) {
		return setProperty(key, Float.toString(value));
	}


	/**
	 * Set the value of the specified key.
	 * <p/>
	 * <p>This is equivalent to calling setProperty(key, Boolean.toString(value));</p>
	 *
	 * @param key   the key to be placed in the properties object.
	 * @param value the value to associate with the key.
	 * @return true if the properties object is successfully stored, false if
	 *         there are errors.
	 */
	public boolean setProperty(String key, boolean value) {
		return setProperty(key, Boolean.toString(value));
	}
}