/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons ConcurrentCollections.java 2012-7-6 10:22:15 l.xue.nong$$
 */


package cn.com.rebirth.commons.concurrent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import cn.com.rebirth.commons.collect.MapBackedSet;


/**
 * The Class ConcurrentCollections.
 *
 * @author l.xue.nong
 */
public abstract class ConcurrentCollections {

	
	/**
	 * New concurrent map.
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 * @return the concurrent map
	 */
	public static <K, V> ConcurrentMap<K, V> newConcurrentMap() {
		return new ConcurrentHashMap<K, V>();
	}

	
	/**
	 * New concurrent map long.
	 *
	 * @param <V> the value type
	 * @return the concurrent map long
	 */
	public static <V> ConcurrentMapLong<V> newConcurrentMapLong() {
		return new ConcurrentHashMapLong<V>();
	}

	
	/**
	 * New concurrent set.
	 *
	 * @param <V> the value type
	 * @return the sets the
	 */
	public static <V> Set<V> newConcurrentSet() {
		return new MapBackedSet<V>(new ConcurrentHashMap<V, Boolean>());
	}

	
	/**
	 * Instantiates a new concurrent collections.
	 */
	private ConcurrentCollections() {

	}
}
