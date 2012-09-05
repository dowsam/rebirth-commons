/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons StringStreamable.java 2012-7-6 10:23:44 l.xue.nong$$
 */


package cn.com.rebirth.commons.io.stream;

import java.io.IOException;

import cn.com.rebirth.commons.io.stream.StreamInput;
import cn.com.rebirth.commons.io.stream.StreamOutput;
import cn.com.rebirth.commons.io.stream.Streamable;


/**
 * The Class StringStreamable.
 *
 * @author l.xue.nong
 */
public class StringStreamable implements Streamable {

	
	/** The value. */
	private String value;

	
	/**
	 * Instantiates a new string streamable.
	 */
	public StringStreamable() {
	}

	
	/**
	 * Instantiates a new string streamable.
	 *
	 * @param value the value
	 */
	public StringStreamable(String value) {
		this.value = value;
	}

	
	/**
	 * Sets the.
	 *
	 * @param newValue the new value
	 */
	public void set(String newValue) {
		value = newValue;
	}

	
	/**
	 * Gets the.
	 *
	 * @return the string
	 */
	public String get() {
		return this.value;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.Streamable#readFrom(cn.com.rebirth.search.commons.io.stream.StreamInput)
	 */
	@Override
	public void readFrom(StreamInput in) throws IOException {
		value = in.readUTF();
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.Streamable#writeTo(cn.com.rebirth.search.commons.io.stream.StreamOutput)
	 */
	@Override
	public void writeTo(StreamOutput out) throws IOException {
		out.writeUTF(value);
	}
}