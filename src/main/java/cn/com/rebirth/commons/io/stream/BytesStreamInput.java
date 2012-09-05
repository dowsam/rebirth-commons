/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons BytesStreamInput.java 2012-7-6 10:23:47 l.xue.nong$$
 */

package cn.com.rebirth.commons.io.stream;

import java.io.EOFException;
import java.io.IOException;

import cn.com.rebirth.commons.BytesHolder;

/**
 * The Class BytesStreamInput.
 *
 * @author l.xue.nong
 */
public class BytesStreamInput extends StreamInput {

	/** The buf. */
	protected byte buf[];

	/** The pos. */
	protected int pos;

	/** The count. */
	protected int count;

	/** The unsafe. */
	private final boolean unsafe;

	/**
	 * Instantiates a new bytes stream input.
	 *
	 * @param buf the buf
	 * @param unsafe the unsafe
	 */
	public BytesStreamInput(byte buf[], boolean unsafe) {
		this(buf, 0, buf.length, unsafe);
	}

	/**
	 * Instantiates a new bytes stream input.
	 *
	 * @param buf the buf
	 * @param offset the offset
	 * @param length the length
	 * @param unsafe the unsafe
	 */
	public BytesStreamInput(byte buf[], int offset, int length, boolean unsafe) {
		this.buf = buf;
		this.pos = offset;
		this.count = Math.min(offset + length, buf.length);
		this.unsafe = unsafe;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.StreamInput#readBytesReference()
	 */
	@Override
	public BytesHolder readBytesReference() throws IOException {
		if (unsafe) {
			return readBytesHolder();
		}
		int size = readVInt();
		BytesHolder bytes = new BytesHolder(buf, pos, size);
		pos += size;
		return bytes;
	}

	/* (non-Javadoc)
	 * @see java.io.InputStream#skip(long)
	 */
	@Override
	public long skip(long n) throws IOException {
		if (pos + n > count) {
			n = count - pos;
		}
		if (n < 0) {
			return 0;
		}
		pos += n;
		return n;
	}

	/**
	 * Position.
	 *
	 * @return the int
	 */
	public int position() {
		return this.pos;
	}

	/* (non-Javadoc)
	 * @see java.io.InputStream#read()
	 */
	@Override
	public int read() throws IOException {
		return (pos < count) ? (buf[pos++] & 0xff) : -1;
	}

	/* (non-Javadoc)
	 * @see java.io.InputStream#read(byte[], int, int)
	 */
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		if (b == null) {
			throw new NullPointerException();
		} else if (off < 0 || len < 0 || len > b.length - off) {
			throw new IndexOutOfBoundsException();
		}
		if (pos >= count) {
			return -1;
		}
		if (pos + len > count) {
			len = count - pos;
		}
		if (len <= 0) {
			return 0;
		}
		System.arraycopy(buf, pos, b, off, len);
		pos += len;
		return len;
	}

	/**
	 * Underlying buffer.
	 *
	 * @return the byte[]
	 */
	public byte[] underlyingBuffer() {
		return buf;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.StreamInput#readByte()
	 */
	@Override
	public byte readByte() throws IOException {
		if (pos >= count) {
			throw new EOFException();
		}
		return buf[pos++];
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.StreamInput#readBytes(byte[], int, int)
	 */
	@Override
	public void readBytes(byte[] b, int offset, int len) throws IOException {
		if (len == 0) {
			return;
		}
		if (pos >= count) {
			throw new EOFException();
		}
		if (pos + len > count) {
			len = count - pos;
		}
		if (len <= 0) {
			throw new EOFException();
		}
		System.arraycopy(buf, pos, b, offset, len);
		pos += len;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.StreamInput#reset()
	 */
	@Override
	public void reset() throws IOException {
		pos = 0;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.StreamInput#close()
	 */
	@Override
	public void close() throws IOException {

	}
}
