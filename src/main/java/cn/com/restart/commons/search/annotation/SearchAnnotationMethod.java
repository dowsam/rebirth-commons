/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-client SearchAnnotationMethod.java 2012-2-16 15:12:55 l.xue.nong$$
 */
package cn.com.restart.commons.search.annotation;

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
	private final Method setter;

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
		try {
			this.setter = method.getDeclaringClass().getDeclaredMethod(setterName, getter.getReturnType());
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException("No setter found for method provided: " + getter.getName() + " in class: "
					+ method.getDeclaringClass().getName());
		}
		this.setter.setAccessible(true);
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.search.client.AbstractSearchProperty#getGetter()
	 */
	@Override
	public Method getGetter() {
		return getter;
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.search.client.AbstractSearchProperty#getSetter()
	 */
	@Override
	public Method getSetter() {
		return setter;
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.search.client.AbstractSearchProperty#getFieldName()
	 */
	@Override
	public String getFieldName() {
		return StringUtils.uncapitalize(getGetter().getName().replaceFirst("get", ""));
	}

}
