/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons Natives.java 2012-7-6 10:22:13 l.xue.nong$$
 */
package cn.com.rebirth.commons.jna;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jna.Native;

/**
 * The Class Natives.
 *
 * @author l.xue.nong
 */
public class Natives {

	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(Natives.class);

	/**
	 * Try mlockall.
	 */
	public static void tryMlockall() {
		int errno = Integer.MIN_VALUE;
		try {
			int result = CLibrary.mlockall(CLibrary.MCL_CURRENT);
			if (result != 0)
				errno = Native.getLastError();
		} catch (UnsatisfiedLinkError e) {

			return;
		}

		if (errno != Integer.MIN_VALUE) {
			if (errno == CLibrary.ENOMEM && System.getProperty("os.name").toLowerCase().contains("linux")) {
				logger.warn("Unable to lock JVM memory (ENOMEM)."
						+ " This can result in part of the JVM being swapped out."
						+ " Increase RLIMIT_MEMLOCK or run Rebirthsearch as root.");
			} else if (!System.getProperty("os.name").toLowerCase().contains("mac")) {

				logger.warn("Unknown mlockall error " + errno);
			}
		}
	}
}
