/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons rebirthIllegalStateException.java 2012-7-6 10:22:15 l.xue.nong$$
 */


package cn.com.rebirth.commons.exception;


/**
 * The Class rebirthIllegalStateException.
 *
 * @author l.xue.nong
 */
public class RebirthIllegalStateException extends RebirthException {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2557289320495162342L;

	
	/**
	 * Instantiates a new rebirth illegal state exception.
	 */
	public RebirthIllegalStateException() {
        super(null);
    }

    
    /**
     * Instantiates a new rebirth illegal state exception.
     *
     * @param msg the msg
     */
    public RebirthIllegalStateException(String msg) {
        super(msg);
    }

    
    /**
     * Instantiates a new rebirth illegal state exception.
     *
     * @param msg the msg
     * @param cause the cause
     */
    public RebirthIllegalStateException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
