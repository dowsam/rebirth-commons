/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons IdentitiesUtils.java 2012-7-6 10:22:15 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * The Class IdentitiesUtils.
 *
 * @author l.xue.nong
 */
public abstract class IdentitiesUtils {

	/** The random. */
	private static SecureRandom random = new SecureRandom();

	/**
	 * Uuid.
	 *
	 * @return the string
	 */
	public static String uuid() {
		return UUID.randomUUID().toString();
	}

	/**
	 * Uuid2.
	 *
	 * @return the string
	 */
	public static String uuid2() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * Random long.
	 *
	 * @return the long
	 */
	public static long randomLong() {
		return random.nextLong();
	}

	/**
	 * Random base62.
	 *
	 * @return the string
	 */
	public static String randomBase62() {
		return EncodeUtils.encodeBase62(random.nextLong());
	}

	public static void main(String[] args) {
		System.out.println(EncodeUtils.decodeBase62("BawdtVaDU15"));
	}
}
