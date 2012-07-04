/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons ByteSizeValue.java 2012-3-29 15:15:09 l.xue.nong$$
 */

package cn.com.restart.commons.unit;

import java.io.IOException;
import java.io.Serializable;

import cn.com.restart.commons.Strings;
import cn.com.restart.commons.exception.RestartParseException;
import cn.com.restart.commons.io.stream.StreamInput;
import cn.com.restart.commons.io.stream.StreamOutput;
import cn.com.restart.commons.io.stream.Streamable;

/**
 * The Class ByteSizeValue.
 *
 * @author l.xue.nong
 */
public class ByteSizeValue implements Serializable, Streamable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7387252395104564815L;

	/** The size. */
	private long size;

	/** The size unit. */
	private ByteSizeUnit sizeUnit;

	/**
	 * Instantiates a new byte size value.
	 */
	private ByteSizeValue() {

	}

	/**
	 * Instantiates a new byte size value.
	 *
	 * @param bytes the bytes
	 */
	public ByteSizeValue(long bytes) {
		this(bytes, ByteSizeUnit.BYTES);
	}

	/**
	 * Instantiates a new byte size value.
	 *
	 * @param size the size
	 * @param sizeUnit the size unit
	 */
	public ByteSizeValue(long size, ByteSizeUnit sizeUnit) {
		this.size = size;
		this.sizeUnit = sizeUnit;
	}

	/**
	 * Bytes.
	 *
	 * @return the long
	 */
	public long bytes() {
		return sizeUnit.toBytes(size);
	}

	/**
	 * Gets the bytes.
	 *
	 * @return the bytes
	 */
	public long getBytes() {
		return bytes();
	}

	/**
	 * Kb.
	 *
	 * @return the long
	 */
	public long kb() {
		return sizeUnit.toKB(size);
	}

	/**
	 * Gets the kb.
	 *
	 * @return the kb
	 */
	public long getKb() {
		return kb();
	}

	/**
	 * Mb.
	 *
	 * @return the long
	 */
	public long mb() {
		return sizeUnit.toMB(size);
	}

	/**
	 * Gets the mb.
	 *
	 * @return the mb
	 */
	public long getMb() {
		return mb();
	}

	/**
	 * Gb.
	 *
	 * @return the long
	 */
	public long gb() {
		return sizeUnit.toGB(size);
	}

	/**
	 * Gets the gb.
	 *
	 * @return the gb
	 */
	public long getGb() {
		return gb();
	}

	/**
	 * Kb frac.
	 *
	 * @return the double
	 */
	public double kbFrac() {
		return ((double) bytes()) / ByteSizeUnit.C1;
	}

	/**
	 * Gets the kb frac.
	 *
	 * @return the kb frac
	 */
	public double getKbFrac() {
		return kbFrac();
	}

	/**
	 * Mb frac.
	 *
	 * @return the double
	 */
	public double mbFrac() {
		return ((double) bytes()) / ByteSizeUnit.C2;
	}

	/**
	 * Gets the mb frac.
	 *
	 * @return the mb frac
	 */
	public double getMbFrac() {
		return mbFrac();
	}

	/**
	 * Gb frac.
	 *
	 * @return the double
	 */
	public double gbFrac() {
		return ((double) bytes()) / ByteSizeUnit.C3;
	}

	/**
	 * Gets the gb frac.
	 *
	 * @return the gb frac
	 */
	public double getGbFrac() {
		return gbFrac();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		long bytes = bytes();
		double value = bytes;
		String suffix = "b";
		if (bytes >= ByteSizeUnit.C3) {
			value = gbFrac();
			suffix = "gb";
		} else if (bytes >= ByteSizeUnit.C2) {
			value = mbFrac();
			suffix = "mb";
		} else if (bytes >= ByteSizeUnit.C1) {
			value = kbFrac();
			suffix = "kb";
		}
		return Strings.format1Decimals(value, suffix);
	}

	/**
	 * Parses the bytes size value.
	 *
	 * @param sValue the s value
	 * @return the byte size value
	 * @throws RestartParseException the sum mall search parse exception
	 */
	public static ByteSizeValue parseBytesSizeValue(String sValue) throws RestartParseException {
		return ByteSizeValue.parseBytesSizeValue(sValue, null);
	}

	/**
	 * Parses the bytes size value.
	 *
	 * @param sValue the s value
	 * @param defaultValue the default value
	 * @return the byte size value
	 * @throws RestartParseException the sum mall search parse exception
	 */
	public static ByteSizeValue parseBytesSizeValue(String sValue, ByteSizeValue defaultValue)
			throws RestartParseException {
		if (sValue == null) {
			return defaultValue;
		}
		long bytes;
		try {
			if (sValue.endsWith("k") || sValue.endsWith("K")) {
				bytes = (long) (Double.parseDouble(sValue.substring(0, sValue.length() - 1)) * ByteSizeUnit.C1);
			} else if (sValue.endsWith("kb")) {
				bytes = (long) (Double.parseDouble(sValue.substring(0, sValue.length() - 2)) * ByteSizeUnit.C1);
			} else if (sValue.endsWith("m") || sValue.endsWith("M")) {
				bytes = (long) (Double.parseDouble(sValue.substring(0, sValue.length() - 1)) * ByteSizeUnit.C2);
			} else if (sValue.endsWith("mb")) {
				bytes = (long) (Double.parseDouble(sValue.substring(0, sValue.length() - 2)) * ByteSizeUnit.C2);
			} else if (sValue.endsWith("g") || sValue.endsWith("G")) {
				bytes = (long) (Double.parseDouble(sValue.substring(0, sValue.length() - 1)) * ByteSizeUnit.C3);
			} else if (sValue.endsWith("gb")) {
				bytes = (long) (Double.parseDouble(sValue.substring(0, sValue.length() - 2)) * ByteSizeUnit.C3);
			} else if (sValue.endsWith("b")) {
				bytes = Long.parseLong(sValue.substring(0, sValue.length() - 1));
			} else {
				bytes = Long.parseLong(sValue);
			}
		} catch (NumberFormatException e) {
			throw new RestartParseException("Failed to parse [" + sValue + "]", e);
		}
		return new ByteSizeValue(bytes, ByteSizeUnit.BYTES);
	}

	/**
	 * Read bytes size value.
	 *
	 * @param in the in
	 * @return the byte size value
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static ByteSizeValue readBytesSizeValue(StreamInput in) throws IOException {
		ByteSizeValue sizeValue = new ByteSizeValue();
		sizeValue.readFrom(in);
		return sizeValue;
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.search.commons.io.stream.Streamable#readFrom(cn.com.summall.search.commons.io.stream.StreamInput)
	 */
	@Override
	public void readFrom(StreamInput in) throws IOException {
		size = in.readVLong();
		sizeUnit = ByteSizeUnit.BYTES;
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.search.commons.io.stream.Streamable#writeTo(cn.com.summall.search.commons.io.stream.StreamOutput)
	 */
	@Override
	public void writeTo(StreamOutput out) throws IOException {
		out.writeVLong(bytes());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		ByteSizeValue sizeValue = (ByteSizeValue) o;

		if (size != sizeValue.size)
			return false;
		if (sizeUnit != sizeValue.sizeUnit)
			return false;

		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = (int) (size ^ (size >>> 32));
		result = 31 * result + (sizeUnit != null ? sizeUnit.hashCode() : 0);
		return result;
	}
}