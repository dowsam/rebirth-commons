/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons Streams.java 2012-7-6 10:23:51 l.xue.nong$$
 */

package cn.com.rebirth.commons.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import cn.com.rebirth.commons.Preconditions;
import cn.com.rebirth.commons.io.stream.BytesStreamOutput;
import cn.com.rebirth.commons.io.stream.CachedStreamOutput;

/**
 * The Class Streams.
 *
 * @author l.xue.nong
 */
public abstract class Streams {

	/** The Constant BUFFER_SIZE. */
	public static final int BUFFER_SIZE = 1024 * 8;

	/**
	 * Copy.
	 *
	 * @param in the in
	 * @param out the out
	 * @return the long
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static long copy(File in, File out) throws IOException {
		Preconditions.checkNotNull(in, "No input File specified");
		Preconditions.checkNotNull(out, "No output File specified");
		return copy(new BufferedInputStream(new FileInputStream(in)), new BufferedOutputStream(
				new FileOutputStream(out)));
	}

	/**
	 * Copy.
	 *
	 * @param in the in
	 * @param out the out
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void copy(byte[] in, File out) throws IOException {
		Preconditions.checkNotNull(in, "No input byte array specified");
		Preconditions.checkNotNull(out, "No output File specified");
		ByteArrayInputStream inStream = new ByteArrayInputStream(in);
		OutputStream outStream = new BufferedOutputStream(new FileOutputStream(out));
		copy(inStream, outStream);
	}

	/**
	 * Copy to byte array.
	 *
	 * @param in the in
	 * @return the byte[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static byte[] copyToByteArray(File in) throws IOException {
		Preconditions.checkNotNull(in, "No input File specified");
		return copyToByteArray(new BufferedInputStream(new FileInputStream(in)));
	}

	/**
	 * Copy.
	 *
	 * @param in the in
	 * @param out the out
	 * @return the long
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static long copy(InputStream in, OutputStream out) throws IOException {
		return copy(in, out, new byte[BUFFER_SIZE]);
	}

	/**
	 * Copy.
	 *
	 * @param in the in
	 * @param out the out
	 * @param buffer the buffer
	 * @return the long
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static long copy(InputStream in, OutputStream out, byte[] buffer) throws IOException {
		Preconditions.checkNotNull(in, "No InputStream specified");
		Preconditions.checkNotNull(out, "No OutputStream specified");
		try {
			long byteCount = 0;
			int bytesRead;
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
				byteCount += bytesRead;
			}
			out.flush();
			return byteCount;
		} finally {
			try {
				in.close();
			} catch (IOException ex) {

			}
			try {
				out.close();
			} catch (IOException ex) {

			}
		}
	}

	/**
	 * Copy.
	 *
	 * @param in the in
	 * @param out the out
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void copy(byte[] in, OutputStream out) throws IOException {
		Preconditions.checkNotNull(in, "No input byte array specified");
		Preconditions.checkNotNull(out, "No OutputStream specified");
		try {
			out.write(in);
		} finally {
			try {
				out.close();
			} catch (IOException ex) {

			}
		}
	}

	/**
	 * Copy to byte array.
	 *
	 * @param in the in
	 * @return the byte[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static byte[] copyToByteArray(InputStream in) throws IOException {
		CachedStreamOutput.Entry cachedEntry = CachedStreamOutput.popEntry();
		try {
			BytesStreamOutput out = cachedEntry.cachedBytes();
			copy(in, out);
			return out.copiedByteArray();
		} finally {
			CachedStreamOutput.pushEntry(cachedEntry);
		}
	}

	/**
	 * Copy.
	 *
	 * @param in the in
	 * @param out the out
	 * @return the int
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static int copy(Reader in, Writer out) throws IOException {
		Preconditions.checkNotNull(in, "No Reader specified");
		Preconditions.checkNotNull(out, "No Writer specified");
		try {
			int byteCount = 0;
			char[] buffer = new char[BUFFER_SIZE];
			int bytesRead;
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
				byteCount += bytesRead;
			}
			out.flush();
			return byteCount;
		} finally {
			try {
				in.close();
			} catch (IOException ex) {

			}
			try {
				out.close();
			} catch (IOException ex) {

			}
		}
	}

	/**
	 * Copy.
	 *
	 * @param in the in
	 * @param out the out
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void copy(String in, Writer out) throws IOException {
		Preconditions.checkNotNull(in, "No input String specified");
		Preconditions.checkNotNull(out, "No Writer specified");
		try {
			out.write(in);
		} finally {
			try {
				out.close();
			} catch (IOException ex) {

			}
		}
	}

	/**
	 * Copy to string.
	 *
	 * @param in the in
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String copyToString(Reader in) throws IOException {
		StringWriter out = new StringWriter();
		copy(in, out);
		return out.toString();
	}

	/**
	 * Copy to string from classpath.
	 *
	 * @param classLoader the class loader
	 * @param path the path
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String copyToStringFromClasspath(ClassLoader classLoader, String path) throws IOException {
		InputStream is = classLoader.getResourceAsStream(path);
		if (is == null) {
			throw new FileNotFoundException("Resource [" + path + "] not found in classpath with class loader ["
					+ classLoader + "]");
		}
		return copyToString(new InputStreamReader(is, "UTF-8"));
	}

	/**
	 * Copy to string from classpath.
	 *
	 * @param path the path
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String copyToStringFromClasspath(String path) throws IOException {
		InputStream is = Streams.class.getResourceAsStream(path);
		if (is == null) {
			throw new FileNotFoundException("Resource [" + path + "] not found in classpath");
		}
		return copyToString(new InputStreamReader(is));
	}

	/**
	 * Copy to bytes from classpath.
	 *
	 * @param path the path
	 * @return the byte[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static byte[] copyToBytesFromClasspath(String path) throws IOException {
		InputStream is = Streams.class.getResourceAsStream(path);
		if (is == null) {
			throw new FileNotFoundException("Resource [" + path + "] not found in classpath");
		}
		return copyToByteArray(is);
	}
}
