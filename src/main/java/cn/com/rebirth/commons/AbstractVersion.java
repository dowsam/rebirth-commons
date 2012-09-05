/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons AbstractVersion.java 2012-7-19 13:11:47 l.xue.nong$$
 */
package cn.com.rebirth.commons;

/**
 * The Class AbstractVersion.
 *
 * @author l.xue.nong
 */
public abstract class AbstractVersion implements Version {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7210195069307816283L;

	/* (non-Javadoc)
	 * @see cn.com.rebirth.commons.Version#getModuleVersion()
	 */
	@Override
	public String getModuleVersion() {
		return Version.CURRENTVERSION;
	}

}
