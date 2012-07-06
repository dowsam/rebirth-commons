/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:restart-commons FieldBoost.java 2012-7-4 11:43:31 l.xue.nong$$
 */
package cn.com.rebirth.commons.search.rule;

import cn.com.rebirth.commons.entity.BaseEntity;

/**
 * The Interface FieldBoost.
 *
 * @param <T> the generic type
 * @author l.xue.nong
 */
public interface FieldBoost<T extends BaseEntity> extends LuceneBoost {
	
	/**
	 * Calculate field boost.
	 *
	 * @param entity the entity
	 * @param fieldName the field name
	 * @return the float
	 * @throws Exception the exception
	 */
	public float calculateFieldBoost(T entity, String fieldName) throws Exception;
}
