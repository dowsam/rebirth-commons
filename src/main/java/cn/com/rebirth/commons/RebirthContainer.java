/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons RebirthContainer.java 2012-7-6 15:22:16 l.xue.nong$$
 */
package cn.com.rebirth.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class RebirthContainer.
 *
 * @author l.xue.nong
 */
public final class RebirthContainer {

	/** The logger. */
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * Instantiates a new rebirth container.
	 */
	private RebirthContainer() {
		super();
	}

	/**
	 * Gets the single instance of RebirthContainer.
	 *
	 * @return single instance of RebirthContainer
	 */
	public static RebirthContainer getInstance() {
		return SingletonHolder.rebirthContainer;
	}

	/**
	 * The Class SingletonHolder.
	 *
	 * @author l.xue.nong
	 */
	private static class SingletonHolder {

		/** The rebirth container. */
		static RebirthContainer rebirthContainer = new RebirthContainer();
	}

	/**
	 * Start.
	 */
	public void start() {
		logger.info("Initialization Rebirth Container……………………");
		VersionFactory.getInstance().init();
		logger.info("Initialization Rebirth Container……………………end");
	}

	/**
	 * Stop.
	 */
	public void stop() {
		logger.info("Close Rebirth Container……………………");
		VersionFactory.getInstance().stop();
		logger.info("Close Rebirth Container……………………end");
	}

}
