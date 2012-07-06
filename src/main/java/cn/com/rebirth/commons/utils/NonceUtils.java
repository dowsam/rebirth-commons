/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons NonceUtils.java 2012-7-6 10:22:15 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

import java.io.IOException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.RandomStringUtils;

/**
 * The Class NonceUtils.
 *
 * @author l.xue.nong
 */
public abstract class NonceUtils {
	//RFC3339 日期标准格式,
	/** The Constant INTERNATE_DATE_FORMAT. */
	private static final SimpleDateFormat INTERNATE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	//定长格式化所用字符串, 含1,2,4,8位的字符串.
	/** The Constant SPACES. */
	private static final String[] SPACES = { "0", "00", "0000", "00000000" };

	//同一JVM同一毫秒内请求的计数器.
	/** The last time. */
	private static Date lastTime;

	/** The counter. */
	private static int counter = 0;

	//-- Random function --//
	/**
	 * Random string.
	 *
	 * @param length the length
	 * @return the string
	 */
	public static String randomString(int length) {
		return RandomStringUtils.randomAlphanumeric(length);
	}

	/**
	 * Random int.
	 *
	 * @return the int
	 */
	public static int randomInt() {
		return new SecureRandom().nextInt();
	}

	/**
	 * Random hex int.
	 *
	 * @return the string
	 */
	public static String randomHexInt() {
		return Integer.toHexString(randomInt());
	}

	/**
	 * Random long.
	 *
	 * @return the long
	 */
	public static long randomLong() {
		return new SecureRandom().nextLong();
	}

	/**
	 * Random hex long.
	 *
	 * @return the string
	 */
	public static String randomHexLong() {
		return Long.toHexString(randomLong());
	}

	/**
	 * Random uuid.
	 *
	 * @return the string
	 */
	public static String randomUUID() {
		return UUID.randomUUID().toString();
	}

	//-- Timestamp function --//
	/**
	 * Current timestamp.
	 *
	 * @return the string
	 */
	public static String currentTimestamp() {
		Date now = new Date();
		return INTERNATE_DATE_FORMAT.format(now);
	}

	/**
	 * Current mills.
	 *
	 * @return the long
	 */
	public static long currentMills() {
		return System.currentTimeMillis();
	}

	/**
	 * Current hex mills.
	 *
	 * @return the string
	 */
	public static String currentHexMills() {
		return Long.toHexString(currentMills());
	}

	//-- Helper function --//
	/**
	 * Gets the counter.
	 *
	 * @return the counter
	 */
	public static synchronized String getCounter() {
		Date currentTime = new Date();

		if (currentTime.equals(lastTime)) {
			counter++;
		} else {
			lastTime = currentTime;
			counter = 0;
		}
		return Integer.toHexString(counter);
	}

	//-- Helper function --//
	/**
	 * Format.
	 *
	 * @param source the source
	 * @param length the length
	 * @return the string
	 */
	public static String format(String source, int length) {
		int spaceLength = length - source.length();
		StringBuilder buf = new StringBuilder();

		while (spaceLength >= 8) {
			buf.append(SPACES[3]);
			spaceLength -= 8;
		}

		for (int i = 2; i >= 0; i--) {
			if ((spaceLength & (1 << i)) != 0) {
				buf.append(SPACES[i]);
			}
		}

		buf.append(source);
		return buf.toString();
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String args[]) throws IOException {
		System.out.println(randomLong() + currentMills());
	}
}
