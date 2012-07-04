/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons BaseFuture.java 2012-3-29 15:15:11 l.xue.nong$$
 */


package cn.com.restart.commons.concurrent;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

import cn.com.restart.commons.Nullable;

import com.google.common.annotations.Beta;


/**
 * The Class BaseFuture.
 *
 * @param <V> the value type
 * @author l.xue.nong
 */
public abstract class BaseFuture<V> implements Future<V> {

    
    /** The sync. */
    private final Sync<V> sync = new Sync<V>();

    

    
    /* (non-Javadoc)
     * @see java.util.concurrent.Future#get(long, java.util.concurrent.TimeUnit)
     */
    @Override
    public V get(long timeout, TimeUnit unit) throws InterruptedException,
            TimeoutException, ExecutionException {
        return sync.get(unit.toNanos(timeout));
    }

    

    
    /* (non-Javadoc)
     * @see java.util.concurrent.Future#get()
     */
    @Override
    public V get() throws InterruptedException, ExecutionException {
        return sync.get();
    }

    
    /* (non-Javadoc)
     * @see java.util.concurrent.Future#isDone()
     */
    @Override
    public boolean isDone() {
        return sync.isDone();
    }

    
    /* (non-Javadoc)
     * @see java.util.concurrent.Future#isCancelled()
     */
    @Override
    public boolean isCancelled() {
        return sync.isCancelled();
    }

    
    /* (non-Javadoc)
     * @see java.util.concurrent.Future#cancel(boolean)
     */
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (!sync.cancel()) {
            return false;
        }
        done();
        if (mayInterruptIfRunning) {
            interruptTask();
        }
        return true;
    }

    
    /**
     * Interrupt task.
     */
    protected void interruptTask() {
    }

    
    /**
     * Sets the.
     *
     * @param value the value
     * @return true, if successful
     */
    protected boolean set(@Nullable V value) {
        boolean result = sync.set(value);
        if (result) {
            done();
        }
        return result;
    }

    
    /**
     * Sets the exception.
     *
     * @param throwable the throwable
     * @return true, if successful
     */
    protected boolean setException(Throwable throwable) {
        boolean result = sync.setException(checkNotNull(throwable));
        if (result) {
            done();
        }

        
        
        if (throwable instanceof Error) {
            throw (Error) throwable;
        }
        return result;
    }

    
    /**
     * Done.
     */
    @Beta
    protected void done() {
    }

    
    /**
     * The Class Sync.
     *
     * @param <V> the value type
     * @author l.xue.nong
     */
    static final class Sync<V> extends AbstractQueuedSynchronizer {

        
        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 0L;

        
        
        /** The Constant RUNNING. */
        static final int RUNNING = 0;
        
        
        /** The Constant COMPLETING. */
        static final int COMPLETING = 1;
        
        
        /** The Constant COMPLETED. */
        static final int COMPLETED = 2;
        
        
        /** The Constant CANCELLED. */
        static final int CANCELLED = 4;

        
        /** The value. */
        private V value;
        
        
        /** The exception. */
        private Throwable exception;

        
        
        /* (non-Javadoc)
         * @see java.util.concurrent.locks.AbstractQueuedSynchronizer#tryAcquireShared(int)
         */
        @Override
        protected int tryAcquireShared(int ignored) {
            if (isDone()) {
                return 1;
            }
            return -1;
        }

        
        
        /* (non-Javadoc)
         * @see java.util.concurrent.locks.AbstractQueuedSynchronizer#tryReleaseShared(int)
         */
        @Override
        protected boolean tryReleaseShared(int finalState) {
            setState(finalState);
            return true;
        }

        
        /**
         * Gets the.
         *
         * @param nanos the nanos
         * @return the v
         * @throws TimeoutException the timeout exception
         * @throws CancellationException the cancellation exception
         * @throws ExecutionException the execution exception
         * @throws InterruptedException the interrupted exception
         */
        V get(long nanos) throws TimeoutException, CancellationException,
                ExecutionException, InterruptedException {

            
            if (!tryAcquireSharedNanos(-1, nanos)) {
                throw new TimeoutException("Timeout waiting for task.");
            }

            return getValue();
        }

        
        /**
         * Gets the.
         *
         * @return the v
         * @throws CancellationException the cancellation exception
         * @throws ExecutionException the execution exception
         * @throws InterruptedException the interrupted exception
         */
        V get() throws CancellationException, ExecutionException,
                InterruptedException {

            
            acquireSharedInterruptibly(-1);
            return getValue();
        }

        
        /**
         * Gets the value.
         *
         * @return the value
         * @throws CancellationException the cancellation exception
         * @throws ExecutionException the execution exception
         */
        private V getValue() throws CancellationException, ExecutionException {
            int state = getState();
            switch (state) {
                case COMPLETED:
                    if (exception != null) {
                        throw new ExecutionException(exception);
                    } else {
                        return value;
                    }

                case CANCELLED:
                    throw new CancellationException("Task was cancelled.");

                default:
                    throw new IllegalStateException(
                            "Error, synchronizer in invalid state: " + state);
            }
        }

        
        /**
         * Checks if is done.
         *
         * @return true, if is done
         */
        boolean isDone() {
            return (getState() & (COMPLETED | CANCELLED)) != 0;
        }

        
        /**
         * Checks if is cancelled.
         *
         * @return true, if is cancelled
         */
        boolean isCancelled() {
            return getState() == CANCELLED;
        }

        
        /**
         * Sets the.
         *
         * @param v the v
         * @return true, if successful
         */
        boolean set(@Nullable V v) {
            return complete(v, null, COMPLETED);
        }

        
        /**
         * Sets the exception.
         *
         * @param t the t
         * @return true, if successful
         */
        boolean setException(Throwable t) {
            return complete(null, t, COMPLETED);
        }

        
        /**
         * Cancel.
         *
         * @return true, if successful
         */
        boolean cancel() {
            return complete(null, null, CANCELLED);
        }

        
        /**
         * Complete.
         *
         * @param v the v
         * @param t the t
         * @param finalState the final state
         * @return true, if successful
         */
        private boolean complete(@Nullable V v, Throwable t, int finalState) {
            if (compareAndSetState(RUNNING, COMPLETING)) {
                this.value = v;
                this.exception = t;
                releaseShared(finalState);
                return true;
            }

            
            return false;
        }
    }
}
