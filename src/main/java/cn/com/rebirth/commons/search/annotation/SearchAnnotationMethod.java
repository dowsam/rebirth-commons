/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons SearchAnnotationMethod.java 2012-7-6 10:22:13 l.xue.nong$$
 */
package cn.com.rebirth.commons.search.annotation;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;

/**
 * The Class SearchAnnotationMethod.
 *
 * @author l.xue.nong
 */
public class SearchAnnotationMethod extends AbstractSearchProperty {

	/** The getter. */
	private final Method getter;

	/** The setter. */
	private Method setter;

	/**
	 * Instantiates a new search annotation method.
	 *
	 * @param method the method
	 */
	public SearchAnnotationMethod(Method method) {
		super(method);
		this.getter = method;
		this.getter.setAccessible(true);
		String setterName = getter.getName().replaceFirst("get", "set");
		if (getter.getName().startsWith("is")
				&& (Boolean.class.isAssignableFrom(getter.getReturnType()) || boolean.class.isAssignableFrom(getter
						.getReturnType()))) {
			setterName = getter.getName().replaceFirst("is", "set");
		}
		try {
			this.setter = method.getDeclaringClass().getDeclaredMethod(setterName, getter.getReturnType());
			this.setter.setAccessible(true);
		} catch (NoSuchMethodException e) {
			logger.error("No setter found for method provided: " + getter.getName() + " in class: "
					+ method.getDeclaringClass().getName());
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.commons.search.annotation.AbstractSearchProperty#getGetter()
	 */
	@Override
	public Method getGetter() {
		return getter;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.commons.search.annotation.AbstractSearchProperty#getSetter()
	 */
	@Override
	public Method getSetter() {
		return setter;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.commons.search.annotation.AbstractSearchProperty#getFieldName()
	 */
	@Override
	public String getFieldName() {
		if (getGetter().getName().startsWith("is")) {
			return StringUtils.uncapitalize(getGetter().getName().replaceFirst("is", ""));
		}
		return StringUtils.uncapitalize(getGetter().getName().replaceFirst("get", ""));
	}

}
