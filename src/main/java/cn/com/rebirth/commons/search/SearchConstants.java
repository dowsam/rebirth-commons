/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons SearchConstants.java 2012-7-6 10:22:15 l.xue.nong$$
 */
package cn.com.rebirth.commons.search;

import cn.com.rebirth.commons.Constants;
import cn.com.rebirth.commons.utils.TemplateMatcher;

/**
 * The Class SearchConstants.
 *
 * @author l.xue.nong
 */
public abstract class SearchConstants implements Constants {
	
	/** The rebirth search server default zk config name. */
	private static String rebirth_SEARCH_SERVER_DEFAULT_ZK_CONFIG_NAME = "/rebirth/search/server/config";

	/** The rebirth search bulid zk config name. */
	public static String rebirth_SEARCH_BULID_ZK_CONFIG_NAME = "${rebirth.search.bulid.zk.config.name}";

	/**
	 * Gets the rebirth search bulid zk config.
	 *
	 * @return the rebirth search bulid zk config
	 */
	public static String getRebirthSearchBulidZKConfig() {
		return new TemplateMatcher("${", "}").replace(rebirth_SEARCH_BULID_ZK_CONFIG_NAME,
				new TemplateMatcher.VariableResolver() {

					@Override
					public String resolve(String variable) {
						return System.getProperty(variable, rebirth_SEARCH_SERVER_DEFAULT_ZK_CONFIG_NAME);
					}
				});
	}
}
