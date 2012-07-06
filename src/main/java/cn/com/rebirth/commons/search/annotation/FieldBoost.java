/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons FieldBoost.java 2012-7-6 10:22:12 l.xue.nong$$
 */
package cn.com.rebirth.commons.search.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Interface FieldBoost.
 *
 * @author l.xue.nong
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.FIELD, ElementType.METHOD })
public @interface FieldBoost {

	/**
	 * Boost.
	 *
	 * @return the float
	 */
	public float boost();
}
