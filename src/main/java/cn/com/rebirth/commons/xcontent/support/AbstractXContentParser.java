/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons AbstractXContentParser.java 2012-7-6 10:23:53 l.xue.nong$$
 */
package cn.com.rebirth.commons.xcontent.support;

import java.io.IOException;
import java.util.Map;

import cn.com.rebirth.commons.Booleans;
import cn.com.rebirth.commons.xcontent.XContentParser;

/**
 * The Class AbstractXContentParser.
 *
 * @author l.xue.nong
 */
public abstract class AbstractXContentParser implements XContentParser {

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentParser#booleanValue()
	 */
	@Override
	public boolean booleanValue() throws IOException {
		Token token = currentToken();
		if (token == Token.VALUE_NUMBER) {
			return intValue() != 0;
		} else if (token == Token.VALUE_STRING) {
			return Booleans.parseBoolean(textCharacters(), textOffset(), textLength(), false);
		}
		return doBooleanValue();
	}

	/**
	 * Do boolean value.
	 *
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected abstract boolean doBooleanValue() throws IOException;

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentParser#shortValue()
	 */
	@Override
	public short shortValue() throws IOException {
		Token token = currentToken();
		if (token == Token.VALUE_STRING) {
			return Short.parseShort(text());
		}
		return doShortValue();
	}

	/**
	 * Do short value.
	 *
	 * @return the short
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected abstract short doShortValue() throws IOException;

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentParser#intValue()
	 */
	@Override
	public int intValue() throws IOException {
		Token token = currentToken();
		if (token == Token.VALUE_STRING) {
			return Integer.parseInt(text());
		}
		return doIntValue();
	}

	/**
	 * Do int value.
	 *
	 * @return the int
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected abstract int doIntValue() throws IOException;

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentParser#longValue()
	 */
	@Override
	public long longValue() throws IOException {
		Token token = currentToken();
		if (token == Token.VALUE_STRING) {
			return Long.parseLong(text());
		}
		return doLongValue();
	}

	/**
	 * Do long value.
	 *
	 * @return the long
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected abstract long doLongValue() throws IOException;

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentParser#floatValue()
	 */
	@Override
	public float floatValue() throws IOException {
		Token token = currentToken();
		if (token == Token.VALUE_STRING) {
			return Float.parseFloat(text());
		}
		return doFloatValue();
	}

	/**
	 * Do float value.
	 *
	 * @return the float
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected abstract float doFloatValue() throws IOException;

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentParser#doubleValue()
	 */
	@Override
	public double doubleValue() throws IOException {
		Token token = currentToken();
		if (token == Token.VALUE_STRING) {
			return Double.parseDouble(text());
		}
		return doDoubleValue();
	}

	/**
	 * Do double value.
	 *
	 * @return the double
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected abstract double doDoubleValue() throws IOException;

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentParser#textOrNull()
	 */
	@Override
	public String textOrNull() throws IOException {
		if (currentToken() == Token.VALUE_NULL) {
			return null;
		}
		return text();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentParser#map()
	 */
	@Override
	public Map<String, Object> map() throws IOException {
		return XContentMapConverter.readMap(this);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentParser#mapOrdered()
	 */
	@Override
	public Map<String, Object> mapOrdered() throws IOException {
		return XContentMapConverter.readOrderedMap(this);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentParser#mapAndClose()
	 */
	@Override
	public Map<String, Object> mapAndClose() throws IOException {
		try {
			return map();
		} finally {
			close();
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.XContentParser#mapOrderedAndClose()
	 */
	@Override
	public Map<String, Object> mapOrderedAndClose() throws IOException {
		try {
			return mapOrdered();
		} finally {
			close();
		}
	}
}
