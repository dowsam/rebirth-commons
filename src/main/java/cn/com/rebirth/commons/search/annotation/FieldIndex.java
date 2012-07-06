/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-commons FieldIndex.java 2012-2-16 14:26:33 l.xue.nong$$
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

	/** 不索引. */
	public static final String NO_INDEX = "NO";

	/** 索引不分词. */
	public static final String NOT_ANALYZED = "NOT_ANALYZED";

	/** The Constant NO_ANALYZED. */
	public static final String NO_ANALYZED = "NO_ANALYZED";

	/** 索引且分词. */
	public static final String ANALYZED = "ANALYZED";

	/** The Constant FIELD. */
	public static final String FIELD = "field";

	/** The Constant NUMERICFIELD. */
	public static final String NUMERICFIELD = "numericField";

	/**
	 * lucene document field 索引属性
	 * 默认值 ： "NOT_ANALYZED" ： 索引不分词
	 * 字符型枚举值：
	 * ”NO" : 不索引
	 * "NOT_ANALYZED" ： 索引不分词
	 * "ANALYZED" ： 索引且分词.
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
