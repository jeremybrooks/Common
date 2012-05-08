package net.jeremybrooks.common;

import net.jeremybrooks.common.util.IOUtil;
import net.jeremybrooks.common.util.StringUtil;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


/**
 * Provides a disk based cache for Serializable objects.
 * <p/>
 * <p>When an instance of ObjectCache is created, it will create a directory in
 * the system temp directory. All cached objects will be placed in this directory.
 * Optionally, you can specify the name of the directory,
 * or the exact directory to be used.</p>
 * <p/>
 * <p>All directories and files created by this class are marked as
 * deleteOnExit, so they should be cleaned up when the JVM exits.</p>
 * <p/>
 * <p>This class is not thread-safe. It is possible for two different threads to attempt to, for example,
 * retrieve and delete the same object, or attempt to delete the same object twice.</p>
 *
 * @author Jeremy Brooks
 * @author jeremyb@whirljack.net
 */
public class ObjectCache {

	/**
	 * Logging.
	 */
	private Logger logger = Logger.getLogger(ObjectCache.class);

	/**
	 * The directory that will contain the cached objects.
	 */
	private File cacheDir;


	/**
	 * Create a new instance of Object Cache using a random directory name
	 * in the system temp directory.
	 *
	 * @throws IOException if the directory cannot be created.
	 */
	public ObjectCache() throws IOException {
		this("ObjectCacheDir-" + System.currentTimeMillis() + System.getProperty("java.io.tmpdir"));
	}


	/**
	 * Create a new instance of ObjectCache.
	 * <p/>
	 * The cache directory will located in the system temp directory and named as specified.
	 *
	 * @param cacheDirName the directory to use.
	 * @throws IOException if the directory is null, or cannot be created.
	 */
	public ObjectCache(String cacheDirName) throws IOException {
		this(new File(System.getProperty("java.io.tmpdir"), cacheDirName));
	}


	/**
	 * Create a new instance of ObjectCache using the specified directory.
	 *
	 * @param cacheDir the directory to use.
	 * @throws IOException if the specified directory is null, is not a directory,
	 *                     or if the directory cannot be created.
	 */
	public ObjectCache(File cacheDir) throws IOException {
		if (cacheDir == null) {
			throw new IOException("Cannot use a null directory.");
		}
		if (cacheDir.exists()) {
			if (!cacheDir.isDirectory()) {
				throw new IOException(cacheDir.getAbsolutePath() + " is not a directory.");
			}
		} else {
			if (!cacheDir.mkdirs()) {
				throw new IOException("Could not create directory " + cacheDir.getAbsolutePath());
			}
		}

		this.cacheDir = cacheDir;
		this.cacheDir.deleteOnExit();

		if (logger.isDebugEnabled()) {
			logger.debug("Using cache directory " + this.cacheDir.getAbsolutePath());
		}
	}


	/**
	 * Put the object in cache, with the specified name.
	 *
	 * @param name the name which identifies the object in cache.
	 * @param ser  the object to cache.
	 * @throws IOException if there is an error writing the object to disk,
	 *                     or if the parameters are null.
	 */
	public void put(String name, Serializable ser) throws IOException {
		if (name == null || name.isEmpty()) {
			throw new IOException("Name cannot be null or empty.");
		}
		if (ser == null) {
			throw new IOException("Cannot cache a null object.");
		}

		ObjectOutputStream out = null;
		File cacheFile = new File(this.cacheDir, name);
		cacheFile.deleteOnExit();

		try {
			out = new ObjectOutputStream(new FileOutputStream(cacheFile));
			out.writeObject(ser);
			out.flush();

			if (logger.isDebugEnabled()) {
				logger.debug("Cached object '" + name + "' as " + cacheFile.getAbsolutePath());
			}
		} finally {
			IOUtil.close(out);
		}

	}


	/**
	 * Get the specified object from the cache.
	 *
	 * @param name identifies the object to be retrieved.
	 * @return the requested object, or null if the object is not in cache.
	 * @throws IOException if there is an error reading the object from disk,
	 *                     or if the name is null or empty.
	 */
	public Object get(String name) throws IOException {
		if (StringUtil.isNullOrEmpty(name)) {
			throw new IOException("Name cannot be null or empty.");
		}

		Object obj = null;
		ObjectInputStream in = null;
		File cacheFile = new File(this.cacheDir, name);

		if (cacheFile.exists()) {
			in = new ObjectInputStream(new FileInputStream(cacheFile));

			try {
				obj = in.readObject();
			} catch (ClassNotFoundException cnfe) {
				throw new IOException("Unable to read the object from cache.", cnfe);
			} finally {
				IOUtil.close(in);
			}
		}

		return obj;
	}


	/**
	 * Delete the specified object from the cache.
	 * <p/>
	 * If the object does not exist in the cache, nothing happens.
	 *
	 * @param name identifies the object to delete.
	 */
	public void delete(String name) {
		if (StringUtil.isNullOrEmpty(name)) {
			return;
		}
		File cacheFile = new File(this.cacheDir, name);

		if (cacheFile.exists()) {
			cacheFile.delete();
			if (logger.isDebugEnabled()) {
				logger.debug("Deleted " + cacheFile.getAbsolutePath());
			}
		}
	}


	/**
	 * Clear the cache directory.
	 * <p/>
	 * This will delete all the objects in the cache.
	 */
	public void clear() {
		File[] files = this.cacheDir.listFiles();
		for (File f : files) {
			f.delete();
		}
	}

}
