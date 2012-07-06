/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons ExtTHashMap.java 2012-7-6 10:22:16 l.xue.nong$$
 */
package cn.com.rebirth.commons.trove;

import gnu.trove.map.hash.THashMap;

import java.util.Map;


/**
 * The Class ExtTHashMap.
 *
 * @param <K> the key type
 * @param <V> the value type
 * @author l.xue.nong
 */
public class ExtTHashMap<K, V> extends THashMap<K, V> {

	
	/**
	 * Instantiates a new ext t hash map.
	 */
	public ExtTHashMap() {
	}

	
	/**
	 * Instantiates a new ext t hash map.
	 *
	 * @param initialCapacity the initial capacity
	 */
	public ExtTHashMap(int initialCapacity) {
		super(initialCapacity);
	}

	
	/**
	 * Instantiates a new ext t hash map.
	 *
	 * @param initialCapacity the initial capacity
	 * @param loadFactor the load factor
	 */
	public ExtTHashMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	
	/**
	 * Instantiates a new ext t hash map.
	 *
	 * @param kvMap the kv map
	 */
	public ExtTHashMap(Map<K, V> kvMap) {
		super(kvMap);
	}

	
	/**
	 * Instantiates a new ext t hash map.
	 *
	 * @param kvtHashMap the kvt hash map
	 */
	public ExtTHashMap(THashMap<K, V> kvtHashMap) {
		super(kvtHashMap);
	}

	
	/**
	 * Internal values.
	 *
	 * @return the object[]
	 */
	public Object[] internalValues() {
		return this._values;
	}
}