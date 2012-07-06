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

	@Override
	public Method getGetter() {
		return getter;
	}

	@Override
	public Method getSetter() {
		return setter;
	}

	@Override
	public String getFieldName() {
		return StringUtils.uncapitalize(getGetter().getName().replaceFirst("get", ""));
	}

}
