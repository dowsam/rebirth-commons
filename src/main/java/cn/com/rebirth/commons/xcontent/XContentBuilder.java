/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons XContentBuilder.java 2012-7-6 10:23:46 l.xue.nong$$
 */
package cn.com.rebirth.commons.xcontent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;

import org.joda.time.DateTimeZone;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import cn.com.rebirth.commons.Nullable;
import cn.com.rebirth.commons.Strings;
import cn.com.rebirth.commons.Unicode;
import cn.com.rebirth.commons.io.BytesStream;
import cn.com.rebirth.commons.io.FastByteArrayOutputStream;
import cn.com.rebirth.commons.xcontent.support.XContentMapConverter;

/**
 * The Class XContentBuilder.
 *
 * @author l.xue.nong
 */
public final class XContentBuilder {

	/**
	 * The Enum FieldCaseConversion.
	 *
	 * @author l.xue.nong
	 */
	public static enum FieldCaseConversion {

		/** The none. */
		NONE,

		/** The underscore. */
		UNDERSCORE,

		/** The camelcase. */
		CAMELCASE
	}

	/** The Constant defaultDatePrinter. */
	public final static DateTimeFormatter defaultDatePrinter = ISODateTimeFormat.dateTime().withZone(DateTimeZone.UTC);

	/** The global field case conversion. */
	protected static FieldCaseConversion globalFieldCaseConversion = FieldCaseConversion.NONE;

	/**
	 * Global field case conversion.
	 *
	 * @param globalFieldCaseConversion the global field case conversion
	 */
	public static void globalFieldCaseConversion(FieldCaseConversion globalFieldCaseConversion) {
		XContentBuilder.globalFieldCaseConversion = globalFieldCaseConversion;
	}

	/**
	 * Builder.
	 *
	 * @param xContent the x content
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static XContentBuilder builder(XContent xContent) throws IOException {
		return new XContentBuilder(xContent, new FastByteArrayOutputStream());
	}

	/** The generator. */
	private XContentGenerator generator;

	/** The bos. */
	private final OutputStream bos;

	/** The payload. */
	private final Object payload;

	/** The field case conversion. */
	private FieldCaseConversion fieldCaseConversion = globalFieldCaseConversion;

	/** The cached string builder. */
	private StringBuilder cachedStringBuilder;

	/**
	 * Instantiates a new x content builder.
	 *
	 * @param xContent the x content
	 * @param bos the bos
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder(XContent xContent, OutputStream bos) throws IOException {
		this(xContent, bos, null);
	}

	/**
	 * Instantiates a new x content builder.
	 *
	 * @param xContent the x content
	 * @param bos the bos
	 * @param payload the payload
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder(XContent xContent, OutputStream bos, @Nullable Object payload) throws IOException {
		this.bos = bos;
		this.generator = xContent.createGenerator(bos);
		this.payload = payload;
	}

	/**
	 * Field case conversion.
	 *
	 * @param fieldCaseConversion the field case conversion
	 * @return the x content builder
	 */
	public XContentBuilder fieldCaseConversion(FieldCaseConversion fieldCaseConversion) {
		this.fieldCaseConversion = fieldCaseConversion;
		return this;
	}

	/**
	 * Content type.
	 *
	 * @return the x content type
	 */
	public XContentType contentType() {
		return generator.contentType();
	}

	/**
	 * Pretty print.
	 *
	 * @return the x content builder
	 */
	public XContentBuilder prettyPrint() {
		generator.usePrettyPrint();
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param xContent the x content
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(String name, ToXContent xContent) throws IOException {
		field(name);
		xContent.toXContent(this, ToXContent.EMPTY_PARAMS);
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param xContent the x content
	 * @param params the params
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(String name, ToXContent xContent, ToXContent.Params params) throws IOException {
		field(name);
		xContent.toXContent(this, params);
		return this;
	}

	/**
	 * Start object.
	 *
	 * @param name the name
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder startObject(String name) throws IOException {
		field(name);
		startObject();
		return this;
	}

	/**
	 * Start object.
	 *
	 * @param name the name
	 * @param conversion the conversion
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder startObject(String name, FieldCaseConversion conversion) throws IOException {
		field(name, conversion);
		startObject();
		return this;
	}

	/**
	 * Start object.
	 *
	 * @param name the name
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder startObject(XContentBuilderString name) throws IOException {
		field(name);
		startObject();
		return this;
	}

	/**
	 * Start object.
	 *
	 * @param name the name
	 * @param conversion the conversion
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder startObject(XContentBuilderString name, FieldCaseConversion conversion) throws IOException {
		field(name, conversion);
		startObject();
		return this;
	}

	/**
	 * Start object.
	 *
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder startObject() throws IOException {
		generator.writeStartObject();
		return this;
	}

	/**
	 * End object.
	 *
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder endObject() throws IOException {
		generator.writeEndObject();
		return this;
	}

	/**
	 * Array.
	 *
	 * @param name the name
	 * @param values the values
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder array(String name, String... values) throws IOException {
		startArray(name);
		for (String value : values) {
			value(value);
		}
		endArray();
		return this;
	}

	/**
	 * Array.
	 *
	 * @param name the name
	 * @param values the values
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder array(XContentBuilderString name, String... values) throws IOException {
		startArray(name);
		for (String value : values) {
			value(value);
		}
		endArray();
		return this;
	}

	/**
	 * Array.
	 *
	 * @param name the name
	 * @param values the values
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder array(String name, Object... values) throws IOException {
		startArray(name);
		for (Object value : values) {
			value(value);
		}
		endArray();
		return this;
	}

	/**
	 * Array.
	 *
	 * @param name the name
	 * @param values the values
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder array(XContentBuilderString name, Object... values) throws IOException {
		startArray(name);
		for (Object value : values) {
			value(value);
		}
		endArray();
		return this;
	}

	/**
	 * Start array.
	 *
	 * @param name the name
	 * @param conversion the conversion
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder startArray(String name, FieldCaseConversion conversion) throws IOException {
		field(name, conversion);
		startArray();
		return this;
	}

	/**
	 * Start array.
	 *
	 * @param name the name
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder startArray(String name) throws IOException {
		field(name);
		startArray();
		return this;
	}

	/**
	 * Start array.
	 *
	 * @param name the name
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder startArray(XContentBuilderString name) throws IOException {
		field(name);
		startArray();
		return this;
	}

	/**
	 * Start array.
	 *
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder startArray() throws IOException {
		generator.writeStartArray();
		return this;
	}

	/**
	 * End array.
	 *
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder endArray() throws IOException {
		generator.writeEndArray();
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(XContentBuilderString name) throws IOException {
		if (fieldCaseConversion == FieldCaseConversion.UNDERSCORE) {
			generator.writeFieldName(name.underscore());
		} else if (fieldCaseConversion == FieldCaseConversion.CAMELCASE) {
			generator.writeFieldName(name.camelCase());
		} else {
			generator.writeFieldName(name.underscore());
		}
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param conversion the conversion
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(XContentBuilderString name, FieldCaseConversion conversion) throws IOException {
		if (conversion == FieldCaseConversion.UNDERSCORE) {
			generator.writeFieldName(name.underscore());
		} else if (conversion == FieldCaseConversion.CAMELCASE) {
			generator.writeFieldName(name.camelCase());
		} else {
			generator.writeFieldName(name.underscore());
		}
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(String name) throws IOException {
		if (fieldCaseConversion == FieldCaseConversion.UNDERSCORE) {
			if (cachedStringBuilder == null) {
				cachedStringBuilder = new StringBuilder();
			}
			name = Strings.toUnderscoreCase(name, cachedStringBuilder);
		} else if (fieldCaseConversion == FieldCaseConversion.CAMELCASE) {
			if (cachedStringBuilder == null) {
				cachedStringBuilder = new StringBuilder();
			}
			name = Strings.toCamelCase(name, cachedStringBuilder);
		}
		generator.writeFieldName(name);
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param conversion the conversion
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(String name, FieldCaseConversion conversion) throws IOException {
		if (conversion == FieldCaseConversion.UNDERSCORE) {
			if (cachedStringBuilder == null) {
				cachedStringBuilder = new StringBuilder();
			}
			name = Strings.toUnderscoreCase(name, cachedStringBuilder);
		} else if (conversion == FieldCaseConversion.CAMELCASE) {
			if (cachedStringBuilder == null) {
				cachedStringBuilder = new StringBuilder();
			}
			name = Strings.toCamelCase(name, cachedStringBuilder);
		}
		generator.writeFieldName(name);
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @param offset the offset
	 * @param length the length
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(String name, char[] value, int offset, int length) throws IOException {
		field(name);
		if (value == null) {
			generator.writeNull();
		} else {
			generator.writeString(value, offset, length);
		}
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @param offset the offset
	 * @param length the length
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(XContentBuilderString name, char[] value, int offset, int length) throws IOException {
		field(name);
		if (value == null) {
			generator.writeNull();
		} else {
			generator.writeString(value, offset, length);
		}
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(String name, String value) throws IOException {
		field(name);
		if (value == null) {
			generator.writeNull();
		} else {
			generator.writeString(value);
		}
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @param conversion the conversion
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(String name, String value, FieldCaseConversion conversion) throws IOException {
		field(name, conversion);
		if (value == null) {
			generator.writeNull();
		} else {
			generator.writeString(value);
		}
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(XContentBuilderString name, String value) throws IOException {
		field(name);
		if (value == null) {
			generator.writeNull();
		} else {
			generator.writeString(value);
		}
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @param conversion the conversion
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(XContentBuilderString name, String value, FieldCaseConversion conversion)
			throws IOException {
		field(name, conversion);
		if (value == null) {
			generator.writeNull();
		} else {
			generator.writeString(value);
		}
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(String name, Integer value) throws IOException {
		field(name);
		if (value == null) {
			generator.writeNull();
		} else {
			generator.writeNumber(value.intValue());
		}
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(XContentBuilderString name, Integer value) throws IOException {
		field(name);
		if (value == null) {
			generator.writeNull();
		} else {
			generator.writeNumber(value.intValue());
		}
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(String name, int value) throws IOException {
		field(name);
		generator.writeNumber(value);
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(XContentBuilderString name, int value) throws IOException {
		field(name);
		generator.writeNumber(value);
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(String name, Long value) throws IOException {
		field(name);
		if (value == null) {
			generator.writeNull();
		} else {
			generator.writeNumber(value.longValue());
		}
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(XContentBuilderString name, Long value) throws IOException {
		field(name);
		if (value == null) {
			generator.writeNull();
		} else {
			generator.writeNumber(value.longValue());
		}
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(String name, long value) throws IOException {
		field(name);
		generator.writeNumber(value);
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(XContentBuilderString name, long value) throws IOException {
		field(name);
		generator.writeNumber(value);
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(String name, Float value) throws IOException {
		field(name);
		if (value == null) {
			generator.writeNull();
		} else {
			generator.writeNumber(value.floatValue());
		}
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(XContentBuilderString name, Float value) throws IOException {
		field(name);
		if (value == null) {
			generator.writeNull();
		} else {
			generator.writeNumber(value.floatValue());
		}
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(String name, float value) throws IOException {
		field(name);
		generator.writeNumber(value);
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(XContentBuilderString name, float value) throws IOException {
		field(name);
		generator.writeNumber(value);
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(String name, Double value) throws IOException {
		field(name);
		if (value == null) {
			generator.writeNull();
		} else {
			generator.writeNumber(value.doubleValue());
		}
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(XContentBuilderString name, Double value) throws IOException {
		field(name);
		if (value == null) {
			generator.writeNull();
		} else {
			generator.writeNumber(value.doubleValue());
		}
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(String name, double value) throws IOException {
		field(name);
		generator.writeNumber(value);
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(XContentBuilderString name, double value) throws IOException {
		field(name);
		generator.writeNumber(value);
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @param offset the offset
	 * @param length the length
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(String name, byte[] value, int offset, int length) throws IOException {
		field(name);
		generator.writeBinary(value, offset, length);
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(String name, Map<String, Object> value) throws IOException {
		field(name);
		value(value);
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(XContentBuilderString name, Map<String, Object> value) throws IOException {
		field(name);
		value(value);
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(String name, Iterable<Object> value) throws IOException {
		startArray(name);
		for (Object o : value) {
			value(o);
		}
		endArray();
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(XContentBuilderString name, Iterable<Object> value) throws IOException {
		startArray(name);
		for (Object o : value) {
			value(o);
		}
		endArray();
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(String name, String... value) throws IOException {
		startArray(name);
		for (String o : value) {
			value(o);
		}
		endArray();
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(XContentBuilderString name, String... value) throws IOException {
		startArray(name);
		for (String o : value) {
			value(o);
		}
		endArray();
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(String name, Object... value) throws IOException {
		startArray(name);
		for (Object o : value) {
			value(o);
		}
		endArray();
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(XContentBuilderString name, Object... value) throws IOException {
		startArray(name);
		for (Object o : value) {
			value(o);
		}
		endArray();
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(String name, int... value) throws IOException {
		startArray(name);
		for (Object o : value) {
			value(o);
		}
		endArray();
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(XContentBuilderString name, int... value) throws IOException {
		startArray(name);
		for (Object o : value) {
			value(o);
		}
		endArray();
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(String name, long... value) throws IOException {
		startArray(name);
		for (Object o : value) {
			value(o);
		}
		endArray();
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(XContentBuilderString name, long... value) throws IOException {
		startArray(name);
		for (Object o : value) {
			value(o);
		}
		endArray();
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(String name, float... value) throws IOException {
		startArray(name);
		for (Object o : value) {
			value(o);
		}
		endArray();
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(XContentBuilderString name, float... value) throws IOException {
		startArray(name);
		for (Object o : value) {
			value(o);
		}
		endArray();
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(String name, double... value) throws IOException {
		startArray(name);
		for (Object o : value) {
			value(o);
		}
		endArray();
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(XContentBuilderString name, double... value) throws IOException {
		startArray(name);
		for (Object o : value) {
			value(o);
		}
		endArray();
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public XContentBuilder field(String name, Object value) throws IOException {
		if (value == null) {
			nullField(name);
			return this;
		}
		Class<?> type = value.getClass();
		if (type == String.class) {
			field(name, (String) value);
		} else if (type == Float.class) {
			field(name, ((Float) value).floatValue());
		} else if (type == Double.class) {
			field(name, ((Double) value).doubleValue());
		} else if (type == Integer.class) {
			field(name, ((Integer) value).intValue());
		} else if (type == Long.class) {
			field(name, ((Long) value).longValue());
		} else if (type == Short.class) {
			field(name, ((Short) value).shortValue());
		} else if (type == Byte.class) {
			field(name, ((Byte) value).byteValue());
		} else if (type == Boolean.class) {
			field(name, ((Boolean) value).booleanValue());
		} else if (value instanceof Date) {
			field(name, (Date) value);
		} else if (type == byte[].class) {
			field(name, (byte[]) value);
		} else if (value instanceof ReadableInstant) {
			field(name, (ReadableInstant) value);
		} else if (value instanceof Map) {
			field(name, (Map<String, Object>) value);
		} else if (value instanceof Iterable) {
			field(name, (Iterable) value);
		} else if (value instanceof Object[]) {
			field(name, (Object[]) value);
		} else if (value instanceof int[]) {
			field(name, (int[]) value);
		} else if (value instanceof long[]) {
			field(name, (long[]) value);
		} else if (value instanceof float[]) {
			field(name, (float[]) value);
		} else if (value instanceof double[]) {
			field(name, (double[]) value);
		} else if (value instanceof ToXContent) {
			field(name, (ToXContent) value);
		} else {
			field(name, value.toString());
		}
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public XContentBuilder field(XContentBuilderString name, Object value) throws IOException {
		if (value == null) {
			nullField(name);
			return this;
		}
		Class<?> type = value.getClass();
		if (type == String.class) {
			field(name, (String) value);
		} else if (type == Float.class) {
			field(name, ((Float) value).floatValue());
		} else if (type == Double.class) {
			field(name, ((Double) value).doubleValue());
		} else if (type == Integer.class) {
			field(name, ((Integer) value).intValue());
		} else if (type == Long.class) {
			field(name, ((Long) value).longValue());
		} else if (type == Short.class) {
			field(name, ((Short) value).shortValue());
		} else if (type == Byte.class) {
			field(name, ((Byte) value).byteValue());
		} else if (type == Boolean.class) {
			field(name, ((Boolean) value).booleanValue());
		} else if (value instanceof Date) {
			field(name, (Date) value);
		} else if (type == byte[].class) {
			field(name, (byte[]) value);
		} else if (value instanceof ReadableInstant) {
			field(name, (ReadableInstant) value);
		} else if (value instanceof Map) {

			field(name, (Map<String, Object>) value);
		} else if (value instanceof Iterable) {
			field(name, (Iterable) value);
		} else if (value instanceof Object[]) {
			field(name, (Object[]) value);
		} else if (value instanceof int[]) {
			field(name, (int[]) value);
		} else if (value instanceof long[]) {
			field(name, (long[]) value);
		} else if (value instanceof float[]) {
			field(name, (float[]) value);
		} else if (value instanceof double[]) {
			field(name, (double[]) value);
		} else {
			field(name, value.toString());
		}
		return this;
	}

	/**
	 * Value.
	 *
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public XContentBuilder value(Object value) throws IOException {
		if (value == null) {
			return nullValue();
		}
		Class<?> type = value.getClass();
		if (type == String.class) {
			value((String) value);
		} else if (type == Float.class) {
			value(((Float) value).floatValue());
		} else if (type == Double.class) {
			value(((Double) value).doubleValue());
		} else if (type == Integer.class) {
			value(((Integer) value).intValue());
		} else if (type == Long.class) {
			value(((Long) value).longValue());
		} else if (type == Short.class) {
			value(((Short) value).shortValue());
		} else if (type == Byte.class) {
			value(((Byte) value).byteValue());
		} else if (type == Boolean.class) {
			value((Boolean) value);
		} else if (type == byte[].class) {
			value((byte[]) value);
		} else if (value instanceof Date) {
			value((Date) value);
		} else if (value instanceof ReadableInstant) {
			value((ReadableInstant) value);
		} else if (value instanceof Map) {

			value((Map<String, Object>) value);
		} else if (value instanceof Iterable) {
			value((Iterable) value);
		} else {
			throw new IOException("Type not allowed [" + type + "]");
		}
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(String name, boolean value) throws IOException {
		field(name);
		generator.writeBoolean(value);
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(XContentBuilderString name, boolean value) throws IOException {
		field(name);
		generator.writeBoolean(value);
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(String name, byte[] value) throws IOException {
		field(name);
		if (value == null) {
			generator.writeNull();
		} else {
			generator.writeBinary(value);
		}
		return this;
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(XContentBuilderString name, byte[] value) throws IOException {
		field(name);
		return value(value);
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param date the date
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(String name, ReadableInstant date) throws IOException {
		field(name);
		return value(date);
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param date the date
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(XContentBuilderString name, ReadableInstant date) throws IOException {
		field(name);
		return value(date);
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param date the date
	 * @param formatter the formatter
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(String name, ReadableInstant date, DateTimeFormatter formatter) throws IOException {
		field(name);
		return value(date, formatter);
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param date the date
	 * @param formatter the formatter
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(XContentBuilderString name, ReadableInstant date, DateTimeFormatter formatter)
			throws IOException {
		field(name);
		return value(date, formatter);
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param date the date
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(String name, Date date) throws IOException {
		field(name);
		return value(date);
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param date the date
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(XContentBuilderString name, Date date) throws IOException {
		field(name);
		return value(date);
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param date the date
	 * @param formatter the formatter
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(String name, Date date, DateTimeFormatter formatter) throws IOException {
		field(name);
		return value(date, formatter);
	}

	/**
	 * Field.
	 *
	 * @param name the name
	 * @param date the date
	 * @param formatter the formatter
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder field(XContentBuilderString name, Date date, DateTimeFormatter formatter) throws IOException {
		field(name);
		return value(date, formatter);
	}

	/**
	 * Null field.
	 *
	 * @param name the name
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder nullField(String name) throws IOException {
		generator.writeNullField(name);
		return this;
	}

	/**
	 * Null field.
	 *
	 * @param name the name
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder nullField(XContentBuilderString name) throws IOException {
		field(name);
		generator.writeNull();
		return this;
	}

	/**
	 * Null value.
	 *
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder nullValue() throws IOException {
		generator.writeNull();
		return this;
	}

	/**
	 * Raw field.
	 *
	 * @param fieldName the field name
	 * @param content the content
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder rawField(String fieldName, byte[] content) throws IOException {
		generator.writeRawField(fieldName, content, bos);
		return this;
	}

	/**
	 * Raw field.
	 *
	 * @param fieldName the field name
	 * @param content the content
	 * @param offset the offset
	 * @param length the length
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder rawField(String fieldName, byte[] content, int offset, int length) throws IOException {
		generator.writeRawField(fieldName, content, offset, length, bos);
		return this;
	}

	/**
	 * Raw field.
	 *
	 * @param fieldName the field name
	 * @param content the content
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder rawField(String fieldName, InputStream content) throws IOException {
		generator.writeRawField(fieldName, content, bos);
		return this;
	}

	/**
	 * Value.
	 *
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder value(Boolean value) throws IOException {
		return value(value.booleanValue());
	}

	/**
	 * Value.
	 *
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder value(boolean value) throws IOException {
		generator.writeBoolean(value);
		return this;
	}

	/**
	 * Value.
	 *
	 * @param date the date
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder value(ReadableInstant date) throws IOException {
		return value(date, defaultDatePrinter);
	}

	/**
	 * Value.
	 *
	 * @param date the date
	 * @param dateTimeFormatter the date time formatter
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder value(ReadableInstant date, DateTimeFormatter dateTimeFormatter) throws IOException {
		if (date == null) {
			return nullValue();
		}
		return value(dateTimeFormatter.print(date));
	}

	/**
	 * Value.
	 *
	 * @param date the date
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder value(Date date) throws IOException {
		return value(date, defaultDatePrinter);
	}

	/**
	 * Value.
	 *
	 * @param date the date
	 * @param dateTimeFormatter the date time formatter
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder value(Date date, DateTimeFormatter dateTimeFormatter) throws IOException {
		if (date == null) {
			return nullValue();
		}
		return value(dateTimeFormatter.print(date.getTime()));
	}

	/**
	 * Value.
	 *
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder value(Integer value) throws IOException {
		if (value == null) {
			return nullValue();
		}
		return value(value.intValue());
	}

	/**
	 * Value.
	 *
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder value(int value) throws IOException {
		generator.writeNumber(value);
		return this;
	}

	/**
	 * Value.
	 *
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder value(Long value) throws IOException {
		if (value == null) {
			return nullValue();
		}
		return value(value.longValue());
	}

	/**
	 * Value.
	 *
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder value(long value) throws IOException {
		generator.writeNumber(value);
		return this;
	}

	/**
	 * Value.
	 *
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder value(Float value) throws IOException {
		if (value == null) {
			return nullValue();
		}
		return value(value.floatValue());
	}

	/**
	 * Value.
	 *
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder value(float value) throws IOException {
		generator.writeNumber(value);
		return this;
	}

	/**
	 * Value.
	 *
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder value(Double value) throws IOException {
		if (value == null) {
			return nullValue();
		}
		return value(value.doubleValue());
	}

	/**
	 * Value.
	 *
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder value(double value) throws IOException {
		generator.writeNumber(value);
		return this;
	}

	/**
	 * Value.
	 *
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder value(String value) throws IOException {
		if (value == null) {
			return nullValue();
		}
		generator.writeString(value);
		return this;
	}

	/**
	 * Value.
	 *
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder value(byte[] value) throws IOException {
		if (value == null) {
			return nullValue();
		}
		generator.writeBinary(value);
		return this;
	}

	/**
	 * Value.
	 *
	 * @param value the value
	 * @param offset the offset
	 * @param length the length
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder value(byte[] value, int offset, int length) throws IOException {
		if (value == null) {
			return nullValue();
		}
		generator.writeBinary(value, offset, length);
		return this;
	}

	/**
	 * Map.
	 *
	 * @param map the map
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder map(Map<String, Object> map) throws IOException {
		if (map == null) {
			return nullValue();
		}
		XContentMapConverter.writeMap(generator, map);
		return this;
	}

	/**
	 * Value.
	 *
	 * @param map the map
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder value(Map<String, Object> map) throws IOException {
		if (map == null) {
			return nullValue();
		}
		XContentMapConverter.writeMap(generator, map);
		return this;
	}

	/**
	 * Value.
	 *
	 * @param value the value
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder value(Iterable<Object> value) throws IOException {
		if (value == null) {
			return nullValue();
		}
		startArray();
		for (Object o : value) {
			value(o);
		}
		endArray();
		return this;
	}

	/**
	 * Copy current structure.
	 *
	 * @param parser the parser
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder copyCurrentStructure(XContentParser parser) throws IOException {
		generator.copyCurrentStructure(parser);
		return this;
	}

	/**
	 * Flush.
	 *
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public XContentBuilder flush() throws IOException {
		generator.flush();
		return this;
	}

	/**
	 * Close.
	 */
	public void close() {
		try {
			generator.close();
		} catch (IOException e) {

		}
	}

	/**
	 * Payload.
	 *
	 * @return the object
	 */
	@Nullable
	public Object payload() {
		return this.payload;
	}

	/**
	 * Stream.
	 *
	 * @return the output stream
	 */
	public OutputStream stream() {
		return this.bos;
	}

	/**
	 * Underlying bytes.
	 *
	 * @return the byte[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public byte[] underlyingBytes() throws IOException {
		close();
		return ((BytesStream) bos).underlyingBytes();
	}

	/**
	 * Underlying bytes length.
	 *
	 * @return the int
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public int underlyingBytesLength() throws IOException {
		close();
		return ((BytesStream) bos).size();
	}

	/**
	 * Underlying stream.
	 *
	 * @return the bytes stream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public BytesStream underlyingStream() throws IOException {
		close();
		return (BytesStream) bos;
	}

	/**
	 * Copied bytes.
	 *
	 * @return the byte[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public byte[] copiedBytes() throws IOException {
		close();
		return ((BytesStream) bos).copiedByteArray();
	}

	/**
	 * String.
	 *
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String string() throws IOException {
		close();
		return Unicode.fromBytes(underlyingBytes(), 0, underlyingBytesLength());
	}
}
