/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:restart-commons SearchPageRequest.java 2012-7-4 10:15:57 l.xue.nong$$
 */
package cn.com.restart.commons.search;

import org.apache.lucene.search.Similarity;

import cn.com.restart.commons.PageRequest;

/**
 * The Class SearchPageRequest.
 *
 * @author l.xue.nong
 */
public class SearchPageRequest extends PageRequest {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2521900772005207084L;
	/** The similarity. */
	protected Similarity similarity;

	/** The search debug. */
	protected boolean searchDebug = false;

	/**
	 * Instantiates a new search page request.
	 */
	public SearchPageRequest() {
		super();
	}

	/**
	 * Instantiates a new search page request.
	 *
	 * @param pageNo the page no
	 * @param pageSize the page size
	 */
	public SearchPageRequest(int pageNo, int pageSize) {
		super(pageNo, pageSize);
	}

	/**
	 * The Class SearchOrder.
	 *
	 * @author l.xue.nong
	 */
	public static class SearchOrder extends Order {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 5739825660411325675L;

		/** The sort type. */
		private final SortType sortType;

		/**
		 * Instantiates a new search order.
		 *
		 * @param sortType the sort type
		 */
		public SearchOrder(SortType sortType) {
			super();
			this.sortType = sortType;
		}

		/**
		 * Instantiates a new search order.
		 *
		 * @param direction the direction
		 * @param property the property
		 * @param sortType the sort type
		 */
		public SearchOrder(Direction direction, String property, SortType sortType) {
			super(direction, property);
			this.sortType = sortType;
		}

		/**
		 * Instantiates a new search order.
		 *
		 * @param property the property
		 * @param sortType the sort type
		 */
		public SearchOrder(String property, SortType sortType) {
			super(property);
			this.sortType = sortType;
		}

		/**
		 * Gets the sort type.
		 *
		 * @return the sort type
		 */
		public SortType getSortType() {
			return sortType;
		}

	}

	/**
	 * Gets the similarity.
	 *
	 * @return the similarity
	 */
	public Similarity getSimilarity() {
		return similarity;
	}

	/**
	 * Sets the similarity.
	 *
	 * @param similarity the new similarity
	 */
	public void setSimilarity(Similarity similarity) {
		this.similarity = similarity;
	}

	/**
	 * Checks if is search debug.
	 *
	 * @return true, if is search debug
	 */
	public boolean isSearchDebug() {
		return searchDebug;
	}

	/**
	 * Sets the search debug.
	 *
	 * @param searchDebug the new search debug
	 */
	public void setSearchDebug(boolean searchDebug) {
		this.searchDebug = searchDebug;
	}

}
