/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons OsUtils.java 2012-7-6 10:22:15 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

/**
 * The Class OsUtils.
 *
 * @author l.xue.nong
 */
public class OsUtils {

	/** The Constant OS_NAME. */
	public static final String OS_NAME = System.getProperty("os.name");

	/** The Constant LINUX. */
	public static final boolean LINUX = OS_NAME.trim().toLowerCase().startsWith("linux");

	/** The Constant WINDOWS. */
	public static final boolean WINDOWS = OS_NAME.trim().toLowerCase().startsWith("windows");

	/** The Constant SOLARIS. */
	public static final boolean SOLARIS = OS_NAME.trim().toLowerCase().startsWith("sun");

	/** The Constant MAC. */
	public static final boolean MAC = OS_NAME.trim().toLowerCase().startsWith("mac");

	/** The Constant HP. */
	public static final boolean HP = OS_NAME.trim().toLowerCase().startsWith("hp");

	/**
	 * Instantiates a new os utils.
	 */
	private OsUtils() {

	}
}
