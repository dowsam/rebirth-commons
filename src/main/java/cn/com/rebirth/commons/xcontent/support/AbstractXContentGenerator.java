/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons AbstractXContentGenerator.java 2012-7-6 10:23:46 l.xue.nong$$
 */
package cn.com.rebirth.commons.xcontent.support;

import java.io.IOException;

import cn.com.rebirth.commons.xcontent.XContentGenerator;

/**
 * The Class AbstractXContentGenerator.
 *
 * @author l.xue.nong
 */
public abstract class AbstractXContentGenerator implements XContentGenerator {

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeStringField(java.lang.String, java.lang.String)
	 */
	@Override
	public void writeStringField(String fieldName, String value) throws IOException {
		writeFieldName(fieldName);
		writeString(value);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeBooleanField(java.lang.String, boolean)
	 */
	@Override
	public void writeBooleanField(String fieldName, boolean value) throws IOException {
		writeFieldName(fieldName);
		writeBoolean(value);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeNullField(java.lang.String)
	 */
	@Override
	public void writeNullField(String fieldName) throws IOException {
		writeFieldName(fieldName);
		writeNull();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeNumberField(java.lang.String, int)
	 */
	@Override
	public void writeNumberField(String fieldName, int value) throws IOException {
		writeFieldName(fieldName);
		writeNumber(value);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeNumberField(java.lang.String, long)
	 */
	@Override
	public void writeNumberField(String fieldName, long value) throws IOException {
		writeFieldName(fieldName);
		writeNumber(value);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeNumberField(java.lang.String, double)
	 */
	@Override
	public void writeNumberField(String fieldName, double value) throws IOException {
		writeFieldName(fieldName);
		writeNumber(value);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeNumberField(java.lang.String, float)
	 */
	@Override
	public void writeNumberField(String fieldName, float value) throws IOException {
		writeFieldName(fieldName);
		writeNumber(value);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeBinaryField(java.lang.String, byte[])
	 */
	@Override
	public void writeBinaryField(String fieldName, byte[] data) throws IOException {
		writeFieldName(fieldName);
		writeBinary(data);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeArrayFieldStart(java.lang.String)
	 */
	@Override
	public void writeArrayFieldStart(String fieldName) throws IOException {
		writeFieldName(fieldName);
		writeStartArray();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentGenerator#writeObjectFieldStart(java.lang.String)
	 */
	@Override
	public void writeObjectFieldStart(String fieldName) throws IOException {
		writeFieldName(fieldName);
		writeStartObject();
	}
}
