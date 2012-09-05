/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons Lifecycle.java 2012-7-17 10:45:15 l.xue.nong$$
 */
package cn.com.rebirth.commons.component;

import cn.com.rebirth.commons.exception.RebirthIllegalStateException;


/**
 * The Class Lifecycle.
 *
 * @author l.xue.nong
 */
public class Lifecycle {

	
	/**
	 * The Enum State.
	 *
	 * @author l.xue.nong
	 */
	public static enum State {

		
		/** The initialized. */
		INITIALIZED,
		
		/** The stopped. */
		STOPPED,
		
		/** The started. */
		STARTED,
		
		/** The closed. */
		CLOSED
	}

	
	/** The state. */
	private volatile State state = State.INITIALIZED;

	
	/**
	 * State.
	 *
	 * @return the state
	 */
	public State state() {
		return this.state;
	}

	
	/**
	 * Initialized.
	 *
	 * @return true, if successful
	 */
	public boolean initialized() {
		return state == State.INITIALIZED;
	}

	
	/**
	 * Started.
	 *
	 * @return true, if successful
	 */
	public boolean started() {
		return state == State.STARTED;
	}

	
	/**
	 * Stopped.
	 *
	 * @return true, if successful
	 */
	public boolean stopped() {
		return state == State.STOPPED;
	}

	
	/**
	 * Closed.
	 *
	 * @return true, if successful
	 */
	public boolean closed() {
		return state == State.CLOSED;
	}

	
	/**
	 * Stopped or closed.
	 *
	 * @return true, if successful
	 */
	public boolean stoppedOrClosed() {
		Lifecycle.State state = this.state;
		return state == State.STOPPED || state == State.CLOSED;
	}

	
	/**
	 * Can move to started.
	 *
	 * @return true, if successful
	 * @throws RebirthIllegalStateException the rebirth illegal state exception
	 */
	public boolean canMoveToStarted() throws RebirthIllegalStateException {
		State localState = this.state;
		if (localState == State.INITIALIZED || localState == State.STOPPED) {
			return true;
		}
		if (localState == State.STARTED) {
			return false;
		}
		if (localState == State.CLOSED) {
			throw new RebirthIllegalStateException("Can't move to started state when closed");
		}
		throw new RebirthIllegalStateException("Can't move to started with unknown state");
	}

	
	/**
	 * Move to started.
	 *
	 * @return true, if successful
	 * @throws RebirthIllegalStateException the rebirth illegal state exception
	 */
	public boolean moveToStarted() throws RebirthIllegalStateException {
		State localState = this.state;
		if (localState == State.INITIALIZED || localState == State.STOPPED) {
			state = State.STARTED;
			return true;
		}
		if (localState == State.STARTED) {
			return false;
		}
		if (localState == State.CLOSED) {
			throw new RebirthIllegalStateException("Can't move to started state when closed");
		}
		throw new RebirthIllegalStateException("Can't move to started with unknown state");
	}

	
	/**
	 * Can move to stopped.
	 *
	 * @return true, if successful
	 * @throws RebirthIllegalStateException the rebirth illegal state exception
	 */
	public boolean canMoveToStopped() throws RebirthIllegalStateException {
		State localState = state;
		if (localState == State.STARTED) {
			return true;
		}
		if (localState == State.INITIALIZED || localState == State.STOPPED) {
			return false;
		}
		if (localState == State.CLOSED) {
			throw new RebirthIllegalStateException("Can't move to started state when closed");
		}
		throw new RebirthIllegalStateException("Can't move to started with unknown state");
	}

	
	/**
	 * Move to stopped.
	 *
	 * @return true, if successful
	 * @throws RebirthIllegalStateException the rebirth illegal state exception
	 */
	public boolean moveToStopped() throws RebirthIllegalStateException {
		State localState = state;
		if (localState == State.STARTED) {
			state = State.STOPPED;
			return true;
		}
		if (localState == State.INITIALIZED || localState == State.STOPPED) {
			return false;
		}
		if (localState == State.CLOSED) {
			throw new RebirthIllegalStateException("Can't move to started state when closed");
		}
		throw new RebirthIllegalStateException("Can't move to started with unknown state");
	}

	
	/**
	 * Can move to closed.
	 *
	 * @return true, if successful
	 * @throws RebirthIllegalStateException the rebirth illegal state exception
	 */
	public boolean canMoveToClosed() throws RebirthIllegalStateException {
		State localState = state;
		if (localState == State.CLOSED) {
			return false;
		}
		if (localState == State.STARTED) {
			throw new RebirthIllegalStateException("Can't move to closed before moving to stopped mode");
		}
		return true;
	}

	
	/**
	 * Move to closed.
	 *
	 * @return true, if successful
	 * @throws RebirthIllegalStateException the rebirth illegal state exception
	 */
	public boolean moveToClosed() throws RebirthIllegalStateException {
		State localState = state;
		if (localState == State.CLOSED) {
			return false;
		}
		if (localState == State.STARTED) {
			throw new RebirthIllegalStateException("Can't move to closed before moving to stopped mode");
		}
		state = State.CLOSED;
		return true;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return state.toString();
	}
}
