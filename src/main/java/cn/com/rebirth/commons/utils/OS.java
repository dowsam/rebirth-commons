/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-commons OS.java 2012-3-14 9:49:29 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

/**
 * The Enum OS.
 *
 * @author l.xue.nong
 */
public enum OS {

	/** The WINDOW s_ nt. */
	WINDOWS_NT("Windows NT"),
	/** The WINDOW s_95. */
	WINDOWS_95("Windows 95"),
	/** The WINDOW s_98. */
	WINDOWS_98("Windows 98"),
	/** The WINDOW s_2000. */
	WINDOWS_2000("Windows 2000"),
	/** The WINDOW s_ vista. */
	WINDOWS_VISTA("Windows Vista"),
	/** The WINDOW s_7. */
	WINDOWS_7("Windows 7"),
	// add new windows versions here
	/** The WINDOW s_ other. */
	WINDOWS_OTHER("Windows"),

	/** The SOLARIS. */
	SOLARIS("Solaris"), /** The LINUX. */
	LINUX("Linux"), /** The H p_ ux. */
	HP_UX("HP-UX"), /** The IB m_ aix. */
	IBM_AIX("AIX"), /** The SG i_ irix. */
	SGI_IRIX("Irix"), /** The SU n_ os. */
	SUN_OS("SunOS"), /** The COMPA q_ tr u64_ unix. */
	COMPAQ_TRU64_UNIX("Digital UNIX"),
	/** The MAC. */
	MAC("Mac OS X", "Darwin"),
	/** The FREEBSD. */
	FREEBSD("freebsd"),
	// add new unix versions here

	/** The O s2. */
	OS2("OS/2"), /** The COMPA q_ ope n_ vms. */
	COMPAQ_OPEN_VMS("OpenVMS"),

	/** Unrecognized OS. */
	OTHER("");

	/** The names. */
	private String names[];

	/**
	 * Instantiates a new oS.
	 *
	 * @param names the names
	 */
	private OS(String... names) {
		this.names = names;
	}

	/**
	 * Checks if is windows.
	 *
	 * @return true if this OS belongs to windows family
	 */
	public boolean isWindows() {
		return ordinal() <= WINDOWS_OTHER.ordinal();
	}

	/**
	 * Checks if is unix.
	 *
	 * @return true if this OS belongs to *nix family
	 */
	public boolean isUnix() {
		return ordinal() > WINDOWS_OTHER.ordinal() && ordinal() < OS2.ordinal();
	}

	/*-------------------------------------------------[ Static Methods ]---------------------------------------------------*/

	/**
	 * Gets the.
	 *
	 * @param osName name of OS as returned by <code>System.getProperty("os.name")</code>
	 * @return OS for the specified {@code osName}
	 */
	public static OS get(String osName) {
		osName = osName.toLowerCase();
		for (OS os : values()) {
			for (String name : os.names) {
				if (osName.contains(name.toLowerCase()))
					return os;
			}
		}
		throw new RuntimeException();
	}

	/** The current. */
	private static OS current;

	/**
	 * Gets the.
	 *
	 * @return OS on which this JVM is running
	 */
	public static OS get() {
		if (current == null)
			current = get(System.getProperty("os.name"));
		return current;
	}
}
