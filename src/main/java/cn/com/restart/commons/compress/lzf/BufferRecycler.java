/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons BufferRecycler.java 2012-3-29 15:15:20 l.xue.nong$$
 */


package cn.com.restart.commons.compress.lzf;

import java.lang.ref.SoftReference;


/**
 * The Class BufferRecycler.
 *
 * @author l.xue.nong
 */
public class BufferRecycler {
    
    
    /** The Constant MIN_ENCODING_BUFFER. */
    private final static int MIN_ENCODING_BUFFER = 4000;

    
    /** The Constant MIN_OUTPUT_BUFFER. */
    private final static int MIN_OUTPUT_BUFFER = 8000;

    
    /** The Constant _recyclerRef. */
    final protected static ThreadLocal<SoftReference<BufferRecycler>> _recyclerRef
            = new ThreadLocal<SoftReference<BufferRecycler>>();


    
    /** The _input buffer. */
    private byte[] _inputBuffer;
    
    
    /** The _output buffer. */
    private byte[] _outputBuffer;

    
    /** The _decoding buffer. */
    private byte[] _decodingBuffer;
    
    
    /** The _encoding buffer. */
    private byte[] _encodingBuffer;

    
    /** The _encoding hash. */
    private int[] _encodingHash;


    
    /**
     * Instance.
     *
     * @return the buffer recycler
     */
    public static BufferRecycler instance() {
        SoftReference<BufferRecycler> ref = _recyclerRef.get();
        BufferRecycler br = (ref == null) ? null : ref.get();
        if (br == null) {
            br = new BufferRecycler();
            _recyclerRef.set(new SoftReference<BufferRecycler>(br));
        }
        return br;
    }

    
    /**
     * Clean.
     */
    public static void clean() {
        _recyclerRef.remove();
    }

    

    
    /**
     * Alloc encoding buffer.
     *
     * @param minSize the min size
     * @return the byte[]
     */
    public byte[] allocEncodingBuffer(int minSize) {
        byte[] buf = _encodingBuffer;
        if (buf == null || buf.length < minSize) {
            buf = new byte[Math.max(minSize, MIN_ENCODING_BUFFER)];
        } else {
            _encodingBuffer = null;
        }
        return buf;
    }

    
    /**
     * Release encode buffer.
     *
     * @param buffer the buffer
     */
    public void releaseEncodeBuffer(byte[] buffer) {
        if (_encodingBuffer == null || buffer.length > _encodingBuffer.length) {
            _encodingBuffer = buffer;
        }
    }

    
    /**
     * Alloc output buffer.
     *
     * @param minSize the min size
     * @return the byte[]
     */
    public byte[] allocOutputBuffer(int minSize) {
        byte[] buf = _outputBuffer;
        if (buf == null || buf.length < minSize) {
            buf = new byte[Math.max(minSize, MIN_OUTPUT_BUFFER)];
        } else {
            _outputBuffer = null;
        }
        return buf;
    }

    
    /**
     * Release output buffer.
     *
     * @param buffer the buffer
     */
    public void releaseOutputBuffer(byte[] buffer) {
        if (_outputBuffer == null || (buffer != null && buffer.length > _outputBuffer.length)) {
            _outputBuffer = buffer;
        }
    }

    
    /**
     * Alloc encoding hash.
     *
     * @param suggestedSize the suggested size
     * @return the int[]
     */
    public int[] allocEncodingHash(int suggestedSize) {
        int[] buf = _encodingHash;
        if (buf == null || buf.length < suggestedSize) {
            buf = new int[suggestedSize];
        } else {
            _encodingHash = null;
        }
        return buf;
    }

    
    /**
     * Release encoding hash.
     *
     * @param buffer the buffer
     */
    public void releaseEncodingHash(int[] buffer) {
        if (_encodingHash == null || (buffer != null && buffer.length > _encodingHash.length)) {
            _encodingHash = buffer;
        }
    }

    

    
    /**
     * Alloc input buffer.
     *
     * @param minSize the min size
     * @return the byte[]
     */
    public byte[] allocInputBuffer(int minSize) {
        byte[] buf = _inputBuffer;
        if (buf == null || buf.length < minSize) {
            buf = new byte[Math.max(minSize, MIN_OUTPUT_BUFFER)];
        } else {
            _inputBuffer = null;
        }
        return buf;
    }

    
    /**
     * Release input buffer.
     *
     * @param buffer the buffer
     */
    public void releaseInputBuffer(byte[] buffer) {
        if (_inputBuffer == null || (buffer != null && buffer.length > _inputBuffer.length)) {
            _inputBuffer = buffer;
        }
    }

    
    /**
     * Alloc decode buffer.
     *
     * @param size the size
     * @return the byte[]
     */
    public byte[] allocDecodeBuffer(int size) {
        byte[] buf = _decodingBuffer;
        if (buf == null || buf.length < size) {
            buf = new byte[size];
        } else {
            _decodingBuffer = null;
        }
        return buf;
    }

    
    /**
     * Release decode buffer.
     *
     * @param buffer the buffer
     */
    public void releaseDecodeBuffer(byte[] buffer) {
        if (_decodingBuffer == null || (buffer != null && buffer.length > _decodingBuffer.length)) {
            _decodingBuffer = buffer;
        }
    }

}