/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons Index.java 2012-7-6 10:22:13 l.xue.nong$$
 */
package cn.com.rebirth.commons.search.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Interface Index.
 *
 * @author l.xue.nong
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.TYPE })
@Documented
public @interface Index {

	/**
	 * Index name.
	 *
	 * @return the string
	 */
	String indexName();

	/**
	 * Index type.
	 *
	 * @return the string
	 */
	String indexType();
}
