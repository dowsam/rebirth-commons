/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons Unicode.java 2012-3-29 15:15:10 l.xue.nong$$
 */

package cn.com.restart.commons;

import java.util.Arrays;

import org.apache.lucene.util.UnicodeUtil;

import cn.com.restart.commons.thread.ThreadLocals;


/**
 * The Class Unicode.
 *
 * @author l.xue.nong
 */
public class Unicode {

	
	/** The cached utf8 result. */
	private static ThreadLocal<ThreadLocals.CleanableValue<UnicodeUtil.UTF8Result>> cachedUtf8Result = new ThreadLocal<ThreadLocals.CleanableValue<UnicodeUtil.UTF8Result>>() {
		@Override
		protected ThreadLocals.CleanableValue<UnicodeUtil.UTF8Result> initialValue() {
			return new ThreadLocals.CleanableValue<UnicodeUtil.UTF8Result>(new UnicodeUtil.UTF8Result());
		}
	};

	
	/** The cached utf16 result. */
	private static ThreadLocal<ThreadLocals.CleanableValue<UTF16Result>> cachedUtf16Result = new ThreadLocal<ThreadLocals.CleanableValue<UTF16Result>>() {
		@Override
		protected ThreadLocals.CleanableValue<UTF16Result> initialValue() {
			return new ThreadLocals.CleanableValue<UTF16Result>(new UTF16Result());
		}
	};

	
	/**
	 * From string as bytes.
	 *
	 * @param source the source
	 * @return the byte[]
	 */
	public static byte[] fromStringAsBytes(String source) {
		if (source == null) {
			return null;
		}
		UnicodeUtil.UTF8Result result = unsafeFromStringAsUtf8(source);
		return Arrays.copyOfRange(result.result, 0, result.length);
	}

	
	/**
	 * From string as utf8.
	 *
	 * @param source the source
	 * @return the unicode util. ut f8 result
	 */
	public static UnicodeUtil.UTF8Result fromStringAsUtf8(String source) {
		if (source == null) {
			return null;
		}
		UnicodeUtil.UTF8Result result = new UnicodeUtil.UTF8Result();
		UnicodeUtil.UTF16toUTF8(source, 0, source.length(), result);
		return result;
	}

	
	/**
	 * Unsafe from string as utf8.
	 *
	 * @param source the source
	 * @return the unicode util. ut f8 result
	 */
	public static UnicodeUtil.UTF8Result unsafeFromStringAsUtf8(String source) {
		if (source == null) {
			return null;
		}
		UnicodeUtil.UTF8Result result = cachedUtf8Result.get().get();
		UnicodeUtil.UTF16toUTF8(source, 0, source.length(), result);
		return result;
	}

	
	/**
	 * From bytes.
	 *
	 * @param source the source
	 * @return the string
	 */
	public static String fromBytes(byte[] source) {
		return fromBytes(source, 0, source.length);
	}

	
	/**
	 * From bytes.
	 *
	 * @param source the source
	 * @param offset the offset
	 * @param length the length
	 * @return the string
	 */
	public static String fromBytes(byte[] source, int offset, int length) {
		if (source == null) {
			return null;
		}
		UTF16Result result = unsafeFromBytesAsUtf16(source, offset, length);
		return new String(result.result, 0, result.length);
	}

	
	/**
	 * From bytes as utf16.
	 *
	 * @param source the source
	 * @return the uT f16 result
	 */
	public static UTF16Result fromBytesAsUtf16(byte[] source) {
		return fromBytesAsUtf16(source, 0, source.length);
	}

	
	/**
	 * From bytes as utf16.
	 *
	 * @param source the source
	 * @param offset the offset
	 * @param length the length
	 * @return the uT f16 result
	 */
	public static UTF16Result fromBytesAsUtf16(byte[] source, int offset, int length) {
		if (source == null) {
			return null;
		}
		UTF16Result result = new UTF16Result();
		UTF8toUTF16(source, offset, length, result);
		return result;
	}

	
	/**
	 * Unsafe from bytes as utf16.
	 *
	 * @param source the source
	 * @return the uT f16 result
	 */
	public static UTF16Result unsafeFromBytesAsUtf16(byte[] source) {
		return unsafeFromBytesAsUtf16(source, 0, source.length);
	}

	
	/**
	 * Unsafe from bytes as utf16.
	 *
	 * @param source the source
	 * @param offset the offset
	 * @param length the length
	 * @return the uT f16 result
	 */
	public static UTF16Result unsafeFromBytesAsUtf16(byte[] source, int offset, int length) {
		if (source == null) {
			return null;
		}
		UTF16Result result = cachedUtf16Result.get().get();
		UTF8toUTF16(source, offset, length, result);
		return result;
	}

	

	
	

	
	/**
	 * The Class UTF16Result.
	 *
	 * @author l.xue.nong
	 */
	public static final class UTF16Result {
		
		
		/** The result. */
		public char[] result = new char[10];
		
		
		/** The length. */
		public int length;

		
		/**
		 * Sets the length.
		 *
		 * @param newLength the new length
		 */
		public void setLength(int newLength) {
			if (result.length < newLength) {
				char[] newArray = new char[(int) (1.5 * newLength)];
				System.arraycopy(result, 0, newArray, 0, length);
				result = newArray;
			}
			length = newLength;
		}

		
		/**
		 * Copy text.
		 *
		 * @param other the other
		 */
		public void copyText(UTF16Result other) {
			setLength(other.length);
			System.arraycopy(other.result, 0, result, 0, length);
		}
	}

	
	/**
	 * UT f8to ut f16.
	 *
	 * @param utf8 the utf8
	 * @param offset the offset
	 * @param length the length
	 * @param result the result
	 */
	public static void UTF8toUTF16(final byte[] utf8, final int offset, final int length, final UTF16Result result) {

		final int end = offset + length;
		char[] out = result.result;
		
		
		
		
		
		

		
		
		int upto = offset;
		
		

		int outUpto = 0; 

		
		if (outUpto + length >= out.length) {
			char[] newOut = new char[2 * (outUpto + length)];
			System.arraycopy(out, 0, newOut, 0, outUpto);
			result.result = out = newOut;
		}

		while (upto < end) {

			final int b = utf8[upto] & 0xff;
			final int ch;

			upto += 1; 
			

			if (b < 0xc0) {
				assert b < 0x80;
				ch = b;
			} else if (b < 0xe0) {
				ch = ((b & 0x1f) << 6) + (utf8[upto] & 0x3f);
				upto += 1; 
				
			} else if (b < 0xf0) {
				ch = ((b & 0xf) << 12) + ((utf8[upto] & 0x3f) << 6) + (utf8[upto + 1] & 0x3f);
				upto += 2; 
				
				
			} else {
				assert b < 0xf8;
				ch = ((b & 0x7) << 18) + ((utf8[upto] & 0x3f) << 12) + ((utf8[upto + 1] & 0x3f) << 6)
						+ (utf8[upto + 2] & 0x3f);
				upto += 3; 
				
				
				
			}

			if (ch <= UNI_MAX_BMP) {
				
				out[outUpto++] = (char) ch;
			} else {
				
				final int chHalf = ch - HALF_BASE;
				out[outUpto++] = (char) ((chHalf >> HALF_SHIFT) + UnicodeUtil.UNI_SUR_HIGH_START);
				out[outUpto++] = (char) ((chHalf & HALF_MASK) + UnicodeUtil.UNI_SUR_LOW_START);
			}
		}

		
		result.length = outUpto;
	}

	
	/** The Constant UNI_MAX_BMP. */
	private static final long UNI_MAX_BMP = 0x0000FFFF;

	
	/** The Constant HALF_BASE. */
	private static final int HALF_BASE = 0x0010000;
	
	
	/** The Constant HALF_SHIFT. */
	private static final long HALF_SHIFT = 10;
	
	
	/** The Constant HALF_MASK. */
	private static final long HALF_MASK = 0x3FFL;
}
