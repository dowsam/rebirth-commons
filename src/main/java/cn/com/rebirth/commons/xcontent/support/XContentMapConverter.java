/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons XContentMapConverter.java 2012-7-6 10:23:40 l.xue.nong$$
 */
package cn.com.rebirth.commons.xcontent.support;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.com.rebirth.commons.xcontent.XContentBuilder;
import cn.com.rebirth.commons.xcontent.XContentGenerator;
import cn.com.rebirth.commons.xcontent.XContentParser;

/**
 * The Class XContentMapConverter.
 *
 * @author l.xue.nong
 */
public class XContentMapConverter {

	/**
	 * A factory for creating Map objects.
	 */
	public static interface MapFactory {

		/**
		 * New map.
		 *
		 * @return the map
		 */
		Map<String, Object> newMap();
	}

	/** The Constant SIMPLE_MAP_FACTORY. */
	public static final MapFactory SIMPLE_MAP_FACTORY = new MapFactory() {
		@Override
		public Map<String, Object> newMap() {
			return new HashMap<String, Object>();
		}
	};

	/** The Constant ORDERED_MAP_FACTORY. */
	public static final MapFactory ORDERED_MAP_FACTORY = new MapFactory() {
		@Override
		public Map<String, Object> newMap() {
			return new LinkedHashMap<String, Object>();
		}
	};

	/**
	 * Read map.
	 *
	 * @param parser the parser
	 * @return the map
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Map<String, Object> readMap(XContentParser parser) throws IOException {
		return readMap(parser, SIMPLE_MAP_FACTORY);
	}

	/**
	 * Read ordered map.
	 *
	 * @param parser the parser
	 * @return the map
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Map<String, Object> readOrderedMap(XContentParser parser) throws IOException {
		return readMap(parser, ORDERED_MAP_FACTORY);
	}

	/**
	 * Read map.
	 *
	 * @param parser the parser
	 * @param mapFactory the map factory
	 * @return the map
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Map<String, Object> readMap(XContentParser parser, MapFactory mapFactory) throws IOException {
		Map<String, Object> map = mapFactory.newMap();
		XContentParser.Token t = parser.currentToken();
		if (t == null) {
			t = parser.nextToken();
		}
		if (t == XContentParser.Token.START_OBJECT) {
			t = parser.nextToken();
		}
		for (; t == XContentParser.Token.FIELD_NAME; t = parser.nextToken()) {

			String fieldName = parser.currentName();

			t = parser.nextToken();
			Object value = readValue(parser, mapFactory, t);
			map.put(fieldName, value);
		}
		return map;
	}

	/**
	 * Read list.
	 *
	 * @param parser the parser
	 * @param mapFactory the map factory
	 * @param t the t
	 * @return the list
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static List<Object> readList(XContentParser parser, MapFactory mapFactory, XContentParser.Token t)
			throws IOException {
		ArrayList<Object> list = new ArrayList<Object>();
		while ((t = parser.nextToken()) != XContentParser.Token.END_ARRAY) {
			list.add(readValue(parser, mapFactory, t));
		}
		return list;
	}

	/**
	 * Read value.
	 *
	 * @param parser the parser
	 * @param mapFactory the map factory
	 * @param t the t
	 * @return the object
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static Object readValue(XContentParser parser, MapFactory mapFactory, XContentParser.Token t)
			throws IOException {
		if (t == XContentParser.Token.VALUE_NULL) {
			return null;
		} else if (t == XContentParser.Token.VALUE_STRING) {
			return parser.text();
		} else if (t == XContentParser.Token.VALUE_NUMBER) {
			XContentParser.NumberType numberType = parser.numberType();
			if (numberType == XContentParser.NumberType.INT) {
				return parser.intValue();
			} else if (numberType == XContentParser.NumberType.LONG) {
				return parser.longValue();
			} else if (numberType == XContentParser.NumberType.FLOAT) {
				return parser.floatValue();
			} else if (numberType == XContentParser.NumberType.DOUBLE) {
				return parser.doubleValue();
			}
		} else if (t == XContentParser.Token.VALUE_BOOLEAN) {
			return parser.booleanValue();
		} else if (t == XContentParser.Token.START_OBJECT) {
			return readMap(parser, mapFactory);
		} else if (t == XContentParser.Token.START_ARRAY) {
			return readList(parser, mapFactory, t);
		} else if (t == XContentParser.Token.VALUE_EMBEDDED_OBJECT) {
			return parser.binaryValue();
		}
		return null;
	}

	/**
	 * Write map.
	 *
	 * @param gen the gen
	 * @param map the map
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeMap(XContentGenerator gen, Map<String, Object> map) throws IOException {
		gen.writeStartObject();

		for (Map.Entry<String, Object> entry : map.entrySet()) {
			gen.writeFieldName(entry.getKey());
			Object value = entry.getValue();
			if (value == null) {
				gen.writeNull();
			} else {
				writeValue(gen, value);
			}
		}

		gen.writeEndObject();
	}

	/**
	 * Write iterable.
	 *
	 * @param gen the gen
	 * @param iterable the iterable
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void writeIterable(XContentGenerator gen, Iterable<?> iterable) throws IOException {
		gen.writeStartArray();
		for (Object value : iterable) {
			writeValue(gen, value);
		}
		gen.writeEndArray();
	}

	/**
	 * Write object array.
	 *
	 * @param gen the gen
	 * @param array the array
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void writeObjectArray(XContentGenerator gen, Object[] array) throws IOException {
		gen.writeStartArray();
		for (Object value : array) {
			writeValue(gen, value);
		}
		gen.writeEndArray();
	}

	/**
	 * Write value.
	 *
	 * @param gen the gen
	 * @param value the value
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void writeValue(XContentGenerator gen, Object value) throws IOException {
		if (value == null) {
			gen.writeNull();
			return;
		}
		Class<?> type = value.getClass();
		if (type == String.class) {
			gen.writeString((String) value);
		} else if (type == Integer.class) {
			gen.writeNumber(((Integer) value).intValue());
		} else if (type == Long.class) {
			gen.writeNumber(((Long) value).longValue());
		} else if (type == Float.class) {
			gen.writeNumber(((Float) value).floatValue());
		} else if (type == Double.class) {
			gen.writeNumber(((Double) value).doubleValue());
		} else if (type == Short.class) {
			gen.writeNumber(((Short) value).shortValue());
		} else if (type == Boolean.class) {
			gen.writeBoolean(((Boolean) value).booleanValue());
		} else if (value instanceof Map) {
			writeMap(gen, (Map) value);
		} else if (value instanceof Iterable) {
			writeIterable(gen, (Iterable) value);
		} else if (value instanceof Object[]) {
			writeObjectArray(gen, (Object[]) value);
		} else if (type == byte[].class) {
			gen.writeBinary((byte[]) value);
		} else if (value instanceof Date) {
			gen.writeString(XContentBuilder.defaultDatePrinter.print(((Date) value).getTime()));
		} else {
			gen.writeString(value.toString());
		}
	}
}
