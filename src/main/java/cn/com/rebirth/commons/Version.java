/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:restart-commons Version.java 2012-7-4 9:42:12 l.xue.nong$$
 */
package cn.com.rebirth.commons;

import java.io.Serializable;

/**
 * 统一定义项目间的版本管理.
 *
 * @author l.xue.nong
 */
public interface Version extends Serializable {

	/**
	 * 获取该模块的版本号
	 *
	 * @return the module version
	 */
	public String getModuleVersion();

	/**
	 * 获取该模块的模块名称
	 *
	 * @return the module name
	 */
	public String getModuleName();
}
