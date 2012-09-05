/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons rebirthCommonsVersion.java 2012-7-6 10:22:15 l.xue.nong$$
 */
package cn.com.rebirth.commons;

/**
 * The Class rebirthCommonsVersion.
 *
 * @author l.xue.nong
 */
public final class RebirthCommonsVersion extends AbstractVersion implements Version {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -367283822037017652L;

	/* (non-Javadoc)
	 * @see cn.com.rebirth.commons.Version#getModuleName()
	 */
	@Override
	public String getModuleName() {
		return "rebirth-commons";
	}

}
