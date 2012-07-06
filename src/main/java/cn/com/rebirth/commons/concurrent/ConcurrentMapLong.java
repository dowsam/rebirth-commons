/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons ConcurrentMapLong.java 2012-7-6 10:22:14 l.xue.nong$$
 */


package cn.com.rebirth.commons.concurrent;

import java.util.concurrent.ConcurrentMap;


/**
 * The Interface ConcurrentMapLong.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public interface ConcurrentMapLong<T> extends ConcurrentMap<Long, T> {

    
    /**
     * Gets the.
     *
     * @param key the key
     * @return the t
     */
    T get(long key);

    
    /**
     * Removes the.
     *
     * @param key the key
     * @return the t
     */
    T remove(long key);

    
    /**
     * Put.
     *
     * @param key the key
     * @param value the value
     * @return the t
     */
    T put(long key, T value);

    
    /**
     * Put if absent.
     *
     * @param key the key
     * @param value the value
     * @return the t
     */
    T putIfAbsent(long key, T value);
}
