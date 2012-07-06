/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons DigestUtils.java 2012-7-6 10:22:15 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

/**
 * The Class DigestUtils.
 *
 * @author l.xue.nong
 */
public abstract class DigestUtils {
	
	/** The Constant SHA1. */
	private static final String SHA1 = "SHA-1";

	/** The Constant MD5. */
	private static final String MD5 = "MD5";

	// -- String Hash function --//
	/**
	 * Sha1 to hex.
	 *
	 * @param input the input
	 * @return the string
	 */
	public static String sha1ToHex(String input) {
		byte[] digestResult = digest(input, SHA1);
		return EncodeUtils.encodeHex(digestResult);
	}

	/**
	 * Sha1 to base64.
	 *
	 * @param input the input
	 * @return the string
	 */
	public static String sha1ToBase64(String input) {
		byte[] digestResult = digest(input, SHA1);
		return EncodeUtils.encodeBase64(digestResult);
	}

	/**
	 * Sha1 to base64 url safe.
	 *
	 * @param input the input
	 * @return the string
	 */
	public static String sha1ToBase64UrlSafe(String input) {
		byte[] digestResult = digest(input, SHA1);
		return EncodeUtils.encodeUrlSafeBase64(digestResult);
	}

	/**
	 * Digest.
	 *
	 * @param input the input
	 * @param algorithm the algorithm
	 * @return the byte[]
	 */
	private static byte[] digest(String input, String algorithm) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			return messageDigest.digest(input.getBytes());
		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("Security exception", e);
		}
	}

	// -- File Hash function --//
	/**
	 * Md5 to hex.
	 *
	 * @param input the input
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String md5ToHex(InputStream input) throws IOException {
		return digest(input, MD5);
	}

	/**
	 * Sha1 to hex.
	 *
	 * @param input the input
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String sha1ToHex(InputStream input) throws IOException {
		return digest(input, SHA1);
	}

	/**
	 * Digest.
	 *
	 * @param input the input
	 * @param algorithm the algorithm
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static String digest(InputStream input, String algorithm) throws IOException {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			int bufferLength = 1024;
			byte[] buffer = new byte[bufferLength];
			int read = input.read(buffer, 0, bufferLength);

			while (read > -1) {
				messageDigest.update(buffer, 0, read);
				read = input.read(buffer, 0, bufferLength);
			}

			return EncodeUtils.encodeHex(messageDigest.digest());

		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("Security exception", e);
		}
	}
}
