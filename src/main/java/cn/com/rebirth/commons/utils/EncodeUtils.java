/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons EncodeUtils.java 2012-7-6 10:22:13 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.Validate;

/**
 * The Class EncodeUtils.
 *
 * @author l.xue.nong
 */
public abstract class EncodeUtils {
	
	/** The Constant ALPHABET. */
	private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	/** The Constant DEFAULT_URL_ENCODING. */
	private static final String DEFAULT_URL_ENCODING = "UTF-8";

	
	/**
	 * Encode hex.
	 *
	 * @param input the input
	 * @return the string
	 */
	public static String encodeHex(byte[] input) {
		return Hex.encodeHexString(input);
	}

	/**
	 * Decode hex.
	 *
	 * @param input the input
	 * @return the byte[]
	 */
	public static byte[] decodeHex(String input) {
		try {
			return Hex.decodeHex(input.toCharArray());
		} catch (DecoderException e) {
			throw new IllegalStateException("Hex Decoder exception", e);
		}
	}

	/**
	 * Encode base64.
	 *
	 * @param input the input
	 * @return the string
	 */
	public static String encodeBase64(byte[] input) {
		return Base64.encodeBase64String(input);
	}

	/**
	 * Encode url safe base64.
	 *
	 * @param input the input
	 * @return the string
	 */
	public static String encodeUrlSafeBase64(byte[] input) {
		return Base64.encodeBase64URLSafeString(input);
	}

	/**
	 * Decode base64.
	 *
	 * @param input the input
	 * @return the byte[]
	 */
	public static byte[] decodeBase64(String input) {
		return Base64.decodeBase64(input);
	}

	/**
	 * Encode base62.
	 *
	 * @param num the num
	 * @return the string
	 */
	public static String encodeBase62(long num) {
		return alphabetEncode(num, 62);
	}

	/**
	 * Decode base62.
	 *
	 * @param str the str
	 * @return the long
	 */
	public static long decodeBase62(String str) {
		return alphabetDecode(str, 62);
	}

	/**
	 * Alphabet encode.
	 *
	 * @param num the num
	 * @param base the base
	 * @return the string
	 */
	private static String alphabetEncode(long num, int base) {
		num = Math.abs(num);
		StringBuilder sb = new StringBuilder();
		for (; num > 0; num /= base) {
			sb.append(ALPHABET.charAt((int) (num % base)));
		}

		return sb.toString();
	}

	/**
	 * Alphabet decode.
	 *
	 * @param str the str
	 * @param base the base
	 * @return the long
	 */
	private static long alphabetDecode(String str, int base) {
		Validate.notBlank(str);

		long result = 0;
		for (int i = 0; i < str.length(); i++) {
			result += ALPHABET.indexOf(str.charAt(i)) * Math.pow(base, i);
		}

		return result;
	}

	/**
	 * Escape html.
	 *
	 * @param html the html
	 * @return the string
	 */
	public static String escapeHtml(String html) {
		return StringEscapeUtils.escapeHtml4(html);
	}

	/**
	 * Unescape html.
	 *
	 * @param htmlEscaped the html escaped
	 * @return the string
	 */
	public static String unescapeHtml(String htmlEscaped) {
		return StringEscapeUtils.unescapeHtml4(htmlEscaped);
	}

	/**
	 * Escape xml.
	 *
	 * @param xml the xml
	 * @return the string
	 */
	public static String escapeXml(String xml) {
		return StringEscapeUtils.escapeXml(xml);
	}

	/**
	 * Unescape xml.
	 *
	 * @param xmlEscaped the xml escaped
	 * @return the string
	 */
	public static String unescapeXml(String xmlEscaped) {
		return StringEscapeUtils.unescapeXml(xmlEscaped);
	}

	/**
	 * Escape csv.
	 *
	 * @param csv the csv
	 * @return the string
	 */
	public static String escapeCsv(String csv) {
		return StringEscapeUtils.escapeCsv(csv);
	}

	/**
	 * Unescape csv.
	 *
	 * @param csvEscaped the csv escaped
	 * @return the string
	 */
	public static String unescapeCsv(String csvEscaped) {
		return StringEscapeUtils.unescapeCsv(csvEscaped);
	}

	/**
	 * Url encode.
	 *
	 * @param part the part
	 * @return the string
	 */
	public static String urlEncode(String part) {
		try {
			return URLEncoder.encode(part, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * Url decode.
	 *
	 * @param part the part
	 * @return the string
	 */
	public static String urlDecode(String part) {

		try {
			return URLDecoder.decode(part, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw ExceptionUtils.unchecked(e);
		}
	}
}
