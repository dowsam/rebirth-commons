/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons ExtTLongObjectHashMap.java 2012-3-29 15:15:07 l.xue.nong$$
 */
package cn.com.rebirth.commons.trove;

import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;


/**
 * The Class ExtTLongObjectHashMap.
 *
 * @param <V> the value type
 * @author l.xue.nong
 */
public class ExtTLongObjectHashMap<V> extends TLongObjectHashMap<V> {

    
    /**
     * Instantiates a new ext t long object hash map.
     */
    public ExtTLongObjectHashMap() {
    }

    
    /**
     * Instantiates a new ext t long object hash map.
     *
     * @param initialCapacity the initial capacity
     */
    public ExtTLongObjectHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    
    /**
     * Instantiates a new ext t long object hash map.
     *
     * @param initialCapacity the initial capacity
     * @param loadFactor the load factor
     */
    public ExtTLongObjectHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    
    /**
     * Instantiates a new ext t long object hash map.
     *
     * @param initialCapacity the initial capacity
     * @param loadFactor the load factor
     * @param noEntryKey the no entry key
     */
    public ExtTLongObjectHashMap(int initialCapacity, float loadFactor, long noEntryKey) {
        super(initialCapacity, loadFactor, noEntryKey);
    }

    
    /**
     * Instantiates a new ext t long object hash map.
     *
     * @param vtLongObjectMap the vt long object map
     */
    public ExtTLongObjectHashMap(TLongObjectMap<V> vtLongObjectMap) {
        super(vtLongObjectMap);
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