/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons CryptoUtils.java 2012-7-6 10:22:13 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

import java.security.GeneralSecurityException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * The Class CryptoUtils.
 *
 * @author l.xue.nong
 */
public abstract class CryptoUtils {

	/** The Constant DES. */
	private static final String DES = "DES";

	/** The Constant AES. */
	private static final String AES = "AES";

	/** The Constant HMACSHA1. */
	private static final String HMACSHA1 = "HmacSHA1";

	/** The Constant DEFAULT_HMACSHA1_KEYSIZE. */
	private static final int DEFAULT_HMACSHA1_KEYSIZE = 160;//RFC2401

	/** The Constant DEFAULT_AES_KEYSIZE. */
	private static final int DEFAULT_AES_KEYSIZE = 128;

	//-- HMAC-SHA1 funciton --//
	/**
	 * Hmac sha1.
	 *
	 * @param input the input
	 * @param keyBytes the key bytes
	 * @return the byte[]
	 */
	public static byte[] hmacSha1(String input, byte[] keyBytes) {
		try {
			SecretKey secretKey = new SecretKeySpec(keyBytes, HMACSHA1);
			Mac mac = Mac.getInstance(HMACSHA1);
			mac.init(secretKey);
			return mac.doFinal(input.getBytes());
		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("Security exception", e);
		}
	}

	/**
	 * Hmac sha1 to hex.
	 *
	 * @param input the input
	 * @param keyBytes the key bytes
	 * @return the string
	 */
	public static String hmacSha1ToHex(String input, byte[] keyBytes) {
		byte[] macResult = hmacSha1(input, keyBytes);
		return EncodeUtils.encodeHex(macResult);
	}

	/**
	 * Hmac sha1 to base64.
	 *
	 * @param input the input
	 * @param keyBytes the key bytes
	 * @return the string
	 */
	public static String hmacSha1ToBase64(String input, byte[] keyBytes) {
		byte[] macResult = hmacSha1(input, keyBytes);
		return EncodeUtils.encodeBase64(macResult);
	}

	/**
	 * Hmac sha1 to base64 url safe.
	 *
	 * @param input the input
	 * @param keyBytes the key bytes
	 * @return the string
	 */
	public static String hmacSha1ToBase64UrlSafe(String input, byte[] keyBytes) {
		byte[] macResult = hmacSha1(input, keyBytes);
		return EncodeUtils.encodeUrlSafeBase64(macResult);
	}

	/**
	 * Checks if is hex mac valid.
	 *
	 * @param hexMac the hex mac
	 * @param input the input
	 * @param keyBytes the key bytes
	 * @return true, if is hex mac valid
	 */
	public static boolean isHexMacValid(String hexMac, String input, byte[] keyBytes) {
		byte[] expected = EncodeUtils.decodeHex(hexMac);
		byte[] actual = hmacSha1(input, keyBytes);

		return Arrays.equals(expected, actual);
	}

	/**
	 * Checks if is base64 mac valid.
	 *
	 * @param base64Mac the base64 mac
	 * @param input the input
	 * @param keyBytes the key bytes
	 * @return true, if is base64 mac valid
	 */
	public static boolean isBase64MacValid(String base64Mac, String input, byte[] keyBytes) {
		byte[] expected = EncodeUtils.decodeBase64(base64Mac);
		byte[] actual = hmacSha1(input, keyBytes);

		return Arrays.equals(expected, actual);
	}

	/**
	 * Generate mac sha1 key.
	 *
	 * @return the byte[]
	 */
	public static byte[] generateMacSha1Key() {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance(HMACSHA1);
			keyGenerator.init(DEFAULT_HMACSHA1_KEYSIZE);
			SecretKey secretKey = keyGenerator.generateKey();
			return secretKey.getEncoded();
		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("Security exception", e);
		}
	}

	/**
	 * Generate mac sha1 hex key.
	 *
	 * @return the string
	 */
	public static String generateMacSha1HexKey() {
		return EncodeUtils.encodeHex(generateMacSha1Key());
	}

	//-- DES function --//
	/**
	 * Des encrypt to hex.
	 *
	 * @param input the input
	 * @param keyBytes the key bytes
	 * @return the string
	 */
	public static String desEncryptToHex(String input, byte[] keyBytes) {
		byte[] encryptResult = des(input.getBytes(), keyBytes, Cipher.ENCRYPT_MODE);
		return EncodeUtils.encodeHex(encryptResult);
	}

	/**
	 * Des encrypt to base64.
	 *
	 * @param input the input
	 * @param keyBytes the key bytes
	 * @return the string
	 */
	public static String desEncryptToBase64(String input, byte[] keyBytes) {
		byte[] encryptResult = des(input.getBytes(), keyBytes, Cipher.ENCRYPT_MODE);
		return EncodeUtils.encodeBase64(encryptResult);
	}

	/**
	 * Des decrypt from hex.
	 *
	 * @param input the input
	 * @param keyBytes the key bytes
	 * @return the string
	 */
	public static String desDecryptFromHex(String input, byte[] keyBytes) {
		byte[] decryptResult = des(EncodeUtils.decodeHex(input), keyBytes, Cipher.DECRYPT_MODE);
		return new String(decryptResult);
	}

	/**
	 * Des decrypt from base64.
	 *
	 * @param input the input
	 * @param keyBytes the key bytes
	 * @return the string
	 */
	public static String desDecryptFromBase64(String input, byte[] keyBytes) {
		byte[] decryptResult = des(EncodeUtils.decodeBase64(input), keyBytes, Cipher.DECRYPT_MODE);
		return new String(decryptResult);
	}

	/**
	 * Des.
	 *
	 * @param inputBytes the input bytes
	 * @param keyBytes the key bytes
	 * @param mode the mode
	 * @return the byte[]
	 */
	private static byte[] des(byte[] inputBytes, byte[] keyBytes, int mode) {
		try {
			DESKeySpec desKeySpec = new DESKeySpec(keyBytes);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

			Cipher cipher = Cipher.getInstance(DES);
			cipher.init(mode, secretKey);
			return cipher.doFinal(inputBytes);
		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("Security exception", e);
		}
	}

	/**
	 * Generate des key.
	 *
	 * @return the byte[]
	 */
	public static byte[] generateDesKey() {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance(DES);
			SecretKey secretKey = keyGenerator.generateKey();
			return secretKey.getEncoded();
		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("Security exception", e);
		}
	}

	/**
	 * Generate des hex key.
	 *
	 * @return the string
	 */
	public static String generateDesHexKey() {
		return EncodeUtils.encodeHex(generateDesKey());
	}

	//-- AES funciton --//
	/**
	 * Aes encrypt to hex.
	 *
	 * @param input the input
	 * @param keyBytes the key bytes
	 * @return the string
	 */
	public static String aesEncryptToHex(String input, byte[] keyBytes) {
		byte[] encryptResult = aes(input.getBytes(), keyBytes, Cipher.ENCRYPT_MODE);
		return EncodeUtils.encodeHex(encryptResult);
	}

	/**
	 * Aes encrypt to base64.
	 *
	 * @param input the input
	 * @param keyBytes the key bytes
	 * @return the string
	 */
	public static String aesEncryptToBase64(String input, byte[] keyBytes) {
		byte[] encryptResult = aes(input.getBytes(), keyBytes, Cipher.ENCRYPT_MODE);
		return EncodeUtils.encodeBase64(encryptResult);
	}

	/**
	 * Aes decrypt from hex.
	 *
	 * @param input the input
	 * @param keyBytes the key bytes
	 * @return the string
	 */
	public static String aesDecryptFromHex(String input, byte[] keyBytes) {
		byte[] decryptResult = aes(EncodeUtils.decodeHex(input), keyBytes, Cipher.DECRYPT_MODE);
		return new String(decryptResult);
	}

	/**
	 * Aes decrypt from base64.
	 *
	 * @param input the input
	 * @param keyBytes the key bytes
	 * @return the string
	 */
	public static String aesDecryptFromBase64(String input, byte[] keyBytes) {
		byte[] decryptResult = aes(EncodeUtils.decodeBase64(input), keyBytes, Cipher.DECRYPT_MODE);
		return new String(decryptResult);
	}

	/**
	 * Aes.
	 *
	 * @param inputBytes the input bytes
	 * @param keyBytes the key bytes
	 * @param mode the mode
	 * @return the byte[]
	 */
	private static byte[] aes(byte[] inputBytes, byte[] keyBytes, int mode) {
		try {
			SecretKey secretKey = new SecretKeySpec(keyBytes, AES);
			Cipher cipher = Cipher.getInstance(AES);
			cipher.init(mode, secretKey);
			return cipher.doFinal(inputBytes);
		} catch (Exception e) {
			throw new IllegalStateException("Security exception", e);
		}
	}

	/**
	 * Generate aes key.
	 *
	 * @return the byte[]
	 */
	public static byte[] generateAesKey() {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
			keyGenerator.init(DEFAULT_AES_KEYSIZE);
			SecretKey secretKey = keyGenerator.generateKey();
			return secretKey.getEncoded();
		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("Security exception", e);
		}
	}

	/**
	 * Generate aes hex key.
	 *
	 * @return the string
	 */
	public static String generateAesHexKey() {
		return EncodeUtils.encodeHex(generateAesKey());
	}
}
