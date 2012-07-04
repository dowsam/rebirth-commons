/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-commons CollectionMapperUtils.java 2012-2-2 10:35:33 l.xue.nong$$
 */
package cn.com.restart.commons.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

/**
 * The Class CollectionMapperUtils.
 *
 * @author l.xue.nong
 */
@SuppressWarnings("all")
public abstract class CollectionMapperUtils {

	/**
	 * Subtract.
	 *
	 * @param <T> the generic type
	 * @param a the a
	 * @param b the b
	 * @return the list
	 */
	public static <T> List<T> subtract(final Collection<T> a, final Collection<T> b) {
		ArrayList<T> list = new ArrayList<T>(a);
		for (Iterator it = b.iterator(); it.hasNext();) {
			list.remove(it.next());
		}
		return list;
	}

	/**
	 * 提取集合中的对象的属性(通过Getter函数), 组合成Map.
	 *
	 * @param collection 来源集合.
	 * @param keyPropertyName 要提取为Map中的Key值的属性名.
	 * @param valuePropertyName 要提取为Map中的Value值的属性名.
	 * @return the map
	 */
	public static Map extractToMap(final Collection collection, final String keyPropertyName,
			final String valuePropertyName) {
		Map map = new HashMap();

		try {
			for (Object obj : collection) {
				map.put(PropertyUtils.getProperty(obj, keyPropertyName),
						PropertyUtils.getProperty(obj, valuePropertyName));
			}
		} catch (Exception e) {
			throw ReflectionUtils.convertReflectionExceptionToUnchecked(e);
		}

		return map;
	}

	/**
	 * 提取集合中的对象的属性(通过Getter函数), 组合成List.
	 *
	 * @param collection 来源集合.
	 * @param propertyName 要提取的属性名.
	 * @return the list
	 */
	@SuppressWarnings("rawtypes")
	public static List extractToList(final Collection collection, final String propertyName) {
		List list = new ArrayList();

		try {
			for (Object obj : collection) {
				list.add(PropertyUtils.getProperty(obj, propertyName));
			}
		} catch (Exception e) {
			throw ReflectionUtils.convertReflectionExceptionToUnchecked(e);
		}

		return list;
	}

	/**
	 * 提取集合中的对象的属性(通过Getter函数), 组合成由分割符分隔的字符串.
	 *
	 * @param collection 来源集合.
	 * @param propertyName 要提取的属性名.
	 * @param separator 分隔符.
	 * @return the string
	 */
	public static String extractToString(final Collection collection, final String propertyName, final String separator) {
		List list = extractToList(collection, propertyName);
		return StringUtils.join(list, separator);
	}
}
