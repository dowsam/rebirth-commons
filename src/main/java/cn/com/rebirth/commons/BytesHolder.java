/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons BytesHolder.java 2012-7-6 10:22:16 l.xue.nong$$
 */
package cn.com.rebirth.commons;

import java.util.Arrays;

/**
 * The Class BytesHolder.
 *
 * @author l.xue.nong
 */
public class BytesHolder {

	/** The Constant EMPTY. */
	public static final BytesHolder EMPTY = new BytesHolder(Bytes.EMPTY_ARRAY, 0, 0);

	/** The bytes. */
	private byte[] bytes;

	/** The offset. */
	private int offset;

	/** The length. */
	private int length;

	/**
	 * Instantiates a new bytes holder.
	 */
	BytesHolder() {

	}

	/**
	 * Instantiates a new bytes holder.
	 *
	 * @param bytes the bytes
	 */
	public BytesHolder(byte[] bytes) {
		this.bytes = bytes;
		this.offset = 0;
		this.length = bytes.length;
	}

	/**
	 * Instantiates a new bytes holder.
	 *
	 * @param bytes the bytes
	 * @param offset the offset
	 * @param length the length
	 */
	public BytesHolder(byte[] bytes, int offset, int length) {
		this.bytes = bytes;
		this.offset = offset;
		this.length = length;
	}

	/**
	 * Copy bytes.
	 *
	 * @return the byte[]
	 */
	public byte[] copyBytes() {
		return Arrays.copyOfRange(bytes, offset, offset + length);
	}

	/**
	 * Bytes.
	 *
	 * @return the byte[]
	 */
	public byte[] bytes() {
		return bytes;
	}

	/**
	 * Offset.
	 *
	 * @return the int
	 */
	public int offset() {
		return offset;
	}

	/**
	 * Length.
	 *
	 * @return the int
	 */
	public int length() {
		return length;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return bytesEquals((BytesHolder) obj);
	}

	/**
	 * Bytes equals.
	 *
	 * @param other the other
	 * @return true, if successful
	 */
	public boolean bytesEquals(BytesHolder other) {
		if (length == other.length) {
			int otherUpto = other.offset;
			final byte[] otherBytes = other.bytes;
			final int end = offset + length;
			for (int upto = offset; upto < end; upto++, otherUpto++) {
				if (bytes[upto] != otherBytes[otherUpto]) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = 0;
		final int end = offset + length;
		for (int i = offset; i < end; i++) {
			result = 31 * result + bytes[i];
		}
		return result;
	}
}