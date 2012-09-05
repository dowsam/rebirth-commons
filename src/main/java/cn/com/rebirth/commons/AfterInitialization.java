/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons AfterInitialization.java 2012-8-8 11:36:19 l.xue.nong$$
 */
package cn.com.rebirth.commons;

/**
 * The Interface AfterInitialization.
 *
 * @author l.xue.nong
 */
public interface AfterInitialization extends SortInitialization {
	
	/**
	 * After.
	 *
	 * @param rebirthContainer the rebirth container
	 */
	void afterInit(RebirthContainer rebirthContainer);
}
