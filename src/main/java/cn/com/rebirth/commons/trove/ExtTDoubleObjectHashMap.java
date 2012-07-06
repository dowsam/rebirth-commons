/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons ExtTDoubleObjectHashMap.java 2012-3-29 15:15:17 l.xue.nong$$
 */
package cn.com.rebirth.commons.trove;

import gnu.trove.map.TDoubleObjectMap;
import gnu.trove.map.hash.TDoubleObjectHashMap;


/**
 * The Class ExtTDoubleObjectHashMap.
 *
 * @param <V> the value type
 * @author l.xue.nong
 */
public class ExtTDoubleObjectHashMap<V> extends TDoubleObjectHashMap<V> {

    
    /**
     * Instantiates a new ext t double object hash map.
     */
    public ExtTDoubleObjectHashMap() {
    }

    
    /**
     * Instantiates a new ext t double object hash map.
     *
     * @param initialCapacity the initial capacity
     */
    public ExtTDoubleObjectHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    
    /**
     * Instantiates a new ext t double object hash map.
     *
     * @param initialCapacity the initial capacity
     * @param loadFactor the load factor
     */
    public ExtTDoubleObjectHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    
    /**
     * Instantiates a new ext t double object hash map.
     *
     * @param initialCapacity the initial capacity
     * @param loadFactor the load factor
     * @param noEntryKey the no entry key
     */
    public ExtTDoubleObjectHashMap(int initialCapacity, float loadFactor, double noEntryKey) {
        super(initialCapacity, loadFactor, noEntryKey);
    }

    
    /**
     * Instantiates a new ext t double object hash map.
     *
     * @param vtDoubleObjectMap the vt double object map
     */
    public ExtTDoubleObjectHashMap(TDoubleObjectMap<V> vtDoubleObjectMap) {
        super(vtDoubleObjectMap);
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