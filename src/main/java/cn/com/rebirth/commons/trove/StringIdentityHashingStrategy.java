/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons StringIdentityHashingStrategy.java 2012-7-6 10:22:15 l.xue.nong$$
 */
package cn.com.rebirth.commons.trove;

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