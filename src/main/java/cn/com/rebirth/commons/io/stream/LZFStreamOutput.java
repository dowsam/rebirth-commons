/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons LZFStreamOutput.java 2012-7-6 10:23:41 l.xue.nong$$
 */


package cn.com.rebirth.commons.io.stream;

import java.io.IOException;

import cn.com.rebirth.commons.compress.lzf.BufferRecycler;
import cn.com.rebirth.commons.compress.lzf.ChunkEncoder;
import cn.com.rebirth.commons.compress.lzf.LZFChunk;
import cn.com.rebirth.commons.io.stream.StreamOutput;


/**
 * The Class LZFStreamOutput.
 *
 * @author l.xue.nong
 */
public class LZFStreamOutput extends StreamOutput {

    
    /** The output buffer size. */
    private static int OUTPUT_BUFFER_SIZE = LZFChunk.MAX_CHUNK_LEN;

    
    /** The _encoder. */
    private final ChunkEncoder _encoder;
    
    
    /** The _recycler. */
    private final BufferRecycler _recycler;

    
    /** The _output stream. */
    protected StreamOutput _outputStream;
    
    
    /** The _output buffer. */
    protected byte[] _outputBuffer;
    
    
    /** The _position. */
    protected int _position = 0;


    
    /** The _cfg finish block on flush. */
    protected boolean _cfgFinishBlockOnFlush = true;

    
    /** The never close. */
    private final boolean neverClose;

    
    /**
     * Instantiates a new lZF stream output.
     *
     * @param out the out
     * @param neverClose the never close
     */
    public LZFStreamOutput(StreamOutput out, boolean neverClose) {
        this.neverClose = neverClose;
        _recycler = neverClose ? new BufferRecycler() : BufferRecycler.instance();
        _encoder = new ChunkEncoder(OUTPUT_BUFFER_SIZE, _recycler);
        _outputStream = out;
        _outputBuffer = _recycler.allocOutputBuffer(OUTPUT_BUFFER_SIZE);
    }

    
    /* (non-Javadoc)
     * @see cn.com.rebirth.search.commons.io.stream.StreamOutput#write(int)
     */
    @Override
    public void write(final int singleByte) throws IOException {
        if (_position >= _outputBuffer.length) {
            writeCompressedBlock();
        }
        _outputBuffer[_position++] = (byte) singleByte;
    }

    
    /* (non-Javadoc)
     * @see cn.com.rebirth.search.commons.io.stream.StreamOutput#writeByte(byte)
     */
    @Override
    public void writeByte(byte b) throws IOException {
        if (_position >= _outputBuffer.length) {
            writeCompressedBlock();
        }
        _outputBuffer[_position++] = b;
    }

    
    /* (non-Javadoc)
     * @see cn.com.rebirth.search.commons.io.stream.StreamOutput#writeBytes(byte[], int, int)
     */
    @Override
    public void writeBytes(byte[] buffer, int offset, int length) throws IOException {
        
        if (length == 0) {
            return;
        }
        final int BUFFER_LEN = _outputBuffer.length;

        
        int free = BUFFER_LEN - _position;
        if (free >= length) {
            System.arraycopy(buffer, offset, _outputBuffer, _position, length);
            _position += length;
            return;
        }
        
        System.arraycopy(buffer, offset, _outputBuffer, _position, free);
        offset += free;
        length -= free;
        _position += free;
        writeCompressedBlock();

        
        while (length >= BUFFER_LEN) {
            _encoder.encodeAndWriteChunk(buffer, offset, BUFFER_LEN, _outputStream);
            offset += BUFFER_LEN;
            length -= BUFFER_LEN;
        }

        
        if (length > 0) {
            System.arraycopy(buffer, offset, _outputBuffer, 0, length);
        }
        _position = length;
    }

    
    /* (non-Javadoc)
     * @see cn.com.rebirth.search.commons.io.stream.StreamOutput#flush()
     */
    @Override
    public void flush() throws IOException {
        if (_cfgFinishBlockOnFlush && _position > 0) {
            writeCompressedBlock();
        }
        _outputStream.flush();
    }

    
    /* (non-Javadoc)
     * @see cn.com.rebirth.search.commons.io.stream.StreamOutput#close()
     */
    @Override
    public void close() throws IOException {
        if (_position > 0) {
            writeCompressedBlock();
        }
        if (neverClose) {
            
            _position = 0;
            return;
        }
        _outputStream.flush();
        _encoder.close();
        byte[] buf = _outputBuffer;
        if (buf != null) {
            _outputBuffer = null;
            _recycler.releaseOutputBuffer(buf);
        }
        _outputStream.close();
    }

    
    /* (non-Javadoc)
     * @see cn.com.rebirth.search.commons.io.stream.StreamOutput#reset()
     */
    @Override
    public void reset() throws IOException {
        _position = 0;
        _outputStream.reset();
    }

    
    /**
     * Reset.
     *
     * @param out the out
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void reset(StreamOutput out) throws IOException {
        this._outputStream = out;
        reset();
    }

    
    /**
     * Wrapped out.
     *
     * @return the stream output
     */
    public StreamOutput wrappedOut() {
        return this._outputStream;
    }

    
    /**
     * Write compressed block.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void writeCompressedBlock() throws IOException {
        int left = _position;
        _position = 0;
        int offset = 0;

        do {
            int chunkLen = Math.min(LZFChunk.MAX_CHUNK_LEN, left);
            _encoder.encodeAndWriteChunk(_outputBuffer, offset, chunkLen, _outputStream);
            offset += chunkLen;
            left -= chunkLen;
        } while (left > 0);
    }
}
