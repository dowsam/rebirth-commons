/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons TraceUtils.java 2012-7-6 10:22:12 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.MDC;

/**
 * The Class TraceUtils.
 *
 * @author l.xue.nong
 */
public abstract class TraceUtils {
	
	/** The Constant TRACE_ID_KEY. */
	public static final String TRACE_ID_KEY = "traceId";

	/** The Constant TRACE_ID_LENGTH. */
	public static final int TRACE_ID_LENGTH = 8;

	/**
	 * Begin trace.
	 */
	public static void beginTrace() {
		String traceId = RandomStringUtils.randomAlphanumeric(TRACE_ID_LENGTH);
		MDC.put(TRACE_ID_KEY, traceId);
	}

	/**
	 * Begin trace.
	 *
	 * @param traceId the trace id
	 */
	public static void beginTrace(String traceId) {
		MDC.put(TRACE_ID_KEY, traceId);
	}

	/**
	 * End trace.
	 */
	public static void endTrace() {
		MDC.remove(TRACE_ID_KEY);
	}
}
