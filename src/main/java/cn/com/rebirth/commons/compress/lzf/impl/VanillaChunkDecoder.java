/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons VanillaChunkDecoder.java 2012-7-6 10:35:20 l.xue.nong$$
 */

package cn.com.rebirth.commons.compress.lzf.impl;

import java.io.IOException;
import java.io.InputStream;

import cn.com.rebirth.commons.compress.lzf.ChunkDecoder;
import cn.com.rebirth.commons.compress.lzf.LZFChunk;


/**
 * The Class VanillaChunkDecoder.
 *
 * @author l.xue.nong
 */
public class VanillaChunkDecoder extends ChunkDecoder {
	
	
	/**
	 * Instantiates a new vanilla chunk decoder.
	 */
	public VanillaChunkDecoder() {
	}

	
	/* (non-Javadoc)
	 * @see cn.com.rebirth.commons.compress.lzf.ChunkDecoder#decodeChunk(java.io.InputStream, byte[], byte[])
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
	 * @see cn.com.rebirth.commons.compress.lzf.ChunkDecoder#decodeChunk(byte[], int, byte[], int, int)
	 */
	@Override
	public final void decodeChunk(byte[] in, int inPos, byte[] out, int outPos, int outEnd) throws IOException {
		do {
			int ctrl = in[inPos++] & 255;
			if (ctrl < LZFChunk.MAX_LITERAL) { 
				switch (ctrl) {
				case 31:
					out[outPos++] = in[inPos++];
				case 30:
					out[outPos++] = in[inPos++];
				case 29:
					out[outPos++] = in[inPos++];
				case 28:
					out[outPos++] = in[inPos++];
				case 27:
					out[outPos++] = in[inPos++];
				case 26:
					out[outPos++] = in[inPos++];
				case 25:
					out[outPos++] = in[inPos++];
				case 24:
					out[outPos++] = in[inPos++];
				case 23:
					out[outPos++] = in[inPos++];
				case 22:
					out[outPos++] = in[inPos++];
				case 21:
					out[outPos++] = in[inPos++];
				case 20:
					out[outPos++] = in[inPos++];
				case 19:
					out[outPos++] = in[inPos++];
				case 18:
					out[outPos++] = in[inPos++];
				case 17:
					out[outPos++] = in[inPos++];
				case 16:
					out[outPos++] = in[inPos++];
				case 15:
					out[outPos++] = in[inPos++];
				case 14:
					out[outPos++] = in[inPos++];
				case 13:
					out[outPos++] = in[inPos++];
				case 12:
					out[outPos++] = in[inPos++];
				case 11:
					out[outPos++] = in[inPos++];
				case 10:
					out[outPos++] = in[inPos++];
				case 9:
					out[outPos++] = in[inPos++];
				case 8:
					out[outPos++] = in[inPos++];
				case 7:
					out[outPos++] = in[inPos++];
				case 6:
					out[outPos++] = in[inPos++];
				case 5:
					out[outPos++] = in[inPos++];
				case 4:
					out[outPos++] = in[inPos++];
				case 3:
					out[outPos++] = in[inPos++];
				case 2:
					out[outPos++] = in[inPos++];
				case 1:
					out[outPos++] = in[inPos++];
				case 0:
					out[outPos++] = in[inPos++];
				}
				continue;
			}
			
			int len = ctrl >> 5;
			ctrl = -((ctrl & 0x1f) << 8) - 1;
			if (len < 7) { 
				ctrl -= in[inPos++] & 255;
				out[outPos] = out[outPos++ + ctrl];
				out[outPos] = out[outPos++ + ctrl];
				switch (len) {
				case 6:
					out[outPos] = out[outPos++ + ctrl];
				case 5:
					out[outPos] = out[outPos++ + ctrl];
				case 4:
					out[outPos] = out[outPos++ + ctrl];
				case 3:
					out[outPos] = out[outPos++ + ctrl];
				case 2:
					out[outPos] = out[outPos++ + ctrl];
				case 1:
					out[outPos] = out[outPos++ + ctrl];
				}
				continue;
			}

			
			len = in[inPos++] & 255;
			ctrl -= in[inPos++] & 255;

			
			if ((ctrl + len) < -9) {
				len += 9;
				if (len <= 32) {
					copyUpTo32WithSwitch(out, outPos + ctrl, out, outPos, len - 1);
				} else {
					System.arraycopy(out, outPos + ctrl, out, outPos, len);
				}
				outPos += len;
				continue;
			}

			
			out[outPos] = out[outPos++ + ctrl];
			out[outPos] = out[outPos++ + ctrl];
			out[outPos] = out[outPos++ + ctrl];
			out[outPos] = out[outPos++ + ctrl];
			out[outPos] = out[outPos++ + ctrl];
			out[outPos] = out[outPos++ + ctrl];
			out[outPos] = out[outPos++ + ctrl];
			out[outPos] = out[outPos++ + ctrl];
			out[outPos] = out[outPos++ + ctrl];

			
			
			
			
			len += outPos;
			final int end = len - 3;
			while (outPos < end) {
				out[outPos] = out[outPos++ + ctrl];
				out[outPos] = out[outPos++ + ctrl];
				out[outPos] = out[outPos++ + ctrl];
				out[outPos] = out[outPos++ + ctrl];
			}
			switch (len - outPos) {
			case 3:
				out[outPos] = out[outPos++ + ctrl];
			case 2:
				out[outPos] = out[outPos++ + ctrl];
			case 1:
				out[outPos] = out[outPos++ + ctrl];
			}
		} while (outPos < outEnd);

		
		if (outPos != outEnd)
			throw new IOException("Corrupt data: overrun in decompress, input offset " + inPos + ", output offset "
					+ outPos);
	}

	

	
	/**
	 * Copy up to32 with switch.
	 *
	 * @param in the in
	 * @param inPos the in pos
	 * @param out the out
	 * @param outPos the out pos
	 * @param lengthMinusOne the length minus one
	 */
	protected static final void copyUpTo32WithSwitch(byte[] in, int inPos, byte[] out, int outPos, int lengthMinusOne) {
		switch (lengthMinusOne) {
		case 31:
			out[outPos++] = in[inPos++];
		case 30:
			out[outPos++] = in[inPos++];
		case 29:
			out[outPos++] = in[inPos++];
		case 28:
			out[outPos++] = in[inPos++];
		case 27:
			out[outPos++] = in[inPos++];
		case 26:
			out[outPos++] = in[inPos++];
		case 25:
			out[outPos++] = in[inPos++];
		case 24:
			out[outPos++] = in[inPos++];
		case 23:
			out[outPos++] = in[inPos++];
		case 22:
			out[outPos++] = in[inPos++];
		case 21:
			out[outPos++] = in[inPos++];
		case 20:
			out[outPos++] = in[inPos++];
		case 19:
			out[outPos++] = in[inPos++];
		case 18:
			out[outPos++] = in[inPos++];
		case 17:
			out[outPos++] = in[inPos++];
		case 16:
			out[outPos++] = in[inPos++];
		case 15:
			out[outPos++] = in[inPos++];
		case 14:
			out[outPos++] = in[inPos++];
		case 13:
			out[outPos++] = in[inPos++];
		case 12:
			out[outPos++] = in[inPos++];
		case 11:
			out[outPos++] = in[inPos++];
		case 10:
			out[outPos++] = in[inPos++];
		case 9:
			out[outPos++] = in[inPos++];
		case 8:
			out[outPos++] = in[inPos++];
		case 7:
			out[outPos++] = in[inPos++];
		case 6:
			out[outPos++] = in[inPos++];
		case 5:
			out[outPos++] = in[inPos++];
		case 4:
			out[outPos++] = in[inPos++];
		case 3:
			out[outPos++] = in[inPos++];
		case 2:
			out[outPos++] = in[inPos++];
		case 1:
			out[outPos++] = in[inPos++];
		case 0:
			out[outPos++] = in[inPos++];
		}
	}

}
