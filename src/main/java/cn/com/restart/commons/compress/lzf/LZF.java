/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons LZF.java 2012-3-29 15:15:20 l.xue.nong$$
 */


package cn.com.restart.commons.compress.lzf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * The Class LZF.
 *
 * @author l.xue.nong
 */
public class LZF {

    
    /**
     * Checks if is compressed.
     *
     * @param buffer the buffer
     * @return true, if is compressed
     */
    public static boolean isCompressed(final byte[] buffer) {
        return buffer.length >= 2 && buffer[0] == LZFChunk.BYTE_Z && buffer[1] == LZFChunk.BYTE_V;
    }

    
    /**
     * Checks if is compressed.
     *
     * @param buffer the buffer
     * @param offset the offset
     * @param length the length
     * @return true, if is compressed
     */
    public static boolean isCompressed(final byte[] buffer, int offset, int length) {
        return length >= 2 && buffer[offset] == LZFChunk.BYTE_Z && buffer[offset + 1] == LZFChunk.BYTE_V;
    }

    
    /** The Constant SUFFIX. */
    public final static String SUFFIX = ".lzf";

    
    /**
     * Process.
     *
     * @param args the args
     * @throws IOException Signals that an I/O exception has occurred.
     */
    void process(String[] args) throws IOException {
        if (args.length == 2) {
            String oper = args[0];
            boolean compress = "-c".equals(oper);
            if (compress || "-d".equals(oper)) {
                String filename = args[1];
                File src = new File(filename);
                if (!src.exists()) {
                    System.err.println("File '" + filename + "' does not exist.");
                    System.exit(1);
                }
                if (!compress && !filename.endsWith(SUFFIX)) {
                    System.err.println("File '" + filename + "' does end with expected suffix ('" + SUFFIX + "', won't decompress.");
                    System.exit(1);
                }
                byte[] data = readData(src);
                System.out.println("Read " + data.length + " bytes.");
                byte[] result = compress ? LZFEncoder.encode(data) : LZFDecoder.decode(data);
                System.out.println("Processed into " + result.length + " bytes.");
                File resultFile = compress ? new File(filename + SUFFIX) : new File(filename.substring(0, filename.length() - SUFFIX.length()));
                FileOutputStream out = new FileOutputStream(resultFile);
                out.write(result);
                out.close();
                System.out.println("Wrote in file '" + resultFile.getAbsolutePath() + "'.");
                return;
            }
        }
        System.err.println("Usage: java " + getClass().getName() + " -c/-d file");
        System.exit(1);
    }

    
    /**
     * Read data.
     *
     * @param in the in
     * @return the byte[]
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private byte[] readData(File in) throws IOException {
        int len = (int) in.length();
        byte[] result = new byte[len];
        int offset = 0;
        FileInputStream fis = new FileInputStream(in);

        while (len > 0) {
            int count = fis.read(result, offset, len);
            if (count < 0) break;
            len -= count;
            offset += count;
        }
        fis.close();
        if (len > 0) { 
            throw new IOException("Could not read the whole file -- received EOF when there was " + len + " bytes left to read");
        }
        return result;
    }

    
    /**
     * The main method.
     *
     * @param args the arguments
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void main(String[] args) throws IOException {
        new LZF().process(args);
    }
}

