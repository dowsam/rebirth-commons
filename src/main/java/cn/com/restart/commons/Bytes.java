/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons Bytes.java 2012-3-29 15:15:14 l.xue.nong$$
 */
package cn.com.restart.commons;

/**
 * The Class Bytes.
 *
 * @author l.xue.nong
 */
public class Bytes {

	/** The Constant EMPTY_ARRAY. */
	public static final byte[] EMPTY_ARRAY = new byte[0];

	/** The Constant sizeTable. */
	final static int[] sizeTable = { 9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, Integer.MAX_VALUE };

	/** The Constant LONG_MIN_VALUE_BYTES. */
	private static final byte[] LONG_MIN_VALUE_BYTES = "-9223372036854775808".getBytes();

	/**
	 * String size.
	 *
	 * @param x the x
	 * @return the int
	 */
	static int stringSize(int x) {
		for (int i = 0;; i++)
			if (x <= sizeTable[i])
				return i + 1;
	}

	/**
	 * Itoa.
	 *
	 * @param i the i
	 * @return the byte[]
	 */
	public static byte[] itoa(int i) {
		int size = (i < 0) ? stringSize(-i) + 1 : stringSize(i);
		byte[] buf = new byte[size];
		getChars(i, size, buf);
		return buf;
	}

	/** The Constant digits. */
	final static byte[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
			'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

	/** The Constant DigitTens. */
	final static byte[] DigitTens = { '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '1', '1', '1', '1', '1', '1',
			'1', '1', '1', '1', '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '3', '3', '3', '3', '3', '3', '3',
			'3', '3', '3', '4', '4', '4', '4', '4', '4', '4', '4', '4', '4', '5', '5', '5', '5', '5', '5', '5', '5',
			'5', '5', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '7', '7', '7', '7', '7', '7', '7', '7', '7',
			'7', '8', '8', '8', '8', '8', '8', '8', '8', '8', '8', '9', '9', '9', '9', '9', '9', '9', '9', '9', '9', };

	/** The Constant DigitOnes. */
	final static byte[] DigitOnes = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', };

	/**
	 * Gets the chars.
	 *
	 * @param i the i
	 * @param index the index
	 * @param buf the buf
	 * @return the chars
	 */
	static void getChars(int i, int index, byte[] buf) {
		int q, r;
		int charPos = index;
		byte sign = 0;

		if (i < 0) {
			sign = '-';
			i = -i;
		}

		while (i >= 65536) {
			q = i / 100;

			r = i - ((q << 6) + (q << 5) + (q << 2));
			i = q;
			buf[--charPos] = DigitOnes[r];
			buf[--charPos] = DigitTens[r];
		}

		for (;;) {
			q = (i * 52429) >>> (16 + 3);
			r = i - ((q << 3) + (q << 1));
			buf[--charPos] = digits[r];
			i = q;
			if (i == 0)
				break;
		}
		if (sign != 0) {
			buf[--charPos] = sign;
		}
	}

	/**
	 * Atoi.
	 *
	 * @param s the s
	 * @return the int
	 * @throws NumberFormatException the number format exception
	 */
	public static int atoi(byte[] s) throws NumberFormatException {
		int result = 0;
		boolean negative = false;
		int i = 0, len = s.length;
		int limit = -Integer.MAX_VALUE;
		int multmin;
		int digit;

		if (len > 0) {
			byte firstChar = s[0];
			if (firstChar < '0') {
				if (firstChar == '-') {
					negative = true;
					limit = Integer.MIN_VALUE;
				} else
					throw new NumberFormatException();

				if (len == 1)
					throw new NumberFormatException();
				i++;
			}
			multmin = limit / 10;
			while (i < len) {

				digit = Character.digit(s[i++], 10);
				if (digit < 0) {
					throw new NumberFormatException();
				}
				if (result < multmin) {
					throw new NumberFormatException();
				}
				result *= 10;
				if (result < limit + digit) {
					throw new NumberFormatException();
				}
				result -= digit;
			}
		} else {
			throw new NumberFormatException();
		}
		return negative ? result : -result;
	}

	/**
	 * Ltoa.
	 *
	 * @param i the i
	 * @return the byte[]
	 */
	public static byte[] ltoa(long i) {
		if (i == Long.MIN_VALUE)
			return LONG_MIN_VALUE_BYTES;
		int size = (i < 0) ? stringSize(-i) + 1 : stringSize(i);
		byte[] buf = new byte[size];
		getChars(i, size, buf);
		return buf;
	}

	/**
	 * Gets the chars.
	 *
	 * @param i the i
	 * @param index the index
	 * @param buf the buf
	 * @return the chars
	 */
	static void getChars(long i, int index, byte[] buf) {
		long q;
		int r;
		int charPos = index;
		byte sign = 0;

		if (i < 0) {
			sign = '-';
			i = -i;
		}

		while (i > Integer.MAX_VALUE) {
			q = i / 100;

			r = (int) (i - ((q << 6) + (q << 5) + (q << 2)));
			i = q;
			buf[--charPos] = DigitOnes[r];
			buf[--charPos] = DigitTens[r];
		}

		int q2;
		int i2 = (int) i;
		while (i2 >= 65536) {
			q2 = i2 / 100;

			r = i2 - ((q2 << 6) + (q2 << 5) + (q2 << 2));
			i2 = q2;
			buf[--charPos] = DigitOnes[r];
			buf[--charPos] = DigitTens[r];
		}

		for (;;) {
			q2 = (i2 * 52429) >>> (16 + 3);
			r = i2 - ((q2 << 3) + (q2 << 1));
			buf[--charPos] = digits[r];
			i2 = q2;
			if (i2 == 0)
				break;
		}
		if (sign != 0) {
			buf[--charPos] = sign;
		}
	}

	/**
	 * String size.
	 *
	 * @param x the x
	 * @return the int
	 */
	static int stringSize(long x) {
		long p = 10;
		for (int i = 1; i < 19; i++) {
			if (x < p)
				return i;
			p = 10 * p;
		}
		return 19;
	}

	/**
	 * Atol.
	 *
	 * @param s the s
	 * @return the long
	 * @throws NumberFormatException the number format exception
	 */
	public static long atol(byte[] s) throws NumberFormatException {
		long result = 0;
		boolean negative = false;
		int i = 0, len = s.length;
		long limit = -Long.MAX_VALUE;
		long multmin;
		int digit;

		if (len > 0) {
			byte firstChar = s[0];
			if (firstChar < '0') {
				if (firstChar == '-') {
					negative = true;
					limit = Long.MIN_VALUE;
				} else
					throw new NumberFormatException();

				if (len == 1)
					throw new NumberFormatException();
				i++;
			}
			multmin = limit / 10;
			while (i < len) {

				digit = Character.digit(s[i++], 10);
				if (digit < 0) {
					throw new NumberFormatException();
				}
				if (result < multmin) {
					throw new NumberFormatException();
				}
				result *= 10;
				if (result < limit + digit) {
					throw new NumberFormatException();
				}
				result -= digit;
			}
		} else {
			throw new NumberFormatException();
		}
		return negative ? result : -result;
	}
}
