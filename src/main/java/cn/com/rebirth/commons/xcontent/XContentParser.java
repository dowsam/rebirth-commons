/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons XContentParser.java 2012-7-6 10:23:53 l.xue.nong$$
 */
package cn.com.rebirth.commons.xcontent;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;


/**
 * The Interface XContentParser.
 *
 * @author l.xue.nong
 */
public interface XContentParser extends Closeable {

	
	/**
	 * The Enum Token.
	 *
	 * @author l.xue.nong
	 */
	enum Token {
		
		
		/** The start object. */
		START_OBJECT {
			@Override
			public boolean isValue() {
				return false;
			}
		},

		
		/** The end object. */
		END_OBJECT {
			@Override
			public boolean isValue() {
				return false;
			}
		},

		
		/** The start array. */
		START_ARRAY {
			@Override
			public boolean isValue() {
				return false;
			}
		},

		
		/** The end array. */
		END_ARRAY {
			@Override
			public boolean isValue() {
				return false;
			}
		},

		
		/** The field name. */
		FIELD_NAME {
			@Override
			public boolean isValue() {
				return false;
			}
		},

		
		/** The value string. */
		VALUE_STRING {
			@Override
			public boolean isValue() {
				return true;
			}
		},

		
		/** The value number. */
		VALUE_NUMBER {
			@Override
			public boolean isValue() {
				return true;
			}
		},

		
		/** The value boolean. */
		VALUE_BOOLEAN {
			@Override
			public boolean isValue() {
				return true;
			}
		},

		
		
		/** The value embedded object. */
		VALUE_EMBEDDED_OBJECT {
			@Override
			public boolean isValue() {
				return true;
			}
		},

		
		/** The value null. */
		VALUE_NULL {
			@Override
			public boolean isValue() {
				return false;
			}
		};

		
		/**
		 * Checks if is value.
		 *
		 * @return true, if is value
		 */
		public abstract boolean isValue();
	}

	
	/**
	 * The Enum NumberType.
	 *
	 * @author l.xue.nong
	 */
	enum NumberType {
		
		
		/** The int. */
		INT, 
 
 
 /** The long. */
 LONG, 
 
 
 /** The float. */
 FLOAT, 
 
 
 /** The double. */
 DOUBLE
	}

	
	/**
	 * Content type.
	 *
	 * @return the x content type
	 */
	XContentType contentType();

	
	/**
	 * Next token.
	 *
	 * @return the token
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	Token nextToken() throws IOException;

	
	/**
	 * Skip children.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void skipChildren() throws IOException;

	
	/**
	 * Current token.
	 *
	 * @return the token
	 */
	Token currentToken();

	
	/**
	 * Current name.
	 *
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	String currentName() throws IOException;

	
	/**
	 * Map.
	 *
	 * @return the map
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	Map<String, Object> map() throws IOException;

	
	/**
	 * Map ordered.
	 *
	 * @return the map
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	Map<String, Object> mapOrdered() throws IOException;

	
	/**
	 * Map and close.
	 *
	 * @return the map
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	Map<String, Object> mapAndClose() throws IOException;

	
	/**
	 * Map ordered and close.
	 *
	 * @return the map
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	Map<String, Object> mapOrderedAndClose() throws IOException;

	
	/**
	 * Text.
	 *
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	String text() throws IOException;

	
	/**
	 * Text or null.
	 *
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	String textOrNull() throws IOException;

	
	/**
	 * Checks for text characters.
	 *
	 * @return true, if successful
	 */
	boolean hasTextCharacters();

	
	/**
	 * Text characters.
	 *
	 * @return the char[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	char[] textCharacters() throws IOException;

	
	/**
	 * Text length.
	 *
	 * @return the int
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	int textLength() throws IOException;

	
	/**
	 * Text offset.
	 *
	 * @return the int
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	int textOffset() throws IOException;

	
	/**
	 * Number value.
	 *
	 * @return the number
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	Number numberValue() throws IOException;

	
	/**
	 * Number type.
	 *
	 * @return the number type
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	NumberType numberType() throws IOException;

	
	/**
	 * Estimated number type.
	 *
	 * @return true, if successful
	 */
	boolean estimatedNumberType();

	
	/**
	 * Short value.
	 *
	 * @return the short
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	short shortValue() throws IOException;

	
	/**
	 * Int value.
	 *
	 * @return the int
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	int intValue() throws IOException;

	
	/**
	 * Long value.
	 *
	 * @return the long
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	long longValue() throws IOException;

	
	/**
	 * Float value.
	 *
	 * @return the float
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	float floatValue() throws IOException;

	
	/**
	 * Double value.
	 *
	 * @return the double
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	double doubleValue() throws IOException;

	
	/**
	 * Boolean value.
	 *
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	boolean booleanValue() throws IOException;

	
	/**
	 * Binary value.
	 *
	 * @return the byte[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	byte[] binaryValue() throws IOException;

	
	/* (non-Javadoc)
	 * @see java.io.Closeable#close()
	 */
	void close();
}
