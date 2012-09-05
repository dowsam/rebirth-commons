/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons BytesStreamOutput.java 2012-7-6 10:23:40 l.xue.nong$$
 */

package cn.com.rebirth.commons.io.stream;

import java.io.IOException;
import java.util.Arrays;

import cn.com.rebirth.commons.io.BytesStream;
import cn.com.rebirth.commons.io.stream.StreamOutput;

/**
 * The Class BytesStreamOutput.
 *
 * @author l.xue.nong
 */
public class BytesStreamOutput extends StreamOutput implements BytesStream {

	/** The buf. */
	protected byte buf[];

	/** The count. */
	protected int count;

	/**
	 * Instantiates a new bytes stream output.
	 */
	public BytesStreamOutput() {
		this(126);
	}

	/**
	 * Instantiates a new bytes stream output.
	 *
	 * @param size the size
	 */
	public BytesStreamOutput(int size) {
		this.buf = new byte[size];
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.StreamOutput#writeByte(byte)
	 */
	@Override
	public void writeByte(byte b) throws IOException {
		int newcount = count + 1;
		if (newcount > buf.length) {
			buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newcount));
		}
		buf[count] = b;
		count = newcount;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.StreamOutput#writeBytes(byte[], int, int)
	 */
	@Override
	public void writeBytes(byte[] b, int offset, int length) throws IOException {
		if (length == 0) {
			return;
		}
		int newcount = count + length;
		if (newcount > buf.length) {
			buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newcount));
		}
		System.arraycopy(b, offset, buf, count, length);
		count = newcount;
	}

	/**
	 * Seek.
	 *
	 * @param seekTo the seek to
	 */
	public void seek(int seekTo) {
		count = seekTo;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.StreamOutput#reset()
	 */
	public void reset() {
		count = 0;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.StreamOutput#flush()
	 */
	@Override
	public void flush() throws IOException {

	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.StreamOutput#close()
	 */
	@Override
	public void close() throws IOException {

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
}
