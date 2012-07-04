/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons SumMallSearchIllegalStateException.java 2012-3-29 15:14:34 l.xue.nong$$
 */


package cn.com.restart.commons.exception;


/**
 * The Class SumMallSearchIllegalStateException.
 *
 * @author l.xue.nong
 */
public class RestartIllegalStateException extends RestartException {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2557289320495162342L;

	
	/**
	 * Instantiates a new sum mall search illegal state exception.
	 */
	public RestartIllegalStateException() {
        super(null);
    }

    
    /**
     * Instantiates a new sum mall search illegal state exception.
     *
     * @param msg the msg
     */
    public RestartIllegalStateException(String msg) {
        super(msg);
    }

    
    /**
     * Instantiates a new sum mall search illegal state exception.
     *
     * @param msg the msg
     * @param cause the cause
     */
    public RestartIllegalStateException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
