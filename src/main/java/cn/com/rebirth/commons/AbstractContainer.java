/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons AbstractContainer.java 2012-8-14 12:02:12 l.xue.nong$$
 */
package cn.com.rebirth.commons;

/**
 * The Class AbstractContainer.
 *
 * @author l.xue.nong
 */
public abstract class AbstractContainer implements Container {

	/**
	 * Before container start.
	 *
	 * @param container the container
	 */
	protected  void beforeContainerStart(RebirthContainer container){}

	/**
	 * After container start.
	 *
	 * @param container the container
	 */
	protected  void afterContainerStart(RebirthContainer container){}
}
