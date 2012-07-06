/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons DataUtils.java 2012-7-6 10:22:15 l.xue.nong$$
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
	 * Random id.
	 *
	 * @return the long
	 */
	public static long randomId() {
		return random.nextLong();
	}

	/**
	 * Random name.
	 *
	 * @param prefix the prefix
	 * @return the string
	 */
	public static String randomName(String prefix) {
		return prefix + random.nextInt(10000);
	}

	/**
	 * Random one.
	 *
	 * @param <T> the generic type
	 * @param list the list
	 * @return the t
	 */
	public static <T> T randomOne(List<T> list) {
		return randomSome(list, 1).get(0);
	}

	/**
	 * Random some.
	 *
	 * @param <T> the generic type
	 * @param list the list
	 * @return the list
	 */
	public static <T> List<T> randomSome(List<T> list) {
		return randomSome(list, random.nextInt(list.size()));
	}

	/**
	 * Random some.
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

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		List<Integer> list = Lists.newArrayList(1, 2, 3, 4);
		Collections.shuffle(list);
		System.out.println(list);
	}
}
