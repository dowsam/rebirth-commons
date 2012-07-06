/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons ExtTIntArrayList.java 2012-7-6 10:22:16 l.xue.nong$$
 */
package cn.com.rebirth.commons.trove;

import gnu.trove.list.array.TIntArrayList;


/**
 * The Class ExtTIntArrayList.
 *
 * @author l.xue.nong
 */
public class ExtTIntArrayList extends TIntArrayList {

    
    /**
     * Instantiates a new ext t int array list.
     */
    public ExtTIntArrayList() {
    }

    
    /**
     * Instantiates a new ext t int array list.
     *
     * @param capacity the capacity
     */
    public ExtTIntArrayList(int capacity) {
        super(capacity);
    }

    
    /**
     * Instantiates a new ext t int array list.
     *
     * @param values the values
     */
    public ExtTIntArrayList(int[] values) {
        super(values);
    }

    
    /**
     * Unsafe array.
     *
     * @return the int[]
     */
    public int[] unsafeArray() {
        return _data;
    }
}
