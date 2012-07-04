/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons SizeValue.java 2012-3-29 15:15:10 l.xue.nong$$
 */


package cn.com.restart.commons.unit;

import java.io.IOException;
import java.io.Serializable;

import cn.com.restart.commons.Strings;
import cn.com.restart.commons.exception.RestartParseException;
import cn.com.restart.commons.io.stream.StreamInput;
import cn.com.restart.commons.io.stream.StreamOutput;
import cn.com.restart.commons.io.stream.Streamable;


/**
 * The Class SizeValue.
 *
 * @author l.xue.nong
 */
public class SizeValue implements Serializable, Streamable {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5697227627989100649L;

	
	/** The size. */
	private long size;

	
	/** The size unit. */
	private SizeUnit sizeUnit;

	
	/**
	 * Instantiates a new size value.
	 */
	private SizeValue() {

	}

	
	/**
	 * Instantiates a new size value.
	 *
	 * @param singles the singles
	 */
	public SizeValue(long singles) {
		this(singles, SizeUnit.SINGLE);
	}

	
	/**
	 * Instantiates a new size value.
	 *
	 * @param size the size
	 * @param sizeUnit the size unit
	 */
	public SizeValue(long size, SizeUnit sizeUnit) {
		this.size = size;
		this.sizeUnit = sizeUnit;
	}

	
	/**
	 * Singles.
	 *
	 * @return the long
	 */
	public long singles() {
		return sizeUnit.toSingles(size);
	}

	
	/**
	 * Gets the singles.
	 *
	 * @return the singles
	 */
	public long getSingles() {
		return singles();
	}

	
	/**
	 * Kilo.
	 *
	 * @return the long
	 */
	public long kilo() {
		return sizeUnit.toKilo(size);
	}

	
	/**
	 * Gets the kilo.
	 *
	 * @return the kilo
	 */
	public long getKilo() {
		return kilo();
	}

	
	/**
	 * Mega.
	 *
	 * @return the long
	 */
	public long mega() {
		return sizeUnit.toMega(size);
	}

	
	/**
	 * Gets the mega.
	 *
	 * @return the mega
	 */
	public long getMega() {
		return mega();
	}

	
	/**
	 * Giga.
	 *
	 * @return the long
	 */
	public long giga() {
		return sizeUnit.toGiga(size);
	}

	
	/**
	 * Gets the giga.
	 *
	 * @return the giga
	 */
	public long getGiga() {
		return giga();
	}

	
	/**
	 * Kilo frac.
	 *
	 * @return the double
	 */
	public double kiloFrac() {
		return ((double) singles()) / SizeUnit.C1;
	}

	
	/**
	 * Gets the kilo frac.
	 *
	 * @return the kilo frac
	 */
	public double getKiloFrac() {
		return kiloFrac();
	}

	
	/**
	 * Mega frac.
	 *
	 * @return the double
	 */
	public double megaFrac() {
		return ((double) singles()) / SizeUnit.C2;
	}

	
	/**
	 * Gets the mega frac.
	 *
	 * @return the mega frac
	 */
	public double getMegaFrac() {
		return megaFrac();
	}

	
	/**
	 * Giga frac.
	 *
	 * @return the double
	 */
	public double gigaFrac() {
		return ((double) singles()) / SizeUnit.C3;
	}

	
	/**
	 * Gets the giga frac.
	 *
	 * @return the giga frac
	 */
	public double getGigaFrac() {
		return gigaFrac();
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		long singles = singles();
		double value = singles;
		String suffix = "";
		if (singles >= SizeUnit.C3) {
			value = gigaFrac();
			suffix = "g";
		} else if (singles >= SizeUnit.C2) {
			value = megaFrac();
			suffix = "m";
		} else if (singles >= SizeUnit.C1) {
			value = kiloFrac();
			suffix = "k";
		}
		return Strings.format1Decimals(value, suffix);
	}

	
	/**
	 * Parses the size value.
	 *
	 * @param sValue the s value
	 * @return the size value
	 * @throws RestartParseException the sum mall search parse exception
	 */
	public static SizeValue parseSizeValue(String sValue) throws RestartParseException {
		return SizeValue.parseSizeValue(sValue, null);
	}

	
	/**
	 * Parses the size value.
	 *
	 * @param sValue the s value
	 * @param defaultValue the default value
	 * @return the size value
	 * @throws RestartParseException the sum mall search parse exception
	 */
	public static SizeValue parseSizeValue(String sValue, SizeValue defaultValue) throws RestartParseException {
		if (sValue == null) {
			return defaultValue;
		}
		long singles;
		try {
			if (sValue.endsWith("b")) {
				singles = Long.parseLong(sValue.substring(0, sValue.length() - 1));
			} else if (sValue.endsWith("k") || sValue.endsWith("K")) {
				singles = (long) (Double.parseDouble(sValue.substring(0, sValue.length() - 1)) * SizeUnit.C1);
			} else if (sValue.endsWith("m") || sValue.endsWith("M")) {
				singles = (long) (Double.parseDouble(sValue.substring(0, sValue.length() - 1)) * SizeUnit.C2);
			} else if (sValue.endsWith("g") || sValue.endsWith("G")) {
				singles = (long) (Double.parseDouble(sValue.substring(0, sValue.length() - 1)) * SizeUnit.C3);
			} else {
				singles = Long.parseLong(sValue);
			}
		} catch (NumberFormatException e) {
			throw new RestartParseException("Failed to parse [" + sValue + "]", e);
		}
		return new SizeValue(singles, SizeUnit.SINGLE);
	}

	
	/**
	 * Read size value.
	 *
	 * @param in the in
	 * @return the size value
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static SizeValue readSizeValue(StreamInput in) throws IOException {
		SizeValue sizeValue = new SizeValue();
		sizeValue.readFrom(in);
		return sizeValue;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.summall.search.commons.io.stream.Streamable#readFrom(cn.com.summall.search.commons.io.stream.StreamInput)
	 */
	@Override
	public void readFrom(StreamInput in) throws IOException {
		size = in.readVLong();
		sizeUnit = SizeUnit.SINGLE;
	}

	
	/* (non-Javadoc)
	 * @see cn.com.summall.search.commons.io.stream.Streamable#writeTo(cn.com.summall.search.commons.io.stream.StreamOutput)
	 */
	@Override
	public void writeTo(StreamOutput out) throws IOException {
		out.writeVLong(singles());
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

		SizeValue sizeValue = (SizeValue) o;

		if (size != sizeValue.size)
			return false;
		if (sizeUnit != sizeValue.sizeUnit)
			return false;

		return true;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = (int) (size ^ (size >>> 32));
		result = 31 * result + (sizeUnit != null ? sizeUnit.hashCode() : 0);
		return result;
	}
}