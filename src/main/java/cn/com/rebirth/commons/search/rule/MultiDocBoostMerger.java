/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons MultiDocBoostMerger.java 2012-7-6 15:57:23 l.xue.nong$$
 */
package cn.com.rebirth.commons.search.rule;

/**
 * The Interface MultiDocBoostMerger.
 *
 * @author l.xue.nong
 */
public interface MultiDocBoostMerger extends LuceneBoost {

	/**
	 * Merger.
	 *
	 * @param fs the fs
	 * @return the float
	 */
	public float merger(Float... fs);
}
