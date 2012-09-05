/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons FastByteArrayOutputStream.java 2012-7-6 10:23:51 l.xue.nong$$
 */
package cn.com.rebirth.commons.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;


/**
 * The Class FastByteArrayOutputStream.
 *
 * @author l.xue.nong
 */
public class FastByteArrayOutputStream extends OutputStream implements BytesStream {

	
	/** The buf. */
	protected byte buf[];

	
	/** The count. */
	protected int count;

	
	/**
	 * Instantiates a new fast byte array output stream.
	 */
	public FastByteArrayOutputStream() {
		this(1024);
	}

	
	/**
	 * Instantiates a new fast byte array output stream.
	 *
	 * @param size the size
	 */
	public FastByteArrayOutputStream(int size) {
		if (size < 0) {
			throw new IllegalArgumentException("Negative initial size: " + size);
		}
		buf = new byte[size];
	}

	
	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(int)
	 */
	public void write(int b) {
		int newcount = count + 1;
		if (newcount > buf.length) {
			buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newcount));
		}
		buf[count] = (byte) b;
		count = newcount;
	}

	
	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(byte[], int, int)
	 */
	public void write(byte b[], int off, int len) {
		if (len == 0) {
			return;
		}
		int newcount = count + len;
		if (newcount > buf.length) {
			buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newcount));
		}
		System.arraycopy(b, off, buf, count, len);
		count = newcount;
	}

	
	/**
	 * Write to.
	 *
	 * @param out the out
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeTo(OutputStream out) throws IOException {
		out.write(buf, 0, count);
	}

	
	/**
	 * Reset.
	 */
	public void reset() {
		count = 0;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.BytesStream#copiedByteArray()
	 */
	public byte copiedByteArray()[] {
		return Arrays.copyOf(buf, count);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.BytesStream#underlyingBytes()
	 */
	public byte[] underlyingBytes() {
		return buf;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.BytesStream#size()
	 */
	public int size() {
		return count;
	}

	
	/**
	 * Seek.
	 *
	 * @param position the position
	 */
	public void seek(int position) {
		this.count = position;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new String(buf, 0, count);
	}

	
	/**
	 * To string.
	 *
	 * @param charsetName the charset name
	 * @return the string
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public String toString(String charsetName) throws UnsupportedEncodingException {
		return new String(buf, 0, count, charsetName);
	}

	
	/* (non-Javadoc)
	 * @see java.io.OutputStream#close()
	 */
	public void close() throws IOException {
	}
}
