/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:restart-commons FormatConstants.java 2012-7-4 9:39:31 l.xue.nong$$
 */
package cn.com.restart.commons;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * 定义格式化常量的接口语义.
 *
 * @author l.xue.nong
 */
public interface FormatConstants extends Constants {
	/** The Constant DATE_FORMAT. */
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.CHINA);

	/** The Constant TIME_FORMAT. */
	public static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss", java.util.Locale.CHINA);

	/** The Constant DATE_TIME_FORMAT. */
	public static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
			java.util.Locale.CHINA);

	/** The Constant DATE_TIME_FORMAT_IMAGE. */
	public static final DateFormat DATE_TIME_FORMAT_IMAGE = new SimpleDateFormat("yyyyMMddHHmmss",
			java.util.Locale.CHINA);
}
