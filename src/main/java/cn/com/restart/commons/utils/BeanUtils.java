/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-commons BeanUtils.java 2012-2-2 10:19:41 l.xue.nong$$
 */
package cn.com.restart.commons.utils;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;

/**
 * The Class BeanUtils.
 *
 * @author l.xue.nong
 */
public abstract class BeanUtils {
	/** The b. */
	private static BeanUtilsBean b = BeanUtilsBean.getInstance();

	/**
	 * Clone bean.
	 *
	 * @param bean the bean
	 * @return the object
	 */
	public static Object cloneBean(Object bean) {
		try {
			return b.cloneBean(bean);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	/**
	 * Copy properties.
	 *
	 * @param <T> the generic type
	 * @param destClass the dest class
	 * @param orig the orig
	 * @return the t
	 */
	@SuppressWarnings("unchecked")
	public static <T> T copyProperties(Class<T> destClass, Object orig) {
		Object target = null;
		try {
			target = destClass.newInstance();
			copyProperties((Object) target, orig);
			return (T) target;
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	/**
	 * Copy properties.
	 *
	 * @param dest the dest
	 * @param orig the orig
	 */
	public static void copyProperties(Object dest, Object orig) {
		try {
			b.copyProperties(dest, orig);
		} catch (Exception e) {
			handleReflectionException(e);
		}
	}

	/**
	 * Copy property.
	 *
	 * @param bean the bean
	 * @param name the name
	 * @param value the value
	 */
	public static void copyProperty(Object bean, String name, Object value) {
		try {
			b.copyProperty(bean, name, value);
		} catch (Exception e) {
			handleReflectionException(e);
		}
	}

	/**
	 * Describe.
	 *
	 * @param bean the bean
	 * @return the map
	 */
	@SuppressWarnings("rawtypes")
	public static Map describe(Object bean) {
		try {
			return b.describe(bean);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	/**
	 * Gets the array property.
	 *
	 * @param bean the bean
	 * @param name the name
	 * @return the array property
	 */
	public static String[] getArrayProperty(Object bean, String name) {
		try {
			return b.getArrayProperty(bean, name);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	/**
	 * Gets the convert utils.
	 *
	 * @return the convert utils
	 */
	public static ConvertUtilsBean getConvertUtils() {
		return b.getConvertUtils();
	}

	/**
	 * Gets the indexed property.
	 *
	 * @param bean the bean
	 * @param name the name
	 * @param index the index
	 * @return the indexed property
	 */
	public static String getIndexedProperty(Object bean, String name, int index) {
		try {
			return b.getIndexedProperty(bean, name, index);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	/**
	 * Gets the indexed property.
	 *
	 * @param bean the bean
	 * @param name the name
	 * @return the indexed property
	 */
	public static String getIndexedProperty(Object bean, String name) {
		try {
			return b.getIndexedProperty(bean, name);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	/**
	 * Gets the mapped property.
	 *
	 * @param bean the bean
	 * @param name the name
	 * @param key the key
	 * @return the mapped property
	 */
	public static String getMappedProperty(Object bean, String name, String key) {
		try {
			return b.getMappedProperty(bean, name, key);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	/**
	 * Gets the mapped property.
	 *
	 * @param bean the bean
	 * @param name the name
	 * @return the mapped property
	 */
	public static String getMappedProperty(Object bean, String name) {
		try {
			return b.getMappedProperty(bean, name);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	/**
	 * Gets the nested property.
	 *
	 * @param bean the bean
	 * @param name the name
	 * @return the nested property
	 */
	public static String getNestedProperty(Object bean, String name) {
		try {
			return b.getNestedProperty(bean, name);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	/**
	 * Gets the property.
	 *
	 * @param bean the bean
	 * @param name the name
	 * @return the property
	 */
	public static String getProperty(Object bean, String name) {
		try {
			return b.getProperty(bean, name);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	/**
	 * Gets the property utils.
	 *
	 * @return the property utils
	 */
	public static PropertyUtilsBean getPropertyUtils() {
		try {
			return b.getPropertyUtils();
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	/**
	 * Gets the simple property.
	 *
	 * @param bean the bean
	 * @param name the name
	 * @return the simple property
	 */
	public static String getSimpleProperty(Object bean, String name) {
		try {
			return b.getSimpleProperty(bean, name);
		} catch (Exception e) {
			handleReflectionException(e);
			return null;
		}
	}

	/**
	 * Populate.
	 *
	 * @param bean the bean
	 * @param properties the properties
	 */
	@SuppressWarnings("rawtypes")
	public static void populate(Object bean, Map properties) {
		try {
			b.populate(bean, properties);
		} catch (Exception e) {
			handleReflectionException(e);
		}
	}

	/**
	 * Sets the property.
	 *
	 * @param bean the bean
	 * @param name the name
	 * @param value the value
	 */
	public static void setProperty(Object bean, String name, Object value) {
		try {
			b.setProperty(bean, name, value);
		} catch (Exception e) {
			handleReflectionException(e);
		}
	}

	/**
	 * Handle reflection exception.
	 *
	 * @param e the e
	 */
	private static void handleReflectionException(Exception e) {
		ExceptionUtils.unchecked(e);
	}
}
