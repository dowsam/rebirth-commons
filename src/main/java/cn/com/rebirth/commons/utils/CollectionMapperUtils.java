/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons CollectionMapperUtils.java 2012-7-6 10:22:13 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

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
	 * Extract to map.
	 *
	 * @param collection the collection
	 * @param keyPropertyName the key property name
	 * @param valuePropertyName the value property name
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
	 * Extract to list.
	 *
	 * @param collection the collection
	 * @param propertyName the property name
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
	 * Extract to string.
	 *
	 * @param collection the collection
	 * @param propertyName the property name
	 * @param separator the separator
	 * @return the string
	 */
	public static String extractToString(final Collection collection, final String propertyName, final String separator) {
		List list = extractToList(collection, propertyName);
		return StringUtils.join(list, separator);
	}
}
