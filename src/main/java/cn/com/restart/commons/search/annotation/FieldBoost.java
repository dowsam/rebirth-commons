/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-commons FieldBoost.java 2012-2-16 17:51:19 l.xue.nong$$
 */
package cn.com.restart.commons.search.annotation;

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
