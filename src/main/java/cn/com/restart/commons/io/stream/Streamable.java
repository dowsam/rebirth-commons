/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons Streamable.java 2012-3-29 15:15:11 l.xue.nong$$
 */

package cn.com.restart.commons.io.stream;

import java.io.IOException;


/**
 * The Interface Streamable.
 *
 * @author l.xue.nong
 */
public interface Streamable {

	
	/**
	 * Read from.
	 *
	 * @param in the in
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void readFrom(StreamInput in) throws IOException;

	
	/**
	 * Write to.
	 *
	 * @param out the out
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeTo(StreamOutput out) throws IOException;
}
