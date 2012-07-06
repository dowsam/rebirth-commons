/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons JndiUtils.java 2012-7-6 10:22:15 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

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
