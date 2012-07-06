/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons FieldStore.java 2012-7-6 10:22:14 l.xue.nong$$
 */
package cn.com.rebirth.commons.search.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Interface FieldStore.
 *
 * @author l.xue.nong
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD })
public @interface FieldStore {

	/** The Constant NO. */
	public static final String NO = "NO";

	/** The Constant YES. */
	public static final String YES = "YES";

	/**
	 * Value.
	 *
	 * @return the string
	 */
	public String value() default YES;
}
