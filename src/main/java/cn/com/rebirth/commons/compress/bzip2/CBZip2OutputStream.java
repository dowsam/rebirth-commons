/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons CBZip2OutputStream.java 2012-7-6 10:22:13 l.xue.nong$$
 */
package cn.com.rebirth.commons.compress.bzip2;

import java.io.IOException;
import java.io.OutputStream;


/**
 * The Class CBZip2OutputStream.
 *
 * @author l.xue.nong
 */
public class CBZip2OutputStream extends OutputStream
        implements BZip2Constants {

    
    /** The Constant MIN_BLOCKSIZE. */
    public static final int MIN_BLOCKSIZE = 1;

    
    /** The Constant MAX_BLOCKSIZE. */
    public static final int MAX_BLOCKSIZE = 9;

    
    /** The Constant SETMASK. */
    protected static final int SETMASK = (1 << 21);

    
    /** The Constant CLEARMASK. */
    protected static final int CLEARMASK = (~SETMASK);

    
    /** The Constant GREATER_ICOST. */
    protected static final int GREATER_ICOST = 15;

    
    /** The Constant LESSER_ICOST. */
    protected static final int LESSER_ICOST = 0;

    
    /** The Constant SMALL_THRESH. */
    protected static final int SMALL_THRESH = 20;

    
    /** The Constant DEPTH_THRESH. */
    protected static final int DEPTH_THRESH = 10;

    
    /** The Constant WORK_FACTOR. */
    protected static final int WORK_FACTOR = 30;

    
    /** The Constant QSORT_STACK_SIZE. */
    protected static final int QSORT_STACK_SIZE = 1000;

    
    /** The Constant INCS. */
    private static final int[] INCS = {1, 4, 13, 40, 121, 364, 1093, 3280,
            9841, 29524, 88573, 265720, 797161,
            2391484};

    
    /**
     * Hb make code lengths.
     *
     * @param len the len
     * @param freq the freq
     * @param alphaSize the alpha size
     * @param maxLen the max len
     */
    protected static void hbMakeCodeLengths(char[] len, int[] freq,
                                            int alphaSize, int maxLen) {
        
        final int[] heap = new int[MAX_ALPHA_SIZE * 2];
        final int[] weight = new int[MAX_ALPHA_SIZE * 2];
        final int[] parent = new int[MAX_ALPHA_SIZE * 2];

        for (int i = alphaSize; --i >= 0; ) {
            weight[i + 1] = (freq[i] == 0 ? 1 : freq[i]) << 8;
        }

        for (boolean tooLong = true; tooLong; ) {
            tooLong = false;

            int nNodes = alphaSize;
            int nHeap = 0;
            heap[0] = 0;
            weight[0] = 0;
            parent[0] = -2;

            for (int i = 1; i <= alphaSize; i++) {
                parent[i] = -1;
                nHeap++;
                heap[nHeap] = i;

                int zz = nHeap;
                int tmp = heap[zz];
                while (weight[tmp] < weight[heap[zz >> 1]]) {
                    heap[zz] = heap[zz >> 1];
                    zz >>= 1;
                }
                heap[zz] = tmp;
            }

            

            while (nHeap > 1) {
                int n1 = heap[1];
                heap[1] = heap[nHeap];
                nHeap--;

                int yy = 0;
                int zz = 1;
                int tmp = heap[1];

                while (true) {
                    yy = zz << 1;

                    if (yy > nHeap) {
                        break;
                    }

                    if ((yy < nHeap)
                            && (weight[heap[yy + 1]] < weight[heap[yy]])) {
                        yy++;
                    }

                    if (weight[tmp] < weight[heap[yy]]) {
                        break;
                    }

                    heap[zz] = heap[yy];
                    zz = yy;
                }

                heap[zz] = tmp;

                int n2 = heap[1];
                heap[1] = heap[nHeap];
                nHeap--;

                yy = 0;
                zz = 1;
                tmp = heap[1];

                while (true) {
                    yy = zz << 1;

                    if (yy > nHeap) {
                        break;
                    }

                    if ((yy < nHeap)
                            && (weight[heap[yy + 1]] < weight[heap[yy]])) {
                        yy++;
                    }

                    if (weight[tmp] < weight[heap[yy]]) {
                        break;
                    }

                    heap[zz] = heap[yy];
                    zz = yy;
                }

                heap[zz] = tmp;
                nNodes++;
                parent[n1] = parent[n2] = nNodes;

                final int weight_n1 = weight[n1];
                final int weight_n2 = weight[n2];
                weight[nNodes] = (((weight_n1 & 0xffffff00)
                        + (weight_n2 & 0xffffff00))
                        |
                        (1 + (((weight_n1 & 0x000000ff)
                                > (weight_n2 & 0x000000ff))
                                ? (weight_n1 & 0x000000ff)
                                : (weight_n2 & 0x000000ff))
                        ));

                parent[nNodes] = -1;
                nHeap++;
                heap[nHeap] = nNodes;

                tmp = 0;
                zz = nHeap;
                tmp = heap[zz];
                final int weight_tmp = weight[tmp];
                while (weight_tmp < weight[heap[zz >> 1]]) {
                    heap[zz] = heap[zz >> 1];
                    zz >>= 1;
                }
                heap[zz] = tmp;

            }

            

            for (int i = 1; i <= alphaSize; i++) {
                int j = 0;
                int k = i;

                for (int parent_k; (parent_k = parent[k]) >= 0; ) {
                    k = parent_k;
                    j++;
                }

                len[i - 1] = (char) j;
                if (j > maxLen) {
                    tooLong = true;
                }
            }

            if (tooLong) {
                for (int i = 1; i < alphaSize; i++) {
                    int j = weight[i] >> 8;
                    j = 1 + (j >> 1);
                    weight[i] = j << 8;
                }
            }
        }
    }

    
    /**
     * Hb make code lengths.
     *
     * @param len the len
     * @param freq the freq
     * @param dat the dat
     * @param alphaSize the alpha size
     * @param maxLen the max len
     */
    private static void hbMakeCodeLengths(final byte[] len, final int[] freq,
                                          final Data dat, final int alphaSize,
                                          final int maxLen) {
        
        final int[] heap = dat.heap;
        final int[] weight = dat.weight;
        final int[] parent = dat.parent;

        for (int i = alphaSize; --i >= 0; ) {
            weight[i + 1] = (freq[i] == 0 ? 1 : freq[i]) << 8;
        }

        for (boolean tooLong = true; tooLong; ) {
            tooLong = false;

            int nNodes = alphaSize;
            int nHeap = 0;
            heap[0] = 0;
            weight[0] = 0;
            parent[0] = -2;

            for (int i = 1; i <= alphaSize; i++) {
                parent[i] = -1;
                nHeap++;
                heap[nHeap] = i;

                int zz = nHeap;
                int tmp = heap[zz];
                while (weight[tmp] < weight[heap[zz >> 1]]) {
                    heap[zz] = heap[zz >> 1];
                    zz >>= 1;
                }
                heap[zz] = tmp;
            }

            while (nHeap > 1) {
                int n1 = heap[1];
                heap[1] = heap[nHeap];
                nHeap--;

                int yy = 0;
                int zz = 1;
                int tmp = heap[1];

                while (true) {
                    yy = zz << 1;

                    if (yy > nHeap) {
                        break;
                    }

                    if ((yy < nHeap)
                            && (weight[heap[yy + 1]] < weight[heap[yy]])) {
                        yy++;
                    }

                    if (weight[tmp] < weight[heap[yy]]) {
                        break;
                    }

                    heap[zz] = heap[yy];
                    zz = yy;
                }

                heap[zz] = tmp;

                int n2 = heap[1];
                heap[1] = heap[nHeap];
                nHeap--;

                yy = 0;
                zz = 1;
                tmp = heap[1];

                while (true) {
                    yy = zz << 1;

                    if (yy > nHeap) {
                        break;
                    }

                    if ((yy < nHeap)
                            && (weight[heap[yy + 1]] < weight[heap[yy]])) {
                        yy++;
                    }

                    if (weight[tmp] < weight[heap[yy]]) {
                        break;
                    }

                    heap[zz] = heap[yy];
                    zz = yy;
                }

                heap[zz] = tmp;
                nNodes++;
                parent[n1] = parent[n2] = nNodes;

                final int weight_n1 = weight[n1];
                final int weight_n2 = weight[n2];
                weight[nNodes] = ((weight_n1 & 0xffffff00)
                        + (weight_n2 & 0xffffff00))
                        | (1 + (((weight_n1 & 0x000000ff)
                        > (weight_n2 & 0x000000ff))
                        ? (weight_n1 & 0x000000ff)
                        : (weight_n2 & 0x000000ff)));

                parent[nNodes] = -1;
                nHeap++;
                heap[nHeap] = nNodes;

                tmp = 0;
                zz = nHeap;
                tmp = heap[zz];
                final int weight_tmp = weight[tmp];
                while (weight_tmp < weight[heap[zz >> 1]]) {
                    heap[zz] = heap[zz >> 1];
                    zz >>= 1;
                }
                heap[zz] = tmp;

            }

            for (int i = 1; i <= alphaSize; i++) {
                int j = 0;
                int k = i;

                for (int parent_k; (parent_k = parent[k]) >= 0; ) {
                    k = parent_k;
                    j++;
                }

                len[i - 1] = (byte) j;
                if (j > maxLen) {
                    tooLong = true;
                }
            }

            if (tooLong) {
                for (int i = 1; i < alphaSize; i++) {
                    int j = weight[i] >> 8;
                    j = 1 + (j >> 1);
                    weight[i] = j << 8;
                }
            }
        }
    }

    
    /** The last. */
    private int last;

    
    /** The orig ptr. */
    private int origPtr;

    
    /** The block size100k. */
    private final int blockSize100k;

    
    /** The block randomised. */
    private boolean blockRandomised;

    
    /** The bs buff. */
    private int bsBuff;
    
    
    /** The bs live. */
    private int bsLive;
    
    
    /** The crc. */
    private final CRC crc = new CRC();

    
    /** The n in use. */
    private int nInUse;

    
    /** The n mtf. */
    private int nMTF;

    
    
    /** The work done. */
    private int workDone;
    
    
    /** The work limit. */
    private int workLimit;
    
    
    /** The first attempt. */
    private boolean firstAttempt;

    
    /** The current char. */
    private int currentChar = -1;
    
    
    /** The run length. */
    private int runLength = 0;

    
    /** The block crc. */
    private int blockCRC;
    
    
    /** The combined crc. */
    private int combinedCRC;
    
    
    /** The allowable block size. */
    private int allowableBlockSize;

    
    /** The data. */
    private Data data;

    
    /** The out. */
    private OutputStream out;

    
    /**
     * Choose block size.
     *
     * @param inputLength the input length
     * @return the int
     */
    public static int chooseBlockSize(long inputLength) {
        return (inputLength > 0) ? (int) Math
                .min((inputLength / 132000) + 1, 9) : MAX_BLOCKSIZE;
    }

    
    /**
     * Instantiates a new cB zip2 output stream.
     *
     * @param out the out
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public CBZip2OutputStream(final OutputStream out) throws IOException {
        this(out, MAX_BLOCKSIZE);
    }

    
    /**
     * Instantiates a new cB zip2 output stream.
     *
     * @param out the out
     * @param blockSize the block size
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public CBZip2OutputStream(final OutputStream out, final int blockSize)
            throws IOException {
        super();

        if (blockSize < 1) {
            throw new IllegalArgumentException("blockSize(" + blockSize
                    + ") < 1");
        }
        if (blockSize > 9) {
            throw new IllegalArgumentException("blockSize(" + blockSize
                    + ") > 9");
        }

        this.blockSize100k = blockSize;
        this.out = out;
        init();
    }

    
    /* (non-Javadoc)
     * @see java.io.OutputStream#write(int)
     */
    public void write(final int b) throws IOException {
        if (this.out != null) {
            write0(b);
        } else {
            throw new IOException("closed");
        }
    }

    
    /**
     * Write run.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void writeRun() throws IOException {
        final int lastShadow = this.last;

        if (lastShadow < this.allowableBlockSize) {
            final int currentCharShadow = this.currentChar;
            final Data dataShadow = this.data;
            dataShadow.inUse[currentCharShadow] = true;
            final byte ch = (byte) currentCharShadow;

            int runLengthShadow = this.runLength;
            this.crc.updateCRC(currentCharShadow, runLengthShadow);

            switch (runLengthShadow) {
                case 1:
                    dataShadow.block[lastShadow + 2] = ch;
                    this.last = lastShadow + 1;
                    break;

                case 2:
                    dataShadow.block[lastShadow + 2] = ch;
                    dataShadow.block[lastShadow + 3] = ch;
                    this.last = lastShadow + 2;
                    break;

                case 3: {
                    final byte[] block = dataShadow.block;
                    block[lastShadow + 2] = ch;
                    block[lastShadow + 3] = ch;
                    block[lastShadow + 4] = ch;
                    this.last = lastShadow + 3;
                }
                break;

                default: {
                    runLengthShadow -= 4;
                    dataShadow.inUse[runLengthShadow] = true;
                    final byte[] block = dataShadow.block;
                    block[lastShadow + 2] = ch;
                    block[lastShadow + 3] = ch;
                    block[lastShadow + 4] = ch;
                    block[lastShadow + 5] = ch;
                    block[lastShadow + 6] = (byte) runLengthShadow;
                    this.last = lastShadow + 5;
                }
                break;

            }
        } else {
            endBlock();
            initBlock();
            writeRun();
        }
    }

    
    /* (non-Javadoc)
     * @see java.lang.Object#finalize()
     */
    protected void finalize() throws Throwable {
        finish();
        super.finalize();
    }


    
    /**
     * Finish.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void finish() throws IOException {
        if (out != null) {
            try {
                if (this.runLength > 0) {
                    writeRun();
                }
                this.currentChar = -1;
                endBlock();
                endCompression();
            } finally {
                this.out = null;
                this.data = null;
            }
        }
    }

    
    /* (non-Javadoc)
     * @see java.io.OutputStream#close()
     */
    public void close() throws IOException {
        if (out != null) {
            OutputStream outShadow = this.out;
            finish();
            outShadow.close();
        }
    }

    
    /* (non-Javadoc)
     * @see java.io.OutputStream#flush()
     */
    public void flush() throws IOException {
        OutputStream outShadow = this.out;
        if (outShadow != null) {
            outShadow.flush();
        }
    }

    
    /**
     * Inits the.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void init() throws IOException {
        
        
        

        this.data = new Data(this.blockSize100k);

        
        bsPutUByte('h');
        bsPutUByte('0' + this.blockSize100k);

        this.combinedCRC = 0;
        initBlock();
    }

    
    /**
     * Inits the block.
     */
    private void initBlock() {
        
        this.crc.initialiseCRC();
        this.last = -1;
        

        boolean[] inUse = this.data.inUse;
        for (int i = 256; --i >= 0; ) {
            inUse[i] = false;
        }

        
        this.allowableBlockSize = (this.blockSize100k * BZip2Constants.baseBlockSize) - 20;
    }

    
    /**
     * End block.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void endBlock() throws IOException {
        this.blockCRC = this.crc.getFinalCRC();
        this.combinedCRC = (this.combinedCRC << 1) | (this.combinedCRC >>> 31);
        this.combinedCRC ^= this.blockCRC;

        
        if (this.last == -1) {
            return;
        }

        
        blockSort();

        
        bsPutUByte(0x31);
        bsPutUByte(0x41);
        bsPutUByte(0x59);
        bsPutUByte(0x26);
        bsPutUByte(0x53);
        bsPutUByte(0x59);

        
        bsPutInt(this.blockCRC);

        
        if (this.blockRandomised) {
            bsW(1, 1);
        } else {
            bsW(1, 0);
        }

        
        moveToFrontCodeAndSend();
    }

    
    /**
     * End compression.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void endCompression() throws IOException {
        
        bsPutUByte(0x17);
        bsPutUByte(0x72);
        bsPutUByte(0x45);
        bsPutUByte(0x38);
        bsPutUByte(0x50);
        bsPutUByte(0x90);

        bsPutInt(this.combinedCRC);
        bsFinishedWithStream();
    }

    
    /**
     * Gets the block size.
     *
     * @return the block size
     */
    public final int getBlockSize() {
        return this.blockSize100k;
    }

    
    /* (non-Javadoc)
     * @see java.io.OutputStream#write(byte[], int, int)
     */
    public void write(final byte[] buf, int offs, final int len)
            throws IOException {
        if (offs < 0) {
            throw new IndexOutOfBoundsException("offs(" + offs + ") < 0.");
        }
        if (len < 0) {
            throw new IndexOutOfBoundsException("len(" + len + ") < 0.");
        }
        if (offs + len > buf.length) {
            throw new IndexOutOfBoundsException("offs(" + offs + ") + len("
                    + len + ") > buf.length("
                    + buf.length + ").");
        }
        if (this.out == null) {
            throw new IOException("stream closed");
        }

        for (int hi = offs + len; offs < hi; ) {
            write0(buf[offs++]);
        }
    }

    
    /**
     * Write0.
     *
     * @param b the b
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void write0(int b) throws IOException {
        if (this.currentChar != -1) {
            b &= 0xff;
            if (this.currentChar == b) {
                if (++this.runLength > 254) {
                    writeRun();
                    this.currentChar = -1;
                    this.runLength = 0;
                }
                
            } else {
                writeRun();
                this.runLength = 1;
                this.currentChar = b;
            }
        } else {
            this.currentChar = b & 0xff;
            this.runLength++;
        }
    }

    
    /**
     * Hb assign codes.
     *
     * @param code the code
     * @param length the length
     * @param minLen the min len
     * @param maxLen the max len
     * @param alphaSize the alpha size
     */
    private static void hbAssignCodes(final int[] code, final byte[] length,
                                      final int minLen, final int maxLen,
                                      final int alphaSize) {
        int vec = 0;
        for (int n = minLen; n <= maxLen; n++) {
            for (int i = 0; i < alphaSize; i++) {
                if ((length[i] & 0xff) == n) {
                    code[i] = vec;
                    vec++;
                }
            }
            vec <<= 1;
        }
    }

    
    /**
     * Bs finished with stream.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void bsFinishedWithStream() throws IOException {
        while (this.bsLive > 0) {
            int ch = this.bsBuff >> 24;
            this.out.write(ch); 
            this.bsBuff <<= 8;
            this.bsLive -= 8;
        }
    }

    
    /**
     * Bs w.
     *
     * @param n the n
     * @param v the v
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void bsW(final int n, final int v) throws IOException {
        final OutputStream outShadow = this.out;
        int bsLiveShadow = this.bsLive;
        int bsBuffShadow = this.bsBuff;

        while (bsLiveShadow >= 8) {
            outShadow.write(bsBuffShadow >> 24); 
            bsBuffShadow <<= 8;
            bsLiveShadow -= 8;
        }

        this.bsBuff = bsBuffShadow | (v << (32 - bsLiveShadow - n));
        this.bsLive = bsLiveShadow + n;
    }

    
    /**
     * Bs put u byte.
     *
     * @param c the c
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void bsPutUByte(final int c) throws IOException {
        bsW(8, c);
    }

    
    /**
     * Bs put int.
     *
     * @param u the u
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void bsPutInt(final int u) throws IOException {
        bsW(8, (u >> 24) & 0xff);
        bsW(8, (u >> 16) & 0xff);
        bsW(8, (u >> 8) & 0xff);
        bsW(8, u & 0xff);
    }

    
    /**
     * Send mtf values.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void sendMTFValues() throws IOException {
        final byte[][] len = this.data.sendMTFValues_len;
        final int alphaSize = this.nInUse + 2;

        for (int t = N_GROUPS; --t >= 0; ) {
            byte[] len_t = len[t];
            for (int v = alphaSize; --v >= 0; ) {
                len_t[v] = GREATER_ICOST;
            }
        }

        
        
        final int nGroups = (this.nMTF < 200) ? 2 : (this.nMTF < 600) ? 3
                : (this.nMTF < 1200) ? 4 : (this.nMTF < 2400) ? 5 : 6;

        
        sendMTFValues0(nGroups, alphaSize);

        
        final int nSelectors = sendMTFValues1(nGroups, alphaSize);

        
        sendMTFValues2(nGroups, nSelectors);

        
        sendMTFValues3(nGroups, alphaSize);

        
        sendMTFValues4();

        
        sendMTFValues5(nGroups, nSelectors);

        
        sendMTFValues6(nGroups, alphaSize);

        
        sendMTFValues7(nSelectors);
    }

    
    /**
     * Send mtf values0.
     *
     * @param nGroups the n groups
     * @param alphaSize the alpha size
     */
    private void sendMTFValues0(final int nGroups, final int alphaSize) {
        final byte[][] len = this.data.sendMTFValues_len;
        final int[] mtfFreq = this.data.mtfFreq;

        int remF = this.nMTF;
        int gs = 0;

        for (int nPart = nGroups; nPart > 0; nPart--) {
            final int tFreq = remF / nPart;
            int ge = gs - 1;
            int aFreq = 0;

            for (final int a = alphaSize - 1; (aFreq < tFreq) && (ge < a); ) {
                aFreq += mtfFreq[++ge];
            }

            if ((ge > gs) && (nPart != nGroups) && (nPart != 1)
                    && (((nGroups - nPart) & 1) != 0)) {
                aFreq -= mtfFreq[ge--];
            }

            final byte[] len_np = len[nPart - 1];
            for (int v = alphaSize; --v >= 0; ) {
                if ((v >= gs) && (v <= ge)) {
                    len_np[v] = LESSER_ICOST;
                } else {
                    len_np[v] = GREATER_ICOST;
                }
            }

            gs = ge + 1;
            remF -= aFreq;
        }
    }

    
    /**
     * Send mtf values1.
     *
     * @param nGroups the n groups
     * @param alphaSize the alpha size
     * @return the int
     */
    private int sendMTFValues1(final int nGroups, final int alphaSize) {
        final Data dataShadow = this.data;
        final int[][] rfreq = dataShadow.sendMTFValues_rfreq;
        final int[] fave = dataShadow.sendMTFValues_fave;
        final short[] cost = dataShadow.sendMTFValues_cost;
        final char[] sfmap = dataShadow.sfmap;
        final byte[] selector = dataShadow.selector;
        final byte[][] len = dataShadow.sendMTFValues_len;
        final byte[] len_0 = len[0];
        final byte[] len_1 = len[1];
        final byte[] len_2 = len[2];
        final byte[] len_3 = len[3];
        final byte[] len_4 = len[4];
        final byte[] len_5 = len[5];
        final int nMTFShadow = this.nMTF;

        int nSelectors = 0;

        for (int iter = 0; iter < N_ITERS; iter++) {
            for (int t = nGroups; --t >= 0; ) {
                fave[t] = 0;
                int[] rfreqt = rfreq[t];
                for (int i = alphaSize; --i >= 0; ) {
                    rfreqt[i] = 0;
                }
            }

            nSelectors = 0;

            for (int gs = 0; gs < this.nMTF; ) {
                

                

                final int ge = Math.min(gs + G_SIZE - 1, nMTFShadow - 1);

                if (nGroups == N_GROUPS) {
                    

                    short cost0 = 0;
                    short cost1 = 0;
                    short cost2 = 0;
                    short cost3 = 0;
                    short cost4 = 0;
                    short cost5 = 0;

                    for (int i = gs; i <= ge; i++) {
                        final int icv = sfmap[i];
                        cost0 += len_0[icv] & 0xff;
                        cost1 += len_1[icv] & 0xff;
                        cost2 += len_2[icv] & 0xff;
                        cost3 += len_3[icv] & 0xff;
                        cost4 += len_4[icv] & 0xff;
                        cost5 += len_5[icv] & 0xff;
                    }

                    cost[0] = cost0;
                    cost[1] = cost1;
                    cost[2] = cost2;
                    cost[3] = cost3;
                    cost[4] = cost4;
                    cost[5] = cost5;

                } else {
                    for (int t = nGroups; --t >= 0; ) {
                        cost[t] = 0;
                    }

                    for (int i = gs; i <= ge; i++) {
                        final int icv = sfmap[i];
                        for (int t = nGroups; --t >= 0; ) {
                            cost[t] += len[t][icv] & 0xff;
                        }
                    }
                }

                
                int bt = -1;
                for (int t = nGroups, bc = 999999999; --t >= 0; ) {
                    final int cost_t = cost[t];
                    if (cost_t < bc) {
                        bc = cost_t;
                        bt = t;
                    }
                }

                fave[bt]++;
                selector[nSelectors] = (byte) bt;
                nSelectors++;

                
                final int[] rfreq_bt = rfreq[bt];
                for (int i = gs; i <= ge; i++) {
                    rfreq_bt[sfmap[i]]++;
                }

                gs = ge + 1;
            }

            
            for (int t = 0; t < nGroups; t++) {
                hbMakeCodeLengths(len[t], rfreq[t], this.data, alphaSize, 20);
            }
        }

        return nSelectors;
    }

    
    /**
     * Send mtf values2.
     *
     * @param nGroups the n groups
     * @param nSelectors the n selectors
     */
    private void sendMTFValues2(final int nGroups, final int nSelectors) {
        

        final Data dataShadow = this.data;
        byte[] pos = dataShadow.sendMTFValues2_pos;

        for (int i = nGroups; --i >= 0; ) {
            pos[i] = (byte) i;
        }

        for (int i = 0; i < nSelectors; i++) {
            final byte ll_i = dataShadow.selector[i];
            byte tmp = pos[0];
            int j = 0;

            while (ll_i != tmp) {
                j++;
                byte tmp2 = tmp;
                tmp = pos[j];
                pos[j] = tmp2;
            }

            pos[0] = tmp;
            dataShadow.selectorMtf[i] = (byte) j;
        }
    }

    
    /**
     * Send mtf values3.
     *
     * @param nGroups the n groups
     * @param alphaSize the alpha size
     */
    private void sendMTFValues3(final int nGroups, final int alphaSize) {
        int[][] code = this.data.sendMTFValues_code;
        byte[][] len = this.data.sendMTFValues_len;

        for (int t = 0; t < nGroups; t++) {
            int minLen = 32;
            int maxLen = 0;
            final byte[] len_t = len[t];
            for (int i = alphaSize; --i >= 0; ) {
                final int l = len_t[i] & 0xff;
                if (l > maxLen) {
                    maxLen = l;
                }
                if (l < minLen) {
                    minLen = l;
                }
            }

            
            

            hbAssignCodes(code[t], len[t], minLen, maxLen, alphaSize);
        }
    }

    
    /**
     * Send mtf values4.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void sendMTFValues4() throws IOException {
        final boolean[] inUse = this.data.inUse;
        final boolean[] inUse16 = this.data.sentMTFValues4_inUse16;

        for (int i = 16; --i >= 0; ) {
            inUse16[i] = false;
            final int i16 = i * 16;
            for (int j = 16; --j >= 0; ) {
                if (inUse[i16 + j]) {
                    inUse16[i] = true;
                }
            }
        }

        for (int i = 0; i < 16; i++) {
            bsW(1, inUse16[i] ? 1 : 0);
        }

        final OutputStream outShadow = this.out;
        int bsLiveShadow = this.bsLive;
        int bsBuffShadow = this.bsBuff;

        for (int i = 0; i < 16; i++) {
            if (inUse16[i]) {
                final int i16 = i * 16;
                for (int j = 0; j < 16; j++) {
                    
                    while (bsLiveShadow >= 8) {
                        outShadow.write(bsBuffShadow >> 24); 
                        bsBuffShadow <<= 8;
                        bsLiveShadow -= 8;
                    }
                    if (inUse[i16 + j]) {
                        bsBuffShadow |= 1 << (32 - bsLiveShadow - 1);
                    }
                    bsLiveShadow++;
                }
            }
        }

        this.bsBuff = bsBuffShadow;
        this.bsLive = bsLiveShadow;
    }

    
    /**
     * Send mtf values5.
     *
     * @param nGroups the n groups
     * @param nSelectors the n selectors
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void sendMTFValues5(final int nGroups, final int nSelectors)
            throws IOException {
        bsW(3, nGroups);
        bsW(15, nSelectors);

        final OutputStream outShadow = this.out;
        final byte[] selectorMtf = this.data.selectorMtf;

        int bsLiveShadow = this.bsLive;
        int bsBuffShadow = this.bsBuff;

        for (int i = 0; i < nSelectors; i++) {
            for (int j = 0, hj = selectorMtf[i] & 0xff; j < hj; j++) {
                
                while (bsLiveShadow >= 8) {
                    outShadow.write(bsBuffShadow >> 24);
                    bsBuffShadow <<= 8;
                    bsLiveShadow -= 8;
                }
                bsBuffShadow |= 1 << (32 - bsLiveShadow - 1);
                bsLiveShadow++;
            }

            
            while (bsLiveShadow >= 8) {
                outShadow.write(bsBuffShadow >> 24);
                bsBuffShadow <<= 8;
                bsLiveShadow -= 8;
            }
            
            bsLiveShadow++;
        }

        this.bsBuff = bsBuffShadow;
        this.bsLive = bsLiveShadow;
    }

    
    /**
     * Send mtf values6.
     *
     * @param nGroups the n groups
     * @param alphaSize the alpha size
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void sendMTFValues6(final int nGroups, final int alphaSize)
            throws IOException {
        final byte[][] len = this.data.sendMTFValues_len;
        final OutputStream outShadow = this.out;

        int bsLiveShadow = this.bsLive;
        int bsBuffShadow = this.bsBuff;

        for (int t = 0; t < nGroups; t++) {
            byte[] len_t = len[t];
            int curr = len_t[0] & 0xff;

            
            while (bsLiveShadow >= 8) {
                outShadow.write(bsBuffShadow >> 24); 
                bsBuffShadow <<= 8;
                bsLiveShadow -= 8;
            }
            bsBuffShadow |= curr << (32 - bsLiveShadow - 5);
            bsLiveShadow += 5;

            for (int i = 0; i < alphaSize; i++) {
                int lti = len_t[i] & 0xff;
                while (curr < lti) {
                    
                    while (bsLiveShadow >= 8) {
                        outShadow.write(bsBuffShadow >> 24); 
                        bsBuffShadow <<= 8;
                        bsLiveShadow -= 8;
                    }
                    bsBuffShadow |= 2 << (32 - bsLiveShadow - 2);
                    bsLiveShadow += 2;

                    curr++; 
                }

                while (curr > lti) {
                    
                    while (bsLiveShadow >= 8) {
                        outShadow.write(bsBuffShadow >> 24); 
                        bsBuffShadow <<= 8;
                        bsLiveShadow -= 8;
                    }
                    bsBuffShadow |= 3 << (32 - bsLiveShadow - 2);
                    bsLiveShadow += 2;

                    curr--; 
                }

                
                while (bsLiveShadow >= 8) {
                    outShadow.write(bsBuffShadow >> 24); 
                    bsBuffShadow <<= 8;
                    bsLiveShadow -= 8;
                }
                
                bsLiveShadow++;
            }
        }

        this.bsBuff = bsBuffShadow;
        this.bsLive = bsLiveShadow;
    }

    
    /**
     * Send mtf values7.
     *
     * @param nSelectors the n selectors
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void sendMTFValues7(final int nSelectors) throws IOException {
        final Data dataShadow = this.data;
        final byte[][] len = dataShadow.sendMTFValues_len;
        final int[][] code = dataShadow.sendMTFValues_code;
        final OutputStream outShadow = this.out;
        final byte[] selector = dataShadow.selector;
        final char[] sfmap = dataShadow.sfmap;
        final int nMTFShadow = this.nMTF;

        int selCtr = 0;

        int bsLiveShadow = this.bsLive;
        int bsBuffShadow = this.bsBuff;

        for (int gs = 0; gs < nMTFShadow; ) {
            final int ge = Math.min(gs + G_SIZE - 1, nMTFShadow - 1);
            final int selector_selCtr = selector[selCtr] & 0xff;
            final int[] code_selCtr = code[selector_selCtr];
            final byte[] len_selCtr = len[selector_selCtr];

            while (gs <= ge) {
                final int sfmap_i = sfmap[gs];

                
                
                
                
                while (bsLiveShadow >= 8) {
                    outShadow.write(bsBuffShadow >> 24);
                    bsBuffShadow <<= 8;
                    bsLiveShadow -= 8;
                }
                final int n = len_selCtr[sfmap_i] & 0xFF;
                bsBuffShadow |= code_selCtr[sfmap_i] << (32 - bsLiveShadow - n);
                bsLiveShadow += n;

                gs++;
            }

            gs = ge + 1;
            selCtr++;
        }

        this.bsBuff = bsBuffShadow;
        this.bsLive = bsLiveShadow;
    }

    
    /**
     * Move to front code and send.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void moveToFrontCodeAndSend() throws IOException {
        bsW(24, this.origPtr);
        generateMTFValues();
        sendMTFValues();
    }

    
    /**
     * Main simple sort.
     *
     * @param dataShadow the data shadow
     * @param lo the lo
     * @param hi the hi
     * @param d the d
     * @return true, if successful
     */
    private boolean mainSimpleSort(final Data dataShadow, final int lo,
                                   final int hi, final int d) {
        final int bigN = hi - lo + 1;
        if (bigN < 2) {
            return this.firstAttempt && (this.workDone > this.workLimit);
        }

        int hp = 0;
        while (INCS[hp] < bigN) {
            hp++;
        }

        final int[] fmap = dataShadow.fmap;
        final char[] quadrant = dataShadow.quadrant;
        final byte[] block = dataShadow.block;
        final int lastShadow = this.last;
        final int lastPlus1 = lastShadow + 1;
        final boolean firstAttemptShadow = this.firstAttempt;
        final int workLimitShadow = this.workLimit;
        int workDoneShadow = this.workDone;

        
        

        HP:
        while (--hp >= 0) {
            final int h = INCS[hp];
            final int mj = lo + h - 1;

            for (int i = lo + h; i <= hi; ) {
                
                for (int k = 3; (i <= hi) && (--k >= 0); i++) {
                    final int v = fmap[i];
                    final int vd = v + d;
                    int j = i;

                    
                    
                    
                    
                    
                    
                    
                    

                    
                    boolean onceRunned = false;
                    int a = 0;

                    HAMMER:
                    while (true) {
                        if (onceRunned) {
                            fmap[j] = a;
                            if ((j -= h) <= mj) {
                                break HAMMER;
                            }
                        } else {
                            onceRunned = true;
                        }

                        a = fmap[j - h];
                        int i1 = a + d;
                        int i2 = vd;

                        
                        
                        if (block[i1 + 1] == block[i2 + 1]) {
                            if (block[i1 + 2] == block[i2 + 2]) {
                                if (block[i1 + 3] == block[i2 + 3]) {
                                    if (block[i1 + 4] == block[i2 + 4]) {
                                        if (block[i1 + 5] == block[i2 + 5]) {
                                            if (block[(i1 += 6)] == block[(i2 += 6)]) {
                                                int x = lastShadow;
                                                X:
                                                while (x > 0) {
                                                    x -= 4;

                                                    if (block[i1 + 1] == block[i2 + 1]) {
                                                        if (quadrant[i1] == quadrant[i2]) {
                                                            if (block[i1 + 2] == block[i2 + 2]) {
                                                                if (quadrant[i1 + 1] == quadrant[i2 + 1]) {
                                                                    if (block[i1 + 3] == block[i2 + 3]) {
                                                                        if (quadrant[i1 + 2] == quadrant[i2 + 2]) {
                                                                            if (block[i1 + 4] == block[i2 + 4]) {
                                                                                if (quadrant[i1 + 3] == quadrant[i2 + 3]) {
                                                                                    if ((i1 += 4) >= lastPlus1) {
                                                                                        i1 -= lastPlus1;
                                                                                    }
                                                                                    if ((i2 += 4) >= lastPlus1) {
                                                                                        i2 -= lastPlus1;
                                                                                    }
                                                                                    workDoneShadow++;
                                                                                    continue X;
                                                                                } else if ((quadrant[i1 + 3] > quadrant[i2 + 3])) {
                                                                                    continue HAMMER;
                                                                                } else {
                                                                                    break HAMMER;
                                                                                }
                                                                            } else if ((block[i1 + 4] & 0xff) > (block[i2 + 4] & 0xff)) {
                                                                                continue HAMMER;
                                                                            } else {
                                                                                break HAMMER;
                                                                            }
                                                                        } else if ((quadrant[i1 + 2] > quadrant[i2 + 2])) {
                                                                            continue HAMMER;
                                                                        } else {
                                                                            break HAMMER;
                                                                        }
                                                                    } else if ((block[i1 + 3] & 0xff) > (block[i2 + 3] & 0xff)) {
                                                                        continue HAMMER;
                                                                    } else {
                                                                        break HAMMER;
                                                                    }
                                                                } else if ((quadrant[i1 + 1] > quadrant[i2 + 1])) {
                                                                    continue HAMMER;
                                                                } else {
                                                                    break HAMMER;
                                                                }
                                                            } else if ((block[i1 + 2] & 0xff) > (block[i2 + 2] & 0xff)) {
                                                                continue HAMMER;
                                                            } else {
                                                                break HAMMER;
                                                            }
                                                        } else if ((quadrant[i1] > quadrant[i2])) {
                                                            continue HAMMER;
                                                        } else {
                                                            break HAMMER;
                                                        }
                                                    } else if ((block[i1 + 1] & 0xff) > (block[i2 + 1] & 0xff)) {
                                                        continue HAMMER;
                                                    } else {
                                                        break HAMMER;
                                                    }

                                                }
                                                break HAMMER;
                                            } 
                                            else {
                                                if ((block[i1] & 0xff) > (block[i2] & 0xff)) {
                                                    continue HAMMER;
                                                } else {
                                                    break HAMMER;
                                                }
                                            }
                                        } else if ((block[i1 + 5] & 0xff) > (block[i2 + 5] & 0xff)) {
                                            continue HAMMER;
                                        } else {
                                            break HAMMER;
                                        }
                                    } else if ((block[i1 + 4] & 0xff) > (block[i2 + 4] & 0xff)) {
                                        continue HAMMER;
                                    } else {
                                        break HAMMER;
                                    }
                                } else if ((block[i1 + 3] & 0xff) > (block[i2 + 3] & 0xff)) {
                                    continue HAMMER;
                                } else {
                                    break HAMMER;
                                }
                            } else if ((block[i1 + 2] & 0xff) > (block[i2 + 2] & 0xff)) {
                                continue HAMMER;
                            } else {
                                break HAMMER;
                            }
                        } else if ((block[i1 + 1] & 0xff) > (block[i2 + 1] & 0xff)) {
                            continue HAMMER;
                        } else {
                            break HAMMER;
                        }

                    }
                    
                    

                    fmap[j] = v;
                }

                if (firstAttemptShadow && (i <= hi)
                        && (workDoneShadow > workLimitShadow)) {
                    break HP;
                }
            }
        }

        this.workDone = workDoneShadow;
        return firstAttemptShadow && (workDoneShadow > workLimitShadow);
    }

    
    /**
     * Vswap.
     *
     * @param fmap the fmap
     * @param p1 the p1
     * @param p2 the p2
     * @param n the n
     */
    private static void vswap(int[] fmap, int p1, int p2, int n) {
        n += p1;
        while (p1 < n) {
            int t = fmap[p1];
            fmap[p1++] = fmap[p2];
            fmap[p2++] = t;
        }
    }

    
    /**
     * Med3.
     *
     * @param a the a
     * @param b the b
     * @param c the c
     * @return the byte
     */
    private static byte med3(byte a, byte b, byte c) {
        return (a < b) ? (b < c ? b : a < c ? c : a) : (b > c ? b : a > c ? c
                : a);
    }

    
    /**
     * Block sort.
     */
    private void blockSort() {
        this.workLimit = WORK_FACTOR * this.last;
        this.workDone = 0;
        this.blockRandomised = false;
        this.firstAttempt = true;
        mainSort();

        if (this.firstAttempt && (this.workDone > this.workLimit)) {
            randomiseBlock();
            this.workLimit = this.workDone = 0;
            this.firstAttempt = false;
            mainSort();
        }

        int[] fmap = this.data.fmap;
        this.origPtr = -1;
        for (int i = 0, lastShadow = this.last; i <= lastShadow; i++) {
            if (fmap[i] == 0) {
                this.origPtr = i;
                break;
            }
        }

        
    }

    
    /**
     * Main q sort3.
     *
     * @param dataShadow the data shadow
     * @param loSt the lo st
     * @param hiSt the hi st
     * @param dSt the d st
     */
    private void mainQSort3(final Data dataShadow, final int loSt,
                            final int hiSt, final int dSt) {
        final int[] stack_ll = dataShadow.stack_ll;
        final int[] stack_hh = dataShadow.stack_hh;
        final int[] stack_dd = dataShadow.stack_dd;
        final int[] fmap = dataShadow.fmap;
        final byte[] block = dataShadow.block;

        stack_ll[0] = loSt;
        stack_hh[0] = hiSt;
        stack_dd[0] = dSt;

        for (int sp = 1; --sp >= 0; ) {
            final int lo = stack_ll[sp];
            final int hi = stack_hh[sp];
            final int d = stack_dd[sp];

            if ((hi - lo < SMALL_THRESH) || (d > DEPTH_THRESH)) {
                if (mainSimpleSort(dataShadow, lo, hi, d)) {
                    return;
                }
            } else {
                final int d1 = d + 1;
                final int med = med3(block[fmap[lo] + d1],
                        block[fmap[hi] + d1], block[fmap[(lo + hi) >>> 1] + d1]) & 0xff;

                int unLo = lo;
                int unHi = hi;
                int ltLo = lo;
                int gtHi = hi;

                while (true) {
                    while (unLo <= unHi) {
                        final int n = ((int) block[fmap[unLo] + d1] & 0xff)
                                - med;
                        if (n == 0) {
                            final int temp = fmap[unLo];
                            fmap[unLo++] = fmap[ltLo];
                            fmap[ltLo++] = temp;
                        } else if (n < 0) {
                            unLo++;
                        } else {
                            break;
                        }
                    }

                    while (unLo <= unHi) {
                        final int n = ((int) block[fmap[unHi] + d1] & 0xff)
                                - med;
                        if (n == 0) {
                            final int temp = fmap[unHi];
                            fmap[unHi--] = fmap[gtHi];
                            fmap[gtHi--] = temp;
                        } else if (n > 0) {
                            unHi--;
                        } else {
                            break;
                        }
                    }

                    if (unLo <= unHi) {
                        final int temp = fmap[unLo];
                        fmap[unLo++] = fmap[unHi];
                        fmap[unHi--] = temp;
                    } else {
                        break;
                    }
                }

                if (gtHi < ltLo) {
                    stack_ll[sp] = lo;
                    stack_hh[sp] = hi;
                    stack_dd[sp] = d1;
                    sp++;
                } else {
                    int n = ((ltLo - lo) < (unLo - ltLo)) ? (ltLo - lo)
                            : (unLo - ltLo);
                    vswap(fmap, lo, unLo - n, n);
                    int m = ((hi - gtHi) < (gtHi - unHi)) ? (hi - gtHi)
                            : (gtHi - unHi);
                    vswap(fmap, unLo, hi - m + 1, m);

                    n = lo + unLo - ltLo - 1;
                    m = hi - (gtHi - unHi) + 1;

                    stack_ll[sp] = lo;
                    stack_hh[sp] = n;
                    stack_dd[sp] = d;
                    sp++;

                    stack_ll[sp] = n + 1;
                    stack_hh[sp] = m - 1;
                    stack_dd[sp] = d1;
                    sp++;

                    stack_ll[sp] = m;
                    stack_hh[sp] = hi;
                    stack_dd[sp] = d;
                    sp++;
                }
            }
        }
    }

    
    /**
     * Main sort.
     */
    private void mainSort() {
        final Data dataShadow = this.data;
        final int[] runningOrder = dataShadow.mainSort_runningOrder;
        final int[] copy = dataShadow.mainSort_copy;
        final boolean[] bigDone = dataShadow.mainSort_bigDone;
        final int[] ftab = dataShadow.ftab;
        final byte[] block = dataShadow.block;
        final int[] fmap = dataShadow.fmap;
        final char[] quadrant = dataShadow.quadrant;
        final int lastShadow = this.last;
        final int workLimitShadow = this.workLimit;
        final boolean firstAttemptShadow = this.firstAttempt;

        
        for (int i = 65537; --i >= 0; ) {
            ftab[i] = 0;
        }

        
        for (int i = 0; i < NUM_OVERSHOOT_BYTES; i++) {
            block[lastShadow + i + 2] = block[(i % (lastShadow + 1)) + 1];
        }
        for (int i = lastShadow + NUM_OVERSHOOT_BYTES + 1; --i >= 0; ) {
            quadrant[i] = 0;
        }
        block[0] = block[lastShadow + 1];

        

        int c1 = block[0] & 0xff;
        for (int i = 0; i <= lastShadow; i++) {
            final int c2 = block[i + 1] & 0xff;
            ftab[(c1 << 8) + c2]++;
            c1 = c2;
        }

        for (int i = 1; i <= 65536; i++)
            ftab[i] += ftab[i - 1];

        c1 = block[1] & 0xff;
        for (int i = 0; i < lastShadow; i++) {
            final int c2 = block[i + 2] & 0xff;
            fmap[--ftab[(c1 << 8) + c2]] = i;
            c1 = c2;
        }

        fmap[--ftab[((block[lastShadow + 1] & 0xff) << 8) + (block[1] & 0xff)]] = lastShadow;

        
        for (int i = 256; --i >= 0; ) {
            bigDone[i] = false;
            runningOrder[i] = i;
        }

        for (int h = 364; h != 1; ) {
            h /= 3;
            for (int i = h; i <= 255; i++) {
                final int vv = runningOrder[i];
                final int a = ftab[(vv + 1) << 8] - ftab[vv << 8];
                final int b = h - 1;
                int j = i;
                for (int ro = runningOrder[j - h]; (ftab[(ro + 1) << 8] - ftab[ro << 8]) > a; ro = runningOrder[j
                        - h]) {
                    runningOrder[j] = ro;
                    j -= h;
                    if (j <= b) {
                        break;
                    }
                }
                runningOrder[j] = vv;
            }
        }

        
        for (int i = 0; i <= 255; i++) {
            
            final int ss = runningOrder[i];

            
            
            for (int j = 0; j <= 255; j++) {
                final int sb = (ss << 8) + j;
                final int ftab_sb = ftab[sb];
                if ((ftab_sb & SETMASK) != SETMASK) {
                    final int lo = ftab_sb & CLEARMASK;
                    final int hi = (ftab[sb + 1] & CLEARMASK) - 1;
                    if (hi > lo) {
                        mainQSort3(dataShadow, lo, hi, 2);
                        if (firstAttemptShadow
                                && (this.workDone > workLimitShadow)) {
                            return;
                        }
                    }
                    ftab[sb] = ftab_sb | SETMASK;
                }
            }

            
            
            

            for (int j = 0; j <= 255; j++) {
                copy[j] = ftab[(j << 8) + ss] & CLEARMASK;
            }

            for (int j = ftab[ss << 8] & CLEARMASK, hj = (ftab[(ss + 1) << 8] & CLEARMASK); j < hj; j++) {
                final int fmap_j = fmap[j];
                c1 = block[fmap_j] & 0xff;
                if (!bigDone[c1]) {
                    fmap[copy[c1]] = (fmap_j == 0) ? lastShadow : (fmap_j - 1);
                    copy[c1]++;
                }
            }

            for (int j = 256; --j >= 0; )
                ftab[(j << 8) + ss] |= SETMASK;

            
            
            bigDone[ss] = true;

            if (i < 255) {
                final int bbStart = ftab[ss << 8] & CLEARMASK;
                final int bbSize = (ftab[(ss + 1) << 8] & CLEARMASK) - bbStart;
                int shifts = 0;

                while ((bbSize >> shifts) > 65534) {
                    shifts++;
                }

                for (int j = 0; j < bbSize; j++) {
                    final int a2update = fmap[bbStart + j];
                    final char qVal = (char) (j >> shifts);
                    quadrant[a2update] = qVal;
                    if (a2update < NUM_OVERSHOOT_BYTES) {
                        quadrant[a2update + lastShadow + 1] = qVal;
                    }
                }
            }

        }
    }

    
    /**
     * Randomise block.
     */
    private void randomiseBlock() {
        final boolean[] inUse = this.data.inUse;
        final byte[] block = this.data.block;
        final int lastShadow = this.last;

        for (int i = 256; --i >= 0; )
            inUse[i] = false;

        int rNToGo = 0;
        int rTPos = 0;
        for (int i = 0, j = 1; i <= lastShadow; i = j, j++) {
            if (rNToGo == 0) {
                rNToGo = (char) BZip2Constants.rNums[rTPos];
                if (++rTPos == 512) {
                    rTPos = 0;
                }
            }

            rNToGo--;
            block[j] ^= ((rNToGo == 1) ? 1 : 0);

            
            inUse[block[j] & 0xff] = true;
        }

        this.blockRandomised = true;
    }

    
    /**
     * Generate mtf values.
     */
    private void generateMTFValues() {
        final int lastShadow = this.last;
        final Data dataShadow = this.data;
        final boolean[] inUse = dataShadow.inUse;
        final byte[] block = dataShadow.block;
        final int[] fmap = dataShadow.fmap;
        final char[] sfmap = dataShadow.sfmap;
        final int[] mtfFreq = dataShadow.mtfFreq;
        final byte[] unseqToSeq = dataShadow.unseqToSeq;
        final byte[] yy = dataShadow.generateMTFValues_yy;

        
        int nInUseShadow = 0;
        for (int i = 0; i < 256; i++) {
            if (inUse[i]) {
                unseqToSeq[i] = (byte) nInUseShadow;
                nInUseShadow++;
            }
        }
        this.nInUse = nInUseShadow;

        final int eob = nInUseShadow + 1;

        for (int i = eob; i >= 0; i--) {
            mtfFreq[i] = 0;
        }

        for (int i = nInUseShadow; --i >= 0; ) {
            yy[i] = (byte) i;
        }

        int wr = 0;
        int zPend = 0;

        for (int i = 0; i <= lastShadow; i++) {
            final byte ll_i = unseqToSeq[block[fmap[i]] & 0xff];
            byte tmp = yy[0];
            int j = 0;

            while (ll_i != tmp) {
                j++;
                byte tmp2 = tmp;
                tmp = yy[j];
                yy[j] = tmp2;
            }
            yy[0] = tmp;

            if (j == 0) {
                zPend++;
            } else {
                if (zPend > 0) {
                    zPend--;
                    while (true) {
                        if ((zPend & 1) == 0) {
                            sfmap[wr] = RUNA;
                            wr++;
                            mtfFreq[RUNA]++;
                        } else {
                            sfmap[wr] = RUNB;
                            wr++;
                            mtfFreq[RUNB]++;
                        }

                        if (zPend >= 2) {
                            zPend = (zPend - 2) >> 1;
                        } else {
                            break;
                        }
                    }
                    zPend = 0;
                }
                sfmap[wr] = (char) (j + 1);
                wr++;
                mtfFreq[j + 1]++;
            }
        }

        if (zPend > 0) {
            zPend--;
            while (true) {
                if ((zPend & 1) == 0) {
                    sfmap[wr] = RUNA;
                    wr++;
                    mtfFreq[RUNA]++;
                } else {
                    sfmap[wr] = RUNB;
                    wr++;
                    mtfFreq[RUNB]++;
                }

                if (zPend >= 2) {
                    zPend = (zPend - 2) >> 1;
                } else {
                    break;
                }
            }
        }

        sfmap[wr] = (char) eob;
        mtfFreq[eob]++;
        this.nMTF = wr + 1;
    }

    
    /**
     * The Class Data.
     *
     * @author l.xue.nong
     */
    private static final class Data extends Object {

        
        
        /** The in use. */
        final boolean[] inUse = new boolean[256]; 
        
        
        /** The unseq to seq. */
        final byte[] unseqToSeq = new byte[256]; 
        
        
        /** The mtf freq. */
        final int[] mtfFreq = new int[MAX_ALPHA_SIZE]; 
        
        
        /** The selector. */
        final byte[] selector = new byte[MAX_SELECTORS]; 
        
        
        /** The selector mtf. */
        final byte[] selectorMtf = new byte[MAX_SELECTORS]; 

        
        /** The generate mtf values_yy. */
        final byte[] generateMTFValues_yy = new byte[256]; 
        
        
        /** The send mtf values_len. */
        final byte[][] sendMTFValues_len = new byte[N_GROUPS][MAX_ALPHA_SIZE]; 
        
        
        /** The send mtf values_rfreq. */
        final int[][] sendMTFValues_rfreq = new int[N_GROUPS][MAX_ALPHA_SIZE]; 
        
        
        /** The send mtf values_fave. */
        final int[] sendMTFValues_fave = new int[N_GROUPS]; 
        
        
        /** The send mtf values_cost. */
        final short[] sendMTFValues_cost = new short[N_GROUPS]; 
        
        
        /** The send mtf values_code. */
        final int[][] sendMTFValues_code = new int[N_GROUPS][MAX_ALPHA_SIZE]; 
        
        
        /** The send mtf values2_pos. */
        final byte[] sendMTFValues2_pos = new byte[N_GROUPS]; 
        
        
        /** The sent mtf values4_in use16. */
        final boolean[] sentMTFValues4_inUse16 = new boolean[16]; 

        
        /** The stack_ll. */
        final int[] stack_ll = new int[QSORT_STACK_SIZE]; 
        
        
        /** The stack_hh. */
        final int[] stack_hh = new int[QSORT_STACK_SIZE]; 
        
        
        /** The stack_dd. */
        final int[] stack_dd = new int[QSORT_STACK_SIZE]; 

        
        /** The main sort_running order. */
        final int[] mainSort_runningOrder = new int[256]; 
        
        
        /** The main sort_copy. */
        final int[] mainSort_copy = new int[256]; 
        
        
        /** The main sort_big done. */
        final boolean[] mainSort_bigDone = new boolean[256]; 

        
        /** The heap. */
        final int[] heap = new int[MAX_ALPHA_SIZE + 2]; 
        
        
        /** The weight. */
        final int[] weight = new int[MAX_ALPHA_SIZE * 2]; 
        
        
        /** The parent. */
        final int[] parent = new int[MAX_ALPHA_SIZE * 2]; 

        
        /** The ftab. */
        final int[] ftab = new int[65537]; 
        
        

        
        /** The block. */
        final byte[] block; 
        
        
        /** The fmap. */
        final int[] fmap; 
        
        
        /** The sfmap. */
        final char[] sfmap; 
        
        
        

        
        /** The quadrant. */
        final char[] quadrant;

        
        /**
         * Instantiates a new data.
         *
         * @param blockSize100k the block size100k
         */
        Data(int blockSize100k) {
            super();

            final int n = blockSize100k * BZip2Constants.baseBlockSize;
            this.block = new byte[(n + 1 + NUM_OVERSHOOT_BYTES)];
            this.fmap = new int[n];
            this.sfmap = new char[2 * n];
            this.quadrant = this.sfmap;
        }

    }

}
