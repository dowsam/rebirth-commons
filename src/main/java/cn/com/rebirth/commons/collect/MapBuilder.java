/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons MapBuilder.java 2012-3-29 15:15:12 l.xue.nong$$
 */


package cn.com.rebirth.commons.collect;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;


/**
 * The Class MapBuilder.
 *
 * @param <K> the key type
 * @param <V> the value type
 * @author l.xue.nong
 */
public class MapBuilder<K, V> {

    
    /**
     * New map builder.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @return the map builder
     */
    public static <K, V> MapBuilder<K, V> newMapBuilder() {
        return new MapBuilder<K, V>();
    }

    
    /**
     * New map builder.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @param map the map
     * @return the map builder
     */
    public static <K, V> MapBuilder<K, V> newMapBuilder(Map<K, V> map) {
        return new MapBuilder<K, V>().putAll(map);
    }

    
    /** The map. */
    private Map<K, V> map = newHashMap();

    
    /**
     * Instantiates a new map builder.
     */
    public MapBuilder() {
        this.map = newHashMap();
    }

    
    /**
     * Put all.
     *
     * @param map the map
     * @return the map builder
     */
    public MapBuilder<K, V> putAll(Map<K, V> map) {
        this.map.putAll(map);
        return this;
    }

    
    /**
     * Put.
     *
     * @param key the key
     * @param value the value
     * @return the map builder
     */
    public MapBuilder<K, V> put(K key, V value) {
        this.map.put(key, value);
        return this;
    }

    
    /**
     * Removes the.
     *
     * @param key the key
     * @return the map builder
     */
    public MapBuilder<K, V> remove(K key) {
        this.map.remove(key);
        return this;
    }

    
    /**
     * Clear.
     *
     * @return the map builder
     */
    public MapBuilder<K, V> clear() {
        this.map.clear();
        return this;
    }

    
    /**
     * Gets the.
     *
     * @param key the key
     * @return the v
     */
    public V get(K key) {
        return map.get(key);
    }

    
    /**
     * Contains key.
     *
     * @param key the key
     * @return true, if successful
     */
    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    
    /**
     * Map.
     *
     * @return the map
     */
    public Map<K, V> map() {
        return this.map;
    }

    
    /**
     * Immutable map.
     *
     * @return the immutable map
     */
    public ImmutableMap<K, V> immutableMap() {
        return ImmutableMap.copyOf(map);
    }
}
