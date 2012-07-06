/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons rebirthParseException.java 2012-7-6 10:22:14 l.xue.nong$$
 */

package cn.com.rebirth.commons.exception;


/**
 * The Class rebirthParseException.
 *
 * @author l.xue.nong
 */
public class RebirthParseException extends RebirthException {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3375382240133599228L;

	
	/**
	 * Instantiates a new rebirth parse exception.
	 *
	 * @param msg the msg
	 */
	public RebirthParseException(String msg) {
		super(msg);
	}

	
	/**
	 * Instantiates a new rebirth parse exception.
	 *
	 * @param msg the msg
	 * @param cause the cause
	 */
	public RebirthParseException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
