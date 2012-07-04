/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:restart-commons IdentitiesUtils.java 2012-7-4 10:44:30 l.xue.nong$$
 */
package cn.com.restart.commons.utils;

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
	 * 封装JDK自带的UUID, 通过Random数字生成,中间有-分割.
	 *
	 * @return the string
	 */
	public static String uuid() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 封装JDK自带的UUID, 通过Random数字生成,中间无-分割.
	 *
	 * @return the string
	 */
	public static String uuid2() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 使用SecureRandom随机生成Long.
	 *
	 * @return the long
	 */
	public static long randomLong() {
		return random.nextLong();
	}

	/**
	 * 基于Base62编码的随机生成Long.
	 *
	 * @return the string
	 */
	public static String randomBase62() {
		return EncodeUtils.encodeBase62(random.nextLong());
	}
}
