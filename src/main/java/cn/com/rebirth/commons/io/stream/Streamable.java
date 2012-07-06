/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons Streamable.java 2012-7-6 10:22:13 l.xue.nong$$
 */

package cn.com.rebirth.commons.io.stream;

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
