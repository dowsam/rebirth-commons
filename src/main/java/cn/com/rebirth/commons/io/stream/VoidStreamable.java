/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons VoidStreamable.java 2012-7-6 10:23:49 l.xue.nong$$
 */


package cn.com.rebirth.commons.io.stream;

import java.io.IOException;

import cn.com.rebirth.commons.io.stream.StreamInput;
import cn.com.rebirth.commons.io.stream.StreamOutput;
import cn.com.rebirth.commons.io.stream.Streamable;


/**
 * The Class VoidStreamable.
 *
 * @author l.xue.nong
 */
public class VoidStreamable implements Streamable {

    
    /** The Constant INSTANCE. */
    public static final VoidStreamable INSTANCE = new VoidStreamable();

    
    /* (non-Javadoc)
     * @see cn.com.rebirth.search.commons.io.stream.Streamable#readFrom(cn.com.rebirth.search.commons.io.stream.StreamInput)
     */
    @Override
    public void readFrom(StreamInput in) throws IOException {
    }

    
    /* (non-Javadoc)
     * @see cn.com.rebirth.search.commons.io.stream.Streamable#writeTo(cn.com.rebirth.search.commons.io.stream.StreamOutput)
     */
    @Override
    public void writeTo(StreamOutput out) throws IOException {
    }
}
