/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-commons JndiUtils.java 2012-2-2 10:52:56 l.xue.nong$$
 */
package cn.com.restart.commons.utils;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * The Class JndiUtils.
 *
 * @author l.xue.nong
 */
public abstract class JndiUtils {
	/**
	 * Lookup.
	 *
	 * @param jndiName the jndi name
	 * @return the object
	 */
	public static Object lookup(String jndiName) {
		try {
			Context initCtx = new InitialContext();
			return initCtx.lookup(jndiName);
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}
}
