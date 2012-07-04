/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons HandlesStreamInput.java 2012-3-29 15:15:17 l.xue.nong$$
 */


package cn.com.restart.commons.io.stream;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.IOException;


/**
 * The Class HandlesStreamInput.
 *
 * @author l.xue.nong
 */
public class HandlesStreamInput extends AdapterStreamInput {

    
    /** The handles. */
    private final TIntObjectHashMap<String> handles = new TIntObjectHashMap<String>();

    
    /** The identity handles. */
    private final TIntObjectHashMap<String> identityHandles = new TIntObjectHashMap<String>();

    
    /**
     * Instantiates a new handles stream input.
     */
    HandlesStreamInput() {
        super();
    }

    
    /**
     * Instantiates a new handles stream input.
     *
     * @param in the in
     */
    public HandlesStreamInput(StreamInput in) {
        super(in);
    }

    
    /* (non-Javadoc)
     * @see cn.com.summall.search.commons.io.stream.AdapterStreamInput#readUTF()
     */
    @Override
    public String readUTF() throws IOException {
        byte b = in.readByte();
        if (b == 0) {
            
            int handle = in.readVInt();
            String s = in.readUTF();
            handles.put(handle, s);
            return s;
        } else if (b == 1) {
            return handles.get(in.readVInt());
        } else if (b == 2) {
            
            int handle = in.readVInt();
            String s = in.readUTF();
            identityHandles.put(handle, s);
            return s;
        } else if (b == 3) {
            return identityHandles.get(in.readVInt());
        } else {
            throw new IOException("Expected handle header, got [" + b + "]");
        }
    }

    
    /* (non-Javadoc)
     * @see cn.com.summall.search.commons.io.stream.AdapterStreamInput#reset()
     */
    @Override
    public void reset() throws IOException {
        super.reset();
        handles.clear();
        identityHandles.clear();
    }

    
    /* (non-Javadoc)
     * @see cn.com.summall.search.commons.io.stream.AdapterStreamInput#reset(cn.com.summall.search.commons.io.stream.StreamInput)
     */
    public void reset(StreamInput in) {
        super.reset(in);
        handles.clear();
        identityHandles.clear();
    }

    
    /**
     * Clean handles.
     */
    public void cleanHandles() {
        handles.clear();
        identityHandles.clear();
    }
}
