/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-commons NonceUtils.java 2012-2-2 10:54:11 l.xue.nong$$
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
	 * 使用较低强度的java.util.Random(),生成含所有字母与数字的字符串.
	 *
	 * @param length 返回字符串长度
	 * @return the string
	 */
	public static String randomString(int length) {
		return RandomStringUtils.randomAlphanumeric(length);
	}

	/**
	 * 使用SecureRandom生成Integer.
	 *
	 * @return the int
	 */
	public static int randomInt() {
		return new SecureRandom().nextInt();
	}

	/**
	 * 使用SecureRandom生成Integer,返回长度不大于8的Hex编码结果.
	 *
	 * @return the string
	 */
	public static String randomHexInt() {
		return Integer.toHexString(randomInt());
	}

	/**
	 * 使用SecureRandom生成Long.
	 *
	 * @return the long
	 */
	public static long randomLong() {
		return new SecureRandom().nextLong();
	}

	/**
	 * 使用SecureRandom生成Long, 返回长度不大于16的Hex编码结果.
	 *
	 * @return the string
	 */
	public static String randomHexLong() {
		return Long.toHexString(randomLong());
	}

	/**
	 * 使用SecureRandom生成32字符,每8位带-的UUID,见rfc4122.
	 *
	 * @return the string
	 */
	public static String randomUUID() {
		return UUID.randomUUID().toString();
	}

	//-- Timestamp function --//
	/**
	 * 返回Internate标准格式的当前毫秒级时间戳字符串.
	 * 
	 * 标准格式为yyyy-MM-dd'T'HH:mm:ss.SSS'Z', 如2009-10-15T14:24:50.316Z.
	 *
	 * @return the string
	 */
	public static String currentTimestamp() {
		Date now = new Date();
		return INTERNATE_DATE_FORMAT.format(now);
	}

	/**
	 * 返回当前距离1970年的毫秒数.
	 *
	 * @return the long
	 */
	public static long currentMills() {
		return System.currentTimeMillis();
	}

	/**
	 * 返回当前距离1970年的毫秒数, 返回Hex编码的结果.
	 *
	 * @return the string
	 */
	public static String currentHexMills() {
		return Long.toHexString(currentMills());
	}

	//-- Helper function --//
	/**
	 * 返回Hex编码的同一毫秒内的Counter.
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
	 * 格式化字符串, 固定字符串长度, 不足长度在前面补0.
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
