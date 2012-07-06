/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons SumMallSearchParseException.java 2012-3-29 15:14:34 l.xue.nong$$
 */

package cn.com.rebirth.commons.exception;


/**
 * The Class SumMallSearchParseException.
 *
 * @author l.xue.nong
 */
public class RestartParseException extends RestartException {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3375382240133599228L;

	
	/**
	 * Instantiates a new sum mall search parse exception.
	 *
	 * @param msg the msg
	 */
	public RestartParseException(String msg) {
		super(msg);
	}

	
	/**
	 * Instantiates a new sum mall search parse exception.
	 *
	 * @param msg the msg
	 * @param cause the cause
	 */
	public RestartParseException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
