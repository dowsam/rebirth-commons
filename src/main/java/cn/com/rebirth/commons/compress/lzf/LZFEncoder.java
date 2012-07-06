/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons LZFEncoder.java 2012-3-29 15:15:17 l.xue.nong$$
 */


package cn.com.rebirth.commons.compress.lzf;

import java.io.IOException;


/**
 * The Class LZFEncoder.
 *
 * @author l.xue.nong
 */
public class LZFEncoder {
    
    /**
     * Instantiates a new lZF encoder.
     */
    private LZFEncoder() {
    }

    
    /**
     * Encode.
     *
     * @param data the data
     * @return the byte[]
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static byte[] encode(byte[] data) throws IOException {
        return encode(data, data.length);
    }

    
    /**
     * Encode.
     *
     * @param data the data
     * @param length the length
     * @return the byte[]
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static byte[] encode(byte[] data, int length) throws IOException {
        return encode(data, 0, length);
    }

    
    /**
     * Encode.
     *
     * @param data the data
     * @param offset the offset
     * @param length the length
     * @return the byte[]
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static byte[] encode(byte[] data, int offset, int length) throws IOException {
        ChunkEncoder enc = new ChunkEncoder(length, BufferRecycler.instance());
        byte[] result = encode(enc, data, offset, length);
        
        enc.close();
        return result;
    }

    
    /**
     * Encode.
     *
     * @param enc the enc
     * @param data the data
     * @param length the length
     * @return the byte[]
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static byte[] encode(ChunkEncoder enc, byte[] data, int length)
            throws IOException {
        return encode(enc, data, 0, length);
    }

    
    /**
     * Encode.
     *
     * @param enc the enc
     * @param data the data
     * @param offset the offset
     * @param length the length
     * @return the byte[]
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static byte[] encode(ChunkEncoder enc, byte[] data, int offset, int length)
            throws IOException {
        int left = length;
        int chunkLen = Math.min(LZFChunk.MAX_CHUNK_LEN, left);
        LZFChunk first = enc.encodeChunk(data, offset, chunkLen);
        left -= chunkLen;
        
        if (left < 1) {
            return first.getData();
        }
        
        int resultBytes = first.length();
        offset += chunkLen;
        LZFChunk last = first;

        do {
            chunkLen = Math.min(left, LZFChunk.MAX_CHUNK_LEN);
            LZFChunk chunk = enc.encodeChunk(data, offset, chunkLen);
            offset += chunkLen;
            left -= chunkLen;
            resultBytes += chunk.length();
            last.setNext(chunk);
            last = chunk;
        } while (left > 0);
        
        byte[] result = new byte[resultBytes];
        int ptr = 0;
        for (; first != null; first = first.next()) {
            ptr = first.copyTo(result, ptr);
        }
        return result;
    }
}
