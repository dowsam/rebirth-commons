/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons ChunkDecoder.java 2012-3-29 15:15:15 l.xue.nong$$
 */

package cn.com.rebirth.commons.compress.lzf;

import java.io.IOException;
import java.io.InputStream;


/**
 * The Class ChunkDecoder.
 *
 * @author l.xue.nong
 */
public abstract class ChunkDecoder {

	
	/** The Constant BYTE_NULL. */
	protected final static byte BYTE_NULL = 0;

	
	/** The Constant HEADER_BYTES. */
	protected final static int HEADER_BYTES = 5;

	
	/**
	 * Instantiates a new chunk decoder.
	 */
	public ChunkDecoder() {
	}

	
	/**
	 * Decode.
	 *
	 * @param inputBuffer the input buffer
	 * @return the byte[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public final byte[] decode(final byte[] inputBuffer) throws IOException {
		byte[] result = new byte[calculateUncompressedSize(inputBuffer, 0, inputBuffer.length)];
		decode(inputBuffer, 0, inputBuffer.length, result);
		return result;
	}

	
	/**
	 * Decode.
	 *
	 * @param inputBuffer the input buffer
	 * @param inputPtr the input ptr
	 * @param inputLen the input len
	 * @return the byte[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public final byte[] decode(final byte[] inputBuffer, int inputPtr, int inputLen) throws IOException {
		byte[] result = new byte[calculateUncompressedSize(inputBuffer, inputPtr, inputLen)];
		decode(inputBuffer, inputPtr, inputLen, result);
		return result;
	}

	
	/**
	 * Decode.
	 *
	 * @param inputBuffer the input buffer
	 * @param targetBuffer the target buffer
	 * @return the int
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public final int decode(final byte[] inputBuffer, final byte[] targetBuffer) throws IOException {
		return decode(inputBuffer, 0, inputBuffer.length, targetBuffer);
	}

	
	/**
	 * Decode.
	 *
	 * @param sourceBuffer the source buffer
	 * @param inPtr the in ptr
	 * @param inLength the in length
	 * @param targetBuffer the target buffer
	 * @return the int
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public int decode(final byte[] sourceBuffer, int inPtr, int inLength, final byte[] targetBuffer) throws IOException {
		byte[] result = targetBuffer;
		int outPtr = 0;
		int blockNr = 0;

		final int end = inPtr + inLength - 1; 

		while (inPtr < end) {
			
			if (sourceBuffer[inPtr] != LZFChunk.BYTE_Z || sourceBuffer[inPtr + 1] != LZFChunk.BYTE_V) {
				throw new IOException("Corrupt input data, block #" + blockNr + " (at offset " + inPtr
						+ "): did not start with 'ZV' signature bytes");
			}
			inPtr += 2;
			int type = sourceBuffer[inPtr++];
			int len = uint16(sourceBuffer, inPtr);
			inPtr += 2;
			if (type == LZFChunk.BLOCK_TYPE_NON_COMPRESSED) { 
				System.arraycopy(sourceBuffer, inPtr, result, outPtr, len);
				outPtr += len;
			} else { 
				int uncompLen = uint16(sourceBuffer, inPtr);
				inPtr += 2;
				decodeChunk(sourceBuffer, inPtr, result, outPtr, outPtr + uncompLen);
				outPtr += uncompLen;
			}
			inPtr += len;
			++blockNr;
		}
		return outPtr;
	}

	
	/**
	 * Decode chunk.
	 *
	 * @param is the is
	 * @param inputBuffer the input buffer
	 * @param outputBuffer the output buffer
	 * @return the int
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public abstract int decodeChunk(final InputStream is, final byte[] inputBuffer, final byte[] outputBuffer)
			throws IOException;

	
	/**
	 * Decode chunk.
	 *
	 * @param in the in
	 * @param inPos the in pos
	 * @param out the out
	 * @param outPos the out pos
	 * @param outEnd the out end
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public abstract void decodeChunk(byte[] in, int inPos, byte[] out, int outPos, int outEnd) throws IOException;

	

	
	/**
	 * Calculate uncompressed size.
	 *
	 * @param data the data
	 * @param ptr the ptr
	 * @param length the length
	 * @return the int
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static int calculateUncompressedSize(byte[] data, int ptr, int length) throws IOException {
		int uncompressedSize = 0;
		int blockNr = 0;
		final int end = ptr + length;

		while (ptr < end) {
			
			if (ptr == (data.length + 1) && data[ptr] == BYTE_NULL) {
				++ptr; 
				break;
			}
			
			try {
				if (data[ptr] != LZFChunk.BYTE_Z || data[ptr + 1] != LZFChunk.BYTE_V) {
					throw new IOException("Corrupt input data, block #" + blockNr + " (at offset " + ptr
							+ "): did not start with 'ZV' signature bytes");
				}
				int type = (int) data[ptr + 2];
				int blockLen = uint16(data, ptr + 3);
				if (type == LZFChunk.BLOCK_TYPE_NON_COMPRESSED) { 
					ptr += 5;
					uncompressedSize += blockLen;
				} else if (type == LZFChunk.BLOCK_TYPE_COMPRESSED) { 
					uncompressedSize += uint16(data, ptr + 5);
					ptr += 7;
				} else { 
					throw new IOException("Corrupt input data, block #" + blockNr + " (at offset " + ptr
							+ "): unrecognized block type " + (type & 0xFF));
				}
				ptr += blockLen;
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new IOException("Corrupt input data, block #" + blockNr + " (at offset " + ptr
						+ "): truncated block header");
			}
			++blockNr;
		}
		
		if (ptr != end) {
			throw new IOException("Corrupt input data: block #" + blockNr + " extends " + (data.length - ptr)
					+ " beyond end of input");
		}
		return uncompressedSize;
	}

	

	
	/**
	 * Uint16.
	 *
	 * @param data the data
	 * @param ptr the ptr
	 * @return the int
	 */
	protected final static int uint16(byte[] data, int ptr) {
		return ((data[ptr] & 0xFF) << 8) + (data[ptr + 1] & 0xFF);
	}

	
	/**
	 * Read header.
	 *
	 * @param is the is
	 * @param inputBuffer the input buffer
	 * @return the int
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected final static int readHeader(final InputStream is, final byte[] inputBuffer) throws IOException {
		
		int needed = HEADER_BYTES;
		int count = is.read(inputBuffer, 0, needed);

		if (count == needed) {
			return count;
		}
		if (count <= 0) {
			return 0;
		}

		
		int offset = count;
		needed -= count;

		do {
			count = is.read(inputBuffer, offset, needed);
			if (count <= 0) {
				break;
			}
			offset += count;
			needed -= count;
		} while (needed > 0);
		return offset;
	}

	
	/**
	 * Read fully.
	 *
	 * @param is the is
	 * @param compressed the compressed
	 * @param outputBuffer the output buffer
	 * @param offset the offset
	 * @param len the len
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected final static void readFully(InputStream is, boolean compressed, byte[] outputBuffer, int offset, int len)
			throws IOException {
		int left = len;
		while (left > 0) {
			int count = is.read(outputBuffer, offset, left);
			if (count < 0) { 
				throw new IOException("EOF in " + len + " byte (" + (compressed ? "" : "un")
						+ "compressed) block: could only read " + (len - left) + " bytes");
			}
			offset += count;
			left -= count;
		}
	}
}
