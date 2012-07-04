/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons SumMallSearchInterruptedException.java 2012-3-29 15:14:34 l.xue.nong$$
 */


package cn.com.restart.commons.exception;


/**
 * The Class SumMallSearchInterruptedException.
 *
 * @author l.xue.nong
 */
public class RestartInterruptedException extends RestartException {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4965626865613435140L;

	
	/**
	 * Instantiates a new sum mall search interrupted exception.
	 *
	 * @param message the message
	 */
	public RestartInterruptedException(String message) {
        super(message);
    }

    
    /**
     * Instantiates a new sum mall search interrupted exception.
     *
     * @param message the message
     * @param cause the cause
     */
    public RestartInterruptedException(String message, Throwable cause) {
        super(message, cause);
    }
}
