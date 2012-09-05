/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons FileChannelInputStream.java 2012-7-6 10:23:53 l.xue.nong$$
 */
package cn.com.rebirth.commons.io;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


/**
 * The Class FileChannelInputStream.
 *
 * @author l.xue.nong
 */
public class FileChannelInputStream extends InputStream {

    
    /** The channel. */
    private final FileChannel channel;

    
    /** The position. */
    private long position;

    
    /** The length. */
    private long length;

    
    /** The bb. */
    private ByteBuffer bb = null;
    
    
    /** The bs. */
    private byte[] bs = null; 
    
    
    /** The b1. */
    private byte[] b1 = null;

    
    /** The mark position. */
    private long markPosition;

    
    /**
     * Instantiates a new file channel input stream.
     *
     * @param channel the channel
     * @param position the position
     * @param length the length
     */
    public FileChannelInputStream(FileChannel channel, long position, long length) {
        this.channel = channel;
        this.position = position;
        this.markPosition = position;
        this.length = position + length; 
    }

    
    /* (non-Javadoc)
     * @see java.io.InputStream#read()
     */
    @Override
    public int read() throws IOException {
        if (b1 == null) {
            b1 = new byte[1];
        }
        int n = read(b1);
        if (n == 1) {
            return b1[0] & 0xff;
        }
        return -1;
    }

    
    /* (non-Javadoc)
     * @see java.io.InputStream#read(byte[], int, int)
     */
    @Override
    public int read(byte[] bs, int off, int len) throws IOException {
        if (len == 0) {
            return 0;
        }

        if ((length - position) < len) {
            len = (int) (length - position);
        }

        if (len == 0) {
            return -1;
        }

        ByteBuffer bb = ((this.bs == bs) ? this.bb : ByteBuffer.wrap(bs));
        bb.limit(Math.min(off + len, bb.capacity()));
        bb.position(off);

        this.bb = bb;
        this.bs = bs;
        int read = channel.read(bb, position);
        if (read > 0) {
            position += read;
        }
        return read;
    }

    
    /* (non-Javadoc)
     * @see java.io.InputStream#markSupported()
     */
    @Override
    public boolean markSupported() {
        return true;
    }

    
    /* (non-Javadoc)
     * @see java.io.InputStream#mark(int)
     */
    @Override
    public void mark(int readlimit) {
        this.markPosition = position;
    }

    
    /* (non-Javadoc)
     * @see java.io.InputStream#reset()
     */
    @Override
    public void reset() throws IOException {
        position = markPosition;
    }
}
