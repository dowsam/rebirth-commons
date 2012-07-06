/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons DocumentBoost.java 2012-7-6 10:22:17 l.xue.nong$$
 */
package cn.com.rebirth.commons.search.rule;

import cn.com.rebirth.commons.entity.BaseEntity;

/**
 * The Interface DocumentBoost.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public interface DocumentBoost<T extends BaseEntity> extends LuceneBoost {

	/**
	 * Calculate boost.
	 *
	 * @param entity the entity
	 * @return the float
	 * @throws Exception the exception
	 */
	public float calculateBoost(T entity) throws Exception;
}
