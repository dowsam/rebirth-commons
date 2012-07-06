/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons CBZip2InputStream.java 2012-7-6 10:22:12 l.xue.nong$$
 */

package cn.com.rebirth.commons.compress.bzip2;

import java.io.IOException;
import java.io.InputStream;


/**
 * The Class CBZip2InputStream.
 *
 * @author l.xue.nong
 */
public class CBZip2InputStream extends InputStream implements BZip2Constants {

	
	/**
	 * Report crc error.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void reportCRCError() throws IOException {
		
		

		
		System.err.println("BZip2 CRC error");
	}

	
	/**
	 * Make maps.
	 */
	private void makeMaps() {
		final boolean[] inUse = this.data.inUse;
		final byte[] seqToUnseq = this.data.seqToUnseq;

		int nInUseShadow = 0;

		for (int i = 0; i < 256; i++) {
			if (inUse[i])
				seqToUnseq[nInUseShadow++] = (byte) i;
		}

		this.nInUse = nInUseShadow;
	}

	
	/** The last. */
	private int last;

	
	/** The orig ptr. */
	private int origPtr;

	
	/** The block size100k. */
	private int blockSize100k;

	
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

	
	/** The in. */
	private InputStream in;

	
	/** The current char. */
	private int currentChar = -1;

	
	/** The Constant EOF. */
	private static final int EOF = 0;
	
	
	/** The Constant START_BLOCK_STATE. */
	private static final int START_BLOCK_STATE = 1;
	
	
	/** The Constant RAND_PART_A_STATE. */
	private static final int RAND_PART_A_STATE = 2;
	
	
	/** The Constant RAND_PART_B_STATE. */
	private static final int RAND_PART_B_STATE = 3;
	
	
	/** The Constant RAND_PART_C_STATE. */
	private static final int RAND_PART_C_STATE = 4;
	
	
	/** The Constant NO_RAND_PART_A_STATE. */
	private static final int NO_RAND_PART_A_STATE = 5;
	
	
	/** The Constant NO_RAND_PART_B_STATE. */
	private static final int NO_RAND_PART_B_STATE = 6;
	
	
	/** The Constant NO_RAND_PART_C_STATE. */
	private static final int NO_RAND_PART_C_STATE = 7;

	
	/** The current state. */
	private int currentState = START_BLOCK_STATE;

	
	/** The stored combined crc. */
	private int storedBlockCRC, storedCombinedCRC;
	
	
	/** The computed combined crc. */
	private int computedBlockCRC, computedCombinedCRC;

	

	
	/** The su_count. */
	private int su_count;
	
	
	/** The su_ch2. */
	private int su_ch2;
	
	
	/** The su_ch prev. */
	private int su_chPrev;
	
	
	/** The su_i2. */
	private int su_i2;
	
	
	/** The su_j2. */
	private int su_j2;
	
	
	/** The su_r n to go. */
	private int su_rNToGo;
	
	
	/** The su_r t pos. */
	private int su_rTPos;
	
	
	/** The su_t pos. */
	private int su_tPos;
	
	
	/** The su_z. */
	private char su_z;

	
	/** The data. */
	private Data data;

	
	/**
	 * Instantiates a new cB zip2 input stream.
	 *
	 * @param in the in
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public CBZip2InputStream(final InputStream in) throws IOException {
		super();

		this.in = in;
		init();
	}

	
	/* (non-Javadoc)
	 * @see java.io.InputStream#read()
	 */
	public int read() throws IOException {
		if (this.in != null) {
			return read0();
		} else {
			throw new IOException("stream closed");
		}
	}

	
	/* (non-Javadoc)
	 * @see java.io.InputStream#read(byte[], int, int)
	 */
	public int read(final byte[] dest, final int offs, final int len) throws IOException {
		if (offs < 0) {
			throw new IndexOutOfBoundsException("offs(" + offs + ") < 0.");
		}
		if (len < 0) {
			throw new IndexOutOfBoundsException("len(" + len + ") < 0.");
		}
		if (offs + len > dest.length) {
			throw new IndexOutOfBoundsException("offs(" + offs + ") + len(" + len + ") > dest.length(" + dest.length
					+ ").");
		}
		if (this.in == null) {
			throw new IOException("stream closed");
		}

		final int hi = offs + len;
		int destOffs = offs;
		for (int b; (destOffs < hi) && ((b = read0()) >= 0);) {
			dest[destOffs++] = (byte) b;
		}

		return (destOffs == offs) ? -1 : (destOffs - offs);
	}

	
	/**
	 * Read0.
	 *
	 * @return the int
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private int read0() throws IOException {
		final int retChar = this.currentChar;

		switch (this.currentState) {
		case EOF:
			return -1;

		case START_BLOCK_STATE:
			throw new IllegalStateException();

		case RAND_PART_A_STATE:
			throw new IllegalStateException();

		case RAND_PART_B_STATE:
			setupRandPartB();
			break;

		case RAND_PART_C_STATE:
			setupRandPartC();
			break;

		case NO_RAND_PART_A_STATE:
			throw new IllegalStateException();

		case NO_RAND_PART_B_STATE:
			setupNoRandPartB();
			break;

		case NO_RAND_PART_C_STATE:
			setupNoRandPartC();
			break;

		default:
			throw new IllegalStateException();
		}

		return retChar;
	}

	
	/**
	 * Inits the.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void init() throws IOException {
		if (null == in) {
			throw new IOException("No InputStream");
		}
		if (in.available() == 0) {
			throw new IOException("Empty InputStream");
		}
		int magic2 = this.in.read();
		if (magic2 != 'h') {
			throw new IOException("Stream is not BZip2 formatted: expected 'h'" + " as first byte but got '"
					+ (char) magic2 + "'");
		}

		int blockSize = this.in.read();
		if ((blockSize < '1') || (blockSize > '9')) {
			throw new IOException("Stream is not BZip2 formatted: illegal " + "blocksize " + (char) blockSize);
		}

		this.blockSize100k = blockSize - '0';

		initBlock();
		setupBlock();
	}

	
	/**
	 * Inits the block.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void initBlock() throws IOException {
		char magic0 = bsGetUByte();
		char magic1 = bsGetUByte();
		char magic2 = bsGetUByte();
		char magic3 = bsGetUByte();
		char magic4 = bsGetUByte();
		char magic5 = bsGetUByte();

		if (magic0 == 0x17 && magic1 == 0x72 && magic2 == 0x45 && magic3 == 0x38 && magic4 == 0x50 && magic5 == 0x90) {
			complete(); 
		} else if (magic0 != 0x31 || 
				magic1 != 0x41 || 
				magic2 != 0x59 || 
				magic3 != 0x26 || 
				magic4 != 0x53 || 
				magic5 != 0x59 
		) {
			this.currentState = EOF;
			throw new IOException("bad block header");
		} else {
			this.storedBlockCRC = bsGetInt();
			this.blockRandomised = bsR(1) == 1;

			
			if (this.data == null) {
				this.data = new Data(this.blockSize100k);
			}

			
			getAndMoveToFrontDecode();

			this.crc.initialiseCRC();
			this.currentState = START_BLOCK_STATE;
		}
	}

	
	/**
	 * End block.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void endBlock() throws IOException {
		this.computedBlockCRC = this.crc.getFinalCRC();

		
		if (this.storedBlockCRC != this.computedBlockCRC) {
			
			
			this.computedCombinedCRC = (this.storedCombinedCRC << 1) | (this.storedCombinedCRC >>> 31);
			this.computedCombinedCRC ^= this.storedBlockCRC;

			reportCRCError();
		}

		this.computedCombinedCRC = (this.computedCombinedCRC << 1) | (this.computedCombinedCRC >>> 31);
		this.computedCombinedCRC ^= this.computedBlockCRC;
	}

	
	/**
	 * Complete.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void complete() throws IOException {
		this.storedCombinedCRC = bsGetInt();
		this.currentState = EOF;
		this.data = null;

		if (this.storedCombinedCRC != this.computedCombinedCRC) {
			reportCRCError();
		}
	}

	
	/* (non-Javadoc)
	 * @see java.io.InputStream#close()
	 */
	public void close() throws IOException {
		InputStream inShadow = this.in;
		if (inShadow != null) {
			try {
				if (inShadow != System.in) {
					inShadow.close();
				}
			} finally {
				this.data = null;
				this.in = null;
			}
		}
	}

	
	/**
	 * Bs r.
	 *
	 * @param n the n
	 * @return the int
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private int bsR(final int n) throws IOException {
		int bsLiveShadow = this.bsLive;
		int bsBuffShadow = this.bsBuff;

		if (bsLiveShadow < n) {
			final InputStream inShadow = this.in;
			do {
				int thech = inShadow.read();

				if (thech < 0) {
					throw new IOException("unexpected end of stream");
				}

				bsBuffShadow = (bsBuffShadow << 8) | thech;
				bsLiveShadow += 8;
			} while (bsLiveShadow < n);

			this.bsBuff = bsBuffShadow;
		}

		this.bsLive = bsLiveShadow - n;
		return (bsBuffShadow >> (bsLiveShadow - n)) & ((1 << n) - 1);
	}

	
	/**
	 * Bs get bit.
	 *
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private boolean bsGetBit() throws IOException {
		int bsLiveShadow = this.bsLive;
		int bsBuffShadow = this.bsBuff;

		if (bsLiveShadow < 1) {
			int thech = this.in.read();

			if (thech < 0) {
				throw new IOException("unexpected end of stream");
			}

			bsBuffShadow = (bsBuffShadow << 8) | thech;
			bsLiveShadow += 8;
			this.bsBuff = bsBuffShadow;
		}

		this.bsLive = bsLiveShadow - 1;
		return ((bsBuffShadow >> (bsLiveShadow - 1)) & 1) != 0;
	}

	
	/**
	 * Bs get u byte.
	 *
	 * @return the char
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private char bsGetUByte() throws IOException {
		return (char) bsR(8);
	}

	
	/**
	 * Bs get int.
	 *
	 * @return the int
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private int bsGetInt() throws IOException {
		return (((((bsR(8) << 8) | bsR(8)) << 8) | bsR(8)) << 8) | bsR(8);
	}

	
	/**
	 * Hb create decode tables.
	 *
	 * @param limit the limit
	 * @param base the base
	 * @param perm the perm
	 * @param length the length
	 * @param minLen the min len
	 * @param maxLen the max len
	 * @param alphaSize the alpha size
	 */
	private static void hbCreateDecodeTables(final int[] limit, final int[] base, final int[] perm,
			final char[] length, final int minLen, final int maxLen, final int alphaSize) {
		for (int i = minLen, pp = 0; i <= maxLen; i++) {
			for (int j = 0; j < alphaSize; j++) {
				if (length[j] == i) {
					perm[pp++] = j;
				}
			}
		}

		for (int i = MAX_CODE_LEN; --i > 0;) {
			base[i] = 0;
			limit[i] = 0;
		}

		for (int i = 0; i < alphaSize; i++) {
			base[length[i] + 1]++;
		}

		for (int i = 1, b = base[0]; i < MAX_CODE_LEN; i++) {
			b += base[i];
			base[i] = b;
		}

		for (int i = minLen, vec = 0, b = base[i]; i <= maxLen; i++) {
			final int nb = base[i + 1];
			vec += nb - b;
			b = nb;
			limit[i] = vec - 1;
			vec <<= 1;
		}

		for (int i = minLen + 1; i <= maxLen; i++) {
			base[i] = ((limit[i - 1] + 1) << 1) - base[i];
		}
	}

	
	/**
	 * Recv decoding tables.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void recvDecodingTables() throws IOException {
		final Data dataShadow = this.data;
		final boolean[] inUse = dataShadow.inUse;
		final byte[] pos = dataShadow.recvDecodingTables_pos;
		final byte[] selector = dataShadow.selector;
		final byte[] selectorMtf = dataShadow.selectorMtf;

		int inUse16 = 0;

		
		for (int i = 0; i < 16; i++) {
			if (bsGetBit()) {
				inUse16 |= 1 << i;
			}
		}

		for (int i = 256; --i >= 0;) {
			inUse[i] = false;
		}

		for (int i = 0; i < 16; i++) {
			if ((inUse16 & (1 << i)) != 0) {
				final int i16 = i << 4;
				for (int j = 0; j < 16; j++) {
					if (bsGetBit()) {
						inUse[i16 + j] = true;
					}
				}
			}
		}

		makeMaps();
		final int alphaSize = this.nInUse + 2;

		
		final int nGroups = bsR(3);
		final int nSelectors = bsR(15);

		for (int i = 0; i < nSelectors; i++) {
			int j = 0;
			while (bsGetBit()) {
				j++;
			}
			selectorMtf[i] = (byte) j;
		}

		
		for (int v = nGroups; --v >= 0;) {
			pos[v] = (byte) v;
		}

		for (int i = 0; i < nSelectors; i++) {
			int v = selectorMtf[i] & 0xff;
			final byte tmp = pos[v];
			while (v > 0) {
				
				pos[v] = pos[v - 1];
				v--;
			}
			pos[0] = tmp;
			selector[i] = tmp;
		}

		final char[][] len = dataShadow.temp_charArray2d;

		
		for (int t = 0; t < nGroups; t++) {
			int curr = bsR(5);
			final char[] len_t = len[t];
			for (int i = 0; i < alphaSize; i++) {
				while (bsGetBit()) {
					curr += bsGetBit() ? -1 : 1;
				}
				len_t[i] = (char) curr;
			}
		}

		
		createHuffmanDecodingTables(alphaSize, nGroups);
	}

	
	/**
	 * Creates the huffman decoding tables.
	 *
	 * @param alphaSize the alpha size
	 * @param nGroups the n groups
	 */
	private void createHuffmanDecodingTables(final int alphaSize, final int nGroups) {
		final Data dataShadow = this.data;
		final char[][] len = dataShadow.temp_charArray2d;
		final int[] minLens = dataShadow.minLens;
		final int[][] limit = dataShadow.limit;
		final int[][] base = dataShadow.base;
		final int[][] perm = dataShadow.perm;

		for (int t = 0; t < nGroups; t++) {
			int minLen = 32;
			int maxLen = 0;
			final char[] len_t = len[t];
			for (int i = alphaSize; --i >= 0;) {
				final char lent = len_t[i];
				if (lent > maxLen) {
					maxLen = lent;
				}
				if (lent < minLen) {
					minLen = lent;
				}
			}
			hbCreateDecodeTables(limit[t], base[t], perm[t], len[t], minLen, maxLen, alphaSize);
			minLens[t] = minLen;
		}
	}

	
	/**
	 * Gets the and move to front decode.
	 *
	 * @return the and move to front decode
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void getAndMoveToFrontDecode() throws IOException {
		this.origPtr = bsR(24);
		recvDecodingTables();

		final InputStream inShadow = this.in;
		final Data dataShadow = this.data;
		final byte[] ll8 = dataShadow.ll8;
		final int[] unzftab = dataShadow.unzftab;
		final byte[] selector = dataShadow.selector;
		final byte[] seqToUnseq = dataShadow.seqToUnseq;
		final char[] yy = dataShadow.getAndMoveToFrontDecode_yy;
		final int[] minLens = dataShadow.minLens;
		final int[][] limit = dataShadow.limit;
		final int[][] base = dataShadow.base;
		final int[][] perm = dataShadow.perm;
		final int limitLast = this.blockSize100k * 100000;

		
		for (int i = 256; --i >= 0;) {
			yy[i] = (char) i;
			unzftab[i] = 0;
		}

		int groupNo = 0;
		int groupPos = G_SIZE - 1;
		final int eob = this.nInUse + 1;
		int nextSym = getAndMoveToFrontDecode0(0);
		int bsBuffShadow = this.bsBuff;
		int bsLiveShadow = this.bsLive;
		int lastShadow = -1;
		int zt = selector[groupNo] & 0xff;
		int[] base_zt = base[zt];
		int[] limit_zt = limit[zt];
		int[] perm_zt = perm[zt];
		int minLens_zt = minLens[zt];

		while (nextSym != eob) {
			if ((nextSym == RUNA) || (nextSym == RUNB)) {
				int s = -1;

				for (int n = 1; true; n <<= 1) {
					if (nextSym == RUNA) {
						s += n;
					} else if (nextSym == RUNB) {
						s += n << 1;
					} else {
						break;
					}

					if (groupPos == 0) {
						groupPos = G_SIZE - 1;
						zt = selector[++groupNo] & 0xff;
						base_zt = base[zt];
						limit_zt = limit[zt];
						perm_zt = perm[zt];
						minLens_zt = minLens[zt];
					} else {
						groupPos--;
					}

					int zn = minLens_zt;

					
					
					while (bsLiveShadow < zn) {
						final int thech = inShadow.read();
						if (thech >= 0) {
							bsBuffShadow = (bsBuffShadow << 8) | thech;
							bsLiveShadow += 8;
							continue;
						} else {
							throw new IOException("unexpected end of stream");
						}
					}
					int zvec = (bsBuffShadow >> (bsLiveShadow - zn)) & ((1 << zn) - 1);
					bsLiveShadow -= zn;

					while (zvec > limit_zt[zn]) {
						zn++;
						while (bsLiveShadow < 1) {
							final int thech = inShadow.read();
							if (thech >= 0) {
								bsBuffShadow = (bsBuffShadow << 8) | thech;
								bsLiveShadow += 8;
								continue;
							} else {
								throw new IOException("unexpected end of stream");
							}
						}
						bsLiveShadow--;
						zvec = (zvec << 1) | ((bsBuffShadow >> bsLiveShadow) & 1);
					}
					nextSym = perm_zt[zvec - base_zt[zn]];
				}

				final byte ch = seqToUnseq[yy[0]];
				unzftab[ch & 0xff] += s + 1;

				while (s-- >= 0) {
					ll8[++lastShadow] = ch;
				}

				if (lastShadow >= limitLast) {
					throw new IOException("block overrun");
				}
			} else {
				if (++lastShadow >= limitLast) {
					throw new IOException("block overrun");
				}

				final char tmp = yy[nextSym - 1];
				unzftab[seqToUnseq[tmp] & 0xff]++;
				ll8[lastShadow] = seqToUnseq[tmp];

				
				if (nextSym <= 16) {
					for (int j = nextSym - 1; j > 0;) {
						yy[j] = yy[--j];
					}
				} else {
					System.arraycopy(yy, 0, yy, 1, nextSym - 1);
				}

				yy[0] = tmp;

				if (groupPos == 0) {
					groupPos = G_SIZE - 1;
					zt = selector[++groupNo] & 0xff;
					base_zt = base[zt];
					limit_zt = limit[zt];
					perm_zt = perm[zt];
					minLens_zt = minLens[zt];
				} else {
					groupPos--;
				}

				int zn = minLens_zt;

				
				
				while (bsLiveShadow < zn) {
					final int thech = inShadow.read();
					if (thech >= 0) {
						bsBuffShadow = (bsBuffShadow << 8) | thech;
						bsLiveShadow += 8;
						continue;
					} else {
						throw new IOException("unexpected end of stream");
					}
				}
				int zvec = (bsBuffShadow >> (bsLiveShadow - zn)) & ((1 << zn) - 1);
				bsLiveShadow -= zn;

				while (zvec > limit_zt[zn]) {
					zn++;
					while (bsLiveShadow < 1) {
						final int thech = inShadow.read();
						if (thech >= 0) {
							bsBuffShadow = (bsBuffShadow << 8) | thech;
							bsLiveShadow += 8;
							continue;
						} else {
							throw new IOException("unexpected end of stream");
						}
					}
					bsLiveShadow--;
					zvec = (zvec << 1) | ((bsBuffShadow >> bsLiveShadow) & 1);
				}
				nextSym = perm_zt[zvec - base_zt[zn]];
			}
		}

		this.last = lastShadow;
		this.bsLive = bsLiveShadow;
		this.bsBuff = bsBuffShadow;
	}

	
	/**
	 * Gets the and move to front decode0.
	 *
	 * @param groupNo the group no
	 * @return the and move to front decode0
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private int getAndMoveToFrontDecode0(final int groupNo) throws IOException {
		final InputStream inShadow = this.in;
		final Data dataShadow = this.data;
		final int zt = dataShadow.selector[groupNo] & 0xff;
		final int[] limit_zt = dataShadow.limit[zt];
		int zn = dataShadow.minLens[zt];
		int zvec = bsR(zn);
		int bsLiveShadow = this.bsLive;
		int bsBuffShadow = this.bsBuff;

		while (zvec > limit_zt[zn]) {
			zn++;
			while (bsLiveShadow < 1) {
				final int thech = inShadow.read();

				if (thech >= 0) {
					bsBuffShadow = (bsBuffShadow << 8) | thech;
					bsLiveShadow += 8;
					continue;
				} else {
					throw new IOException("unexpected end of stream");
				}
			}
			bsLiveShadow--;
			zvec = (zvec << 1) | ((bsBuffShadow >> bsLiveShadow) & 1);
		}

		this.bsLive = bsLiveShadow;
		this.bsBuff = bsBuffShadow;

		return dataShadow.perm[zt][zvec - dataShadow.base[zt][zn]];
	}

	
	/**
	 * Setup block.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void setupBlock() throws IOException {
		if (this.data == null) {
			return;
		}

		final int[] cftab = this.data.cftab;
		final int[] tt = this.data.initTT(this.last + 1);
		final byte[] ll8 = this.data.ll8;
		cftab[0] = 0;
		System.arraycopy(this.data.unzftab, 0, cftab, 1, 256);

		for (int i = 1, c = cftab[0]; i <= 256; i++) {
			c += cftab[i];
			cftab[i] = c;
		}

		for (int i = 0, lastShadow = this.last; i <= lastShadow; i++) {
			tt[cftab[ll8[i] & 0xff]++] = i;
		}

		if ((this.origPtr < 0) || (this.origPtr >= tt.length)) {
			throw new IOException("stream corrupted");
		}

		this.su_tPos = tt[this.origPtr];
		this.su_count = 0;
		this.su_i2 = 0;
		this.su_ch2 = 256; 

		if (this.blockRandomised) {
			this.su_rNToGo = 0;
			this.su_rTPos = 0;
			setupRandPartA();
		} else {
			setupNoRandPartA();
		}
	}

	
	/**
	 * Setup rand part a.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void setupRandPartA() throws IOException {
		if (this.su_i2 <= this.last) {
			this.su_chPrev = this.su_ch2;
			int su_ch2Shadow = this.data.ll8[this.su_tPos] & 0xff;
			this.su_tPos = this.data.tt[this.su_tPos];
			if (this.su_rNToGo == 0) {
				this.su_rNToGo = BZip2Constants.rNums[this.su_rTPos] - 1;
				if (++this.su_rTPos == 512) {
					this.su_rTPos = 0;
				}
			} else {
				this.su_rNToGo--;
			}
			this.su_ch2 = su_ch2Shadow ^= (this.su_rNToGo == 1) ? 1 : 0;
			this.su_i2++;
			this.currentChar = su_ch2Shadow;
			this.currentState = RAND_PART_B_STATE;
			this.crc.updateCRC(su_ch2Shadow);
		} else {
			endBlock();
			initBlock();
			setupBlock();
		}
	}

	
	/**
	 * Setup no rand part a.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void setupNoRandPartA() throws IOException {
		if (this.su_i2 <= this.last) {
			this.su_chPrev = this.su_ch2;
			int su_ch2Shadow = this.data.ll8[this.su_tPos] & 0xff;
			this.su_ch2 = su_ch2Shadow;
			this.su_tPos = this.data.tt[this.su_tPos];
			this.su_i2++;
			this.currentChar = su_ch2Shadow;
			this.currentState = NO_RAND_PART_B_STATE;
			this.crc.updateCRC(su_ch2Shadow);
		} else {
			this.currentState = NO_RAND_PART_A_STATE;
			endBlock();
			initBlock();
			setupBlock();
		}
	}

	
	/**
	 * Setup rand part b.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void setupRandPartB() throws IOException {
		if (this.su_ch2 != this.su_chPrev) {
			this.currentState = RAND_PART_A_STATE;
			this.su_count = 1;
			setupRandPartA();
		} else if (++this.su_count >= 4) {
			this.su_z = (char) (this.data.ll8[this.su_tPos] & 0xff);
			this.su_tPos = this.data.tt[this.su_tPos];
			if (this.su_rNToGo == 0) {
				this.su_rNToGo = BZip2Constants.rNums[this.su_rTPos] - 1;
				if (++this.su_rTPos == 512) {
					this.su_rTPos = 0;
				}
			} else {
				this.su_rNToGo--;
			}
			this.su_j2 = 0;
			this.currentState = RAND_PART_C_STATE;
			if (this.su_rNToGo == 1) {
				this.su_z ^= 1;
			}
			setupRandPartC();
		} else {
			this.currentState = RAND_PART_A_STATE;
			setupRandPartA();
		}
	}

	
	/**
	 * Setup rand part c.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void setupRandPartC() throws IOException {
		if (this.su_j2 < this.su_z) {
			this.currentChar = this.su_ch2;
			this.crc.updateCRC(this.su_ch2);
			this.su_j2++;
		} else {
			this.currentState = RAND_PART_A_STATE;
			this.su_i2++;
			this.su_count = 0;
			setupRandPartA();
		}
	}

	
	/**
	 * Setup no rand part b.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void setupNoRandPartB() throws IOException {
		if (this.su_ch2 != this.su_chPrev) {
			this.su_count = 1;
			setupNoRandPartA();
		} else if (++this.su_count >= 4) {
			this.su_z = (char) (this.data.ll8[this.su_tPos] & 0xff);
			this.su_tPos = this.data.tt[this.su_tPos];
			this.su_j2 = 0;
			setupNoRandPartC();
		} else {
			setupNoRandPartA();
		}
	}

	
	/**
	 * Setup no rand part c.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void setupNoRandPartC() throws IOException {
		if (this.su_j2 < this.su_z) {
			int su_ch2Shadow = this.su_ch2;
			this.currentChar = su_ch2Shadow;
			this.crc.updateCRC(su_ch2Shadow);
			this.su_j2++;
			this.currentState = NO_RAND_PART_C_STATE;
		} else {
			this.su_i2++;
			this.su_count = 0;
			setupNoRandPartA();
		}
	}

	
	/**
	 * The Class Data.
	 *
	 * @author l.xue.nong
	 */
	private static final class Data extends Object {

		
		
		/** The in use. */
		final boolean[] inUse = new boolean[256]; 

		
		/** The seq to unseq. */
		final byte[] seqToUnseq = new byte[256]; 
		
		
		/** The selector. */
		final byte[] selector = new byte[MAX_SELECTORS]; 
		
		
		/** The selector mtf. */
		final byte[] selectorMtf = new byte[MAX_SELECTORS]; 

		
		/** The unzftab. */
		final int[] unzftab = new int[256]; 

		
		/** The limit. */
		final int[][] limit = new int[N_GROUPS][MAX_ALPHA_SIZE]; 
		
		
		/** The base. */
		final int[][] base = new int[N_GROUPS][MAX_ALPHA_SIZE]; 
		
		
		/** The perm. */
		final int[][] perm = new int[N_GROUPS][MAX_ALPHA_SIZE]; 
		
		
		/** The min lens. */
		final int[] minLens = new int[N_GROUPS]; 

		
		/** The cftab. */
		final int[] cftab = new int[257]; 
		
		
		/** The get and move to front decode_yy. */
		final char[] getAndMoveToFrontDecode_yy = new char[256]; 
		
		
		/** The temp_char array2d. */
		final char[][] temp_charArray2d = new char[N_GROUPS][MAX_ALPHA_SIZE]; 
		
		
		/** The recv decoding tables_pos. */
		final byte[] recvDecodingTables_pos = new byte[N_GROUPS]; 
		
		

		
		/** The tt. */
		int[] tt; 
		
		
		/** The ll8. */
		byte[] ll8; 

		
		
		

		
		/**
		 * Instantiates a new data.
		 *
		 * @param blockSize100k the block size100k
		 */
		Data(int blockSize100k) {
			super();

			this.ll8 = new byte[blockSize100k * BZip2Constants.baseBlockSize];
		}

		
		/**
		 * Inits the tt.
		 *
		 * @param length the length
		 * @return the int[]
		 */
		final int[] initTT(int length) {
			int[] ttShadow = this.tt;

			
			
			
			
			if ((ttShadow == null) || (ttShadow.length < length)) {
				this.tt = ttShadow = new int[length];
			}

			return ttShadow;
		}

	}
}
