/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons FastCharArrayReader.java 2012-7-6 10:23:47 l.xue.nong$$
 */
package cn.com.rebirth.commons.io;

import java.io.IOException;
import java.io.Reader;


/**
 * The Class FastCharArrayReader.
 *
 * @author l.xue.nong
 */
public class FastCharArrayReader extends Reader {

    
    /** The buf. */
    protected char buf[];

    
    /** The pos. */
    protected int pos;

    
    /** The marked pos. */
    protected int markedPos = 0;

    
    /** The count. */
    protected int count;

    
    /**
     * Instantiates a new fast char array reader.
     *
     * @param buf the buf
     */
    public FastCharArrayReader(char buf[]) {
        this.buf = buf;
        this.pos = 0;
        this.count = buf.length;
    }

    
    /**
     * Instantiates a new fast char array reader.
     *
     * @param buf the buf
     * @param offset the offset
     * @param length the length
     */
    public FastCharArrayReader(char buf[], int offset, int length) {
        if ((offset < 0) || (offset > buf.length) || (length < 0) ||
                ((offset + length) < 0)) {
            throw new IllegalArgumentException();
        }
        this.buf = buf;
        this.pos = offset;
        this.count = Math.min(offset + length, buf.length);
        this.markedPos = offset;
    }

    
    /**
     * Ensure open.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void ensureOpen() throws IOException {
        if (buf == null)
            throw new IOException("Stream closed");
    }

    
    /* (non-Javadoc)
     * @see java.io.Reader#read()
     */
    public int read() throws IOException {
        ensureOpen();
        if (pos >= count)
            return -1;
        else
            return buf[pos++];
    }

    
    /* (non-Javadoc)
     * @see java.io.Reader#read(char[], int, int)
     */
    public int read(char b[], int off, int len) throws IOException {
        ensureOpen();
        if ((off < 0) || (off > b.length) || (len < 0) ||
                ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }

        if (pos >= count) {
            return -1;
        }
        if (pos + len > count) {
            len = count - pos;
        }
        if (len <= 0) {
            return 0;
        }
        System.arraycopy(buf, pos, b, off, len);
        pos += len;
        return len;
    }

    
    /* (non-Javadoc)
     * @see java.io.Reader#skip(long)
     */
    public long skip(long n) throws IOException {
        ensureOpen();
        if (pos + n > count) {
            n = count - pos;
        }
        if (n < 0) {
            return 0;
        }
        pos += n;
        return n;
    }

    
    /* (non-Javadoc)
     * @see java.io.Reader#ready()
     */
    public boolean ready() throws IOException {
        ensureOpen();
        return (count - pos) > 0;
    }

    
    /* (non-Javadoc)
     * @see java.io.Reader#markSupported()
     */
    public boolean markSupported() {
        return true;
    }

    
    /* (non-Javadoc)
     * @see java.io.Reader#mark(int)
     */
    public void mark(int readAheadLimit) throws IOException {
        ensureOpen();
        markedPos = pos;
    }

    
    /* (non-Javadoc)
     * @see java.io.Reader#reset()
     */
    public void reset() throws IOException {
        ensureOpen();
        pos = markedPos;
    }

    
    /* (non-Javadoc)
     * @see java.io.Reader#close()
     */
    public void close() {
        buf = null;
    }
}
