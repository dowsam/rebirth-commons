/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons PreInitialization.java 2012-8-8 11:34:43 l.xue.nong$$
 */
package cn.com.rebirth.commons;

/**
 * The Interface PreInitialization.
 */
public interface PreInitialization extends SortInitialization {
	void beforeInit(RebirthContainer container);
}
