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

package net.jeremybrooks.common;

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
	private final Properties props;
	private final File propsFile;


	/**
	 * Create a new instance of PropertyStore.
   *
	 * <p>Properties will be saved in the user's home directory, in a file
	 * named default.properties. This is probably not what you want,
	 * so use the other constructors.</p>
	 *
	 * @throws Exception if there are any errors creating the file.
	 */
	public PropertyStore() throws Exception {
		this(new File(System.getProperty("user.home")), null);
	}


	/**
	 * Create a new instance of PropertyStore.
   *
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
   *
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
		try (FileInputStream in = new FileInputStream(this.propsFile)) {
			this.props.load(in);
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
   *
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
   *
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
   *
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
   *
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
	 *
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
	 *
	 * <p>This method will set the key/value pair in the Properties object
	 * that is backing this PropertyStore, and write the Properties object
	 * out to disk. If your application needs to know for sure if the save was
	 * successful, check the return value.</p>
   *
   * <p>If there is an error saving the properties file, the stack trace will be printed
   * to system out.</p>
	 *
	 * @param key   the key to be placed in the properties object.
	 * @param value the value to associate with the key.
	 * @return true if the properties object is successfully stored, false if
	 *         there are errors.
	 */
	public boolean setProperty(String key, String value) {
		boolean success = false;

		if (key == null || key.isEmpty()) {
			return success;
		}
		if (value == null) {
			value = "";
		}

		this.props.setProperty(key, value);

		try (FileOutputStream out = new FileOutputStream(this.propsFile)) {
			this.props.store(out, "Saved by " + this.getClass().getName());
			success = true;
		} catch (Exception e) {
		  e.printStackTrace();
		}

		return success;
	}


	/**
	 * Set the value of the specified key.
	 *
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
	 *
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
	 *
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
	 *
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
	 *
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
