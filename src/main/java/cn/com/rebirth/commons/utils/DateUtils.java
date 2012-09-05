/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons DateUtils.java 2012-7-6 10:22:17 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.Validate;

import cn.com.rebirth.commons.FormatConstants;

/**
 * The Class DateUtils.
 *
 * @author l.xue.nong
 */
public abstract class DateUtils extends org.apache.commons.lang3.time.DateUtils {

	/**
	 * Gets the current date time.
	 *
	 * @return the current date time
	 */
	public static Date getCurrentDateTime() {
		java.util.Calendar calNow = java.util.Calendar.getInstance();
		java.util.Date dtNow = calNow.getTime();
		return dtNow;
	}

	/**
	 * Gets the today.
	 *
	 * @return the today
	 */
	public static Date getToday() {
		return truncate(new Date(), Calendar.DATE);
	}

	/**
	 * Gets the today end.
	 *
	 * @return the today end
	 */
	public static Date getTodayEnd() {
		return getDayEnd(new Date());
	}

	/**
	 * Convert to date.
	 *
	 * @param dateString the date string
	 * @return the date
	 */
	public static Date convertToDate(String dateString) {
		try {
			return FormatConstants.DATE_FORMAT.parse(dateString);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Check date string.
	 *
	 * @param dateString the date string
	 * @return true, if successful
	 */
	public static boolean checkDateString(String dateString) {
		return (convertToDate(dateString) != null);
	}

	/**
	 * Convert to date time.
	 *
	 * @param dateTimeString the date time string
	 * @return the date
	 */
	public static Date convertToDateTime(String dateTimeString) {
		try {
			return FormatConstants.DATE_TIME_FORMAT.parse(dateTimeString);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Check date time string.
	 *
	 * @param dateTimeString the date time string
	 * @return true, if successful
	 */
	public static boolean checkDateTimeString(String dateTimeString) {
		return (convertToDateTime(dateTimeString) != null);
	}

	/**
	 * Gets the month end.
	 *
	 * @param year the year
	 * @param month the month
	 * @return the month end
	 */
	public static Date getMonthEnd(int year, int month) {
		StringBuffer sb = new StringBuffer(10);
		Date date;
		if (month < 12) {
			sb.append(Integer.toString(year));
			sb.append("-");
			sb.append(month + 1);
			sb.append("-1");
			date = convertToDate(sb.toString());
		} else {
			sb.append(Integer.toString(year + 1));
			sb.append("-1-1");
			date = convertToDate(sb.toString());
		}
		date.setTime(date.getTime() - 1);
		return date;
	}

	/**
	 * Gets the month end.
	 *
	 * @param when the when
	 * @return the month end
	 */
	public static Date getMonthEnd(Date when) {
		Validate.notNull(when, "date must not be null !");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(when);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		return getMonthEnd(year, month);
	}

	/**
	 * Gets the day end.
	 *
	 * @param when the when
	 * @return the day end
	 */
	public static Date getDayEnd(Date when) {
		Date date = truncate(when, Calendar.DATE);
		date = addDays(date, 1);
		date.setTime(date.getTime() - 1);
		return date;
	}

	/**
	 * Gets the day.
	 *
	 * @param when the when
	 * @return the day
	 */
	public static Date getDay(Date when) {
		Date date = truncate(when, Calendar.DATE);
		date = addDays(date, -1);
		date.setTime(date.getTime() + 1);
		return date;
	}

	/**
	 * Adds the.
	 *
	 * @param when the when
	 * @param field the field
	 * @param amount the amount
	 * @return the date
	 */
	public static Date add(Date when, int field, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(when);
		calendar.add(field, amount);
		return calendar.getTime();
	}

	/**
	 * Adds the days.
	 *
	 * @param when the when
	 * @param amount the amount
	 * @return the date
	 */
	public static Date addDays(Date when, int amount) {
		return add(when, Calendar.DAY_OF_YEAR, amount);
	}

	/**
	 * Adds the months.
	 *
	 * @param when the when
	 * @param amount the amount
	 * @return the date
	 */
	public static Date addMonths(Date when, int amount) {
		return add(when, Calendar.MONTH, amount);
	}

	/**
	 * Format date.
	 *
	 * @param date the date
	 * @param format the format
	 * @return the string
	 */
	public static String formatDate(Date date, String format) {
		if (StringUtils.isNotBlank(format)) {
			DateFormat DATE_TIME_FORMAT = new SimpleDateFormat(format, java.util.Locale.CHINA);
			return DATE_TIME_FORMAT.format(date);
		}
		return FormatConstants.DATE_TIME_FORMAT.format(date);
	}
}
