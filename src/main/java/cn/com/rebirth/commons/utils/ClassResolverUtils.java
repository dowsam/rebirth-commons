/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-commons ClassResolverUtils.java 2012-3-13 12:56:03 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.reflect.ConstructorUtils;

import cn.com.rebirth.commons.search.SearchConstants;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * The Class ClassResolverUtils.
 *
 * @author l.xue.nong
 */
public abstract class ClassResolverUtils {

	/**
	 * Find impl.
	 *
	 * @param <T> the generic type
	 * @param entityClass the entity class
	 * @return the list
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> findImpl(Class<T> entityClass) {
		Class<T>[] classes = new Class[] { entityClass };
		return (List<T>) findImpl(classes).get(entityClass);
	}

	/**
	 * Find impl.
	 *
	 * @param classes the classes
	 * @return the map
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<Class<?>, List<Object>> findImpl(Class<?>... classes) {
		Map<Class<?>, List<Object>> map = Maps.newHashMap();
		if (classes == null)
			return map;
		for (Class<?> class1 : classes) {
			ResolverUtils resolverUtils = new ResolverUtils();
			resolverUtils.findImplementations(class1, SearchConstants.PACKAGE_NAME_FIX);
			Set<Class<?>> set = resolverUtils.getClasses();
			if (set != null) {
				for (Class<?> class2 : set) {
					int mod = class2.getModifiers();
					if (!Modifier.isAbstract(mod) && !Modifier.isInterface(mod) && Modifier.isPublic(mod)) {
						try {
							Object t = ConstructorUtils.invokeConstructor(class2);
							List<Object> list = map.get(class1);
							if (list == null)
								list = Lists.newArrayList();
							list.add(t);
							map.put(class1, list);
						} catch (Exception e) {
							//Slightly
						}
					}
				}
			}
		}
		return map;
	}
}
