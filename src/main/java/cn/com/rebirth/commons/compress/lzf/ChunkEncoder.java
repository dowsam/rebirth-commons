/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons ChunkEncoder.java 2012-3-29 15:15:20 l.xue.nong$$
 */


package cn.com.rebirth.commons.compress.lzf;

import java.io.IOException;
import java.io.OutputStream;


/**
 * The Class ChunkEncoder.
 *
 * @author l.xue.nong
 */
public class ChunkEncoder {
	
	
	/** The Constant MIN_BLOCK_TO_COMPRESS. */
	private static final int MIN_BLOCK_TO_COMPRESS = 16;

	
	/** The Constant MIN_HASH_SIZE. */
	private static final int MIN_HASH_SIZE = 256;

	
	
	/** The Constant MAX_HASH_SIZE. */
	private static final int MAX_HASH_SIZE = 16384;

	
	/** The Constant MAX_OFF. */
	private static final int MAX_OFF = 1 << 13; 
	
	
	/** The Constant MAX_REF. */
	private static final int MAX_REF = (1 << 8) + (1 << 3); 

	

	
	/** The _recycler. */
	private final BufferRecycler _recycler;

	
	/** The _hash table. */
	private int[] _hashTable;

	
	/** The _hash modulo. */
	private final int _hashModulo;

	
	/** The _encode buffer. */
	private byte[] _encodeBuffer;

	
	/** The _header buffer. */
	private byte[] _headerBuffer;

	
	
	/**
	 * Instantiates a new chunk encoder.
	 *
	 * @param totalLength the total length
	 * @param recycler the recycler
	 */
	public ChunkEncoder(int totalLength, BufferRecycler recycler) {
		int largestChunkLen = Math.max(totalLength, LZFChunk.MAX_CHUNK_LEN);

		int suggestedHashLen = calcHashLen(largestChunkLen);
		_recycler = recycler;
		_hashTable = _recycler.allocEncodingHash(suggestedHashLen);
		_hashModulo = _hashTable.length - 1;
		
		
		int bufferLen = largestChunkLen + ((largestChunkLen + 31) >> 5);
		_encodeBuffer = _recycler.allocEncodingBuffer(bufferLen);
	}

	

	
	/**
	 * Close.
	 */
	public void close() {
		byte[] buf = _encodeBuffer;
		if (buf != null) {
			_encodeBuffer = null;
			_recycler.releaseEncodeBuffer(buf);
		}
		int[] ibuf = _hashTable;
		if (ibuf != null) {
			_hashTable = null;
			_recycler.releaseEncodingHash(ibuf);
		}
	}

	
	/**
	 * Encode chunk.
	 *
	 * @param data the data
	 * @param offset the offset
	 * @param len the len
	 * @return the lZF chunk
	 */
	public LZFChunk encodeChunk(byte[] data, int offset, int len) {
		if (len >= MIN_BLOCK_TO_COMPRESS) {
			
			int compLen = tryCompress(data, offset, offset + len, _encodeBuffer, 0);
			if (compLen < (len - 2)) { 
				return LZFChunk.createCompressed(len, _encodeBuffer, 0, compLen);
			}
		}
		
		return LZFChunk.createNonCompressed(data, offset, len);
	}

	
	/**
	 * Encode and write chunk.
	 *
	 * @param data the data
	 * @param offset the offset
	 * @param len the len
	 * @param out the out
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void encodeAndWriteChunk(byte[] data, int offset, int len, OutputStream out) throws IOException {
		byte[] headerBuf = _headerBuffer;
		if (headerBuf == null) {
			_headerBuffer = headerBuf = new byte[LZFChunk.MAX_HEADER_LEN];
		}
		if (len >= MIN_BLOCK_TO_COMPRESS) {
			
			int compLen = tryCompress(data, offset, offset + len, _encodeBuffer, 0);
			if (compLen < (len - 2)) { 
				LZFChunk.writeCompressedHeader(len, compLen, out, headerBuf);
				out.write(_encodeBuffer, 0, compLen);
				return;
			}
		}
		
		LZFChunk.writeNonCompressedHeader(len, out, headerBuf);
		out.write(data, offset, len);
	}

	

	
	/**
	 * Calc hash len.
	 *
	 * @param chunkSize the chunk size
	 * @return the int
	 */
	private static int calcHashLen(int chunkSize) {
		
		chunkSize += chunkSize;
		
		if (chunkSize >= MAX_HASH_SIZE) {
			return MAX_HASH_SIZE;
		}
		
		int hashLen = MIN_HASH_SIZE;
		while (hashLen < chunkSize) {
			hashLen += hashLen;
		}
		return hashLen;
	}

	
	/**
	 * First.
	 *
	 * @param in the in
	 * @param inPos the in pos
	 * @return the int
	 */
	private int first(byte[] in, int inPos) {
		return (in[inPos] << 8) + (in[inPos + 1] & 255);
	}

	

	
	/**
	 * Hash.
	 *
	 * @param h the h
	 * @return the int
	 */
	private final int hash(int h) {
		
		return ((h * 57321) >> 9) & _hashModulo;
		
		
		
	}

	
	/**
	 * Try compress.
	 *
	 * @param in the in
	 * @param inPos the in pos
	 * @param inEnd the in end
	 * @param out the out
	 * @param outPos the out pos
	 * @return the int
	 */
	private int tryCompress(byte[] in, int inPos, int inEnd, byte[] out, int outPos) {
		final int[] hashTable = _hashTable;
		++outPos;
		int seen = first(in, 0); 
		int literals = 0;
		inEnd -= 4;
		final int firstPos = inPos; 

		while (inPos < inEnd) {
			byte p2 = in[inPos + 2];
			
			seen = (seen << 8) + (p2 & 255);
			int off = hash(seen);
			int ref = hashTable[off];
			hashTable[off] = inPos;

			
			if (ref >= inPos 
					|| ref < firstPos 
					|| (off = inPos - ref) > MAX_OFF || in[ref + 2] != p2 
					|| in[ref + 1] != (byte) (seen >> 8) || in[ref] != (byte) (seen >> 16)) {
				out[outPos++] = in[inPos++];
				literals++;
				if (literals == LZFChunk.MAX_LITERAL) {
					out[outPos - 33] = (byte) 31; 
					literals = 0;
					outPos++;
				}
				continue;
			}
			
			int maxLen = inEnd - inPos + 2;
			if (maxLen > MAX_REF) {
				maxLen = MAX_REF;
			}
			if (literals == 0) {
				outPos--;
			} else {
				out[outPos - literals - 1] = (byte) (literals - 1);
				literals = 0;
			}
			int len = 3;
			while (len < maxLen && in[ref + len] == in[inPos + len]) {
				len++;
			}
			len -= 2;
			--off; 
			if (len < 7) {
				out[outPos++] = (byte) ((off >> 8) + (len << 5));
			} else {
				out[outPos++] = (byte) ((off >> 8) + (7 << 5));
				out[outPos++] = (byte) (len - 7);
			}
			out[outPos++] = (byte) off;
			outPos++;
			inPos += len;
			seen = first(in, inPos);
			seen = (seen << 8) + (in[inPos + 2] & 255);
			hashTable[hash(seen)] = inPos;
			++inPos;
			seen = (seen << 8) + (in[inPos + 2] & 255); 
			hashTable[hash(seen)] = inPos;
			++inPos;
		}
		
		return handleTail(in, inPos, inEnd + 4, out, outPos, literals);
	}

	
	/**
	 * Handle tail.
	 *
	 * @param in the in
	 * @param inPos the in pos
	 * @param inEnd the in end
	 * @param out the out
	 * @param outPos the out pos
	 * @param literals the literals
	 * @return the int
	 */
	private int handleTail(byte[] in, int inPos, int inEnd, byte[] out, int outPos, int literals) {
		while (inPos < inEnd) {
			out[outPos++] = in[inPos++];
			literals++;
			if (literals == LZFChunk.MAX_LITERAL) {
				out[outPos - literals - 1] = (byte) (literals - 1);
				literals = 0;
				outPos++;
			}
		}
		out[outPos - literals - 1] = (byte) (literals - 1);
		if (literals == 0) {
			outPos--;
		}
		return outPos;
	}

}
