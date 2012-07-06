/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-commons DataUtils.java 2012-2-2 10:40:54 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

/**
 * The Class DataUtils.
 *
 * @author l.xue.nong
 */
public abstract class DataUtils {
	/** The random. */
	private static Random random = new Random();

	/**
	 * 返回随机ID.
	 *
	 * @return the long
	 */
	public static long randomId() {
		return random.nextLong();
	}

	/**
	 * 返回随机名称, prefix字符串+随机数字.
	 *
	 * @param prefix the prefix
	 * @return the string
	 */
	public static String randomName(String prefix) {
		return prefix + random.nextInt(10000);
	}

	/**
	 * 从输入list中随机返回一个对象.
	 *
	 * @param <T> the generic type
	 * @param list the list
	 * @return the t
	 */
	public static <T> T randomOne(List<T> list) {
		return randomSome(list, 1).get(0);
	}

	/**
	 * 从输入list中随机返回随机个对象.
	 *
	 * @param <T> the generic type
	 * @param list the list
	 * @return the list
	 */
	public static <T> List<T> randomSome(List<T> list) {
		return randomSome(list, random.nextInt(list.size()));
	}

	/**
	 * 从输入list中随机返回count个对象.
	 *
	 * @param <T> the generic type
	 * @param list the list
	 * @param count the count
	 * @return the list
	 */
	public static <T> List<T> randomSome(List<T> list, int count) {
		Collections.shuffle(list);
		return list.subList(0, count);
	}

	public static void main(String[] args) {
		List<Integer> list = Lists.newArrayList(1, 2, 3, 4);
		Collections.shuffle(list);
		System.out.println(list);
	}
}
