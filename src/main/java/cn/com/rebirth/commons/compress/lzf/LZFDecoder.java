/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons LZFDecoder.java 2012-7-6 10:22:17 l.xue.nong$$
 */


package cn.com.rebirth.commons.compress.lzf;

import java.io.IOException;

import cn.com.rebirth.commons.compress.lzf.util.ChunkDecoderFactory;


/**
 * The Class LZFDecoder.
 *
 * @author l.xue.nong
 */
public class LZFDecoder {

	
	/**
	 * Decode.
	 *
	 * @param inputBuffer the input buffer
	 * @return the byte[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static byte[] decode(final byte[] inputBuffer) throws IOException {
		return decode(inputBuffer, 0, inputBuffer.length);
	}

	
	/**
	 * Decode.
	 *
	 * @param inputBuffer the input buffer
	 * @param offset the offset
	 * @param length the length
	 * @return the byte[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static byte[] decode(final byte[] inputBuffer, int offset, int length) throws IOException {
		return ChunkDecoderFactory.optimalInstance().decode(inputBuffer, offset, length);
	}

	
	/**
	 * Decode.
	 *
	 * @param inputBuffer the input buffer
	 * @param targetBuffer the target buffer
	 * @return the int
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static int decode(final byte[] inputBuffer, final byte[] targetBuffer) throws IOException {
		return decode(inputBuffer, 0, inputBuffer.length, targetBuffer);
	}

	
	/**
	 * Decode.
	 *
	 * @param sourceBuffer the source buffer
	 * @param offset the offset
	 * @param length the length
	 * @param targetBuffer the target buffer
	 * @return the int
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static int decode(final byte[] sourceBuffer, int offset, int length, final byte[] targetBuffer)
			throws IOException {
		return ChunkDecoderFactory.optimalInstance().decode(sourceBuffer, offset, length, targetBuffer);
	}

	
	/**
	 * Calculate uncompressed size.
	 *
	 * @param data the data
	 * @param offset the offset
	 * @param length the length
	 * @return the int
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static int calculateUncompressedSize(byte[] data, int offset, int length) throws IOException {
		return ChunkDecoder.calculateUncompressedSize(data, offset, length);
	}
}
