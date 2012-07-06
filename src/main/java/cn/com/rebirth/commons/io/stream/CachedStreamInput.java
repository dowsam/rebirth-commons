/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons CachedStreamInput.java 2012-7-6 10:22:17 l.xue.nong$$
 */


package cn.com.rebirth.commons.io.stream;

import java.io.IOException;
import java.lang.ref.SoftReference;


/**
 * The Class CachedStreamInput.
 *
 * @author l.xue.nong
 */
public class CachedStreamInput {

    
    /**
     * The Class Entry.
     *
     * @author l.xue.nong
     */
    static class Entry {
        
        
        /** The chars. */
        char[] chars = new char[80];
        
        
        /** The handles. */
        final HandlesStreamInput handles;
        
        
        /** The lzf. */
        final LZFStreamInput lzf;

        
        /**
         * Instantiates a new entry.
         *
         * @param handles the handles
         * @param lzf the lzf
         */
        Entry(HandlesStreamInput handles, LZFStreamInput lzf) {
            this.handles = handles;
            this.lzf = lzf;
        }
    }

    
    /** The Constant cache. */
    private static final ThreadLocal<SoftReference<Entry>> cache = new ThreadLocal<SoftReference<Entry>>();

    
    /**
     * Instance.
     *
     * @return the entry
     */
    static Entry instance() {
        SoftReference<Entry> ref = cache.get();
        Entry entry = ref == null ? null : ref.get();
        if (entry == null) {
            HandlesStreamInput handles = new HandlesStreamInput();
            LZFStreamInput lzf = new LZFStreamInput(null, true);
            entry = new Entry(handles, lzf);
            cache.set(new SoftReference<Entry>(entry));
        }
        return entry;
    }

    
    /**
     * Clear.
     */
    public static void clear() {
        cache.remove();
    }

    
    /**
     * Cached lzf.
     *
     * @param in the in
     * @return the lZF stream input
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static LZFStreamInput cachedLzf(StreamInput in) throws IOException {
        LZFStreamInput lzf = instance().lzf;
        lzf.reset(in);
        return lzf;
    }

    
    /**
     * Cached handles.
     *
     * @param in the in
     * @return the handles stream input
     */
    public static HandlesStreamInput cachedHandles(StreamInput in) {
        HandlesStreamInput handles = instance().handles;
        handles.reset(in);
        return handles;
    }

    
    /**
     * Cached handles lzf.
     *
     * @param in the in
     * @return the handles stream input
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static HandlesStreamInput cachedHandlesLzf(StreamInput in) throws IOException {
        Entry entry = instance();
        entry.lzf.reset(in);
        entry.handles.reset(entry.lzf);
        return entry.handles;
    }

    
    /**
     * Gets the char array.
     *
     * @param size the size
     * @return the char array
     */
    public static char[] getCharArray(int size) {
        Entry entry = instance();
        if (entry.chars.length < size) {
            entry.chars = new char[size];
        }
        return entry.chars;
    }
}
