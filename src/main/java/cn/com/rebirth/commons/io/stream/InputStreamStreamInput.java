/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons InputStreamStreamInput.java 2012-7-6 10:23:52 l.xue.nong$$
 */

package cn.com.rebirth.commons.io.stream;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import cn.com.rebirth.commons.io.stream.StreamInput;


/**
 * The Class InputStreamStreamInput.
 *
 * @author l.xue.nong
 */
public class InputStreamStreamInput extends StreamInput {

	
	/** The is. */
	private final InputStream is;

	
	/**
	 * Instantiates a new input stream stream input.
	 *
	 * @param is the is
	 */
	public InputStreamStreamInput(InputStream is) {
		this.is = is;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.StreamInput#readByte()
	 */
	@Override
	public byte readByte() throws IOException {
		int ch = is.read();
		if (ch < 0)
			throw new EOFException();
		return (byte) (ch);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.StreamInput#readBytes(byte[], int, int)
	 */
	@Override
	public void readBytes(byte[] b, int offset, int len) throws IOException {
		if (len < 0)
			throw new IndexOutOfBoundsException();
		int n = 0;
		while (n < len) {
			int count = is.read(b, offset + n, len - n);
			if (count < 0)
				throw new EOFException();
			n += count;
		}
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.StreamInput#reset()
	 */
	@Override
	public void reset() throws IOException {
		is.reset();
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.StreamInput#close()
	 */
	@Override
	public void close() throws IOException {
		is.close();
	}

	
	/* (non-Javadoc)
	 * @see java.io.InputStream#read()
	 */
	@Override
	public int read() throws IOException {
		return is.read();
	}

	
	/* (non-Javadoc)
	 * @see java.io.InputStream#read(byte[])
	 */
	@Override
	public int read(byte[] b) throws IOException {
		return is.read(b);
	}

	
	/* (non-Javadoc)
	 * @see java.io.InputStream#read(byte[], int, int)
	 */
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return is.read(b, off, len);
	}

	
	/* (non-Javadoc)
	 * @see java.io.InputStream#skip(long)
	 */
	@Override
	public long skip(long n) throws IOException {
		return is.skip(n);
	}
}
