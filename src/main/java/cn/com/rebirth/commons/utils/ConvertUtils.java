/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons ConvertUtils.java 2012-7-6 10:22:13 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang.StringUtils;
import org.dozer.DozerBeanMapper;

import com.google.common.collect.Lists;

/**
 * The Class ConvertUtils.
 *
 * @author l.xue.nong
 */
public abstract class ConvertUtils {
	
	/** The dozer. */
	private static DozerBeanMapper dozer = new DozerBeanMapper();

	static {
		registerDateConverter();
	}

	/**
	 * Map.
	 *
	 * @param <T> the generic type
	 * @param source the source
	 * @param destinationClass the destination class
	 * @return the t
	 */
	public static <T> T map(Object source, Class<T> destinationClass) {
		return dozer.map(source, destinationClass);
	}

	/**
	 * Map list.
	 *
	 * @param <T> the generic type
	 * @param sourceList the source list
	 * @param destinationClass the destination class
	 * @return the list
	 */
	@SuppressWarnings("rawtypes")
	public static <T> List<T> mapList(Collection sourceList, Class<T> destinationClass) {
		List<T> destinationList = Lists.newArrayList();
		for (Object sourceObject : sourceList) {
			T destinationObject = dozer.map(sourceObject, destinationClass);
			destinationList.add(destinationObject);
		}
		return destinationList;
	}

	/**
	 * Convert string to object.
	 *
	 * @param value the value
	 * @param toType the to type
	 * @return the object
	 */
	public static Object convertStringToObject(String value, Class<?> toType) {
		try {
			return org.apache.commons.beanutils.ConvertUtils.convert(value, toType);
		} catch (Exception e) {
			throw ReflectionUtils.convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * Convert object to object.
	 *
	 * @param <T> the generic type
	 * @param value the value
	 * @param toType the to type
	 * @return the t
	 */
	@SuppressWarnings("unchecked")
	public static <T> T convertObjectToObject(Object value, Class<?> toType) {
		try {
			return (T) org.apache.commons.beanutils.ConvertUtils.convert(value, toType);
		} catch (Exception e) {
			throw ReflectionUtils.convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * Register date converter.
	 */
	private static void registerDateConverter() {
		DateConverter dc = new DateConverter();
		dc.setUseLocaleFormat(true);
		dc.setPatterns(new String[] { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss" });
		org.apache.commons.beanutils.ConvertUtils.register(dc, Date.class);
	}

	/**
	 * Extract element property to list.
	 *
	 * @param collection the collection
	 * @param propertyName the property name
	 * @return the list
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List extractElementPropertyToList(final Collection collection, final String propertyName) {
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
	 * Extract element property to string.
	 *
	 * @param collection the collection
	 * @param propertyName the property name
	 * @param separator the separator
	 * @return the string
	 */
	@SuppressWarnings("rawtypes")
	public static String extractElementPropertyToString(final Collection collection, final String propertyName,
			final String separator) {
		List list = extractElementPropertyToList(collection, propertyName);
		return StringUtils.join(list, separator);
	}
}
