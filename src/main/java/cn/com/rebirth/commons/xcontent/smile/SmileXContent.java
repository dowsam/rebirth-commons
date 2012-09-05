/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons SmileXContent.java 2012-7-6 10:23:42 l.xue.nong$$
 */
package cn.com.rebirth.commons.xcontent.smile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import cn.com.rebirth.commons.io.FastStringReader;
import cn.com.rebirth.commons.xcontent.XContent;
import cn.com.rebirth.commons.xcontent.XContentBuilder;
import cn.com.rebirth.commons.xcontent.XContentGenerator;
import cn.com.rebirth.commons.xcontent.XContentParser;
import cn.com.rebirth.commons.xcontent.XContentType;
import cn.com.rebirth.commons.xcontent.json.JsonXContentParser;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.fasterxml.jackson.dataformat.smile.SmileGenerator;

/**
 * The Class SmileXContent.
 *
 * @author l.xue.nong
 */
public class SmileXContent implements XContent {

	/**
	 * Content builder.
	 *
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static XContentBuilder contentBuilder() throws IOException {
		return XContentBuilder.builder(smileXContent);
	}

	/** The Constant smileFactory. */
	final static SmileFactory smileFactory;

	/** The Constant smileXContent. */
	public final static SmileXContent smileXContent;

	static {
		smileFactory = new SmileFactory();
		smileFactory.configure(SmileGenerator.Feature.ENCODE_BINARY_AS_7BIT, false);
		smileXContent = new SmileXContent();
	}

	/**
	 * Instantiates a new smile x content.
	 */
	private SmileXContent() {
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContent#type()
	 */
	@Override
	public XContentType type() {
		return XContentType.SMILE;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContent#streamSeparator()
	 */
	@Override
	public byte streamSeparator() {
		return (byte) 0xFF;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContent#createGenerator(java.io.OutputStream)
	 */
	@Override
	public XContentGenerator createGenerator(OutputStream os) throws IOException {
		return new SmileXContentGenerator(smileFactory.createJsonGenerator(os, JsonEncoding.UTF8));
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContent#createGenerator(java.io.Writer)
	 */
	@Override
	public XContentGenerator createGenerator(Writer writer) throws IOException {
		return new SmileXContentGenerator(smileFactory.createJsonGenerator(writer));
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContent#createParser(java.lang.String)
	 */
	@Override
	public XContentParser createParser(String content) throws IOException {
		return new SmileXContentParser(smileFactory.createJsonParser(new FastStringReader(content)));
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContent#createParser(java.io.InputStream)
	 */
	@Override
	public XContentParser createParser(InputStream is) throws IOException {
		return new SmileXContentParser(smileFactory.createJsonParser(is));
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContent#createParser(byte[])
	 */
	@Override
	public XContentParser createParser(byte[] data) throws IOException {
		return new SmileXContentParser(smileFactory.createJsonParser(data));
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContent#createParser(byte[], int, int)
	 */
	@Override
	public XContentParser createParser(byte[] data, int offset, int length) throws IOException {
		return new SmileXContentParser(smileFactory.createJsonParser(data, offset, length));
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContent#createParser(java.io.Reader)
	 */
	@Override
	public XContentParser createParser(Reader reader) throws IOException {
		return new JsonXContentParser(smileFactory.createJsonParser(reader));
	}
}
