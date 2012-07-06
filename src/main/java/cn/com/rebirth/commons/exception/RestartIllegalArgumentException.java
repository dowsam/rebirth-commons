/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons SumMallSearchIllegalArgumentException.java 2012-3-29 15:14:34 l.xue.nong$$
 */


package cn.com.rebirth.commons.exception;


/**
 * The Class SumMallSearchIllegalArgumentException.
 *
 * @author l.xue.nong
 */
public class RestartIllegalArgumentException extends RestartException {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 232987057498557921L;

	
	/**
	 * Instantiates a new sum mall search illegal argument exception.
	 */
	public RestartIllegalArgumentException() {
		super(null);
	}

	
	/**
	 * Instantiates a new sum mall search illegal argument exception.
	 *
	 * @param msg the msg
	 */
	public RestartIllegalArgumentException(String msg) {
		super(msg);
	}

	
	/**
	 * Instantiates a new sum mall search illegal argument exception.
	 *
	 * @param msg the msg
	 * @param cause the cause
	 */
	public RestartIllegalArgumentException(String msg, Throwable cause) {
		super(msg, cause);
	}

}