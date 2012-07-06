/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons LZFStreamInput.java 2012-3-29 15:15:14 l.xue.nong$$
 */


package cn.com.rebirth.commons.io.stream;

import java.io.EOFException;
import java.io.IOException;

import cn.com.rebirth.commons.compress.lzf.BufferRecycler;
import cn.com.rebirth.commons.compress.lzf.ChunkDecoder;
import cn.com.rebirth.commons.compress.lzf.LZFChunk;
import cn.com.rebirth.commons.compress.lzf.util.ChunkDecoderFactory;


/**
 * The Class LZFStreamInput.
 *
 * @author l.xue.nong
 */
public class LZFStreamInput extends StreamInput {
    
    /** The _decoder. */
    private final ChunkDecoder _decoder;

    
    /** The _recycler. */
    private final BufferRecycler _recycler;

    
    /** The input stream. */
    protected StreamInput inputStream;

    
    /** The input stream closed. */
    protected boolean inputStreamClosed;

    
    /** The _cfg full reads. */
    protected boolean _cfgFullReads = true; 

    
    
    /** The _input buffer. */
    private byte[] _inputBuffer;

    
    
    /** The _decoded bytes. */
    private byte[] _decodedBytes;

    
    
    /** The buffer position. */
    private int bufferPosition = 0;

    
    
    /** The buffer length. */
    private int bufferLength = 0;

    
    
    /** The cached. */
    private final boolean cached;

    
    /**
     * Instantiates a new lZF stream input.
     *
     * @param in the in
     * @param cached the cached
     */
    public LZFStreamInput(StreamInput in, boolean cached) {
        super();
        this.cached = cached;
        if (cached) {
            _recycler = new BufferRecycler();
        } else {
            _recycler = BufferRecycler.instance();
        }
        _decoder = ChunkDecoderFactory.optimalInstance();
        inputStream = in;
        inputStreamClosed = false;

        _inputBuffer = _recycler.allocInputBuffer(LZFChunk.MAX_CHUNK_LEN);
        _decodedBytes = _recycler.allocDecodeBuffer(LZFChunk.MAX_CHUNK_LEN);
    }

    
    /* (non-Javadoc)
     * @see java.io.InputStream#available()
     */
    @Override
    public int available() {
        
        if (inputStreamClosed) {
            return -1;
        }
        int left = (bufferLength - bufferPosition);
        return (left <= 0) ? 0 : left;
    }

    
    /* (non-Javadoc)
     * @see java.io.InputStream#read()
     */
    @Override
    public int read() throws IOException {
        if (!readyBuffer()) {
            return -1;
        }
        return _decodedBytes[bufferPosition++] & 255;
    }

    
    /* (non-Javadoc)
     * @see java.io.InputStream#read(byte[], int, int)
     */
    @Override
    public int read(final byte[] buffer, int offset, int length) throws IOException {
        if (length < 1) {
            return 0;
        }
        if (!readyBuffer()) {
            return -1;
        }
        
        int chunkLength = Math.min(bufferLength - bufferPosition, length);
        System.arraycopy(_decodedBytes, bufferPosition, buffer, offset, chunkLength);
        bufferPosition += chunkLength;

        if (chunkLength == length || !_cfgFullReads) {
            return chunkLength;
        }
        
        int totalRead = chunkLength;
        do {
            offset += chunkLength;
            if (!readyBuffer()) {
                break;
            }
            chunkLength = Math.min(bufferLength - bufferPosition, (length - totalRead));
            System.arraycopy(_decodedBytes, bufferPosition, buffer, offset, chunkLength);
            bufferPosition += chunkLength;
            totalRead += chunkLength;
        } while (totalRead < length);

        return totalRead;
    }

    
    /* (non-Javadoc)
     * @see cn.com.summall.search.commons.io.stream.StreamInput#readByte()
     */
    @Override
    public byte readByte() throws IOException {
        if (!readyBuffer()) {
            throw new EOFException();
        }
        return _decodedBytes[bufferPosition++];
    }

    
    /* (non-Javadoc)
     * @see cn.com.summall.search.commons.io.stream.StreamInput#readBytes(byte[], int, int)
     */
    @Override
    public void readBytes(byte[] b, int offset, int len) throws IOException {
        int result = read(b, offset, len);
        if (result < len) {
            throw new EOFException();
        }
    }

    
    /* (non-Javadoc)
     * @see cn.com.summall.search.commons.io.stream.StreamInput#reset()
     */
    @Override
    public void reset() throws IOException {
        this.bufferPosition = 0;
        this.bufferLength = 0;
        inputStream.reset();
    }

    
    /**
     * Reset.
     *
     * @param in the in
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void reset(StreamInput in) throws IOException {
        this.inputStream = in;
        this.bufferPosition = 0;
        this.bufferLength = 0;
    }

    
    /**
     * Reset to buffer start.
     */
    public void resetToBufferStart() {
        this.bufferPosition = 0;
    }

    
    /* (non-Javadoc)
     * @see cn.com.summall.search.commons.io.stream.StreamInput#close()
     */
    @Override
    public void close() throws IOException {
        if (cached) {
            reset();
            return;
        }
        bufferPosition = bufferLength = 0;
        byte[] buf = _inputBuffer;
        if (buf != null) {
            _inputBuffer = null;
            _recycler.releaseInputBuffer(buf);
        }
        buf = _decodedBytes;
        if (buf != null) {
            _decodedBytes = null;
            _recycler.releaseDecodeBuffer(buf);
        }
        if (!inputStreamClosed) {
            inputStreamClosed = true;
            inputStream.close();
        }
    }

    

    
    /**
     * Ready buffer.
     *
     * @return true, if successful
     * @throws IOException Signals that an I/O exception has occurred.
     */
    protected boolean readyBuffer() throws IOException {
        if (bufferPosition < bufferLength) {
            return true;
        }
        if (inputStreamClosed) {
            return false;
        }
        bufferLength = _decoder.decodeChunk(inputStream, _inputBuffer, _decodedBytes);
        if (bufferLength < 0) {
            return false;
        }
        bufferPosition = 0;
        return (bufferPosition < bufferLength);
    }
}
