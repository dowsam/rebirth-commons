/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons VersionFactory.java 2012-7-6 15:15:34 l.xue.nong$$
 */
package cn.com.rebirth.commons;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.rebirth.commons.exception.RebirthException;
import cn.com.rebirth.commons.utils.ClassResolverUtils;

import com.google.common.collect.Maps;

/**
 * A factory for creating Version objects.
 */
public final class VersionFactory implements Initialization, PreInitialization {

	/** The logger. */
	private Logger logger = LoggerFactory.getLogger(getClass());

	/** The versions. */
	private List<Version> versions;

	/** The context. */
	private volatile Map<String, Version> context = Maps.newHashMap();

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
	@Override
	public void init() {
		if (versions != null) {
			for (Version version : versions) {
				logger.info("Initialization Rebirth Module(" + version.getModuleName() + "("
						+ version.getModuleVersion() + "))");
				context.put((version.getModuleName() + "-" + version.getModuleVersion()).toLowerCase(), version);
			}
		}
	}

	/**
	 * Current version.
	 *
	 * @return the version
	 */
	public Version currentVersion() {
		String appName = System.getProperty("app.name");
		if (StringUtils.isBlank(appName)) {
			String arr[] = StringUtils.split(System.getProperty("user.dir"), "\\");
			appName = arr[arr.length - 1];
		}
		if (StringUtils.isBlank(appName)) {
			throw new RebirthException("not find appName!");
		}
		return this.context.get((appName + "-" + Version.CURRENTVERSION).toLowerCase());
	}

	/**
	 * Stop.
	 */
	@Override
	public void stop() {

	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.commons.SortInitialization#sort()
	 */
	@Override
	public Integer sort() {
		return Integer.MAX_VALUE;
	}

	@Override
	public void beforeInit(RebirthContainer container) {

	}

}
