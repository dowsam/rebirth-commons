/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons rebirthException.java 2012-7-6 10:22:15 l.xue.nong$$
 */

package cn.com.rebirth.commons.exception;


/**
 * The Class rebirthException.
 *
 * @author l.xue.nong
 */
public class RebirthException extends RuntimeException {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2554283892764868472L;

	
	/**
	 * Instantiates a new rebirth exception.
	 *
	 * @param msg the msg
	 */
	public RebirthException(String msg) {
		super(msg);
	}

	
	/**
	 * Instantiates a new rebirth exception.
	 *
	 * @param msg the msg
	 * @param cause the cause
	 */
	public RebirthException(String msg, Throwable cause) {
		super(msg, cause);
	}

	
	/**
	 * Unwrap cause.
	 *
	 * @return the throwable
	 */
	public Throwable unwrapCause() {
		return ExceptionsHelper.unwrapCause(this);
	}

	
	/**
	 * Gets the detailed message.
	 *
	 * @return the detailed message
	 */
	public String getDetailedMessage() {
		if (getCause() != null) {
			StringBuilder sb = new StringBuilder();
			sb.append(toString()).append("; ");
			if (getCause() instanceof RebirthException) {
				sb.append(((RebirthException) getCause()).getDetailedMessage());
			} else {
				sb.append(getCause());
			}
			return sb.toString();
		} else {
			return super.toString();
		}
	}

	
	/**
	 * Gets the root cause.
	 *
	 * @return the root cause
	 */
	public Throwable getRootCause() {
		Throwable rootCause = this;
		Throwable cause = getCause();
		while (cause != null && cause != rootCause) {
			rootCause = cause;
			cause = cause.getCause();
		}
		return rootCause;
	}

	
	/**
	 * Gets the most specific cause.
	 *
	 * @return the most specific cause
	 */
	public Throwable getMostSpecificCause() {
		Throwable rootCause = getRootCause();
		return (rootCause != null ? rootCause : this);
	}

	
	/**
	 * Contains.
	 *
	 * @param exType the ex type
	 * @return true, if successful
	 */
	public boolean contains(Class<?> exType) {
		if (exType == null) {
			return false;
		}
		if (exType.isInstance(this)) {
			return true;
		}
		Throwable cause = getCause();
		if (cause == this) {
			return false;
		}
		if (cause instanceof RebirthException) {
			return ((RebirthException) cause).contains(exType);
		} else {
			while (cause != null) {
				if (exType.isInstance(cause)) {
					return true;
				}
				if (cause.getCause() == cause) {
					break;
				}
				cause = cause.getCause();
			}
			return false;
		}
	}
}
