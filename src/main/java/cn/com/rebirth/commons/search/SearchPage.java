/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons SearchPage.java 2012-7-6 10:22:15 l.xue.nong$$
 */
package cn.com.rebirth.commons.search;

import cn.com.rebirth.commons.Page;

/**
 * The Class SearchPage.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public class SearchPage<T> extends Page<T> implements Iterable<T> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -825460407018558440L;
	
	/** The debug msg. */
	protected String debugMsg;

	/**
	 * Instantiates a new search page.
	 */
	public SearchPage() {
		super();
	}

	/**
	 * Instantiates a new search page.
	 *
	 * @param request the request
	 */
	public SearchPage(SearchPageRequest request) {
		super(request);
	}

	/**
	 * Gets the debug msg.
	 *
	 * @return the debug msg
	 */
	public String getDebugMsg() {
		return debugMsg;
	}

	/**
	 * Sets the debug msg.
	 *
	 * @param debugMsg the new debug msg
	 */
	public void setDebugMsg(String debugMsg) {
		this.debugMsg = debugMsg;
	}

}
