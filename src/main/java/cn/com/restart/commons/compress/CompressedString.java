/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons CompressedString.java 2012-3-29 15:15:10 l.xue.nong$$
 */


package cn.com.restart.commons.compress;

import java.io.IOException;
import java.util.Arrays;

import org.apache.lucene.util.UnicodeUtil;

import cn.com.restart.commons.Unicode;
import cn.com.restart.commons.compress.lzf.LZF;
import cn.com.restart.commons.compress.lzf.LZFDecoder;
import cn.com.restart.commons.compress.lzf.LZFEncoder;
import cn.com.restart.commons.io.stream.StreamInput;
import cn.com.restart.commons.io.stream.StreamOutput;
import cn.com.restart.commons.io.stream.Streamable;


/**
 * The Class CompressedString.
 *
 * @author l.xue.nong
 */
public class CompressedString implements Streamable {

    
    /** The bytes. */
    private byte[] bytes;

    
    /**
     * Instantiates a new compressed string.
     */
    CompressedString() {
    }

    
    /**
     * Instantiates a new compressed string.
     *
     * @param compressed the compressed
     */
    public CompressedString(byte[] compressed) {
        this.bytes = compressed;
    }

    
    /**
     * Instantiates a new compressed string.
     *
     * @param data the data
     * @param offset the offset
     * @param length the length
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public CompressedString(byte[] data, int offset, int length) throws IOException {
        if (LZF.isCompressed(data, offset, length)) {
            this.bytes = Arrays.copyOfRange(data, offset, offset + length);
        } else {
            this.bytes = LZFEncoder.encode(data, offset, length);
        }
    }

    
    /**
     * Instantiates a new compressed string.
     *
     * @param str the str
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public CompressedString(String str) throws IOException {
        UnicodeUtil.UTF8Result result = Unicode.unsafeFromStringAsUtf8(str);
        this.bytes = LZFEncoder.encode(result.result, result.length);
    }

    
    /**
     * Compressed.
     *
     * @return the byte[]
     */
    public byte[] compressed() {
        return this.bytes;
    }

    
    /**
     * Uncompressed.
     *
     * @return the byte[]
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public byte[] uncompressed() throws IOException {
        return LZFDecoder.decode(bytes);
    }

    
    /**
     * String.
     *
     * @return the string
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public String string() throws IOException {
        return Unicode.fromBytes(LZFDecoder.decode(bytes));
    }

    
    /**
     * Read compressed string.
     *
     * @param in the in
     * @return the compressed string
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static CompressedString readCompressedString(StreamInput in) throws IOException {
        CompressedString compressedString = new CompressedString();
        compressedString.readFrom(in);
        return compressedString;
    }

    
    /* (non-Javadoc)
     * @see cn.com.summall.search.commons.io.stream.Streamable#readFrom(cn.com.summall.search.commons.io.stream.StreamInput)
     */
    @Override
    public void readFrom(StreamInput in) throws IOException {
        bytes = new byte[in.readVInt()];
        in.readBytes(bytes, 0, bytes.length);
    }

    
    /* (non-Javadoc)
     * @see cn.com.summall.search.commons.io.stream.Streamable#writeTo(cn.com.summall.search.commons.io.stream.StreamOutput)
     */
    @Override
    public void writeTo(StreamOutput out) throws IOException {
        out.writeVInt(bytes.length);
        out.writeBytes(bytes);
    }

    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompressedString that = (CompressedString) o;

        if (!Arrays.equals(bytes, that.bytes)) return false;

        return true;
    }

    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return bytes != null ? Arrays.hashCode(bytes) : 0;
    }

    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        try {
            return string();
        } catch (IOException e) {
            return "_na_";
        }
    }
}
