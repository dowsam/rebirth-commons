/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons XContent.java 2012-7-6 10:23:50 l.xue.nong$$
 */
package cn.com.rebirth.commons.xcontent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * The Interface XContent.
 *
 * @author l.xue.nong
 */
public interface XContent {

	/**
	 * Type.
	 *
	 * @return the x content type
	 */
	XContentType type();

	/**
	 * Stream separator.
	 *
	 * @return the byte
	 */
	byte streamSeparator();

	/**
	 * Creates the generator.
	 *
	 * @param os the os
	 * @return the x content generator
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	XContentGenerator createGenerator(OutputStream os) throws IOException;

	/**
	 * Creates the generator.
	 *
	 * @param writer the writer
	 * @return the x content generator
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	XContentGenerator createGenerator(Writer writer) throws IOException;

	/**
	 * Creates the parser.
	 *
	 * @param content the content
	 * @return the x content parser
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	XContentParser createParser(String content) throws IOException;

	/**
	 * Creates the parser.
	 *
	 * @param is the is
	 * @return the x content parser
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	XContentParser createParser(InputStream is) throws IOException;

	/**
	 * Creates the parser.
	 *
	 * @param data the data
	 * @return the x content parser
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	XContentParser createParser(byte[] data) throws IOException;

	/**
	 * Creates the parser.
	 *
	 * @param data the data
	 * @param offset the offset
	 * @param length the length
	 * @return the x content parser
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	XContentParser createParser(byte[] data, int offset, int length) throws IOException;

	/**
	 * Creates the parser.
	 *
	 * @param reader the reader
	 * @return the x content parser
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	XContentParser createParser(Reader reader) throws IOException;
}
