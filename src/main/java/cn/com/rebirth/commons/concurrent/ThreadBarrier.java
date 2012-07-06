/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons ThreadBarrier.java 2012-3-29 15:15:20 l.xue.nong$$
 */


package cn.com.rebirth.commons.concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * The Class ThreadBarrier.
 *
 * @author l.xue.nong
 */
public class ThreadBarrier extends CyclicBarrier {
	
	/** The cause. */
	private Throwable cause;

	
	/**
	 * Instantiates a new thread barrier.
	 *
	 * @param parties the parties
	 */
	public ThreadBarrier(int parties) {
		super(parties);
	}

	
	/**
	 * Instantiates a new thread barrier.
	 *
	 * @param parties the parties
	 * @param barrierAction the barrier action
	 */
	public ThreadBarrier(int parties, Runnable barrierAction) {
		super(parties, barrierAction);
	}

	
	/* (non-Javadoc)
	 * @see java.util.concurrent.CyclicBarrier#await()
	 */
	@Override
	public int await() throws InterruptedException, BrokenBarrierException {
		try {
			breakIfBroken();
			return super.await();
		} catch (BrokenBarrierException bbe) {
			initCause(bbe);
			throw bbe;
		}
	}

	
	/* (non-Javadoc)
	 * @see java.util.concurrent.CyclicBarrier#await(long, java.util.concurrent.TimeUnit)
	 */
	@Override
	public int await(long timeout, TimeUnit unit) throws InterruptedException, BrokenBarrierException, TimeoutException {
		try {
			breakIfBroken();
			return super.await(timeout, unit);
		} catch (BrokenBarrierException bbe) {
			initCause(bbe);
			throw bbe;
		} catch (TimeoutException te) {
			initCause(te);
			throw te;
		}
	}

	
	/**
	 * Reset.
	 *
	 * @param cause the cause
	 */
	public synchronized void reset(Throwable cause) {
		if (!isBroken()) {
			super.reset();
		}

		if (this.cause == null) {
			this.cause = cause;
		}
	}

	
	/* (non-Javadoc)
	 * @see java.util.concurrent.CyclicBarrier#isBroken()
	 */
	@Override
	public synchronized boolean isBroken() {
		return this.cause != null || super.isBroken();
	}

	
	/**
	 * Inspect.
	 *
	 * @throws BrokenBarrierException the broken barrier exception
	 */
	public synchronized void inspect() throws BrokenBarrierException {
		try {
			breakIfBroken();
		} catch (BrokenBarrierException bbe) {
			initCause(bbe);
			throw bbe;
		}
	}

	
	/**
	 * Break if broken.
	 *
	 * @throws BrokenBarrierException the broken barrier exception
	 */
	private synchronized void breakIfBroken() throws BrokenBarrierException {
		if (isBroken()) {
			throw new BrokenBarrierException();
		}
	}

	
	/**
	 * Inits the cause.
	 *
	 * @param t the t
	 */
	private synchronized void initCause(Throwable t) {
		t.initCause(this.cause);
	}

	
	/**
	 * The Class BarrierTimer.
	 *
	 * @author l.xue.nong
	 */
	public static class BarrierTimer implements Runnable {

		
		/** The started. */
		volatile boolean started;

		
		/** The start time. */
		volatile long startTime;

		
		/** The end time. */
		volatile long endTime;

		
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			long t = System.nanoTime();
			if (!started) {
				started = true;
				startTime = t;
			} else
				endTime = t;
		}

		
		/**
		 * Reset.
		 */
		public void reset() {
			started = false;
		}

		
		/**
		 * Gets the time in nanos.
		 *
		 * @return the time in nanos
		 */
		public long getTimeInNanos() {
			return endTime - startTime;
		}

		
		/**
		 * Gets the time in seconds.
		 *
		 * @return the time in seconds
		 */
		public double getTimeInSeconds() {
			long time = endTime - startTime;
			return (time) / 1000000000.0;
		}
	}
}
