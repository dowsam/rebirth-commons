/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons SettingsLoaderFactory.java 2012-7-6 10:23:47 l.xue.nong$$
 */
package cn.com.rebirth.commons.settings.loader;

/**
 * A factory for creating SettingsLoader objects.
 */
public final class SettingsLoaderFactory {

	/**
	 * Instantiates a new settings loader factory.
	 */
	private SettingsLoaderFactory() {

	}

	/**
	 * Loader from resource.
	 *
	 * @param resourceName the resource name
	 * @return the settings loader
	 */
	public static SettingsLoader loaderFromResource(String resourceName) {
		return new PropertiesSettingsLoader();
	}

	/**
	 * Loader from source.
	 *
	 * @param source the source
	 * @return the settings loader
	 */
	public static SettingsLoader loaderFromSource(String source) {
		return new PropertiesSettingsLoader();
	}
}
