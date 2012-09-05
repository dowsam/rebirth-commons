/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons AdapterStreamOutput.java 2012-7-6 10:23:46 l.xue.nong$$
 */

package cn.com.rebirth.commons.io.stream;

import java.io.IOException;

import cn.com.rebirth.commons.BytesHolder;
import cn.com.rebirth.commons.Nullable;
import cn.com.rebirth.commons.io.stream.StreamOutput;



/**
 * The Class AdapterStreamOutput.
 *
 * @author l.xue.nong
 */
public class AdapterStreamOutput extends StreamOutput {

	
	/** The out. */
	protected StreamOutput out;

	
	/**
	 * Instantiates a new adapter stream output.
	 *
	 * @param out the out
	 */
	public AdapterStreamOutput(StreamOutput out) {
		this.out = out;
	}

	
	/**
	 * Reset.
	 *
	 * @param out the out
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void reset(StreamOutput out) throws IOException {
		this.out = out;
	}

	
	/**
	 * Wrapped out.
	 *
	 * @return the stream output
	 */
	public StreamOutput wrappedOut() {
		return this.out;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.StreamOutput#writeByte(byte)
	 */
	@Override
	public void writeByte(byte b) throws IOException {
		out.writeByte(b);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.StreamOutput#writeBytes(byte[], int, int)
	 */
	@Override
	public void writeBytes(byte[] b, int offset, int length) throws IOException {
		out.writeBytes(b, offset, length);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.StreamOutput#flush()
	 */
	@Override
	public void flush() throws IOException {
		out.flush();
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.StreamOutput#close()
	 */
	@Override
	public void close() throws IOException {
		out.close();
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.StreamOutput#reset()
	 */
	@Override
	public void reset() throws IOException {
		out.reset();
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.StreamOutput#writeBytes(byte[])
	 */
	@Override
	public void writeBytes(byte[] b) throws IOException {
		out.writeBytes(b);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.StreamOutput#writeBytes(byte[], int)
	 */
	@Override
	public void writeBytes(byte[] b, int length) throws IOException {
		out.writeBytes(b, length);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.StreamOutput#writeBytesHolder(byte[], int, int)
	 */
	@Override
	public void writeBytesHolder(byte[] bytes, int offset, int length) throws IOException {
		out.writeBytesHolder(bytes, offset, length);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.StreamOutput#writeBytesHolder(cn.com.rebirth.search.commons.BytesHolder)
	 */
	@Override
	public void writeBytesHolder(@Nullable BytesHolder bytes) throws IOException {
		out.writeBytesHolder(bytes);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.StreamOutput#writeInt(int)
	 */
	@Override
	public void writeInt(int i) throws IOException {
		out.writeInt(i);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.StreamOutput#writeVInt(int)
	 */
	@Override
	public void writeVInt(int i) throws IOException {
		out.writeVInt(i);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.StreamOutput#writeLong(long)
	 */
	@Override
	public void writeLong(long i) throws IOException {
		out.writeLong(i);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.StreamOutput#writeVLong(long)
	 */
	@Override
	public void writeVLong(long i) throws IOException {
		out.writeVLong(i);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.StreamOutput#writeUTF(java.lang.String)
	 */
	@Override
	public void writeUTF(String str) throws IOException {
		out.writeUTF(str);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.StreamOutput#writeFloat(float)
	 */
	@Override
	public void writeFloat(float v) throws IOException {
		out.writeFloat(v);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.StreamOutput#writeDouble(double)
	 */
	@Override
	public void writeDouble(double v) throws IOException {
		out.writeDouble(v);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.StreamOutput#writeBoolean(boolean)
	 */
	@Override
	public void writeBoolean(boolean b) throws IOException {
		out.writeBoolean(b);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.StreamOutput#write(int)
	 */
	@Override
	public void write(int b) throws IOException {
		out.write(b);
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.io.stream.StreamOutput#write(byte[], int, int)
	 */
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		out.write(b, off, len);
	}

	
	/* (non-Javadoc)
	 * @see java.io.OutputStream#write(byte[])
	 */
	@Override
	public void write(byte[] b) throws IOException {
		out.write(b);
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return out.toString();
	}
}
