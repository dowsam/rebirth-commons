/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons UnsafeChunkDecoder.java 2012-3-29 15:15:14 l.xue.nong$$
 */

package cn.com.rebirth.commons.compress.lzf.impl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import sun.misc.Unsafe;
import cn.com.rebirth.commons.compress.lzf.ChunkDecoder;
import cn.com.rebirth.commons.compress.lzf.LZFChunk;

/**
 * The Class UnsafeChunkDecoder.
 *
 * @author l.xue.nong
 */
@SuppressWarnings("restriction")
public class UnsafeChunkDecoder extends ChunkDecoder {

	/** The Constant unsafe. */
	private static final Unsafe unsafe;

	static {
		try {
			Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
			theUnsafe.setAccessible(true);
			unsafe = (Unsafe) theUnsafe.get(null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/** The Constant BYTE_ARRAY_OFFSET. */
	private static final long BYTE_ARRAY_OFFSET = unsafe.arrayBaseOffset(byte[].class);

	/**
	 * Instantiates a new unsafe chunk decoder.
	 */
	public UnsafeChunkDecoder() {
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.search.commons.compress.lzf.ChunkDecoder#decodeChunk(java.io.InputStream, byte[], byte[])
	 */
	@Override
	public final int decodeChunk(final InputStream is, final byte[] inputBuffer, final byte[] outputBuffer)
			throws IOException {
		int bytesInOutput;

		int bytesRead = readHeader(is, inputBuffer);
		if ((bytesRead < HEADER_BYTES) || inputBuffer[0] != LZFChunk.BYTE_Z || inputBuffer[1] != LZFChunk.BYTE_V) {
			if (bytesRead == 0) {
				return -1;
			}
			throw new IOException(
					"Corrupt input data, block did not start with 2 byte signature ('ZV') followed by type byte, 2-byte length)");
		}
		int type = inputBuffer[2];
		int compLen = uint16(inputBuffer, 3);
		if (type == LZFChunk.BLOCK_TYPE_NON_COMPRESSED) {
			readFully(is, false, outputBuffer, 0, compLen);
			bytesInOutput = compLen;
		} else {
			readFully(is, true, inputBuffer, 0, 2 + compLen);
			int uncompLen = uint16(inputBuffer, 0);
			decodeChunk(inputBuffer, 2, outputBuffer, 0, uncompLen);
			bytesInOutput = uncompLen;
		}
		return bytesInOutput;
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.search.commons.compress.lzf.ChunkDecoder#decodeChunk(byte[], int, byte[], int, int)
	 */
	@Override
	public final void decodeChunk(byte[] in, int inPos, byte[] out, int outPos, int outEnd) throws IOException {
		main_loop: do {
			int ctrl = in[inPos++] & 255;
			while (ctrl < LZFChunk.MAX_LITERAL) {
				copyUpTo32(in, inPos, out, outPos, ctrl);
				++ctrl;
				inPos += ctrl;
				outPos += ctrl;
				if (outPos >= outEnd) {
					break main_loop;
				}
				ctrl = in[inPos++] & 255;
			}

			int len = ctrl >> 5;
			ctrl = -((ctrl & 0x1f) << 8) - 1;

			if (len < 7) {
				ctrl -= in[inPos++] & 255;
				if (ctrl < -7) {
					moveLong(out, outPos, outEnd, ctrl);
					outPos += len + 2;
					continue;
				}

				outPos = copyOverlappingShort(out, outPos, ctrl, len);
				continue;
			}

			len = in[inPos++] & 255;
			ctrl -= in[inPos++] & 255;

			if ((ctrl + len) >= -9) {
				outPos = copyOverlappingLong(out, outPos, ctrl, len);
				continue;
			}

			len += 9;
			if (len <= 32) {
				copyUpTo32(out, outPos + ctrl, out, outPos, len - 1);
			} else {
				System.arraycopy(out, outPos + ctrl, out, outPos, len);
			}
			outPos += len;
		} while (outPos < outEnd);

		if (outPos != outEnd)
			throw new IOException("Corrupt data: overrun in decompress, input offset " + inPos + ", output offset "
					+ outPos);
	}

	/**
	 * Copy overlapping short.
	 *
	 * @param out the out
	 * @param outPos the out pos
	 * @param offset the offset
	 * @param len the len
	 * @return the int
	 */
	private final int copyOverlappingShort(final byte[] out, int outPos, final int offset, int len) {
		out[outPos] = out[outPos++ + offset];
		out[outPos] = out[outPos++ + offset];
		switch (len) {
		case 6:
			out[outPos] = out[outPos++ + offset];
		case 5:
			out[outPos] = out[outPos++ + offset];
		case 4:
			out[outPos] = out[outPos++ + offset];
		case 3:
			out[outPos] = out[outPos++ + offset];
		case 2:
			out[outPos] = out[outPos++ + offset];
		case 1:
			out[outPos] = out[outPos++ + offset];
		}
		return outPos;
	}

	/**
	 * Copy overlapping long.
	 *
	 * @param out the out
	 * @param outPos the out pos
	 * @param offset the offset
	 * @param len the len
	 * @return the int
	 */
	private final static int copyOverlappingLong(final byte[] out, int outPos, final int offset, int len) {

		out[outPos] = out[outPos++ + offset];
		out[outPos] = out[outPos++ + offset];
		out[outPos] = out[outPos++ + offset];
		out[outPos] = out[outPos++ + offset];
		out[outPos] = out[outPos++ + offset];
		out[outPos] = out[outPos++ + offset];
		out[outPos] = out[outPos++ + offset];
		out[outPos] = out[outPos++ + offset];
		out[outPos] = out[outPos++ + offset];

		len += outPos;
		final int end = len - 3;
		while (outPos < end) {
			out[outPos] = out[outPos++ + offset];
			out[outPos] = out[outPos++ + offset];
			out[outPos] = out[outPos++ + offset];
			out[outPos] = out[outPos++ + offset];
		}
		switch (len - outPos) {
		case 3:
			out[outPos] = out[outPos++ + offset];
		case 2:
			out[outPos] = out[outPos++ + offset];
		case 1:
			out[outPos] = out[outPos++ + offset];
		}
		return outPos;
	}

	/**
	 * Move long.
	 *
	 * @param data the data
	 * @param resultOffset the result offset
	 * @param dataEnd the data end
	 * @param delta the delta
	 */
	private final static void moveLong(byte[] data, int resultOffset, int dataEnd, int delta) {
		if ((resultOffset + 8) < dataEnd) {
			final long rawOffset = BYTE_ARRAY_OFFSET + resultOffset;
			long value = unsafe.getLong(data, rawOffset + delta);
			unsafe.putLong(data, rawOffset, value);
			return;
		}
		System.arraycopy(data, resultOffset + delta, data, resultOffset, data.length - resultOffset);
	}

	/**
	 * Copy up to32.
	 *
	 * @param in the in
	 * @param inputIndex the input index
	 * @param out the out
	 * @param outputIndex the output index
	 * @param lengthMinusOne the length minus one
	 */
	private final static void copyUpTo32(byte[] in, int inputIndex, byte[] out, int outputIndex, int lengthMinusOne) {
		if ((outputIndex + 32) > out.length) {
			System.arraycopy(in, inputIndex, out, outputIndex, lengthMinusOne + 1);
			return;
		}
		long inPtr = BYTE_ARRAY_OFFSET + inputIndex;
		long outPtr = BYTE_ARRAY_OFFSET + outputIndex;

		switch (lengthMinusOne >>> 3) {
		case 3: {
			long value = unsafe.getLong(in, inPtr);
			unsafe.putLong(out, outPtr, value);
			inPtr += 8;
			outPtr += 8;
			value = unsafe.getLong(in, inPtr);
			unsafe.putLong(out, outPtr, value);
			inPtr += 8;
			outPtr += 8;
			value = unsafe.getLong(in, inPtr);
			unsafe.putLong(out, outPtr, value);
			inPtr += 8;
			outPtr += 8;
			value = unsafe.getLong(in, inPtr);
			unsafe.putLong(out, outPtr, value);
		}
			break;
		case 2: {
			long value = unsafe.getLong(in, inPtr);
			unsafe.putLong(out, outPtr, value);
			inPtr += 8;
			outPtr += 8;
			value = unsafe.getLong(in, inPtr);
			unsafe.putLong(out, outPtr, value);
			inPtr += 8;
			outPtr += 8;
			value = unsafe.getLong(in, inPtr);
			unsafe.putLong(out, outPtr, value);
		}
			break;
		case 1: {
			long value = unsafe.getLong(in, inPtr);
			unsafe.putLong(out, outPtr, value);
			inPtr += 8;
			outPtr += 8;
			value = unsafe.getLong(in, inPtr);
			unsafe.putLong(out, outPtr, value);
		}
			break;
		case 0: {
			long value = unsafe.getLong(in, inPtr);
			unsafe.putLong(out, outPtr, value);
		}
		}
	}
}