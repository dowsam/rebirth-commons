/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-commons FieldStore.java 2012-2-16 14:27:34 l.xue.nong$$
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

	/** 不存储. */
	public static final String NO = "NO";

	/** 存储. */
	public static final String YES = "YES";

	/**
	 * lucene document field 存储属性
	 * 默认值 ： 保存.
	 *
	 * @return the string
	 */
	public String value() default YES;
}
