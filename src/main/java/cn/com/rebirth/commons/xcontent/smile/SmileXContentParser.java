/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons SmileXContentParser.java 2012-7-6 10:23:47 l.xue.nong$$
 */
package cn.com.rebirth.commons.xcontent.smile;

import cn.com.rebirth.commons.xcontent.XContentType;
import cn.com.rebirth.commons.xcontent.json.JsonXContentParser;

import com.fasterxml.jackson.core.JsonParser;

/**
 * The Class SmileXContentParser.
 *
 * @author l.xue.nong
 */
public class SmileXContentParser extends JsonXContentParser {

	/**
	 * Instantiates a new smile x content parser.
	 *
	 * @param parser the parser
	 */
	public SmileXContentParser(JsonParser parser) {
		super(parser);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.xcontent.json.JsonXContentParser#contentType()
	 */
	@Override
	public XContentType contentType() {
		return XContentType.SMILE;
	}
}
