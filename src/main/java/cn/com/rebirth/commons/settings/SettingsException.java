/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons SettingsException.java 2012-7-6 10:22:13 l.xue.nong$$
 */


package cn.com.rebirth.commons.settings;

import cn.com.rebirth.commons.exception.RebirthException;



/**
 * The Class SettingsException.
 *
 * @author l.xue.nong
 */
public class SettingsException extends RebirthException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8080388311603676398L;

	
	/**
	 * Instantiates a new settings exception.
	 *
	 * @param message the message
	 */
	public SettingsException(String message) {
		super(message);
	}

	
	/**
	 * Instantiates a new settings exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public SettingsException(String message, Throwable cause) {
		super(message, cause);
	}
}
