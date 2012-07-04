/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons MapBackedSet.java 2012-3-29 15:15:12 l.xue.nong$$
 */


package cn.com.restart.commons.collect;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;


/**
 * The Class MapBackedSet.
 *
 * @param <E> the element type
 * @author l.xue.nong
 */
public class MapBackedSet<E> extends AbstractSet<E> implements Serializable {

    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -6761513279741915432L;

    
    /** The map. */
    private final Map<E, Boolean> map;

    
    /**
     * Instantiates a new map backed set.
     *
     * @param map the map
     */
    public MapBackedSet(Map<E, Boolean> map) {
        this.map = map;
    }

    
    /* (non-Javadoc)
     * @see java.util.AbstractCollection#size()
     */
    @Override
    public int size() {
        return map.size();
    }

    
    /* (non-Javadoc)
     * @see java.util.AbstractCollection#contains(java.lang.Object)
     */
    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    
    /* (non-Javadoc)
     * @see java.util.AbstractCollection#add(java.lang.Object)
     */
    @Override
    public boolean add(E o) {
        return map.put(o, Boolean.TRUE) == null;
    }

    
    /* (non-Javadoc)
     * @see java.util.AbstractCollection#remove(java.lang.Object)
     */
    @Override
    public boolean remove(Object o) {
        return map.remove(o) != null;
    }

    
    /* (non-Javadoc)
     * @see java.util.AbstractCollection#clear()
     */
    @Override
    public void clear() {
        map.clear();
    }

    
    /* (non-Javadoc)
     * @see java.util.AbstractCollection#iterator()
     */
    @Override
    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }
}
