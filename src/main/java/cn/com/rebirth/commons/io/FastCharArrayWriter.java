/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons FastCharArrayWriter.java 2012-7-6 10:23:48 l.xue.nong$$
 */
package cn.com.rebirth.commons.io;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;


/**
 * The Class FastCharArrayWriter.
 *
 * @author l.xue.nong
 */
public class FastCharArrayWriter extends Writer {

    
    /** The buf. */
    protected char buf[];

    
    /** The count. */
    protected int count;

    
    /**
     * Instantiates a new fast char array writer.
     */
    public FastCharArrayWriter() {
        this(32);
    }

    
    /**
     * Instantiates a new fast char array writer.
     *
     * @param initialSize the initial size
     */
    public FastCharArrayWriter(int initialSize) {
        if (initialSize < 0) {
            throw new IllegalArgumentException("Negative initial size: "
                    + initialSize);
        }
        buf = new char[initialSize];
    }

    
    /* (non-Javadoc)
     * @see java.io.Writer#write(int)
     */
    public void write(int c) {
        int newcount = count + 1;
        if (newcount > buf.length) {
            buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newcount));
        }
        buf[count] = (char) c;
        count = newcount;
    }

    
    /* (non-Javadoc)
     * @see java.io.Writer#write(char[], int, int)
     */
    public void write(char c[], int off, int len) {
        if ((off < 0) || (off > c.length) || (len < 0) ||
                ((off + len) > c.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        int newcount = count + len;
        if (newcount > buf.length) {
            buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newcount));
        }
        System.arraycopy(c, off, buf, count, len);
        count = newcount;
    }

    
    /* (non-Javadoc)
     * @see java.io.Writer#write(java.lang.String, int, int)
     */
    public void write(String str, int off, int len) {
        int newcount = count + len;
        if (newcount > buf.length) {
            buf = Arrays.copyOf(buf, Math.max(buf.length << 1, newcount));
        }
        str.getChars(off, off + len, buf, count);
        count = newcount;
    }

    
    /**
     * Write to.
     *
     * @param out the out
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void writeTo(Writer out) throws IOException {
        out.write(buf, 0, count);
    }

    
    /* (non-Javadoc)
     * @see java.io.Writer#append(java.lang.CharSequence)
     */
    public FastCharArrayWriter append(CharSequence csq) {
        String s = (csq == null ? "null" : csq.toString());
        write(s, 0, s.length());
        return this;
    }

    
    /* (non-Javadoc)
     * @see java.io.Writer#append(java.lang.CharSequence, int, int)
     */
    public FastCharArrayWriter append(CharSequence csq, int start, int end) {
        String s = (csq == null ? "null" : csq).subSequence(start, end).toString();
        write(s, 0, s.length());
        return this;
    }

    
    /* (non-Javadoc)
     * @see java.io.Writer#append(char)
     */
    public FastCharArrayWriter append(char c) {
        write(c);
        return this;
    }

    
    /**
     * Reset.
     */
    public void reset() {
        count = 0;
    }

    
    /**
     * To char array.
     *
     * @return the char[]
     */
    public char toCharArray()[] {
        return Arrays.copyOf(buf, count);
    }

    
    /**
     * Unsafe char array.
     *
     * @return the char[]
     */
    public char[] unsafeCharArray() {
        return buf;
    }

    
    /**
     * Size.
     *
     * @return the int
     */
    public int size() {
        return count;
    }

    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return new String(buf, 0, count);
    }

    
    /**
     * To string trim.
     *
     * @return the string
     */
    public String toStringTrim() {
        int st = 0;
        int len = count;
        char[] val = buf;    

        while ((st < len) && (val[st] <= ' ')) {
            st++;
            len--;
        }
        while ((st < len) && (val[len - 1] <= ' ')) {
            len--;
        }
        return new String(buf, st, len);
    }

    
    /* (non-Javadoc)
     * @see java.io.Writer#flush()
     */
    public void flush() {
    }

    
    /* (non-Javadoc)
     * @see java.io.Writer#close()
     */
    public void close() {
    }

}
