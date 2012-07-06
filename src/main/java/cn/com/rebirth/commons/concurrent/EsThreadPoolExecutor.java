/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons EsThreadPoolExecutor.java 2012-7-6 10:22:13 l.xue.nong$$
 */


package cn.com.rebirth.commons.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * The Class EsThreadPoolExecutor.
 *
 * @author l.xue.nong
 */
public class EsThreadPoolExecutor extends ThreadPoolExecutor {

    
    /**
     * Instantiates a new es thread pool executor.
     *
     * @param corePoolSize the core pool size
     * @param maximumPoolSize the maximum pool size
     * @param keepAliveTime the keep alive time
     * @param unit the unit
     * @param workQueue the work queue
     */
    public EsThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    
    /**
     * Instantiates a new es thread pool executor.
     *
     * @param corePoolSize the core pool size
     * @param maximumPoolSize the maximum pool size
     * @param keepAliveTime the keep alive time
     * @param unit the unit
     * @param workQueue the work queue
     * @param threadFactory the thread factory
     */
    public EsThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    
    /**
     * Instantiates a new es thread pool executor.
     *
     * @param corePoolSize the core pool size
     * @param maximumPoolSize the maximum pool size
     * @param keepAliveTime the keep alive time
     * @param unit the unit
     * @param workQueue the work queue
     * @param handler the handler
     */
    public EsThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    
    /**
     * Instantiates a new es thread pool executor.
     *
     * @param corePoolSize the core pool size
     * @param maximumPoolSize the maximum pool size
     * @param keepAliveTime the keep alive time
     * @param unit the unit
     * @param workQueue the work queue
     * @param threadFactory the thread factory
     * @param handler the handler
     */
    public EsThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }
}
