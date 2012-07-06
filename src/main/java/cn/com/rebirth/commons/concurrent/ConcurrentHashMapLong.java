/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons ConcurrentHashMapLong.java 2012-7-6 10:35:33 l.xue.nong$$
 */


package cn.com.rebirth.commons.concurrent;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * The Class ConcurrentHashMapLong.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public class ConcurrentHashMapLong<T> implements ConcurrentMapLong<T> {

    
    /** The map. */
    private final ConcurrentHashMap<Long, T> map;

    
    /**
     * Instantiates a new concurrent hash map long.
     */
    public ConcurrentHashMapLong() {
        this.map = new ConcurrentHashMap<Long, T>();
    }

    
    /* (non-Javadoc)
     * @see cn.com.rebirth.commons.concurrent.ConcurrentMapLong#get(long)
     */
    @Override
    public T get(long key) {
        return map.get(key);
    }

    
    /* (non-Javadoc)
     * @see cn.com.rebirth.commons.concurrent.ConcurrentMapLong#remove(long)
     */
    @Override
    public T remove(long key) {
        return map.remove(key);
    }

    
    /* (non-Javadoc)
     * @see cn.com.rebirth.commons.concurrent.ConcurrentMapLong#put(long, java.lang.Object)
     */
    @Override
    public T put(long key, T value) {
        return map.put(key, value);
    }

    
    /* (non-Javadoc)
     * @see cn.com.rebirth.commons.concurrent.ConcurrentMapLong#putIfAbsent(long, java.lang.Object)
     */
    @Override
    public T putIfAbsent(long key, T value) {
        return map.putIfAbsent(key, value);
    }

    

    
    /* (non-Javadoc)
     * @see java.util.Map#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    
    /* (non-Javadoc)
     * @see java.util.Map#size()
     */
    @Override
    public int size() {
        return map.size();
    }

    
    /* (non-Javadoc)
     * @see java.util.Map#get(java.lang.Object)
     */
    @Override
    public T get(Object key) {
        return map.get(key);
    }

    
    /* (non-Javadoc)
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    
    /* (non-Javadoc)
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    
    /**
     * Contains.
     *
     * @param value the value
     * @return true, if successful
     */
    public boolean contains(Object value) {
        return map.contains(value);
    }

    
    /* (non-Javadoc)
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    public T put(Long key, T value) {
        return map.put(key, value);
    }

    
    /* (non-Javadoc)
     * @see java.util.concurrent.ConcurrentMap#putIfAbsent(java.lang.Object, java.lang.Object)
     */
    public T putIfAbsent(Long key, T value) {
        return map.putIfAbsent(key, value);
    }

    
    /* (non-Javadoc)
     * @see java.util.Map#putAll(java.util.Map)
     */
    public void putAll(Map<? extends Long, ? extends T> m) {
        map.putAll(m);
    }

    
    /* (non-Javadoc)
     * @see java.util.Map#remove(java.lang.Object)
     */
    @Override
    public T remove(Object key) {
        return map.remove(key);
    }

    
    /* (non-Javadoc)
     * @see java.util.concurrent.ConcurrentMap#remove(java.lang.Object, java.lang.Object)
     */
    @Override
    public boolean remove(Object key, Object value) {
        return map.remove(key, value);
    }

    
    /* (non-Javadoc)
     * @see java.util.concurrent.ConcurrentMap#replace(java.lang.Object, java.lang.Object, java.lang.Object)
     */
    public boolean replace(Long key, T oldValue, T newValue) {
        return map.replace(key, oldValue, newValue);
    }

    
    /* (non-Javadoc)
     * @see java.util.concurrent.ConcurrentMap#replace(java.lang.Object, java.lang.Object)
     */
    public T replace(Long key, T value) {
        return map.replace(key, value);
    }

    
    /* (non-Javadoc)
     * @see java.util.Map#clear()
     */
    @Override
    public void clear() {
        map.clear();
    }

    
    /* (non-Javadoc)
     * @see java.util.Map#keySet()
     */
    @Override
    public Set<Long> keySet() {
        return map.keySet();
    }

    
    /* (non-Javadoc)
     * @see java.util.Map#values()
     */
    @Override
    public Collection<T> values() {
        return map.values();
    }

    
    /* (non-Javadoc)
     * @see java.util.Map#entrySet()
     */
    @Override
    public Set<Entry<Long, T>> entrySet() {
        return map.entrySet();
    }

    
    /**
     * Keys.
     *
     * @return the enumeration
     */
    public Enumeration<Long> keys() {
        return map.keys();
    }

    
    /**
     * Elements.
     *
     * @return the enumeration
     */
    public Enumeration<T> elements() {
        return map.elements();
    }

    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        return map.equals(o);
    }

    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return map.hashCode();
    }

    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return map.toString();
    }
}
