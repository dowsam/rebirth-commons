/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons UncategorizedExecutionException.java 2012-7-6 10:22:15 l.xue.nong$$
 */


package cn.com.rebirth.commons.concurrent;

import cn.com.rebirth.commons.exception.RebirthException;


/**
 * The Class UncategorizedExecutionException.
 *
 * @author l.xue.nong
 */
public class UncategorizedExecutionException extends RebirthException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8542839484876462065L;

	
	/**
	 * Instantiates a new uncategorized execution exception.
	 *
	 * @param msg the msg
	 */
	public UncategorizedExecutionException(String msg) {
		super(msg);
	}

	
	/**
	 * Instantiates a new uncategorized execution exception.
	 *
	 * @param msg the msg
	 * @param cause the cause
	 */
	public UncategorizedExecutionException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
