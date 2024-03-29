/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons NoClassSettingsException.java 2012-7-6 10:22:13 l.xue.nong$$
 */


package cn.com.rebirth.commons.settings;


/**
 * The Class NoClassSettingsException.
 *
 * @author l.xue.nong
 */
public class NoClassSettingsException extends SettingsException {
	
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1018864875713755200L;

	
	/**
	 * Instantiates a new no class settings exception.
	 *
	 * @param message the message
	 */
	public NoClassSettingsException(String message) {
		super(message);
	}

	
	/**
	 * Instantiates a new no class settings exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public NoClassSettingsException(String message, Throwable cause) {
		super(message, cause);
	}
}
