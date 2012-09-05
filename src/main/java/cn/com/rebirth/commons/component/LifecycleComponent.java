/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons LifecycleComponent.java 2012-7-17 10:45:15 l.xue.nong$$
 */
package cn.com.rebirth.commons.component;

import cn.com.rebirth.commons.exception.RebirthException;



/**
 * The Interface LifecycleComponent.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public interface LifecycleComponent<T> extends CloseableComponent {

	
	/**
	 * Lifecycle state.
	 *
	 * @return the lifecycle. state
	 */
	Lifecycle.State lifecycleState();

	
	/**
	 * Adds the lifecycle listener.
	 *
	 * @param listener the listener
	 */
	void addLifecycleListener(LifecycleListener listener);

	
	/**
	 * Removes the lifecycle listener.
	 *
	 * @param listener the listener
	 */
	void removeLifecycleListener(LifecycleListener listener);

	
	/**
	 * Start.
	 *
	 * @return the t
	 * @throws RebirthException the rebirth exception
	 */
	T start() throws RebirthException;

	
	/**
	 * Stop.
	 *
	 * @return the t
	 * @throws RebirthException the rebirth exception
	 */
	T stop() throws RebirthException;
}
