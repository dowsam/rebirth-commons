/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons Booleans.java 2012-3-29 15:15:20 l.xue.nong$$
 */

package cn.com.restart.commons;


/**
 * The Class Booleans.
 *
 * @author l.xue.nong
 */
public class Booleans {

	
	/**
	 * Parses the boolean.
	 *
	 * @param text the text
	 * @param offset the offset
	 * @param length the length
	 * @param defaultValue the default value
	 * @return true, if successful
	 */
	public static boolean parseBoolean(char[] text, int offset, int length, boolean defaultValue) {
		if (text == null || length == 0) {
			return defaultValue;
		}
		if (length == 1) {
			return text[offset] != '0';
		}
		if (length == 2) {
			return !(text[offset] == 'n' && text[offset + 1] == 'o');
		}
		if (length == 3) {
			return !(text[offset] == 'o' && text[offset + 1] == 'f' && text[offset + 2] == 'f');
		}
		if (length == 5) {
			return !(text[offset] == 'f' && text[offset + 1] == 'a' && text[offset + 2] == 'l'
					&& text[offset + 3] == 's' && text[offset + 4] == 'e');
		}
		return true;
	}

	
	/**
	 * Parses the boolean.
	 *
	 * @param value the value
	 * @param defaultValue the default value
	 * @return true, if successful
	 */
	public static boolean parseBoolean(String value, boolean defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		return !(value.equals("false") || value.equals("0") || value.equals("off") || value.equals("no"));
	}

	
	/**
	 * Parses the boolean.
	 *
	 * @param value the value
	 * @param defaultValue the default value
	 * @return the boolean
	 */
	public static Boolean parseBoolean(String value, Boolean defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		return !(value.equals("false") || value.equals("0") || value.equals("off") || value.equals("no"));
	}
}
