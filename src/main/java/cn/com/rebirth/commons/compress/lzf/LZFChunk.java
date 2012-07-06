/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons LZFChunk.java 2012-7-6 10:22:15 l.xue.nong$$
 */


package cn.com.rebirth.commons.compress.lzf;

import java.io.IOException;
import java.io.OutputStream;


/**
 * The Class LZFChunk.
 *
 * @author l.xue.nong
 */
public class LZFChunk {
	
	/** The Constant MAX_LITERAL. */
	public static final int MAX_LITERAL = 1 << 5; 

	
	
	/** The Constant MAX_CHUNK_LEN. */
	public static final int MAX_CHUNK_LEN = 0xFFFF;

	
	/** The Constant MAX_HEADER_LEN. */
	public static final int MAX_HEADER_LEN = 7;

	
	/** The Constant BYTE_Z. */
	public final static byte BYTE_Z = 'Z';
	
	
	/** The Constant BYTE_V. */
	public final static byte BYTE_V = 'V';

	
	/** The Constant BLOCK_TYPE_NON_COMPRESSED. */
	public final static int BLOCK_TYPE_NON_COMPRESSED = 0;
	
	
	/** The Constant BLOCK_TYPE_COMPRESSED. */
	public final static int BLOCK_TYPE_COMPRESSED = 1;

	
	/** The _data. */
	protected final byte[] _data;
	
	
	/** The _next. */
	protected LZFChunk _next;

	
	/**
	 * Instantiates a new lZF chunk.
	 *
	 * @param data the data
	 */
	private LZFChunk(byte[] data) {
		_data = data;
	}

	
	/**
	 * Creates the compressed.
	 *
	 * @param origLen the orig len
	 * @param encData the enc data
	 * @param encPtr the enc ptr
	 * @param encLen the enc len
	 * @return the lZF chunk
	 */
	public static LZFChunk createCompressed(int origLen, byte[] encData, int encPtr, int encLen) {
		byte[] result = new byte[encLen + 7];
		result[0] = BYTE_Z;
		result[1] = BYTE_V;
		result[2] = BLOCK_TYPE_COMPRESSED;
		result[3] = (byte) (encLen >> 8);
		result[4] = (byte) encLen;
		result[5] = (byte) (origLen >> 8);
		result[6] = (byte) origLen;
		System.arraycopy(encData, encPtr, result, 7, encLen);
		return new LZFChunk(result);
	}

	
	/**
	 * Write compressed header.
	 *
	 * @param origLen the orig len
	 * @param encLen the enc len
	 * @param out the out
	 * @param headerBuffer the header buffer
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeCompressedHeader(int origLen, int encLen, OutputStream out, byte[] headerBuffer)
			throws IOException {
		headerBuffer[0] = BYTE_Z;
		headerBuffer[1] = BYTE_V;
		headerBuffer[2] = BLOCK_TYPE_COMPRESSED;
		headerBuffer[3] = (byte) (encLen >> 8);
		headerBuffer[4] = (byte) encLen;
		headerBuffer[5] = (byte) (origLen >> 8);
		headerBuffer[6] = (byte) origLen;
		out.write(headerBuffer, 0, 7);
	}

	
	/**
	 * Creates the non compressed.
	 *
	 * @param plainData the plain data
	 * @param ptr the ptr
	 * @param len the len
	 * @return the lZF chunk
	 */
	public static LZFChunk createNonCompressed(byte[] plainData, int ptr, int len) {
		byte[] result = new byte[len + 5];
		result[0] = BYTE_Z;
		result[1] = BYTE_V;
		result[2] = BLOCK_TYPE_NON_COMPRESSED;
		result[3] = (byte) (len >> 8);
		result[4] = (byte) len;
		System.arraycopy(plainData, ptr, result, 5, len);
		return new LZFChunk(result);
	}

	
	/**
	 * Write non compressed header.
	 *
	 * @param len the len
	 * @param out the out
	 * @param headerBuffer the header buffer
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeNonCompressedHeader(int len, OutputStream out, byte[] headerBuffer) throws IOException {
		headerBuffer[0] = BYTE_Z;
		headerBuffer[1] = BYTE_V;
		headerBuffer[2] = BLOCK_TYPE_NON_COMPRESSED;
		headerBuffer[3] = (byte) (len >> 8);
		headerBuffer[4] = (byte) len;
		out.write(headerBuffer, 0, 5);
	}

	
	/**
	 * Sets the next.
	 *
	 * @param next the new next
	 */
	public void setNext(LZFChunk next) {
		_next = next;
	}

	
	/**
	 * Next.
	 *
	 * @return the lZF chunk
	 */
	public LZFChunk next() {
		return _next;
	}

	
	/**
	 * Length.
	 *
	 * @return the int
	 */
	public int length() {
		return _data.length;
	}

	
	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public byte[] getData() {
		return _data;
	}

	
	/**
	 * Copy to.
	 *
	 * @param dst the dst
	 * @param ptr the ptr
	 * @return the int
	 */
	public int copyTo(byte[] dst, int ptr) {
		int len = _data.length;
		System.arraycopy(_data, 0, dst, ptr, len);
		return ptr + len;
	}
}
