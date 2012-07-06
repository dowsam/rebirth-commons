/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons Tuple.java 2012-7-6 10:22:16 l.xue.nong$$
 */


package cn.com.rebirth.commons.collect;


/**
 * The Class Tuple.
 *
 * @param <V1> the generic type
 * @param <V2> the generic type
 * @author l.xue.nong
 */
public class Tuple<V1, V2> {

	
	/**
	 * Tuple.
	 *
	 * @param <V1> the generic type
	 * @param <V2> the generic type
	 * @param v1 the v1
	 * @param v2 the v2
	 * @return the tuple
	 */
	public static <V1, V2> Tuple<V1, V2> tuple(V1 v1, V2 v2) {
		return new Tuple<V1, V2>(v1, v2);
	}

	
	/** The v1. */
	private final V1 v1;

	
	/** The v2. */
	private final V2 v2;

	
	/**
	 * Instantiates a new tuple.
	 *
	 * @param v1 the v1
	 * @param v2 the v2
	 */
	public Tuple(V1 v1, V2 v2) {
		this.v1 = v1;
		this.v2 = v2;
	}

	
	/**
	 * V1.
	 *
	 * @return the v1
	 */
	public V1 v1() {
		return v1;
	}

	
	/**
	 * V2.
	 *
	 * @return the v2
	 */
	public V2 v2() {
		return v2;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Tuple tuple = (Tuple) o;

		if (v1 != null ? !v1.equals(tuple.v1) : tuple.v1 != null)
			return false;
		if (v2 != null ? !v2.equals(tuple.v2) : tuple.v2 != null)
			return false;

		return true;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = v1 != null ? v1.hashCode() : 0;
		result = 31 * result + (v2 != null ? v2.hashCode() : 0);
		return result;
	}

	
	/**
	 * Creates the.
	 *
	 * @param <V1> the generic type
	 * @param <V2> the generic type
	 * @param v1 the v1
	 * @param v2 the v2
	 * @return the tuple
	 */
	public static <V1, V2> Tuple<V1, V2> create(V1 v1, V2 v2) {
		return new Tuple<V1, V2>(v1, v2);
	}
}
