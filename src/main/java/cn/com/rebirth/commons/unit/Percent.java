/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons Percent.java 2012-3-29 15:15:20 l.xue.nong$$
 */

package cn.com.rebirth.commons.unit;

import java.io.IOException;
import java.io.Serializable;

import cn.com.rebirth.commons.io.stream.StreamInput;
import cn.com.rebirth.commons.io.stream.StreamOutput;
import cn.com.rebirth.commons.io.stream.Streamable;

/**
 * The Class Percent.
 *
 * @author l.xue.nong
 */
public class Percent implements Streamable, Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2856452383918704857L;

	/** The value. */
	private double value;

	/**
	 * Instantiates a new percent.
	 *
	 * @param value the value
	 */
	public Percent(double value) {
		this.value = value;
	}

	/**
	 * Value.
	 *
	 * @return the double
	 */
	public double value() {
		return value;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return format(value);
	}

	/**
	 * Format.
	 *
	 * @param value the value
	 * @return the string
	 */
	public static String format(double value) {
		String p = String.valueOf(value * 100.0);
		int ix = p.indexOf(".") + 1;
		return p.substring(0, ix) + p.substring(ix, ix + 1) + "%";
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.search.commons.io.stream.Streamable#readFrom(cn.com.summall.search.commons.io.stream.StreamInput)
	 */
	@Override
	public void readFrom(StreamInput in) throws IOException {
		value = in.readDouble();
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.search.commons.io.stream.Streamable#writeTo(cn.com.summall.search.commons.io.stream.StreamOutput)
	 */
	@Override
	public void writeTo(StreamOutput out) throws IOException {
		out.writeDouble(value);
	}
}
