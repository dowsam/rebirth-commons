/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons LifecycleListener.java 2012-7-17 10:45:15 l.xue.nong$$
 */
package cn.com.rebirth.commons.component;


/**
 * The listener interface for receiving lifecycle events.
 * The class that is interested in processing a lifecycle
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addLifecycleListener<code> method. When
 * the lifecycle event occurs, that object's appropriate
 * method is invoked.
 *
 * @see LifecycleEvent
 */
public interface LifecycleListener {

	
	/**
	 * Beforebirth.
	 */
	void beforebirth();

	
	/**
	 * After start.
	 */
	void afterStart();

	
	/**
	 * Before stop.
	 */
	void beforeStop();

	
	/**
	 * After stop.
	 */
	void afterStop();

	
	/**
	 * Before close.
	 */
	void beforeClose();

	
	/**
	 * After close.
	 */
	void afterClose();
}
