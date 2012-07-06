/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons AdapterStreamInput.java 2012-7-6 10:36:10 l.xue.nong$$
 */

package cn.com.rebirth.commons.io.stream;

import java.io.IOException;

import cn.com.rebirth.commons.BytesHolder;




/**
 * The Class AdapterStreamInput.
 *
 * @author l.xue.nong
 */
public abstract class AdapterStreamInput extends StreamInput {

	
	/** The in. */
	protected StreamInput in;

	
	/**
	 * Instantiates a new adapter stream input.
	 */
	protected AdapterStreamInput() {
	}

	
	/**
	 * Instantiates a new adapter stream input.
	 *
	 * @param in the in
	 */
	public AdapterStreamInput(StreamInput in) {
		this.in = in;
	}

	
	/**
	 * Reset.
	 *
	 * @param in the in
	 */
	public void reset(StreamInput in) {
		this.in = in;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.commons.io.stream.StreamInput#readByte()
	 */
	@Override
	public byte readByte() throws IOException {
		return in.readByte();
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.commons.io.stream.StreamInput#readBytes(byte[], int, int)
	 */
	@Override
	public void readBytes(byte[] b, int offset, int len) throws IOException {
		in.readBytes(b, offset, len);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.commons.io.stream.StreamInput#readBytesReference()
	 */
	@Override
	public BytesHolder readBytesReference() throws IOException {
		return in.readBytesReference();
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.commons.io.stream.StreamInput#reset()
	 */
	@Override
	public void reset() throws IOException {
		in.reset();
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.commons.io.stream.StreamInput#close()
	 */
	@Override
	public void close() throws IOException {
		in.close();
	}

	
	/* (non-Javadoc)
	 * @see java.io.InputStream#read()
	 */
	@Override
	public int read() throws IOException {
		return in.read();
	}

	

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.commons.io.stream.StreamInput#readFully(byte[])
	 */
	@Override
	public void readFully(byte[] b) throws IOException {
		in.readFully(b);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.commons.io.stream.StreamInput#readShort()
	 */
	@Override
	public short readShort() throws IOException {
		return in.readShort();
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.commons.io.stream.StreamInput#readInt()
	 */
	@Override
	public int readInt() throws IOException {
		return in.readInt();
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.commons.io.stream.StreamInput#readVInt()
	 */
	@Override
	public int readVInt() throws IOException {
		return in.readVInt();
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.commons.io.stream.StreamInput#readLong()
	 */
	@Override
	public long readLong() throws IOException {
		return in.readLong();
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.commons.io.stream.StreamInput#readVLong()
	 */
	@Override
	public long readVLong() throws IOException {
		return in.readVLong();
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.commons.io.stream.StreamInput#readUTF()
	 */
	@Override
	public String readUTF() throws IOException {
		return in.readUTF();
	}

	
	/* (non-Javadoc)
	 * @see java.io.InputStream#read(byte[])
	 */
	@Override
	public int read(byte[] b) throws IOException {
		return in.read(b);
	}

	
	/* (non-Javadoc)
	 * @see java.io.InputStream#read(byte[], int, int)
	 */
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return in.read(b, off, len);
	}

	
	/* (non-Javadoc)
	 * @see java.io.InputStream#skip(long)
	 */
	@Override
	public long skip(long n) throws IOException {
		return in.skip(n);
	}

	
	/* (non-Javadoc)
	 * @see java.io.InputStream#available()
	 */
	@Override
	public int available() throws IOException {
		return in.available();
	}

	
	/* (non-Javadoc)
	 * @see java.io.InputStream#mark(int)
	 */
	@Override
	public void mark(int readlimit) {
		in.mark(readlimit);
	}

	
	/* (non-Javadoc)
	 * @see java.io.InputStream#markSupported()
	 */
	@Override
	public boolean markSupported() {
		return in.markSupported();
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return in.toString();
	}
}
