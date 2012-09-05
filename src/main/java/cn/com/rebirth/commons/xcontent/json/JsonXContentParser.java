/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons JsonXContentParser.java 2012-7-6 10:23:40 l.xue.nong$$
 */
package cn.com.rebirth.commons.xcontent.json;

import java.io.IOException;

import cn.com.rebirth.commons.exception.RebirthIllegalStateException;
import cn.com.rebirth.commons.xcontent.XContentType;
import cn.com.rebirth.commons.xcontent.support.AbstractXContentParser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

/**
 * The Class JsonXContentParser.
 *
 * @author l.xue.nong
 */
public class JsonXContentParser extends AbstractXContentParser {

	/** The parser. */
	final JsonParser parser;

	/**
	 * Instantiates a new json x content parser.
	 *
	 * @param parser the parser
	 */
	public JsonXContentParser(JsonParser parser) {
		this.parser = parser;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentParser#contentType()
	 */
	@Override
	public XContentType contentType() {
		return XContentType.JSON;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentParser#nextToken()
	 */
	@Override
	public Token nextToken() throws IOException {
		return convertToken(parser.nextToken());
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentParser#skipChildren()
	 */
	@Override
	public void skipChildren() throws IOException {
		parser.skipChildren();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentParser#currentToken()
	 */
	@Override
	public Token currentToken() {
		return convertToken(parser.getCurrentToken());
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentParser#numberType()
	 */
	@Override
	public NumberType numberType() throws IOException {
		return convertNumberType(parser.getNumberType());
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentParser#estimatedNumberType()
	 */
	@Override
	public boolean estimatedNumberType() {
		return true;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentParser#currentName()
	 */
	@Override
	public String currentName() throws IOException {
		return parser.getCurrentName();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.support.AbstractXContentParser#doBooleanValue()
	 */
	@Override
	protected boolean doBooleanValue() throws IOException {
		return parser.getBooleanValue();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentParser#text()
	 */
	@Override
	public String text() throws IOException {
		return parser.getText();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentParser#hasTextCharacters()
	 */
	@Override
	public boolean hasTextCharacters() {
		return parser.hasTextCharacters();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentParser#textCharacters()
	 */
	@Override
	public char[] textCharacters() throws IOException {
		return parser.getTextCharacters();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentParser#textLength()
	 */
	@Override
	public int textLength() throws IOException {
		return parser.getTextLength();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentParser#textOffset()
	 */
	@Override
	public int textOffset() throws IOException {
		return parser.getTextOffset();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentParser#numberValue()
	 */
	@Override
	public Number numberValue() throws IOException {
		return parser.getNumberValue();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.support.AbstractXContentParser#doShortValue()
	 */
	@Override
	public short doShortValue() throws IOException {
		return parser.getShortValue();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.support.AbstractXContentParser#doIntValue()
	 */
	@Override
	public int doIntValue() throws IOException {
		return parser.getIntValue();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.support.AbstractXContentParser#doLongValue()
	 */
	@Override
	public long doLongValue() throws IOException {
		return parser.getLongValue();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.support.AbstractXContentParser#doFloatValue()
	 */
	@Override
	public float doFloatValue() throws IOException {
		return parser.getFloatValue();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.support.AbstractXContentParser#doDoubleValue()
	 */
	@Override
	public double doDoubleValue() throws IOException {
		return parser.getDoubleValue();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentParser#binaryValue()
	 */
	@Override
	public byte[] binaryValue() throws IOException {
		return parser.getBinaryValue();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentParser#close()
	 */
	@Override
	public void close() {
		try {
			parser.close();
		} catch (IOException e) {

		}
	}

	/**
	 * Convert number type.
	 *
	 * @param numberType the number type
	 * @return the number type
	 */
	private NumberType convertNumberType(JsonParser.NumberType numberType) {
		switch (numberType) {
		case INT:
			return NumberType.INT;
		case LONG:
			return NumberType.LONG;
		case FLOAT:
			return NumberType.FLOAT;
		case DOUBLE:
			return NumberType.DOUBLE;
		}
		throw new RebirthIllegalStateException("No matching token for number_type [" + numberType + "]");
	}

	/**
	 * Convert token.
	 *
	 * @param token the token
	 * @return the token
	 */
	private Token convertToken(JsonToken token) {
		if (token == null) {
			return null;
		}
		switch (token) {
		case FIELD_NAME:
			return Token.FIELD_NAME;
		case VALUE_FALSE:
		case VALUE_TRUE:
			return Token.VALUE_BOOLEAN;
		case VALUE_STRING:
			return Token.VALUE_STRING;
		case VALUE_NUMBER_INT:
		case VALUE_NUMBER_FLOAT:
			return Token.VALUE_NUMBER;
		case VALUE_NULL:
			return Token.VALUE_NULL;
		case START_OBJECT:
			return Token.START_OBJECT;
		case END_OBJECT:
			return Token.END_OBJECT;
		case START_ARRAY:
			return Token.START_ARRAY;
		case END_ARRAY:
			return Token.END_ARRAY;
		case VALUE_EMBEDDED_OBJECT:
			return Token.VALUE_EMBEDDED_OBJECT;
		}
		throw new RebirthIllegalStateException("No matching token for json_token [" + token + "]");
	}
}
