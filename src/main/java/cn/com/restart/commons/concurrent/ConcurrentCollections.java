/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons ConcurrentCollections.java 2012-3-29 15:15:10 l.xue.nong$$
 */


package cn.com.restart.commons.concurrent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import cn.com.restart.commons.collect.MapBackedSet;


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
