/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:restart-commons SortType.java 2012-7-4 10:11:32 l.xue.nong$$
 */
package cn.com.restart.commons.search;

import org.apache.lucene.search.SortField;

/**
 * The Enum SortType.
 *
 * @author l.xue.nong
 */
public enum SortType {
	/** The DOC. */
	DOC(SortField.DOC),
	/** The SCORE. */
	SCORE(SortField.SCORE),
	/** The BYTE. */
	BYTE(SortField.BYTE),
	/** The SHORT. */
	SHORT(SortField.SHORT),
	/** The INT. */
	INT(SortField.INT),
	/** The LONG. */
	LONG(SortField.LONG),
	/** The FLOAT. */
	FLOAT(SortField.FLOAT),
	/** The DOUBLE. */
	DOUBLE(SortField.DOUBLE),
	/** The STRING. */
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
