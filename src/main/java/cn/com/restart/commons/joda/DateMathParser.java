/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons DateMathParser.java 2012-3-29 15:15:08 l.xue.nong$$
 */

package cn.com.restart.commons.joda;

import java.util.concurrent.TimeUnit;

import org.joda.time.DateTimeZone;
import org.joda.time.MutableDateTime;

import cn.com.restart.commons.exception.RestartParseException;



/**
 * The Class DateMathParser.
 *
 * @author l.xue.nong
 */
public class DateMathParser {

	
	/** The date time formatter. */
	private final FormatDateTimeFormatter dateTimeFormatter;

	
	/** The time unit. */
	private final TimeUnit timeUnit;

	
	/**
	 * Instantiates a new date math parser.
	 *
	 * @param dateTimeFormatter the date time formatter
	 * @param timeUnit the time unit
	 */
	public DateMathParser(FormatDateTimeFormatter dateTimeFormatter, TimeUnit timeUnit) {
		this.dateTimeFormatter = dateTimeFormatter;
		this.timeUnit = timeUnit;
	}

	
	/**
	 * Parses the.
	 *
	 * @param text the text
	 * @param now the now
	 * @return the long
	 */
	public long parse(String text, long now) {
		return parse(text, now, false, false);
	}

	
	/**
	 * Parses the upper inclusive.
	 *
	 * @param text the text
	 * @param now the now
	 * @return the long
	 */
	public long parseUpperInclusive(String text, long now) {
		return parse(text, now, true, true);
	}

	
	/**
	 * Parses the.
	 *
	 * @param text the text
	 * @param now the now
	 * @param roundUp the round up
	 * @param upperInclusive the upper inclusive
	 * @return the long
	 */
	public long parse(String text, long now, boolean roundUp, boolean upperInclusive) {
		long time;
		String mathString;
		if (text.startsWith("now")) {
			time = now;
			mathString = text.substring("now".length());
		} else {
			int index = text.indexOf("||");
			String parseString;
			if (index == -1) {
				parseString = text;
				mathString = ""; 
			} else {
				parseString = text.substring(0, index);
				mathString = text.substring(index + 2);
			}
			if (upperInclusive) {
				time = parseUpperInclusiveStringValue(parseString);
			} else {
				time = parseStringValue(parseString);
			}
		}

		if (mathString.isEmpty()) {
			return time;
		}

		return parseMath(mathString, time, roundUp);
	}

	
	/**
	 * Parses the math.
	 *
	 * @param mathString the math string
	 * @param time the time
	 * @param roundUp the round up
	 * @return the long
	 * @throws SumMallSearchParseException the sum mall search parse exception
	 */
	private long parseMath(String mathString, long time, boolean roundUp) throws RestartParseException {
		MutableDateTime dateTime = new MutableDateTime(time, DateTimeZone.UTC);
		try {
			for (int i = 0; i < mathString.length();) {
				char c = mathString.charAt(i++);
				int type;
				if (c == '/') {
					type = 0;
				} else if (c == '+') {
					type = 1;
				} else if (c == '-') {
					type = 2;
				} else {
					throw new RestartParseException("operator not supported for date math [" + mathString + "]");
				}

				int num;
				if (!Character.isDigit(mathString.charAt(i))) {
					num = 1;
				} else {
					int numFrom = i;
					while (Character.isDigit(mathString.charAt(i))) {
						i++;
					}
					num = Integer.parseInt(mathString.substring(numFrom, i));
				}
				if (type == 0) {
					
					if (num != 1) {
						throw new RestartParseException("rounding `/` can only be used on single unit types ["
								+ mathString + "]");
					}
				}
				char unit = mathString.charAt(i++);
				switch (unit) {
				case 'M':
					if (type == 0) {
						if (roundUp) {
							dateTime.monthOfYear().roundCeiling();
						} else {
							dateTime.monthOfYear().roundFloor();
						}
					} else if (type == 1) {
						dateTime.addMonths(num);
					} else if (type == 2) {
						dateTime.addMonths(-num);
					}
					break;
				case 'w':
					if (type == 0) {
						if (roundUp) {
							dateTime.weekOfWeekyear().roundCeiling();
						} else {
							dateTime.weekOfWeekyear().roundFloor();
						}
					} else if (type == 1) {
						dateTime.addWeeks(num);
					} else if (type == 2) {
						dateTime.addWeeks(-num);
					}
					break;
				case 'd':
					if (type == 0) {
						if (roundUp) {
							dateTime.dayOfMonth().roundCeiling();
						} else {
							dateTime.dayOfMonth().roundFloor();
						}
					} else if (type == 1) {
						dateTime.addDays(num);
					} else if (type == 2) {
						dateTime.addDays(-num);
					}
					break;
				case 'h':
				case 'H':
					if (type == 0) {
						if (roundUp) {
							dateTime.hourOfDay().roundCeiling();
						} else {
							dateTime.hourOfDay().roundFloor();
						}
					} else if (type == 1) {
						dateTime.addHours(num);
					} else if (type == 2) {
						dateTime.addHours(-num);
					}
					break;
				case 'm':
					if (type == 0) {
						if (roundUp) {
							dateTime.minuteOfHour().roundCeiling();
						} else {
							dateTime.minuteOfHour().roundFloor();
						}
					} else if (type == 1) {
						dateTime.addMinutes(num);
					} else if (type == 2) {
						dateTime.addMinutes(-num);
					}
					break;
				case 's':
					if (type == 0) {
						if (roundUp) {
							dateTime.secondOfMinute().roundCeiling();
						} else {
							dateTime.secondOfMinute().roundFloor();
						}
					} else if (type == 1) {
						dateTime.addSeconds(num);
					} else if (type == 2) {
						dateTime.addSeconds(-num);
					}
					break;
				default:
					throw new RestartParseException("unit [" + unit + "] not supported for date math ["
							+ mathString + "]");
				}
			}
		} catch (Exception e) {
			if (e instanceof RestartParseException) {
				throw (RestartParseException) e;
			}
			throw new RestartParseException("failed to parse date math [" + mathString + "]");
		}
		return dateTime.getMillis();
	}

	
	/**
	 * Parses the string value.
	 *
	 * @param value the value
	 * @return the long
	 */
	private long parseStringValue(String value) {
		try {
			return dateTimeFormatter.parser().parseMillis(value);
		} catch (RuntimeException e) {
			try {
				long time = Long.parseLong(value);
				return timeUnit.toMillis(time);
			} catch (NumberFormatException e1) {
				throw new RestartParseException("failed to parse date field [" + value
						+ "], tried both date format [" + dateTimeFormatter.format() + "], and timestamp number", e);
			}
		}
	}

	
	/**
	 * Parses the upper inclusive string value.
	 *
	 * @param value the value
	 * @return the long
	 */
	private long parseUpperInclusiveStringValue(String value) {
		try {
			MutableDateTime dateTime = new MutableDateTime(3000, 12, 31, 23, 59, 59, 999, DateTimeZone.UTC);
			int location = dateTimeFormatter.parser().parseInto(dateTime, value, 0);
			
			if (location == value.length()) {
				return dateTime.getMillis();
			}
			
			
			if (location <= 0 || dateTime.getYear() > 5000) {
				try {
					long time = Long.parseLong(value);
					return timeUnit.toMillis(time);
				} catch (NumberFormatException e1) {
					throw new RestartParseException("failed to parse date field [" + value
							+ "], tried both date format [" + dateTimeFormatter.format() + "], and timestamp number");
				}
			}
			return dateTime.getMillis();
		} catch (RuntimeException e) {
			try {
				long time = Long.parseLong(value);
				return timeUnit.toMillis(time);
			} catch (NumberFormatException e1) {
				throw new RestartParseException("failed to parse date field [" + value
						+ "], tried both date format [" + dateTimeFormatter.format() + "], and timestamp number", e);
			}
		}
	}
}
