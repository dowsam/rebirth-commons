/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-commons TraceUtils.java 2012-2-2 11:01:43 l.xue.nong$$
 */
package cn.com.restart.commons.utils;

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
	 * 开始Trace, 默认生成本次Trace的ID(8字符长)并放入MDC.
	 */
	public static void beginTrace() {
		String traceId = RandomStringUtils.randomAlphanumeric(TRACE_ID_LENGTH);
		MDC.put(TRACE_ID_KEY, traceId);
	}

	/**
	 * 开始Trace, 将traceId放入MDC.
	 *
	 * @param traceId the trace id
	 */
	public static void beginTrace(String traceId) {
		MDC.put(TRACE_ID_KEY, traceId);
	}

	/**
	 * 结束一次Trace.
	 * 清除traceId.
	 */
	public static void endTrace() {
		MDC.remove(TRACE_ID_KEY);
	}
}
