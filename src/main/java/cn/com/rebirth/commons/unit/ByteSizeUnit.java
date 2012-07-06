/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons ByteSizeUnit.java 2012-3-29 15:15:08 l.xue.nong$$
 */


package cn.com.rebirth.commons.unit;


/**
 * The Enum ByteSizeUnit.
 *
 * @author l.xue.nong
 */
public enum ByteSizeUnit {
    
    
    /** The BYTES. */
    BYTES {
        @Override
        public long toBytes(long size) {
            return size;
        }

        @Override
        public long toKB(long size) {
            return size / (C1 / C0);
        }

        @Override
        public long toMB(long size) {
            return size / (C2 / C0);
        }

        @Override
        public long toGB(long size) {
            return size / (C3 / C0);
        }
    },
    
    
    /** The KB. */
    KB {
        @Override
        public long toBytes(long size) {
            return x(size, C1 / C0, MAX / (C1 / C0));
        }

        @Override
        public long toKB(long size) {
            return size;
        }

        @Override
        public long toMB(long size) {
            return size / (C2 / C1);
        }

        @Override
        public long toGB(long size) {
            return size / (C3 / C1);
        }
    },
    
    
    /** The MB. */
    MB {
        @Override
        public long toBytes(long size) {
            return x(size, C2 / C0, MAX / (C2 / C0));
        }

        @Override
        public long toKB(long size) {
            return x(size, C2 / C1, MAX / (C2 / C1));
        }

        @Override
        public long toMB(long size) {
            return size;
        }

        @Override
        public long toGB(long size) {
            return size / (C3 / C2);
        }
    },
    
    
    /** The GB. */
    GB {
        @Override
        public long toBytes(long size) {
            return x(size, C3 / C0, MAX / (C3 / C0));
        }

        @Override
        public long toKB(long size) {
            return x(size, C3 / C1, MAX / (C3 / C1));
        }

        @Override
        public long toMB(long size) {
            return x(size, C3 / C2, MAX / (C3 / C2));
        }

        @Override
        public long toGB(long size) {
            return size;
        }
    };

    
    /** The Constant C0. */
    static final long C0 = 1L;
    
    
    /** The Constant C1. */
    static final long C1 = C0 * 1024L;
    
    
    /** The Constant C2. */
    static final long C2 = C1 * 1024L;
    
    
    /** The Constant C3. */
    static final long C3 = C2 * 1024L;

    
    /** The Constant MAX. */
    static final long MAX = Long.MAX_VALUE;

    
    /**
     * X.
     *
     * @param d the d
     * @param m the m
     * @param over the over
     * @return the long
     */
    static long x(long d, long m, long over) {
        if (d > over) return Long.MAX_VALUE;
        if (d < -over) return Long.MIN_VALUE;
        return d * m;
    }


    
    /**
     * To bytes.
     *
     * @param size the size
     * @return the long
     */
    public long toBytes(long size) {
        throw new AbstractMethodError();
    }

    
    /**
     * To kb.
     *
     * @param size the size
     * @return the long
     */
    public long toKB(long size) {
        throw new AbstractMethodError();
    }

    
    /**
     * To mb.
     *
     * @param size the size
     * @return the long
     */
    public long toMB(long size) {
        throw new AbstractMethodError();
    }

    
    /**
     * To gb.
     *
     * @param size the size
     * @return the long
     */
    public long toGB(long size) {
        throw new AbstractMethodError();
    }
}
