/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons CLibrary.java 2012-3-29 15:15:15 l.xue.nong$$
 */

package cn.com.restart.commons.jna;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jna.Native;


/**
 * The Class CLibrary.
 *
 * @author l.xue.nong
 */
public class CLibrary {

	
	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(CLibrary.class);

	
	/** The Constant MCL_CURRENT. */
	public static final int MCL_CURRENT = 1;

	
	/** The Constant MCL_FUTURE. */
	public static final int MCL_FUTURE = 2;

	
	/** The Constant ENOMEM. */
	public static final int ENOMEM = 12;

	static {
		try {
			Native.register("c");
		} catch (NoClassDefFoundError e) {
			logger.warn("jna not found. native methods (mlockall) will be disabled.");
		} catch (UnsatisfiedLinkError e) {
			logger.debug("unable to link C library. native methods (mlockall) will be disabled.");
		}
	}

	
	/**
	 * Mlockall.
	 *
	 * @param flags the flags
	 * @return the int
	 */
	public static native int mlockall(int flags);

	
	/**
	 * Munlockall.
	 *
	 * @return the int
	 */
	public static native int munlockall();

	
	/**
	 * Instantiates a new c library.
	 */
	private CLibrary() {
	}
}