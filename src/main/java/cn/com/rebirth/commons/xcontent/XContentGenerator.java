/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons XContentGenerator.java 2012-7-6 10:23:51 l.xue.nong$$
 */
package cn.com.rebirth.commons.xcontent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * The Interface XContentGenerator.
 *
 * @author l.xue.nong
 */
public interface XContentGenerator {

	/**
	 * Content type.
	 *
	 * @return the x content type
	 */
	XContentType contentType();

	/**
	 * Use pretty print.
	 */
	void usePrettyPrint();

	/**
	 * Write start array.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeStartArray() throws IOException;

	/**
	 * Write end array.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeEndArray() throws IOException;

	/**
	 * Write start object.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeStartObject() throws IOException;

	/**
	 * Write end object.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeEndObject() throws IOException;

	/**
	 * Write field name.
	 *
	 * @param name the name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeFieldName(String name) throws IOException;

	/**
	 * Write field name.
	 *
	 * @param name the name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeFieldName(XContentString name) throws IOException;

	/**
	 * Write string.
	 *
	 * @param text the text
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeString(String text) throws IOException;

	/**
	 * Write string.
	 *
	 * @param text the text
	 * @param offset the offset
	 * @param len the len
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeString(char[] text, int offset, int len) throws IOException;

	/**
	 * Write binary.
	 *
	 * @param data the data
	 * @param offset the offset
	 * @param len the len
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeBinary(byte[] data, int offset, int len) throws IOException;

	/**
	 * Write binary.
	 *
	 * @param data the data
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeBinary(byte[] data) throws IOException;

	/**
	 * Write number.
	 *
	 * @param v the v
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeNumber(int v) throws IOException;

	/**
	 * Write number.
	 *
	 * @param v the v
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeNumber(long v) throws IOException;

	/**
	 * Write number.
	 *
	 * @param d the d
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeNumber(double d) throws IOException;

	/**
	 * Write number.
	 *
	 * @param f the f
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeNumber(float f) throws IOException;

	/**
	 * Write boolean.
	 *
	 * @param state the state
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeBoolean(boolean state) throws IOException;

	/**
	 * Write null.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeNull() throws IOException;

	/**
	 * Write string field.
	 *
	 * @param fieldName the field name
	 * @param value the value
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeStringField(String fieldName, String value) throws IOException;

	/**
	 * Write string field.
	 *
	 * @param fieldName the field name
	 * @param value the value
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeStringField(XContentString fieldName, String value) throws IOException;

	/**
	 * Write boolean field.
	 *
	 * @param fieldName the field name
	 * @param value the value
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeBooleanField(String fieldName, boolean value) throws IOException;

	/**
	 * Write boolean field.
	 *
	 * @param fieldName the field name
	 * @param value the value
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeBooleanField(XContentString fieldName, boolean value) throws IOException;

	/**
	 * Write null field.
	 *
	 * @param fieldName the field name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeNullField(String fieldName) throws IOException;

	/**
	 * Write null field.
	 *
	 * @param fieldName the field name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeNullField(XContentString fieldName) throws IOException;

	/**
	 * Write number field.
	 *
	 * @param fieldName the field name
	 * @param value the value
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeNumberField(String fieldName, int value) throws IOException;

	/**
	 * Write number field.
	 *
	 * @param fieldName the field name
	 * @param value the value
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeNumberField(XContentString fieldName, int value) throws IOException;

	/**
	 * Write number field.
	 *
	 * @param fieldName the field name
	 * @param value the value
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeNumberField(String fieldName, long value) throws IOException;

	/**
	 * Write number field.
	 *
	 * @param fieldName the field name
	 * @param value the value
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeNumberField(XContentString fieldName, long value) throws IOException;

	/**
	 * Write number field.
	 *
	 * @param fieldName the field name
	 * @param value the value
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeNumberField(String fieldName, double value) throws IOException;

	/**
	 * Write number field.
	 *
	 * @param fieldName the field name
	 * @param value the value
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeNumberField(XContentString fieldName, double value) throws IOException;

	/**
	 * Write number field.
	 *
	 * @param fieldName the field name
	 * @param value the value
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeNumberField(String fieldName, float value) throws IOException;

	/**
	 * Write number field.
	 *
	 * @param fieldName the field name
	 * @param value the value
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeNumberField(XContentString fieldName, float value) throws IOException;

	/**
	 * Write binary field.
	 *
	 * @param fieldName the field name
	 * @param data the data
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeBinaryField(String fieldName, byte[] data) throws IOException;

	/**
	 * Write binary field.
	 *
	 * @param fieldName the field name
	 * @param data the data
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeBinaryField(XContentString fieldName, byte[] data) throws IOException;

	/**
	 * Write array field start.
	 *
	 * @param fieldName the field name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeArrayFieldStart(String fieldName) throws IOException;

	/**
	 * Write array field start.
	 *
	 * @param fieldName the field name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeArrayFieldStart(XContentString fieldName) throws IOException;

	/**
	 * Write object field start.
	 *
	 * @param fieldName the field name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeObjectFieldStart(String fieldName) throws IOException;

	/**
	 * Write object field start.
	 *
	 * @param fieldName the field name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeObjectFieldStart(XContentString fieldName) throws IOException;

	/**
	 * Write raw field.
	 *
	 * @param fieldName the field name
	 * @param content the content
	 * @param bos the bos
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeRawField(String fieldName, byte[] content, OutputStream bos) throws IOException;

	/**
	 * Write raw field.
	 *
	 * @param fieldName the field name
	 * @param content the content
	 * @param offset the offset
	 * @param length the length
	 * @param bos the bos
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeRawField(String fieldName, byte[] content, int offset, int length, OutputStream bos) throws IOException;

	/**
	 * Write raw field.
	 *
	 * @param fieldName the field name
	 * @param content the content
	 * @param bos the bos
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void writeRawField(String fieldName, InputStream content, OutputStream bos) throws IOException;

	/**
	 * Copy current structure.
	 *
	 * @param parser the parser
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void copyCurrentStructure(XContentParser parser) throws IOException;

	/**
	 * Flush.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void flush() throws IOException;

	/**
	 * Close.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void close() throws IOException;
}
