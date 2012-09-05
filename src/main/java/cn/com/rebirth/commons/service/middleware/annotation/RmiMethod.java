/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-service-middleware-server RmiMethod.java 2012-7-17 15:12:20 l.xue.nong$$
 */
package cn.com.rebirth.commons.service.middleware.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Interface RmiMethod.
 *
 * @author l.xue.nong
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD })
@Documented
public @interface RmiMethod {

	/**
	 * Time out.
	 *
	 * @return the long
	 */
	long timeOut() default 600L;
}
