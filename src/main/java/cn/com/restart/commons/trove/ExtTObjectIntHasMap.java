/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons ExtTObjectIntHasMap.java 2012-3-29 15:15:10 l.xue.nong$$
 */
package cn.com.restart.commons.trove;

import gnu.trove.map.hash.TObjectIntHashMap;


/**
 * The Class ExtTObjectIntHasMap.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public class ExtTObjectIntHasMap<T> extends TObjectIntHashMap<T> {

	
	/**
	 * Instantiates a new ext t object int has map.
	 */
	public ExtTObjectIntHasMap() {
	}

	
	/**
	 * Instantiates a new ext t object int has map.
	 *
	 * @param initialCapacity the initial capacity
	 */
	public ExtTObjectIntHasMap(int initialCapacity) {
		super(initialCapacity);
	}

	
	/**
	 * Instantiates a new ext t object int has map.
	 *
	 * @param initialCapacity the initial capacity
	 * @param loadFactor the load factor
	 */
	public ExtTObjectIntHasMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	
	/**
	 * Instantiates a new ext t object int has map.
	 *
	 * @param initialCapacity the initial capacity
	 * @param loadFactor the load factor
	 * @param noEntryValue the no entry value
	 */
	public ExtTObjectIntHasMap(int initialCapacity, float loadFactor, int noEntryValue) {
		super(initialCapacity, loadFactor, noEntryValue);
	}

	
	/**
	 * Key.
	 *
	 * @param key the key
	 * @return the t
	 */
	@SuppressWarnings("unchecked")
	public T key(T key) {
		int index = index(key);
		return index < 0 ? null : (T) _set[index];
	}
}
