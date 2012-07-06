/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons rebirthInterruptedException.java 2012-7-6 10:22:17 l.xue.nong$$
 */


package cn.com.rebirth.commons.exception;


/**
 * The Class rebirthInterruptedException.
 *
 * @author l.xue.nong
 */
public class RebirthInterruptedException extends RebirthException {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4965626865613435140L;

	
	/**
	 * Instantiates a new rebirth interrupted exception.
	 *
	 * @param message the message
	 */
	public RebirthInterruptedException(String message) {
        super(message);
    }

    
    /**
     * Instantiates a new rebirth interrupted exception.
     *
     * @param message the message
     * @param cause the cause
     */
    public RebirthInterruptedException(String message, Throwable cause) {
        super(message, cause);
    }
}
