/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons JsonXContentGenerator.java 2012-7-6 10:23:43 l.xue.nong$$
 */
package cn.com.rebirth.commons.xcontent.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.com.rebirth.commons.io.Streams;
import cn.com.rebirth.commons.xcontent.XContentGenerator;
import cn.com.rebirth.commons.xcontent.XContentHelper;
import cn.com.rebirth.commons.xcontent.XContentParser;
import cn.com.rebirth.commons.xcontent.XContentString;
import cn.com.rebirth.commons.xcontent.XContentType;

import com.fasterxml.jackson.core.JsonGenerator;

/**
 * The Class JsonXContentGenerator.
 *
 * @author l.xue.nong
 */
public class JsonXContentGenerator implements XContentGenerator {

	/** The generator. */
	protected final JsonGenerator generator;

	/**
	 * Instantiates a new json x content generator.
	 *
	 * @param generator the generator
	 */
	public JsonXContentGenerator(JsonGenerator generator) {
		this.generator = generator;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#contentType()
	 */
	@Override
	public XContentType contentType() {
		return XContentType.JSON;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#usePrettyPrint()
	 */
	@Override
	public void usePrettyPrint() {
		generator.useDefaultPrettyPrinter();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeStartArray()
	 */
	@Override
	public void writeStartArray() throws IOException {
		generator.writeStartArray();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeEndArray()
	 */
	@Override
	public void writeEndArray() throws IOException {
		generator.writeEndArray();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeStartObject()
	 */
	@Override
	public void writeStartObject() throws IOException {
		generator.writeStartObject();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeEndObject()
	 */
	@Override
	public void writeEndObject() throws IOException {
		generator.writeEndObject();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeFieldName(java.lang.String)
	 */
	@Override
	public void writeFieldName(String name) throws IOException {
		generator.writeFieldName(name);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeFieldName(cn.com.rebirth.search.commons.xcontent.XContentString)
	 */
	@Override
	public void writeFieldName(XContentString name) throws IOException {
		generator.writeFieldName(name);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeString(java.lang.String)
	 */
	@Override
	public void writeString(String text) throws IOException {
		generator.writeString(text);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeString(char[], int, int)
	 */
	@Override
	public void writeString(char[] text, int offset, int len) throws IOException {
		generator.writeString(text, offset, len);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeBinary(byte[], int, int)
	 */
	@Override
	public void writeBinary(byte[] data, int offset, int len) throws IOException {
		generator.writeBinary(data, offset, len);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeBinary(byte[])
	 */
	@Override
	public void writeBinary(byte[] data) throws IOException {
		generator.writeBinary(data);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeNumber(int)
	 */
	@Override
	public void writeNumber(int v) throws IOException {
		generator.writeNumber(v);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeNumber(long)
	 */
	@Override
	public void writeNumber(long v) throws IOException {
		generator.writeNumber(v);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeNumber(double)
	 */
	@Override
	public void writeNumber(double d) throws IOException {
		generator.writeNumber(d);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeNumber(float)
	 */
	@Override
	public void writeNumber(float f) throws IOException {
		generator.writeNumber(f);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeBoolean(boolean)
	 */
	@Override
	public void writeBoolean(boolean state) throws IOException {
		generator.writeBoolean(state);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeNull()
	 */
	@Override
	public void writeNull() throws IOException {
		generator.writeNull();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeStringField(java.lang.String, java.lang.String)
	 */
	@Override
	public void writeStringField(String fieldName, String value) throws IOException {
		generator.writeStringField(fieldName, value);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeStringField(cn.com.rebirth.search.commons.xcontent.XContentString, java.lang.String)
	 */
	@Override
	public void writeStringField(XContentString fieldName, String value) throws IOException {
		generator.writeFieldName(fieldName);
		generator.writeString(value);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeBooleanField(java.lang.String, boolean)
	 */
	@Override
	public void writeBooleanField(String fieldName, boolean value) throws IOException {
		generator.writeBooleanField(fieldName, value);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeBooleanField(cn.com.rebirth.search.commons.xcontent.XContentString, boolean)
	 */
	@Override
	public void writeBooleanField(XContentString fieldName, boolean value) throws IOException {
		generator.writeFieldName(fieldName);
		generator.writeBoolean(value);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeNullField(java.lang.String)
	 */
	@Override
	public void writeNullField(String fieldName) throws IOException {
		generator.writeNullField(fieldName);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeNullField(cn.com.rebirth.search.commons.xcontent.XContentString)
	 */
	@Override
	public void writeNullField(XContentString fieldName) throws IOException {
		generator.writeFieldName(fieldName);
		generator.writeNull();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeNumberField(java.lang.String, int)
	 */
	@Override
	public void writeNumberField(String fieldName, int value) throws IOException {
		generator.writeNumberField(fieldName, value);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeNumberField(cn.com.rebirth.search.commons.xcontent.XContentString, int)
	 */
	@Override
	public void writeNumberField(XContentString fieldName, int value) throws IOException {
		generator.writeFieldName(fieldName);
		generator.writeNumber(value);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeNumberField(java.lang.String, long)
	 */
	@Override
	public void writeNumberField(String fieldName, long value) throws IOException {
		generator.writeNumberField(fieldName, value);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeNumberField(cn.com.rebirth.search.commons.xcontent.XContentString, long)
	 */
	@Override
	public void writeNumberField(XContentString fieldName, long value) throws IOException {
		generator.writeFieldName(fieldName);
		generator.writeNumber(value);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeNumberField(java.lang.String, double)
	 */
	@Override
	public void writeNumberField(String fieldName, double value) throws IOException {
		generator.writeNumberField(fieldName, value);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeNumberField(cn.com.rebirth.search.commons.xcontent.XContentString, double)
	 */
	@Override
	public void writeNumberField(XContentString fieldName, double value) throws IOException {
		generator.writeFieldName(fieldName);
		generator.writeNumber(value);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeNumberField(java.lang.String, float)
	 */
	@Override
	public void writeNumberField(String fieldName, float value) throws IOException {
		generator.writeNumberField(fieldName, value);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeNumberField(cn.com.rebirth.search.commons.xcontent.XContentString, float)
	 */
	@Override
	public void writeNumberField(XContentString fieldName, float value) throws IOException {
		generator.writeFieldName(fieldName);
		generator.writeNumber(value);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeBinaryField(java.lang.String, byte[])
	 */
	@Override
	public void writeBinaryField(String fieldName, byte[] data) throws IOException {
		generator.writeBinaryField(fieldName, data);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeBinaryField(cn.com.rebirth.search.commons.xcontent.XContentString, byte[])
	 */
	@Override
	public void writeBinaryField(XContentString fieldName, byte[] value) throws IOException {
		generator.writeFieldName(fieldName);
		generator.writeBinary(value);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeArrayFieldStart(java.lang.String)
	 */
	@Override
	public void writeArrayFieldStart(String fieldName) throws IOException {
		generator.writeArrayFieldStart(fieldName);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeArrayFieldStart(cn.com.rebirth.search.commons.xcontent.XContentString)
	 */
	@Override
	public void writeArrayFieldStart(XContentString fieldName) throws IOException {
		generator.writeFieldName(fieldName);
		generator.writeStartArray();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeObjectFieldStart(java.lang.String)
	 */
	@Override
	public void writeObjectFieldStart(String fieldName) throws IOException {
		generator.writeObjectFieldStart(fieldName);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeObjectFieldStart(cn.com.rebirth.search.commons.xcontent.XContentString)
	 */
	@Override
	public void writeObjectFieldStart(XContentString fieldName) throws IOException {
		generator.writeFieldName(fieldName);
		generator.writeStartObject();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeRawField(java.lang.String, byte[], java.io.OutputStream)
	 */
	@Override
	public void writeRawField(String fieldName, byte[] content, OutputStream bos) throws IOException {
		generator.writeRaw(", \"");
		generator.writeRaw(fieldName);
		generator.writeRaw("\" : ");
		flush();
		bos.write(content);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeRawField(java.lang.String, byte[], int, int, java.io.OutputStream)
	 */
	@Override
	public void writeRawField(String fieldName, byte[] content, int offset, int length, OutputStream bos)
			throws IOException {
		generator.writeRaw(", \"");
		generator.writeRaw(fieldName);
		generator.writeRaw("\" : ");
		flush();
		bos.write(content, offset, length);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeRawField(java.lang.String, java.io.InputStream, java.io.OutputStream)
	 */
	@Override
	public void writeRawField(String fieldName, InputStream content, OutputStream bos) throws IOException {
		generator.writeRaw(", \"");
		generator.writeRaw(fieldName);
		generator.writeRaw("\" : ");
		flush();
		Streams.copy(content, bos);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#copyCurrentStructure(cn.com.rebirth.search.commons.xcontent.XContentParser)
	 */
	@Override
	public void copyCurrentStructure(XContentParser parser) throws IOException {

		if (parser.currentToken() == null) {
			parser.nextToken();
		}
		if (parser instanceof JsonXContentParser) {
			generator.copyCurrentStructure(((JsonXContentParser) parser).parser);
		} else {
			XContentHelper.copyCurrentStructure(this, parser);
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#flush()
	 */
	@Override
	public void flush() throws IOException {
		generator.flush();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#close()
	 */
	@Override
	public void close() throws IOException {
		generator.close();
	}
}
