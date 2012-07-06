/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons Version.java 2012-7-6 10:22:15 l.xue.nong$$
 */
package cn.com.rebirth.commons;

import java.io.Serializable;

/**
 * The Interface Version.
 *
 * @author l.xue.nong
 */
public interface Version extends Serializable {

	/**
	 * Gets the module version.
	 *
	 * @return the module version
	 */
	public String getModuleVersion();

	/**
	 * Gets the module name.
	 *
	 * @return the module name
	 */
	public String getModuleName();
}
