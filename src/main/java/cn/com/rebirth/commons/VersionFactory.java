/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons VersionFactory.java 2012-7-6 15:15:34 l.xue.nong$$
 */
package cn.com.rebirth.commons;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.rebirth.commons.utils.ClassResolverUtils;

/**
 * A factory for creating Version objects.
 */
public final class VersionFactory {

	/** The logger. */
	private Logger logger = LoggerFactory.getLogger(getClass());

	/** The versions. */
	private List<Version> versions;

	/**
	 * Instantiates a new version factory.
	 */
	private VersionFactory() {
		versions = ClassResolverUtils.findImpl(Version.class);
	}

	/**
	 * Gets the single instance of VersionFactory.
	 *
	 * @return single instance of VersionFactory
	 */
	public static VersionFactory getInstance() {
		return SingletonHolder.versionFactory;
	}

	/**
	 * The Class SingletonHolder.
	 *
	 * @author l.xue.nong
	 */
	private static class SingletonHolder {

		/** The version factory. */
		static VersionFactory versionFactory = new VersionFactory();
	}

	/**
	 * Inits the.
	 */
	public void init() {
		if (versions != null) {
			for (Version version : versions) {
				logger.info("Initialization Rebirth Module(" + version.getModuleName() + "("
						+ version.getModuleVersion() + "))");
			}
		}
	}

	/**
	 * Stop.
	 */
	public void stop() {

	}
}
