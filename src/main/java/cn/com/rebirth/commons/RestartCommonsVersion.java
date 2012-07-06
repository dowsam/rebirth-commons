/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:restart-commons RestartCommonsVersion.java 2012-7-4 9:44:33 l.xue.nong$$
 */
package cn.com.rebirth.commons;

/**
 * 公用模块的版本实现.
 *
 * @author l.xue.nong
 */
public final class RestartCommonsVersion implements Version {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -367283822037017652L;

	/* (non-Javadoc)
	 * @see cn.com.restart.commons.Version#getModuleVersion()
	 */
	@Override
	public String getModuleVersion() {
		return "0.0.1.RC1-SNAPSHOT";
	}

	/* (non-Javadoc)
	 * @see cn.com.restart.commons.Version#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return "restart-commons";
	}

}
