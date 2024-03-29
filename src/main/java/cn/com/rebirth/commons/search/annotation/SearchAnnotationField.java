/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons SearchAnnotationField.java 2012-7-6 10:40:50 l.xue.nong$$
 */
package cn.com.rebirth.commons.search.annotation;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * The Class SearchAnnotationField.
 *
 * @author l.xue.nong
 */
public class SearchAnnotationField extends AbstractSearchProperty {

	/** The field. */
	private final Field field;

	/** The getter. */
	private Method getter = null;

	/** The setter. */
	private Method setter = null;

	/**
	 * Instantiates a new search annotation field.
	 *
	 * @param field the field
	 */
	public SearchAnnotationField(Field field) {
		super(field);
		this.field = field;
		try {
			BeanInfo info = Introspector.getBeanInfo(field.getDeclaringClass());
			for (PropertyDescriptor descriptor : info.getPropertyDescriptors()) {
				if (descriptor.getName().equals(field.getName())) {
					getter = descriptor.getReadMethod();
					setter = descriptor.getWriteMethod();
					break;
				}
			}
			if (getter == null || setter == null)
				throw new IllegalArgumentException("Only fields with valid JavaBean accessors can be annotated");
		} catch (IntrospectionException e) {
			throw new IllegalArgumentException("Could not introspect field: " + field);
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
		return field.getName();
	}

}
