/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons SettingsException.java 2012-3-29 15:15:10 l.xue.nong$$
 */


package cn.com.restart.commons.settings;

import cn.com.restart.commons.exception.RestartException;



/**
 * The Class SettingsException.
 *
 * @author l.xue.nong
 */
public class SettingsException extends RestartException {

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
