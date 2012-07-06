/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons OS.java 2012-7-6 10:22:17 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

/**
 * The Enum OS.
 *
 * @author l.xue.nong
 */
public enum OS {

	/** The windows nt. */
	WINDOWS_NT("Windows NT"),
	
	/** The WINDOW s_95. */
	WINDOWS_95("Windows 95"),
	
	/** The WINDOW s_98. */
	WINDOWS_98("Windows 98"),
	
	/** The WINDOW s_2000. */
	WINDOWS_2000("Windows 2000"),
	
	/** The windows vista. */
	WINDOWS_VISTA("Windows Vista"),
	
	/** The WINDOW s_7. */
	WINDOWS_7("Windows 7"),
	// add new windows versions here
	/** The windows other. */
	WINDOWS_OTHER("Windows"),

	/** The solaris. */
	SOLARIS("Solaris"), 
 /** The linux. */
	LINUX("Linux"), 
 /** The hp ux. */
	HP_UX("HP-UX"), 
 /** The ibm aix. */
	IBM_AIX("AIX"), 
 /** The sgi irix. */
	SGI_IRIX("Irix"), 
 /** The sun os. */
	SUN_OS("SunOS"), 
 /** The COMPA q_ tr u64_ unix. */
	COMPAQ_TRU64_UNIX("Digital UNIX"),
	
	/** The mac. */
	MAC("Mac OS X", "Darwin"),
	
	/** The freebsd. */
	FREEBSD("freebsd"),
	// add new unix versions here

	/** The O s2. */
	OS2("OS/2"), 
 /** The compaq open vms. */
	COMPAQ_OPEN_VMS("OpenVMS"),

	/** The other. */
	OTHER("");

	/** The names. */
	private String names[];

	/**
	 * Instantiates a new os.
	 *
	 * @param names the names
	 */
	private OS(String... names) {
		this.names = names;
	}

	/**
	 * Checks if is windows.
	 *
	 * @return true, if is windows
	 */
	public boolean isWindows() {
		return ordinal() <= WINDOWS_OTHER.ordinal();
	}

	/**
	 * Checks if is unix.
	 *
	 * @return true, if is unix
	 */
	public boolean isUnix() {
		return ordinal() > WINDOWS_OTHER.ordinal() && ordinal() < OS2.ordinal();
	}

	/*-------------------------------------------------[ Static Methods ]---------------------------------------------------*/

	/**
	 * Gets the.
	 *
	 * @param osName the os name
	 * @return the os
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
	 * @return the os
	 */
	public static OS get() {
		if (current == null)
			current = get(System.getProperty("os.name"));
		return current;
	}
}
