/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons XContentBuilderString.java 2012-7-6 10:23:45 l.xue.nong$$
 */
package cn.com.rebirth.commons.xcontent;

import cn.com.rebirth.commons.Strings;


/**
 * The Class XContentBuilderString.
 *
 * @author l.xue.nong
 */
public class XContentBuilderString {

    
    /** The underscore. */
    private final XContentString underscore;

    
    /** The camel case. */
    private final XContentString camelCase;

    
    /**
     * Instantiates a new x content builder string.
     *
     * @param value the value
     */
    public XContentBuilderString(String value) {
        underscore = new XContentString(Strings.toUnderscoreCase(value));
        camelCase = new XContentString(Strings.toCamelCase(value));
    }

    
    /**
     * Underscore.
     *
     * @return the x content string
     */
    public XContentString underscore() {
        return underscore;
    }

    
    /**
     * Camel case.
     *
     * @return the x content string
     */
    public XContentString camelCase() {
        return camelCase;
    }
}
