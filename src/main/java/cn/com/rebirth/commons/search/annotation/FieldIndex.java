/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons FieldIndex.java 2012-7-6 10:22:12 l.xue.nong$$
 */
package cn.com.rebirth.commons.search.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Interface FieldIndex.
 *
 * @author l.xue.nong
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD })
public @interface FieldIndex {

	/** The Constant NO_INDEX. */
	public static final String NO_INDEX = "NO";

	/** The Constant NOT_ANALYZED. */
	public static final String NOT_ANALYZED = "NOT_ANALYZED";

	/** The Constant NO_ANALYZED. */
	public static final String NO_ANALYZED = "NO_ANALYZED";

	/** The Constant ANALYZED. */
	public static final String ANALYZED = "ANALYZED";

	/** The Constant FIELD. */
	public static final String FIELD = "field";

	/** The Constant NUMERICFIELD. */
	public static final String NUMERICFIELD = "numericField";

	/**
	 * Value.
	 *
	 * @return the string
	 */
	public String value() default NOT_ANALYZED;

	/**
	 * Type.
	 *
	 * @return the string
	 */
	public String type() default FIELD;

	/**
	 * Name.
	 *
	 * @return the string
	 */
	public String name() default "";
}
