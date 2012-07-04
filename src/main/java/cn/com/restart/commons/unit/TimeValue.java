/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons TimeValue.java 2012-3-29 15:15:09 l.xue.nong$$
 */


package cn.com.restart.commons.unit;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormat;
import org.joda.time.format.PeriodFormatter;

import cn.com.restart.commons.Strings;
import cn.com.restart.commons.exception.RestartParseException;
import cn.com.restart.commons.io.stream.StreamInput;
import cn.com.restart.commons.io.stream.StreamOutput;
import cn.com.restart.commons.io.stream.Streamable;



/**
 * The Class TimeValue.
 *
 * @author l.xue.nong
 */
public class TimeValue implements Serializable, Streamable {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5000877509882613034L;

	
	/**
	 * Time value nanos.
	 *
	 * @param nanos the nanos
	 * @return the time value
	 */
	public static TimeValue timeValueNanos(long nanos) {
		return new TimeValue(nanos, TimeUnit.NANOSECONDS);
	}

	
	/**
	 * Time value millis.
	 *
	 * @param millis the millis
	 * @return the time value
	 */
	public static TimeValue timeValueMillis(long millis) {
		return new TimeValue(millis, TimeUnit.MILLISECONDS);
	}

	
	/**
	 * Time value seconds.
	 *
	 * @param seconds the seconds
	 * @return the time value
	 */
	public static TimeValue timeValueSeconds(long seconds) {
		return new TimeValue(seconds, TimeUnit.SECONDS);
	}

	
	/**
	 * Time value minutes.
	 *
	 * @param minutes the minutes
	 * @return the time value
	 */
	public static TimeValue timeValueMinutes(long minutes) {
		return new TimeValue(minutes, TimeUnit.MINUTES);
	}

	
	/**
	 * Time value hours.
	 *
	 * @param hours the hours
	 * @return the time value
	 */
	public static TimeValue timeValueHours(long hours) {
		return new TimeValue(hours, TimeUnit.HOURS);
	}

	
	/** The duration. */
	private long duration;

	
	/** The time unit. */
	private TimeUnit timeUnit;

	
	/**
	 * Instantiates a new time value.
	 */
	private TimeValue() {

	}

	
	/**
	 * Instantiates a new time value.
	 *
	 * @param millis the millis
	 */
	public TimeValue(long millis) {
		this(millis, TimeUnit.MILLISECONDS);
	}

	
	/**
	 * Instantiates a new time value.
	 *
	 * @param duration the duration
	 * @param timeUnit the time unit
	 */
	public TimeValue(long duration, TimeUnit timeUnit) {
		this.duration = duration;
		this.timeUnit = timeUnit;
	}

	
	/**
	 * Nanos.
	 *
	 * @return the long
	 */
	public long nanos() {
		return timeUnit.toNanos(duration);
	}

	
	/**
	 * Gets the nanos.
	 *
	 * @return the nanos
	 */
	public long getNanos() {
		return nanos();
	}

	
	/**
	 * Micros.
	 *
	 * @return the long
	 */
	public long micros() {
		return timeUnit.toMicros(duration);
	}

	
	/**
	 * Gets the micros.
	 *
	 * @return the micros
	 */
	public long getMicros() {
		return micros();
	}

	
	/**
	 * Millis.
	 *
	 * @return the long
	 */
	public long millis() {
		return timeUnit.toMillis(duration);
	}

	
	/**
	 * Gets the millis.
	 *
	 * @return the millis
	 */
	public long getMillis() {
		return millis();
	}

	
	/**
	 * Seconds.
	 *
	 * @return the long
	 */
	public long seconds() {
		return timeUnit.toSeconds(duration);
	}

	
	/**
	 * Gets the seconds.
	 *
	 * @return the seconds
	 */
	public long getSeconds() {
		return seconds();
	}

	
	/**
	 * Minutes.
	 *
	 * @return the long
	 */
	public long minutes() {
		return timeUnit.toMinutes(duration);
	}

	
	/**
	 * Gets the minutes.
	 *
	 * @return the minutes
	 */
	public long getMinutes() {
		return minutes();
	}

	
	/**
	 * Hours.
	 *
	 * @return the long
	 */
	public long hours() {
		return timeUnit.toHours(duration);
	}

	
	/**
	 * Gets the hours.
	 *
	 * @return the hours
	 */
	public long getHours() {
		return hours();
	}

	
	/**
	 * Days.
	 *
	 * @return the long
	 */
	public long days() {
		return timeUnit.toDays(duration);
	}

	
	/**
	 * Gets the days.
	 *
	 * @return the days
	 */
	public long getDays() {
		return days();
	}

	
	/**
	 * Micros frac.
	 *
	 * @return the double
	 */
	public double microsFrac() {
		return ((double) nanos()) / C1;
	}

	
	/**
	 * Gets the micros frac.
	 *
	 * @return the micros frac
	 */
	public double getMicrosFrac() {
		return microsFrac();
	}

	
	/**
	 * Millis frac.
	 *
	 * @return the double
	 */
	public double millisFrac() {
		return ((double) nanos()) / C2;
	}

	
	/**
	 * Gets the millis frac.
	 *
	 * @return the millis frac
	 */
	public double getMillisFrac() {
		return millisFrac();
	}

	
	/**
	 * Seconds frac.
	 *
	 * @return the double
	 */
	public double secondsFrac() {
		return ((double) nanos()) / C3;
	}

	
	/**
	 * Gets the seconds frac.
	 *
	 * @return the seconds frac
	 */
	public double getSecondsFrac() {
		return secondsFrac();
	}

	
	/**
	 * Minutes frac.
	 *
	 * @return the double
	 */
	public double minutesFrac() {
		return ((double) nanos()) / C4;
	}

	
	/**
	 * Gets the minutes frac.
	 *
	 * @return the minutes frac
	 */
	public double getMinutesFrac() {
		return minutesFrac();
	}

	
	/**
	 * Hours frac.
	 *
	 * @return the double
	 */
	public double hoursFrac() {
		return ((double) nanos()) / C5;
	}

	
	/**
	 * Gets the hours frac.
	 *
	 * @return the hours frac
	 */
	public double getHoursFrac() {
		return hoursFrac();
	}

	
	/**
	 * Days frac.
	 *
	 * @return the double
	 */
	public double daysFrac() {
		return ((double) nanos()) / C6;
	}

	
	/**
	 * Gets the days frac.
	 *
	 * @return the days frac
	 */
	public double getDaysFrac() {
		return daysFrac();
	}

	
	/** The default formatter. */
	private final PeriodFormatter defaultFormatter = PeriodFormat.getDefault().withParseType(PeriodType.standard());

	
	/**
	 * Format.
	 *
	 * @return the string
	 */
	public String format() {
		Period period = new Period(millis());
		return defaultFormatter.print(period);
	}

	
	/**
	 * Format.
	 *
	 * @param type the type
	 * @return the string
	 */
	public String format(PeriodType type) {
		Period period = new Period(millis());
		return PeriodFormat.getDefault().withParseType(type).print(period);
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (duration < 0) {
			return Long.toString(duration);
		}
		long nanos = nanos();
		if (nanos == 0) {
			return "0s";
		}
		double value = nanos;
		String suffix = "nanos";
		if (nanos >= C6) {
			value = daysFrac();
			suffix = "d";
		} else if (nanos >= C5) {
			value = hoursFrac();
			suffix = "h";
		} else if (nanos >= C4) {
			value = minutesFrac();
			suffix = "m";
		} else if (nanos >= C3) {
			value = secondsFrac();
			suffix = "s";
		} else if (nanos >= C2) {
			value = millisFrac();
			suffix = "ms";
		} else if (nanos >= C1) {
			value = microsFrac();
			suffix = "micros";
		}
		return Strings.format1Decimals(value, suffix);
	}

	
	/**
	 * Parses the time value.
	 *
	 * @param sValue the s value
	 * @param defaultValue the default value
	 * @return the time value
	 */
	public static TimeValue parseTimeValue(String sValue, TimeValue defaultValue) {
		if (sValue == null) {
			return defaultValue;
		}
		try {
			long millis;
			if (sValue.endsWith("S")) {
				millis = Long.parseLong(sValue.substring(0, sValue.length() - 1));
			} else if (sValue.endsWith("ms")) {
				millis = (long) (Double.parseDouble(sValue.substring(0, sValue.length() - "ms".length())));
			} else if (sValue.endsWith("s")) {
				millis = (long) (Double.parseDouble(sValue.substring(0, sValue.length() - 1)) * 1000);
			} else if (sValue.endsWith("m")) {
				millis = (long) (Double.parseDouble(sValue.substring(0, sValue.length() - 1)) * 60 * 1000);
			} else if (sValue.endsWith("H") || sValue.endsWith("h")) {
				millis = (long) (Double.parseDouble(sValue.substring(0, sValue.length() - 1)) * 60 * 60 * 1000);
			} else if (sValue.endsWith("d")) {
				millis = (long) (Double.parseDouble(sValue.substring(0, sValue.length() - 1)) * 24 * 60 * 60 * 1000);
			} else if (sValue.endsWith("w")) {
				millis = (long) (Double.parseDouble(sValue.substring(0, sValue.length() - 1)) * 7 * 24 * 60 * 60 * 1000);
			} else {
				millis = Long.parseLong(sValue);
			}
			return new TimeValue(millis, TimeUnit.MILLISECONDS);
		} catch (NumberFormatException e) {
			throw new RestartParseException("Failed to parse [" + sValue + "]", e);
		}
	}

	
	/** The Constant C0. */
	static final long C0 = 1L;

	
	/** The Constant C1. */
	static final long C1 = C0 * 1000L;

	
	/** The Constant C2. */
	static final long C2 = C1 * 1000L;

	
	/** The Constant C3. */
	static final long C3 = C2 * 1000L;

	
	/** The Constant C4. */
	static final long C4 = C3 * 60L;

	
	/** The Constant C5. */
	static final long C5 = C4 * 60L;

	
	/** The Constant C6. */
	static final long C6 = C5 * 24L;

	
	/**
	 * Read time value.
	 *
	 * @param in the in
	 * @return the time value
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static TimeValue readTimeValue(StreamInput in) throws IOException {
		TimeValue timeValue = new TimeValue();
		timeValue.readFrom(in);
		return timeValue;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.summall.search.commons.io.stream.Streamable#readFrom(cn.com.summall.search.commons.io.stream.StreamInput)
	 */
	@Override
	public void readFrom(StreamInput in) throws IOException {
		duration = in.readLong();
		timeUnit = TimeUnit.NANOSECONDS;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.summall.search.commons.io.stream.Streamable#writeTo(cn.com.summall.search.commons.io.stream.StreamOutput)
	 */
	@Override
	public void writeTo(StreamOutput out) throws IOException {
		out.writeLong(nanos());
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		TimeValue timeValue = (TimeValue) o;

		if (duration != timeValue.duration)
			return false;
		if (timeUnit != timeValue.timeUnit)
			return false;

		return true;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = (int) (duration ^ (duration >>> 32));
		result = 31 * result + (timeUnit != null ? timeUnit.hashCode() : 0);
		return result;
	}
}
