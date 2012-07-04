/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-commons DocumentBoost.java 2012-3-13 12:37:13 l.xue.nong$$
 */
package cn.com.restart.commons.search.rule;

import cn.com.restart.commons.entity.BaseEntity;

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
