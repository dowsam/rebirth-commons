/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons FormatDateTimeFormatter.java 2012-7-6 10:22:12 l.xue.nong$$
 */


package cn.com.rebirth.commons.joda;

import org.joda.time.format.DateTimeFormatter;


/**
 * The Class FormatDateTimeFormatter.
 *
 * @author l.xue.nong
 */
public class FormatDateTimeFormatter {

	
	/** The format. */
	private final String format;

	
	/** The parser. */
	private final DateTimeFormatter parser;

	
	/** The printer. */
	private final DateTimeFormatter printer;

	
	/**
	 * Instantiates a new format date time formatter.
	 *
	 * @param format the format
	 * @param parser the parser
	 */
	public FormatDateTimeFormatter(String format, DateTimeFormatter parser) {
		this(format, parser, parser);
	}

	
	/**
	 * Instantiates a new format date time formatter.
	 *
	 * @param format the format
	 * @param parser the parser
	 * @param printer the printer
	 */
	public FormatDateTimeFormatter(String format, DateTimeFormatter parser, DateTimeFormatter printer) {
		this.format = format;
		this.parser = parser;
		this.printer = printer;
	}

	
	/**
	 * Format.
	 *
	 * @return the string
	 */
	public String format() {
		return format;
	}

	
	/**
	 * Parser.
	 *
	 * @return the date time formatter
	 */
	public DateTimeFormatter parser() {
		return parser;
	}

	
	/**
	 * Printer.
	 *
	 * @return the date time formatter
	 */
	public DateTimeFormatter printer() {
		return this.printer;
	}
}
