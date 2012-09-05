/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons SmileXContentGenerator.java 2012-7-6 10:23:54 l.xue.nong$$
 */
package cn.com.rebirth.commons.xcontent.smile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.com.rebirth.commons.xcontent.XContentType;
import cn.com.rebirth.commons.xcontent.json.JsonXContentGenerator;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.dataformat.smile.SmileParser;

/**
 * The Class SmileXContentGenerator.
 *
 * @author l.xue.nong
 */
public class SmileXContentGenerator extends JsonXContentGenerator {

	/**
	 * Instantiates a new smile x content generator.
	 *
	 * @param generator the generator
	 */
	public SmileXContentGenerator(JsonGenerator generator) {
		super(generator);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.json.JsonXContentGenerator#contentType()
	 */
	@Override
	public XContentType contentType() {
		return XContentType.SMILE;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.json.JsonXContentGenerator#writeRawField(java.lang.String, java.io.InputStream, java.io.OutputStream)
	 */
	@Override
	public void writeRawField(String fieldName, InputStream content, OutputStream bos) throws IOException {
		writeFieldName(fieldName);
		SmileParser parser = SmileXContent.smileFactory.createJsonParser(content);
		try {
			parser.nextToken();
			generator.copyCurrentStructure(parser);
		} finally {
			parser.close();
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.json.JsonXContentGenerator#writeRawField(java.lang.String, byte[], java.io.OutputStream)
	 */
	@Override
	public void writeRawField(String fieldName, byte[] content, OutputStream bos) throws IOException {
		writeFieldName(fieldName);
		SmileParser parser = SmileXContent.smileFactory.createJsonParser(content);
		try {
			parser.nextToken();
			generator.copyCurrentStructure(parser);
		} finally {
			parser.close();
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.json.JsonXContentGenerator#writeRawField(java.lang.String, byte[], int, int, java.io.OutputStream)
	 */
	@Override
	public void writeRawField(String fieldName, byte[] content, int offset, int length, OutputStream bos)
			throws IOException {
		writeFieldName(fieldName);
		SmileParser parser = SmileXContent.smileFactory.createJsonParser(content, offset, length);
		try {
			parser.nextToken();
			generator.copyCurrentStructure(parser);
		} finally {
			parser.close();
		}
	}
}
