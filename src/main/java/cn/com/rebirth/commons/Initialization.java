/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons Initialization.java 2012-8-8 10:47:26 l.xue.nong$$
 */
package cn.com.rebirth.commons;

import cn.com.rebirth.commons.exception.RebirthException;

/**
 * The Interface Initialization.
 */
public interface Initialization {
	
	/**
	 * Inits the.
	 *
	 * @throws RebirthException the rebirth exception
	 */
	void init() throws RebirthException;

	/**
	 * Stop.
	 *
	 * @throws RebirthException the rebirth exception
	 */
	void stop() throws RebirthException;
}
