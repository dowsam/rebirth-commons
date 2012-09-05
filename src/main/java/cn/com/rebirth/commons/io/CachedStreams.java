/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons CachedStreams.java 2012-7-6 10:23:45 l.xue.nong$$
 */
package cn.com.rebirth.commons.io;

import cn.com.rebirth.commons.io.stream.CachedStreamInput;
import cn.com.rebirth.commons.io.stream.CachedStreamOutput;

/**
 * The Class CachedStreams.
 *
 * @author l.xue.nong
 */
public class CachedStreams {

	/**
	 * Clear.
	 */
	public static void clear() {
		CachedStreamInput.clear();
		CachedStreamOutput.clear();
	}
}