/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons LongStreamable.java 2012-7-6 10:23:47 l.xue.nong$$
 */

package cn.com.rebirth.commons.io.stream;

import java.io.IOException;

import cn.com.rebirth.commons.io.stream.StreamInput;
import cn.com.rebirth.commons.io.stream.StreamOutput;
import cn.com.rebirth.commons.io.stream.Streamable;


/**
 * The Class LongStreamable.
 *
 * @author l.xue.nong
 */
public class LongStreamable implements Streamable {

	
	/** The value. */
	private long value;

	
	/**
	 * Instantiates a new long streamable.
	 */
	public LongStreamable() {
	}

	
	/**
	 * Instantiates a new long streamable.
	 *
	 * @param value the value
	 */
	public LongStreamable(long value) {
		this.value = value;
	}

	
	/**
	 * Sets the.
	 *
	 * @param newValue the new value
	 */
	public void set(long newValue) {
		value = newValue;
	}

	
	/**
	 * Gets the.
	 *
	 * @return the long
	 */
	public long get() {
		return this.value;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.Streamable#readFrom(cn.com.rebirth.search.commons.io.stream.StreamInput)
	 */
	@Override
	public void readFrom(StreamInput in) throws IOException {
		value = in.readLong();
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.Streamable#writeTo(cn.com.rebirth.search.commons.io.stream.StreamOutput)
	 */
	@Override
	public void writeTo(StreamOutput out) throws IOException {
		out.writeLong(value);
	}
}