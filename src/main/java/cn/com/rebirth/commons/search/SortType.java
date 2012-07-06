/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons SortType.java 2012-7-6 10:22:17 l.xue.nong$$
 */
package cn.com.rebirth.commons.search;

import org.apache.lucene.search.SortField;

/**
 * The Enum SortType.
 *
 * @author l.xue.nong
 */
public enum SortType {
	
	/** The doc. */
	DOC(SortField.DOC),
	
	/** The score. */
	SCORE(SortField.SCORE),
	
	/** The byte. */
	BYTE(SortField.BYTE),
	
	/** The short. */
	SHORT(SortField.SHORT),
	
	/** The int. */
	INT(SortField.INT),
	
	/** The long. */
	LONG(SortField.LONG),
	
	/** The float. */
	FLOAT(SortField.FLOAT),
	
	/** The double. */
	DOUBLE(SortField.DOUBLE),
	
	/** The string. */
	STRING(SortField.STRING);

	/** The value. */
	int value;

	/**
	 * Instantiates a new sort type.
	 *
	 * @param value the value
	 */
	SortType(int value) {
		this.value = value;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Value.
	 *
	 * @param value the value
	 * @return the sort type
	 */
	public static SortType value(int value) {
		SortType[] sortTypes = SortType.class.getEnumConstants();
		for (SortType sortType : sortTypes) {
			if (value == sortType.getValue())
				return sortType;
		}
		return null;
	}
}
