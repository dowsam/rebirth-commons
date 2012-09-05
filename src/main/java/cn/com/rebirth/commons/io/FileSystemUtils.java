/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons FileSystemUtils.java 2012-7-6 10:23:46 l.xue.nong$$
 */


package cn.com.rebirth.commons.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.rebirth.commons.unit.TimeValue;



/**
 * The Class FileSystemUtils.
 *
 * @author l.xue.nong
 */
public class FileSystemUtils {

	
	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(FileSystemUtils.class.getName());

	
	/** The Constant mkdirsStallTimeout. */
	private static final long mkdirsStallTimeout = TimeValue.timeValueMinutes(5).millis();

	
	/** The Constant mkdirsMutex. */
	private static final Object mkdirsMutex = new Object();

	
	/** The mkdirs thread. */
	private static volatile Thread mkdirsThread;

	
	/** The mkdirs start time. */
	private static volatile long mkdirsStartTime;

	
	/**
	 * Mkdirs.
	 *
	 * @param dir the dir
	 * @return true, if successful
	 */
	public static boolean mkdirs(File dir) {
		synchronized (mkdirsMutex) {
			try {
				mkdirsThread = Thread.currentThread();
				mkdirsStartTime = System.currentTimeMillis();
				return dir.mkdirs();
			} finally {
				mkdirsThread = null;
			}
		}
	}

	
	/**
	 * Check mkdirs stall.
	 *
	 * @param currentTime the current time
	 */
	public static void checkMkdirsStall(long currentTime) {
		Thread mkdirsThread1 = mkdirsThread;
		long stallTime = currentTime - mkdirsStartTime;
		if (mkdirsThread1 != null && (stallTime > mkdirsStallTimeout)) {
			logger.error("mkdirs stalled for {} on {}, trying to interrupt", new TimeValue(stallTime),
					mkdirsThread1.getName());
			mkdirsThread1.interrupt(); 
		}
	}

	
	/**
	 * Max open files.
	 *
	 * @param testDir the test dir
	 * @return the int
	 */
	public static int maxOpenFiles(File testDir) {
		boolean dirCreated = false;
		if (!testDir.exists()) {
			dirCreated = true;
			testDir.mkdirs();
		}
		List<RandomAccessFile> files = new ArrayList<RandomAccessFile>();
		try {
			while (true) {
				files.add(new RandomAccessFile(new File(testDir, "tmp" + files.size()), "rw"));
			}
		} catch (IOException ioe) {
			int i = 0;
			for (RandomAccessFile raf : files) {
				try {
					raf.close();
				} catch (IOException e) {
					
				}
				new File(testDir, "tmp" + i++).delete();
			}
			if (dirCreated) {
				deleteRecursively(testDir);
			}
		}
		return files.size();
	}

	
	/**
	 * Checks for extensions.
	 *
	 * @param root the root
	 * @param extensions the extensions
	 * @return true, if successful
	 */
	public static boolean hasExtensions(File root, String... extensions) {
		if (root != null && root.exists()) {
			if (root.isDirectory()) {
				File[] children = root.listFiles();
				if (children != null) {
					for (File child : children) {
						if (child.isDirectory()) {
							boolean has = hasExtensions(child, extensions);
							if (has) {
								return true;
							}
						} else {
							for (String extension : extensions) {
								if (child.getName().endsWith(extension)) {
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	
	/**
	 * Delete recursively.
	 *
	 * @param roots the roots
	 * @return true, if successful
	 */
	public static boolean deleteRecursively(File[] roots) {
		boolean deleted = true;
		for (File root : roots) {
			deleted &= deleteRecursively(root);
		}
		return deleted;
	}

	
	/**
	 * Delete recursively.
	 *
	 * @param root the root
	 * @return true, if successful
	 */
	public static boolean deleteRecursively(File root) {
		return deleteRecursively(root, true);
	}

	
	/**
	 * Inner delete recursively.
	 *
	 * @param root the root
	 * @return true, if successful
	 */
	private static boolean innerDeleteRecursively(File root) {
		return deleteRecursively(root, true);
	}

	
	/**
	 * Delete recursively.
	 *
	 * @param root the root
	 * @param deleteRoot the delete root
	 * @return true, if successful
	 */
	public static boolean deleteRecursively(File root, boolean deleteRoot) {
		if (root != null && root.exists()) {
			if (root.isDirectory()) {
				File[] children = root.listFiles();
				if (children != null) {
					for (File aChildren : children) {
						innerDeleteRecursively(aChildren);
					}
				}
			}

			if (deleteRoot) {
				return root.delete();
			} else {
				return true;
			}
		}
		return false;
	}

	
	/**
	 * Sync file.
	 *
	 * @param fileToSync the file to sync
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void syncFile(File fileToSync) throws IOException {
		boolean success = false;
		int retryCount = 0;
		IOException exc = null;
		while (!success && retryCount < 5) {
			retryCount++;
			RandomAccessFile file = null;
			try {
				try {
					file = new RandomAccessFile(fileToSync, "rw");
					file.getFD().sync();
					success = true;
				} finally {
					if (file != null)
						file.close();
				}
			} catch (IOException ioe) {
				if (exc == null)
					exc = ioe;
				try {
					
					Thread.sleep(5);
				} catch (InterruptedException ie) {
					throw new InterruptedIOException(ie.getMessage());
				}
			}
		}
	}

	
	/**
	 * Copy file.
	 *
	 * @param sourceFile the source file
	 * @param destinationFile the destination file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void copyFile(File sourceFile, File destinationFile) throws IOException {
		FileInputStream sourceIs = null;
		FileChannel source = null;
		FileOutputStream destinationOs = null;
		FileChannel destination = null;
		try {
			sourceIs = new FileInputStream(sourceFile);
			source = sourceIs.getChannel();
			destinationOs = new FileOutputStream(destinationFile);
			destination = destinationOs.getChannel();
			destination.transferFrom(source, 0, source.size());
		} finally {
			if (source != null) {
				source.close();
			}
			if (sourceIs != null) {
				sourceIs.close();
			}
			if (destination != null) {
				destination.close();
			}
			if (destinationOs != null) {
				destinationOs.close();
			}
		}
	}

	
	/**
	 * Instantiates a new file system utils.
	 */
	private FileSystemUtils() {

	}
}
