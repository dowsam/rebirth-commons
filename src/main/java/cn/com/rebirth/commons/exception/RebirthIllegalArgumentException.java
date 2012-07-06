/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons rebirthIllegalArgumentException.java 2012-7-6 10:22:14 l.xue.nong$$
 */


package cn.com.rebirth.commons.exception;


/**
 * The Class rebirthIllegalArgumentException.
 *
 * @author l.xue.nong
 */
public class RebirthIllegalArgumentException extends RebirthException {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 232987057498557921L;

	
	/**
	 * Instantiates a new rebirth illegal argument exception.
	 */
	public RebirthIllegalArgumentException() {
		super(null);
	}

	
	/**
	 * Instantiates a new rebirth illegal argument exception.
	 *
	 * @param msg the msg
	 */
	public RebirthIllegalArgumentException(String msg) {
		super(msg);
	}

	
	/**
	 * Instantiates a new rebirth illegal argument exception.
	 *
	 * @param msg the msg
	 * @param cause the cause
	 */
	public RebirthIllegalArgumentException(String msg, Throwable cause) {
		super(msg, cause);
	}

}