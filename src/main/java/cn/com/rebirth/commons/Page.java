/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons Page.java 2012-7-6 10:22:16 l.xue.nong$$
 */
package cn.com.rebirth.commons;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * The Class Page.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public class Page<T> extends PageRequest implements Iterable<T> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3504972339739650008L;

	/** The result. */
	protected List<T> result = null;

	/** The total items. */
	protected long totalItems = -1;

	/**
	 * Instantiates a new page.
	 */
	public Page() {
	}

	/**
	 * Instantiates a new page.
	 *
	 * @param request the request
	 */
	public Page(PageRequest request) {
		this.pageNo = request.pageNo;
		this.pageSize = request.pageSize;
		this.countTotal = request.countTotal;
		this.sort = request.sort;
	}

	/**
	 * Gets the result.
	 *
	 * @return the result
	 */
	public List<T> getResult() {
		return result;
	}

	/**
	 * Sets the result.
	 *
	 * @param result the new result
	 */
	public void setResult(final List<T> result) {
		this.result = result;
	}

	/**
	 * Gets the total items.
	 *
	 * @return the total items
	 */
	public long getTotalItems() {
		return totalItems;
	}

	/**
	 * Sets the total items.
	 *
	 * @param totalItems the new total items
	 */
	public void setTotalItems(final long totalItems) {
		this.totalItems = totalItems;
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		return result.iterator();
	}

	/**
	 * Gets the total pages.
	 *
	 * @return the total pages
	 */
	public int getTotalPages() {
		return (int) Math.ceil((double) totalItems / (double) getPageSize());
	}

	/**
	 * Checks for next page.
	 *
	 * @return true, if successful
	 */
	public boolean hasNextPage() {
		return (getPageNo() + 1 <= getTotalPages());
	}

	/**
	 * Checks if is last page.
	 *
	 * @return true, if is last page
	 */
	public boolean isLastPage() {
		return !hasNextPage();
	}

	/**
	 * Gets the next page.
	 *
	 * @return the next page
	 */
	public int getNextPage() {
		if (hasNextPage()) {
			return getPageNo() + 1;
		} else {
			return getPageNo();
		}
	}

	/**
	 * Checks for pre page.
	 *
	 * @return true, if successful
	 */
	public boolean hasPrePage() {
		return (getPageNo() > 1);
	}

	/**
	 * Checks if is first page.
	 *
	 * @return true, if is first page
	 */
	public boolean isFirstPage() {
		return !hasPrePage();
	}

	/**
	 * Gets the pre page.
	 *
	 * @return the pre page
	 */
	public int getPrePage() {
		if (hasPrePage()) {
			return getPageNo() - 1;
		} else {
			return getPageNo();
		}
	}

	/**
	 * Gets the slider.
	 *
	 * @param count the count
	 * @return the slider
	 */
	public List<Integer> getSlider(int count) {
		int halfSize = count / 2;
		int totalPage = getTotalPages();
		int startPageNo = Math.max(getPageNo() - halfSize, 1);
		int endPageNo = Math.min(startPageNo + count - 1, totalPage);
		if (endPageNo - startPageNo < count) {
			startPageNo = Math.max(endPageNo - count, 1);
		}
		List<Integer> result = Lists.newArrayList();
		for (int i = startPageNo; i <= endPageNo; i++) {
			result.add(i);
		}
		return result;
	}
}
