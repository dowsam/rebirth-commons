/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons XContentHelper.java 2012-7-6 10:23:51 l.xue.nong$$
 */

package cn.com.rebirth.commons.xcontent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import cn.com.rebirth.commons.collect.Tuple;
import cn.com.rebirth.commons.compress.lzf.LZF;
import cn.com.rebirth.commons.exception.RebirthParseException;
import cn.com.rebirth.commons.io.stream.BytesStreamInput;
import cn.com.rebirth.commons.io.stream.CachedStreamInput;
import cn.com.rebirth.commons.io.stream.LZFStreamInput;

import com.google.common.base.Charsets;

/**
 * The Class XContentHelper.
 *
 * @author l.xue.nong
 */
public class XContentHelper {

	/**
	 * Creates the parser.
	 *
	 * @param data the data
	 * @param offset the offset
	 * @param length the length
	 * @return the x content parser
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static XContentParser createParser(byte[] data, int offset, int length) throws IOException {
		if (LZF.isCompressed(data, offset, length)) {
			BytesStreamInput siBytes = new BytesStreamInput(data, offset, length, false);
			LZFStreamInput siLzf = CachedStreamInput.cachedLzf(siBytes);
			XContentType contentType = XContentFactory.xContentType(siLzf);
			siLzf.resetToBufferStart();
			return XContentFactory.xContent(contentType).createParser(siLzf);
		} else {
			return XContentFactory.xContent(data, offset, length).createParser(data, offset, length);
		}
	}

	/**
	 * Convert to map.
	 *
	 * @param data the data
	 * @param offset the offset
	 * @param length the length
	 * @param ordered the ordered
	 * @return the tuple
	 * @throws rebirthParseException the rebirth parse exception
	 */
	public static Tuple<XContentType, Map<String, Object>> convertToMap(byte[] data, int offset, int length,
			boolean ordered) throws RebirthParseException {
		try {
			XContentParser parser;
			XContentType contentType;
			if (LZF.isCompressed(data, offset, length)) {
				BytesStreamInput siBytes = new BytesStreamInput(data, offset, length, false);
				LZFStreamInput siLzf = CachedStreamInput.cachedLzf(siBytes);
				contentType = XContentFactory.xContentType(siLzf);
				siLzf.resetToBufferStart();
				parser = XContentFactory.xContent(contentType).createParser(siLzf);
			} else {
				contentType = XContentFactory.xContentType(data, offset, length);
				parser = XContentFactory.xContent(contentType).createParser(data, offset, length);
			}
			if (ordered) {
				return Tuple.create(contentType, parser.mapOrderedAndClose());
			} else {
				return Tuple.create(contentType, parser.mapAndClose());
			}
		} catch (IOException e) {
			throw new RebirthParseException("Failed to parse content to map", e);
		}
	}

	/**
	 * Convert to json.
	 *
	 * @param data the data
	 * @param offset the offset
	 * @param length the length
	 * @param reformatJson the reformat json
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String convertToJson(byte[] data, int offset, int length, boolean reformatJson) throws IOException {
		XContentType xContentType = XContentFactory.xContentType(data, offset, length);
		if (xContentType == XContentType.JSON && reformatJson) {
			return new String(data, offset, length, Charsets.UTF_8);
		}
		XContentParser parser = null;
		try {
			parser = XContentFactory.xContent(xContentType).createParser(data, offset, length);
			parser.nextToken();
			XContentBuilder builder = XContentFactory.jsonBuilder();
			builder.copyCurrentStructure(parser);
			return builder.string();
		} finally {
			if (parser != null) {
				parser.close();
			}
		}
	}

	/**
	 * Merge defaults.
	 *
	 * @param content the content
	 * @param defaults the defaults
	 */
	@SuppressWarnings({ "unchecked" })
	public static void mergeDefaults(Map<String, Object> content, Map<String, Object> defaults) {
		for (Map.Entry<String, Object> defaultEntry : defaults.entrySet()) {
			if (!content.containsKey(defaultEntry.getKey())) {

				content.put(defaultEntry.getKey(), defaultEntry.getValue());
			} else {

				if (content.get(defaultEntry.getKey()) instanceof Map && defaultEntry.getValue() instanceof Map) {
					mergeDefaults((Map<String, Object>) content.get(defaultEntry.getKey()),
							(Map<String, Object>) defaultEntry.getValue());
				} else if (content.get(defaultEntry.getKey()) instanceof List
						&& defaultEntry.getValue() instanceof List) {

					List<Object> mergedList = new ArrayList<Object>();
					mergedList.addAll((Collection<Object>) defaultEntry.getValue());
					mergedList.addAll((Collection<Object>) content.get(defaultEntry.getKey()));
					content.put(defaultEntry.getKey(), mergedList);
				}
			}
		}
	}

	/**
	 * Copy current structure.
	 *
	 * @param generator the generator
	 * @param parser the parser
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void copyCurrentStructure(XContentGenerator generator, XContentParser parser) throws IOException {
		XContentParser.Token t = parser.currentToken();

		if (t == XContentParser.Token.FIELD_NAME) {
			generator.writeFieldName(parser.currentName());
			t = parser.nextToken();

		}

		switch (t) {
		case START_ARRAY:
			generator.writeStartArray();
			while (parser.nextToken() != XContentParser.Token.END_ARRAY) {
				copyCurrentStructure(generator, parser);
			}
			generator.writeEndArray();
			break;
		case START_OBJECT:
			generator.writeStartObject();
			while (parser.nextToken() != XContentParser.Token.END_OBJECT) {
				copyCurrentStructure(generator, parser);
			}
			generator.writeEndObject();
			break;
		default:
			copyCurrentEvent(generator, parser);
		}
	}

	/**
	 * Copy current event.
	 *
	 * @param generator the generator
	 * @param parser the parser
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void copyCurrentEvent(XContentGenerator generator, XContentParser parser) throws IOException {
		switch (parser.currentToken()) {
		case START_OBJECT:
			generator.writeStartObject();
			break;
		case END_OBJECT:
			generator.writeEndObject();
			break;
		case START_ARRAY:
			generator.writeStartArray();
			break;
		case END_ARRAY:
			generator.writeEndArray();
			break;
		case FIELD_NAME:
			generator.writeFieldName(parser.currentName());
			break;
		case VALUE_STRING:
			if (parser.hasTextCharacters()) {
				generator.writeString(parser.textCharacters(), parser.textOffset(), parser.textLength());
			} else {
				generator.writeString(parser.text());
			}
			break;
		case VALUE_NUMBER:
			switch (parser.numberType()) {
			case INT:
				generator.writeNumber(parser.intValue());
				break;
			case LONG:
				generator.writeNumber(parser.longValue());
				break;
			case FLOAT:
				generator.writeNumber(parser.floatValue());
				break;
			case DOUBLE:
				generator.writeNumber(parser.doubleValue());
				break;
			}
			break;
		case VALUE_BOOLEAN:
			generator.writeBoolean(parser.booleanValue());
			break;
		case VALUE_NULL:
			generator.writeNull();
			break;
		case VALUE_EMBEDDED_OBJECT:
			generator.writeBinary(parser.binaryValue());
		}
	}

}
