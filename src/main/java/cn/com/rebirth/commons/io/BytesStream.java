/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons BytesStream.java 2012-7-6 10:23:49 l.xue.nong$$
 */
package cn.com.rebirth.commons.io;

/**
 * The Interface BytesStream.
 *
 * @author l.xue.nong
 */
public interface BytesStream {

	/**
	 * Underlying bytes.
	 *
	 * @return the byte[]
	 */
	byte[] underlyingBytes();

	/**
	 * Size.
	 *
	 * @return the int
	 */
	int size();

	/**
	 * Copied byte array.
	 *
	 * @return the byte[]
	 */
	byte[] copiedByteArray();
}