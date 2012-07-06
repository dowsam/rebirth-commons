/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-commons ConvertUtils.java 2012-2-2 10:38:37 l.xue.nong$$
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
	/**
	 * 持有Dozer单例, 避免重复创建Mapper消耗资源.
	 */
	private static DozerBeanMapper dozer = new DozerBeanMapper();

	static {
		registerDateConverter();
	}

	/**
	 * 基于Dozer转换对象的类型.
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
	 * 基于Dozer转换List中对象的类型.
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
	 * 基于Apache BeanUtils转换字符串到相应类型.
	 *
	 * @param value 待转换的字符串.
	 * @param toType 转换目标类型.
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
	 * 定义Apache BeanUtils日期Converter的格式: yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss.
	 */
	private static void registerDateConverter() {
		DateConverter dc = new DateConverter();
		dc.setUseLocaleFormat(true);
		dc.setPatterns(new String[] { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss" });
		org.apache.commons.beanutils.ConvertUtils.register(dc, Date.class);
	}

	/**
	 * 提取集合中的对象的属性(通过Getter函数), 组合成List.
	 *
	 * @param collection 来源集合.
	 * @param propertyName 要提取的属性名.
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
	 * 提取集合中的对象的属性(通过Getter函数), 组合成由分割符分隔的字符串.
	 *
	 * @param collection 来源集合.
	 * @param propertyName 要提取的属性名.
	 * @param separator 分隔符.
	 * @return the string
	 */
	@SuppressWarnings("rawtypes")
	public static String extractElementPropertyToString(final Collection collection, final String propertyName,
			final String separator) {
		List list = extractElementPropertyToList(collection, propertyName);
		return StringUtils.join(list, separator);
	}
}
