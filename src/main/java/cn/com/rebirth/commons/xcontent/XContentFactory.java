/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons XContentFactory.java 2012-7-6 10:23:47 l.xue.nong$$
 */


package cn.com.rebirth.commons.xcontent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import cn.com.rebirth.commons.exception.RebirthIllegalArgumentException;
import cn.com.rebirth.commons.exception.RebirthParseException;
import cn.com.rebirth.commons.xcontent.json.JsonXContent;
import cn.com.rebirth.commons.xcontent.smile.SmileXContent;

import com.fasterxml.jackson.dataformat.smile.SmileConstants;



/**
 * A factory for creating XContent objects.
 */
public class XContentFactory {

	
	/** The guess header length. */
	private static int GUESS_HEADER_LENGTH = 20;

	
	/** The Constant contents. */
	private static final XContent[] contents;

	static {
		contents = new XContent[2];
		contents[0] = JsonXContent.jsonXContent;
		contents[1] = SmileXContent.smileXContent;
	}

	
	/**
	 * Json builder.
	 *
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static XContentBuilder jsonBuilder() throws IOException {
		return contentBuilder(XContentType.JSON);
	}

	
	/**
	 * Json builder.
	 *
	 * @param os the os
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static XContentBuilder jsonBuilder(OutputStream os) throws IOException {
		return new XContentBuilder(JsonXContent.jsonXContent, os);
	}

	
	/**
	 * Smile builder.
	 *
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static XContentBuilder smileBuilder() throws IOException {
		return contentBuilder(XContentType.SMILE);
	}

	
	/**
	 * Smile builder.
	 *
	 * @param os the os
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static XContentBuilder smileBuilder(OutputStream os) throws IOException {
		return new XContentBuilder(SmileXContent.smileXContent, os);
	}

	
	/**
	 * Content builder.
	 *
	 * @param type the type
	 * @param outputStream the output stream
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static XContentBuilder contentBuilder(XContentType type, OutputStream outputStream) throws IOException {
		if (type == XContentType.JSON) {
			return jsonBuilder(outputStream);
		} else if (type == XContentType.SMILE) {
			return smileBuilder(outputStream);
		}
		throw new RebirthIllegalArgumentException("No matching content type for " + type);
	}

	
	/**
	 * Content builder.
	 *
	 * @param type the type
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static XContentBuilder contentBuilder(XContentType type) throws IOException {
		if (type == XContentType.JSON) {
			return JsonXContent.contentBuilder();
		} else if (type == XContentType.SMILE) {
			return SmileXContent.contentBuilder();
		}
		throw new RebirthIllegalArgumentException("No matching content type for " + type);
	}

	
	/**
	 * X content.
	 *
	 * @param type the type
	 * @return the x content
	 */
	public static XContent xContent(XContentType type) {
		return contents[type.index()];
	}

	
	/**
	 * X content type.
	 *
	 * @param content the content
	 * @return the x content type
	 */
	public static XContentType xContentType(CharSequence content) {
		int length = content.length() < GUESS_HEADER_LENGTH ? content.length() : GUESS_HEADER_LENGTH;
		for (int i = 0; i < length; i++) {
			char c = content.charAt(i);
			if (c == '{') {
				return XContentType.JSON;
			}
		}
		return null;
	}

	
	/**
	 * X content.
	 *
	 * @param content the content
	 * @return the x content
	 */
	public static XContent xContent(CharSequence content) {
		XContentType type = xContentType(content);
		if (type == null) {
			throw new RebirthParseException("Failed to derive xcontent from " + content);
		}
		return xContent(type);
	}

	
	/**
	 * X content.
	 *
	 * @param data the data
	 * @return the x content
	 */
	public static XContent xContent(byte[] data) {
		return xContent(data, 0, data.length);
	}

	
	/**
	 * X content.
	 *
	 * @param data the data
	 * @param offset the offset
	 * @param length the length
	 * @return the x content
	 */
	public static XContent xContent(byte[] data, int offset, int length) {
		XContentType type = xContentType(data, offset, length);
		if (type == null) {
			throw new RebirthParseException("Failed to derive xcontent from (offset=" + offset + ", length="
					+ length + "): " + Arrays.toString(data));
		}
		return xContent(type);
	}

	
	/**
	 * X content type.
	 *
	 * @param data the data
	 * @return the x content type
	 */
	public static XContentType xContentType(byte[] data) {
		return xContentType(data, 0, data.length);
	}

	
	/**
	 * X content type.
	 *
	 * @param si the si
	 * @return the x content type
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static XContentType xContentType(InputStream si) throws IOException {
		int first = si.read();
		if (first == -1) {
			return null;
		}
		int second = si.read();
		if (second == -1) {
			return null;
		}
		if (first == SmileConstants.HEADER_BYTE_1 && second == SmileConstants.HEADER_BYTE_2) {
			int third = si.read();
			if (third == SmileConstants.HEADER_BYTE_3) {
				return XContentType.SMILE;
			}
		}
		if (first == '{' || second == '{') {
			return XContentType.JSON;
		}
		for (int i = 2; i < GUESS_HEADER_LENGTH; i++) {
			int val = si.read();
			if (val == -1) {
				return null;
			}
			if (val == '{') {
				return XContentType.JSON;
			}
		}
		return null;
	}

	
	/**
	 * X content type.
	 *
	 * @param data the data
	 * @param offset the offset
	 * @param length the length
	 * @return the x content type
	 */
	public static XContentType xContentType(byte[] data, int offset, int length) {
		length = length < GUESS_HEADER_LENGTH ? length : GUESS_HEADER_LENGTH;
		if (length > 2 && data[offset] == SmileConstants.HEADER_BYTE_1
				&& data[offset + 1] == SmileConstants.HEADER_BYTE_2 && data[offset + 2] == SmileConstants.HEADER_BYTE_3) {
			return XContentType.SMILE;
		}
		int size = offset + length;
		for (int i = offset; i < size; i++) {
			if (data[i] == '{') {
				return XContentType.JSON;
			}
		}
		return null;
	}
}
