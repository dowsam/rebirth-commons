/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:restart-commons SearchConstants.java 2012-7-4 11:02:16 l.xue.nong$$
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
	/** The SUMMAL l_ searc h_ serve r_ defaul t_ z k_ confi g_ name. */
	private static String RESTART_SEARCH_SERVER_DEFAULT_ZK_CONFIG_NAME = "/restart/search/server/config";

	/** The SUMMAL l_ searc h_ buli d_ z k_ confi g_ name. */
	public static String RESTART_SEARCH_BULID_ZK_CONFIG_NAME = "${restart.search.bulid.zk.config.name}";

	/**
	 * Gets the sum mall search bulid zk config.
	 *
	 * @return the sum mall search bulid zk config
	 */
	public static String getRestartSearchBulidZKConfig() {
		return new TemplateMatcher("${", "}").replace(RESTART_SEARCH_BULID_ZK_CONFIG_NAME,
				new TemplateMatcher.VariableResolver() {

					@Override
					public String resolve(String variable) {
						return System.getProperty(variable, RESTART_SEARCH_SERVER_DEFAULT_ZK_CONFIG_NAME);
					}
				});
	}
}
