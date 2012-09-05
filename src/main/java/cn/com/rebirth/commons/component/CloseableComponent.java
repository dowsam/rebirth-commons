/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons CloseableComponent.java 2012-7-17 10:45:15 l.xue.nong$$
 */
package cn.com.rebirth.commons.component;

import cn.com.rebirth.commons.exception.RebirthException;



/**
 * The Interface CloseableComponent.
 *
 * @author l.xue.nong
 */
public interface CloseableComponent {

	
	/**
	 * Close.
	 *
	 * @throws RebirthException the rebirth exception
	 */
	void close() throws RebirthException;
}
