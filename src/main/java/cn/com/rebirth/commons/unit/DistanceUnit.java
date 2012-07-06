/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons DistanceUnit.java 2012-3-29 15:15:12 l.xue.nong$$
 */

package cn.com.rebirth.commons.unit;

import java.io.IOException;

import cn.com.rebirth.commons.exception.RestartIllegalArgumentException;
import cn.com.rebirth.commons.io.stream.StreamInput;
import cn.com.rebirth.commons.io.stream.StreamOutput;

/**
 * The Enum DistanceUnit.
 *
 * @author l.xue.nong
 */
public enum DistanceUnit {

	/** The MILES. */
	MILES(3959, 24902) {
		@Override
		public String toString() {
			return "miles";
		}

		@Override
		public double toMiles(double distance) {
			return distance;
		}

		@Override
		public double toKilometers(double distance) {
			return distance * MILES_KILOMETRES_RATIO;
		}

		@Override
		public String toString(double distance) {
			return distance + "mi";
		}
	},

	/** The KILOMETERS. */
	KILOMETERS(6371, 40076) {
		@Override
		public String toString() {
			return "km";
		}

		@Override
		public double toMiles(double distance) {
			return distance / MILES_KILOMETRES_RATIO;
		}

		@Override
		public double toKilometers(double distance) {
			return distance;
		}

		@Override
		public String toString(double distance) {
			return distance + "km";
		}
	};

	/** The Constant MILES_KILOMETRES_RATIO. */
	static final double MILES_KILOMETRES_RATIO = 1.609344;

	/**
	 * Convert.
	 *
	 * @param distance the distance
	 * @param from the from
	 * @param to the to
	 * @return the double
	 */
	public static double convert(double distance, DistanceUnit from, DistanceUnit to) {
		if (from == to) {
			return distance;
		}
		return (to == MILES) ? distance / MILES_KILOMETRES_RATIO : distance * MILES_KILOMETRES_RATIO;
	}

	/**
	 * Parses the.
	 *
	 * @param distance the distance
	 * @param defaultUnit the default unit
	 * @param to the to
	 * @return the double
	 */
	public static double parse(String distance, DistanceUnit defaultUnit, DistanceUnit to) {
		if (distance.endsWith("mi")) {
			return convert(Double.parseDouble(distance.substring(0, distance.length() - "mi".length())), MILES, to);
		} else if (distance.endsWith("miles")) {
			return convert(Double.parseDouble(distance.substring(0, distance.length() - "miles".length())), MILES, to);
		} else if (distance.endsWith("km")) {
			return convert(Double.parseDouble(distance.substring(0, distance.length() - "km".length())), KILOMETERS, to);
		} else {
			return convert(Double.parseDouble(distance), defaultUnit, to);
		}
	}

	/**
	 * Parses the unit.
	 *
	 * @param distance the distance
	 * @param defaultUnit the default unit
	 * @return the distance unit
	 */
	public static DistanceUnit parseUnit(String distance, DistanceUnit defaultUnit) {
		if (distance.endsWith("mi")) {
			return MILES;
		} else if (distance.endsWith("miles")) {
			return MILES;
		} else if (distance.endsWith("km")) {
			return KILOMETERS;
		} else {
			return defaultUnit;
		}
	}

	/** The earth circumference. */
	protected final double earthCircumference;

	/** The earth radius. */
	protected final double earthRadius;

	/** The distance per degree. */
	protected final double distancePerDegree;

	/**
	 * Instantiates a new distance unit.
	 *
	 * @param earthRadius the earth radius
	 * @param earthCircumference the earth circumference
	 */
	DistanceUnit(double earthRadius, double earthCircumference) {
		this.earthCircumference = earthCircumference;
		this.earthRadius = earthRadius;
		this.distancePerDegree = earthCircumference / 360;
	}

	/**
	 * Gets the earth circumference.
	 *
	 * @return the earth circumference
	 */
	public double getEarthCircumference() {
		return earthCircumference;
	}

	/**
	 * Gets the earth radius.
	 *
	 * @return the earth radius
	 */
	public double getEarthRadius() {
		return earthRadius;
	}

	/**
	 * Gets the distance per degree.
	 *
	 * @return the distance per degree
	 */
	public double getDistancePerDegree() {
		return distancePerDegree;
	}

	/**
	 * To miles.
	 *
	 * @param distance the distance
	 * @return the double
	 */
	public abstract double toMiles(double distance);

	/**
	 * To kilometers.
	 *
	 * @param distance the distance
	 * @return the double
	 */
	public abstract double toKilometers(double distance);

	/**
	 * To string.
	 *
	 * @param distance the distance
	 * @return the string
	 */
	public abstract String toString(double distance);

	/**
	 * From string.
	 *
	 * @param unit the unit
	 * @return the distance unit
	 */
	public static DistanceUnit fromString(String unit) {
		if ("km".equals(unit)) {
			return KILOMETERS;
		} else if ("mi".equals(unit)) {
			return MILES;
		} else if ("miles".equals(unit)) {
			return MILES;
		}
		throw new RestartIllegalArgumentException("No distance unit match [" + unit + "]");
	}

	/**
	 * Write distance unit.
	 *
	 * @param out the out
	 * @param unit the unit
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeDistanceUnit(StreamOutput out, DistanceUnit unit) throws IOException {
		if (unit == MILES) {
			out.writeByte((byte) 0);
		} else if (unit == KILOMETERS) {
			out.writeByte((byte) 1);
		}
	}

	/**
	 * Read distance unit.
	 *
	 * @param in the in
	 * @return the distance unit
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static DistanceUnit readDistanceUnit(StreamInput in) throws IOException {
		byte b = in.readByte();
		if (b == 0) {
			return MILES;
		} else if (b == 1) {
			return KILOMETERS;
		} else {
			throw new RestartIllegalArgumentException("No type for distance unit matching [" + b + "]");
		}
	}
}
