/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons JnaInitialization.java 2012-8-9 8:44:10 l.xue.nong$$
 */
package cn.com.rebirth.commons.jna;

import cn.com.rebirth.commons.PreInitialization;
import cn.com.rebirth.commons.RebirthContainer;
import cn.com.rebirth.commons.exception.RebirthException;

/**
 * The Class JnaInitialization.
 *
 * @author l.xue.nong
 */
public class JnaInitialization implements PreInitialization {

	/* (non-Javadoc)
	 * @see cn.com.rebirth.commons.Initialization#init()
	 */
	@Override
	public void init() throws RebirthException {
		Natives.tryMlockall();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.commons.Initialization#stop()
	 */
	@Override
	public void stop() throws RebirthException {

	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.commons.SortInitialization#sort()
	 */
	@Override
	public Integer sort() {
		return 0;
	}

	@Override
	public void beforeInit(RebirthContainer container) {

	}

}
