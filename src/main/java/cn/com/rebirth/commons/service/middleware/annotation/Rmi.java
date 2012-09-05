/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-service-middleware-server Rmi.java 2012-7-17 15:09:36 l.xue.nong$$
 */
package cn.com.rebirth.commons.service.middleware.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.com.rebirth.commons.service.middleware.DefaultProviderService;

/**
 * The Interface Rmi.
 *
 * @author l.xue.nong
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.TYPE })
@Documented
public @interface Rmi {
	/**
	 * Target object.
	 *
	 * @return the class
	 */
	String targetObject() default "";

	/**
	 * Group.
	 *
	 * @return the string
	 */
	String group() default DefaultProviderService.DEFAULT_GROUP;

	/**
	 * Version.
	 *
	 * @return the string
	 */
	String version() default DefaultProviderService.DEFAULT_VERSION;

	/**
	 * Time out.
	 *
	 * @return the long
	 */
	long timeOut() default DefaultProviderService.DEFAULT_TIMEOUT;
}
