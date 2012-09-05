/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons FastStringReader.java 2012-7-6 10:23:45 l.xue.nong$$
 */
package cn.com.rebirth.commons.io;

import java.io.IOException;


/**
 * The Class FastStringReader.
 *
 * @author l.xue.nong
 */
public class FastStringReader extends CharSequenceReader {

    
    /** The str. */
    private String str;
    
    
    /** The length. */
    private int length;
    
    
    /** The next. */
    private int next = 0;
    
    
    /** The mark. */
    private int mark = 0;

    
    /**
     * Instantiates a new fast string reader.
     *
     * @param s the s
     */
    public FastStringReader(String s) {
        this.str = s;
        this.length = s.length();
    }

    
    /**
     * Ensure open.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void ensureOpen() throws IOException {
        if (length == -1)
            throw new IOException("Stream closed");
    }

    
    /* (non-Javadoc)
     * @see java.lang.CharSequence#length()
     */
    @Override
    public int length() {
        return length;
    }

    
    /* (non-Javadoc)
     * @see java.lang.CharSequence#charAt(int)
     */
    @Override
    public char charAt(int index) {
        return str.charAt(index);
    }

    
    /* (non-Javadoc)
     * @see java.lang.CharSequence#subSequence(int, int)
     */
    @Override
    public CharSequence subSequence(int start, int end) {
        return str.subSequence(start, end);
    }

    
    /* (non-Javadoc)
     * @see java.io.Reader#read()
     */
    @Override
    public int read() throws IOException {
        ensureOpen();
        if (next >= length)
            return -1;
        return str.charAt(next++);
    }

    
    /* (non-Javadoc)
     * @see java.io.Reader#read(char[], int, int)
     */
    @Override
    public int read(char cbuf[], int off, int len) throws IOException {
        ensureOpen();
        if (len == 0) {
            return 0;
        }
        if (next >= length)
            return -1;
        int n = Math.min(length - next, len);
        str.getChars(next, next + n, cbuf, off);
        next += n;
        return n;
    }

    
    /* (non-Javadoc)
     * @see java.io.Reader#skip(long)
     */
    @Override
    public long skip(long ns) throws IOException {
        ensureOpen();
        if (next >= length)
            return 0;
        
        long n = Math.min(length - next, ns);
        n = Math.max(-next, n);
        next += n;
        return n;
    }

    
    /* (non-Javadoc)
     * @see java.io.Reader#ready()
     */
    @Override
    public boolean ready() throws IOException {
        ensureOpen();
        return true;
    }

    
    /* (non-Javadoc)
     * @see java.io.Reader#markSupported()
     */
    @Override
    public boolean markSupported() {
        return true;
    }

    
    /* (non-Javadoc)
     * @see java.io.Reader#mark(int)
     */
    @Override
    public void mark(int readAheadLimit) throws IOException {
        if (readAheadLimit < 0) {
            throw new IllegalArgumentException("Read-ahead limit < 0");
        }
        ensureOpen();
        mark = next;
    }

    
    /* (non-Javadoc)
     * @see java.io.Reader#reset()
     */
    @Override
    public void reset() throws IOException {
        ensureOpen();
        next = mark;
    }

    
    /* (non-Javadoc)
     * @see java.io.Reader#close()
     */
    public void close() {
        length = -1;
    }

    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return str;
    }
}
