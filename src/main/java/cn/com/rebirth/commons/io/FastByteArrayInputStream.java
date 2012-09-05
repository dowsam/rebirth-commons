/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons FastByteArrayInputStream.java 2012-7-6 10:23:41 l.xue.nong$$
 */


package cn.com.rebirth.commons.io;

import java.io.IOException;
import java.io.InputStream;


/**
 * The Class FastByteArrayInputStream.
 *
 * @author l.xue.nong
 */
public class FastByteArrayInputStream extends InputStream {

	
	/** The buf. */
	protected byte buf[];

	
	/** The pos. */
	protected int pos;

	
	/** The mark. */
	protected int mark = 0;

	
	/** The count. */
	protected int count;

	
	/**
	 * Instantiates a new fast byte array input stream.
	 *
	 * @param buf the buf
	 */
	public FastByteArrayInputStream(byte buf[]) {
		this.buf = buf;
		this.pos = 0;
		this.count = buf.length;
	}

	
	/**
	 * Instantiates a new fast byte array input stream.
	 *
	 * @param buf the buf
	 * @param offset the offset
	 * @param length the length
	 */
	public FastByteArrayInputStream(byte buf[], int offset, int length) {
		this.buf = buf;
		this.pos = offset;
		this.count = Math.min(offset + length, buf.length);
		this.mark = offset;
	}

	
	/* (non-Javadoc)
	 * @see java.io.InputStream#read()
	 */
	public int read() {
		return (pos < count) ? (buf[pos++] & 0xff) : -1;
	}

	
	/* (non-Javadoc)
	 * @see java.io.InputStream#read(byte[], int, int)
	 */
	public int read(byte b[], int off, int len) {
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

	
	/* (non-Javadoc)
	 * @see java.io.InputStream#skip(long)
	 */
	public long skip(long n) {
		if (pos + n > count) {
			n = count - pos;
		}
		if (n < 0) {
			return 0;
		}
		pos += n;
		return n;
	}

	
	/* (non-Javadoc)
	 * @see java.io.InputStream#available()
	 */
	public int available() {
		return count - pos;
	}

	
	/**
	 * Position.
	 *
	 * @return the int
	 */
	public int position() {
		return pos;
	}

	
	/* (non-Javadoc)
	 * @see java.io.InputStream#markSupported()
	 */
	public boolean markSupported() {
		return true;
	}

	
	/* (non-Javadoc)
	 * @see java.io.InputStream#mark(int)
	 */
	public void mark(int readAheadLimit) {
		mark = pos;
	}

	
	/* (non-Javadoc)
	 * @see java.io.InputStream#reset()
	 */
	public void reset() {
		pos = mark;
	}

	
	/* (non-Javadoc)
	 * @see java.io.InputStream#close()
	 */
	public void close() throws IOException {
	}
}
