/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-search-commons StringIdentityHashingStrategy.java 2012-3-29 15:15:20 l.xue.nong$$
 */
package cn.com.restart.commons.trove;

import gnu.trove.strategy.HashingStrategy;


/**
 * The Class StringIdentityHashingStrategy.
 *
 * @author l.xue.nong
 */
public class StringIdentityHashingStrategy implements HashingStrategy<String> {

	
	/** The Constant serialVersionUID. */
	static final long serialVersionUID = -5188534454583764905L;

	
	/* (non-Javadoc)
	 * @see gnu.trove.strategy.HashingStrategy#computeHashCode(java.lang.Object)
	 */
	public int computeHashCode(String object) {
		return object.hashCode();
	}

	
	/* (non-Javadoc)
	 * @see gnu.trove.strategy.HashingStrategy#equals(java.lang.Object, java.lang.Object)
	 */
	public boolean equals(String o1, String o2) {
		return o1 == o2;
	}
}