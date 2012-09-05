/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Classes.java 2012-7-6 10:23:49 l.xue.nong$$
 */


package cn.com.rebirth.commons;

import java.lang.reflect.Modifier;


/**
 * The Class Classes.
 *
 * @author l.xue.nong
 */
public class Classes {

	
	/** The Constant PACKAGE_SEPARATOR. */
	private static final char PACKAGE_SEPARATOR = '.';

	
	/**
	 * Gets the default class loader.
	 *
	 * @return the default class loader
	 */
	public static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (Throwable ex) {
			
		}
		if (cl == null) {
			
			cl = Classes.class.getClassLoader();
		}
		return cl;
	}

	
	/**
	 * Gets the package name.
	 *
	 * @param clazz the clazz
	 * @return the package name
	 */
	public static String getPackageName(Class<?> clazz) {
		String className = clazz.getName();
		int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
		return (lastDotIndex != -1 ? className.substring(0, lastDotIndex) : "");
	}

	
	/**
	 * Gets the package name no domain.
	 *
	 * @param clazz the clazz
	 * @return the package name no domain
	 */
	public static String getPackageNameNoDomain(Class<?> clazz) {
		String fullPackage = getPackageName(clazz);
		if (fullPackage.startsWith("org.") || fullPackage.startsWith("com.") || fullPackage.startsWith("net.")) {
			return fullPackage.substring(4);
		}
		return fullPackage;
	}

	
	/**
	 * Checks if is inner class.
	 *
	 * @param clazz the clazz
	 * @return true, if is inner class
	 */
	public static boolean isInnerClass(Class<?> clazz) {
		return !Modifier.isStatic(clazz.getModifiers()) && clazz.getEnclosingClass() != null;
	}

	
	/**
	 * Checks if is concrete.
	 *
	 * @param clazz the clazz
	 * @return true, if is concrete
	 */
	public static boolean isConcrete(Class<?> clazz) {
		int modifiers = clazz.getModifiers();
		return !clazz.isInterface() && !Modifier.isAbstract(modifiers);
	}

	
	/**
	 * Instantiates a new classes.
	 */
	private Classes() {

	}
}
