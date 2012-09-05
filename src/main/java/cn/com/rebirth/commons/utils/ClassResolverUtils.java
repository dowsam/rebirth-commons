/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons ClassResolverUtils.java 2012-7-6 10:22:12 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.reflect.ConstructorUtils;

import cn.com.rebirth.commons.exception.RebirthException;
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
	 * Find.
	 *
	 * @param <T> the generic type
	 * @param findCallback the find callback
	 * @return the list
	 */
	public static <T> List<T> find(FindCallback<T> findCallback) {
		List<T> list = Lists.newArrayList();
		ResolverUtils<T> resolverUtils = new ResolverUtils<T>();
		findCallback.findType(resolverUtils);
		Set<Class<? extends T>> classes = resolverUtils.getClasses();
		for (Class<? extends T> class1 : classes) {
			int mod = class1.getModifiers();
			if (findCallback.needFor(mod)) {
				try {
					T t = findCallback.constructor(class1);
					list.add(t);
				} catch (Exception e) {
					//Slightly
				}
			}
		}
		return list;
	}

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
		List<T> d = (List<T>) findImpl(classes).get(entityClass);
		if (d == null)
			return Lists.newArrayList();
		return d;
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
		ResolverUtils resolverUtils = new ResolverUtils();
		for (Class<?> class1 : classes) {
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

	/**
	 * The Interface FindCallback.
	 *
	 * @param <T> the generic type
	 * @author l.xue.nong
	 */
	public static interface FindCallback<T> {

		/**
		 * Find type.
		 *
		 * @param resolverUtils the resolver utils
		 */
		void findType(ResolverUtils<T> resolverUtils);

		/**
		 * Need for.
		 *
		 * @param mod the mod
		 * @return true, if successful
		 */
		boolean needFor(int mod);

		/**
		 * Constructor.
		 *
		 * @param entityClass the entity class
		 * @return the t
		 * @throws RebirthException the rebirth exception
		 */
		T constructor(Class<?> entityClass) throws RebirthException;
	}

	/**
	 * The Class AbstractFindCallback.
	 *
	 * @param <T> the generic type
	 * @author l.xue.nong
	 */
	public static abstract class AbstractFindCallback<T> implements FindCallback<T> {

		/** The entity class. */
		protected Class<T> entityClass;

		/**
		 * Instantiates a new abstract find callback.
		 */
		public AbstractFindCallback() {
			super();
			this.entityClass = ReflectionUtils.getSuperClassGenricType(getClass());
		}

		/**
		 * Instantiates a new abstract find callback.
		 *
		 * @param entityClass the entity class
		 */
		public AbstractFindCallback(Class<T> entityClass) {
			super();
			this.entityClass = entityClass;
		}

		/* (non-Javadoc)
		 * @see cn.com.rebirth.commons.utils.ClassResolverUtils.FindCallback#findType(cn.com.rebirth.commons.utils.ResolverUtils)
		 */
		@Override
		public void findType(ResolverUtils<T> resolverUtils) {
			doFindType(resolverUtils, entityClass);
		}

		/**
		 * Do find type.
		 *
		 * @param resolverUtils the resolver utils
		 * @param entityClass the entity class
		 */
		protected abstract void doFindType(ResolverUtils<T> resolverUtils, Class<T> entityClass);

		/* (non-Javadoc)
		 * @see cn.com.rebirth.commons.utils.ClassResolverUtils.FindCallback#needFor(int)
		 */
		@Override
		public boolean needFor(int mod) {
			return (!Modifier.isAbstract(mod) && !Modifier.isInterface(mod) && Modifier.isPublic(mod));
		}

		/* (non-Javadoc)
		 * @see cn.com.rebirth.commons.utils.ClassResolverUtils.FindCallback#constructor(java.lang.Class)
		 */
		@SuppressWarnings("unchecked")
		@Override
		public T constructor(Class<?> entityClass) throws RebirthException {
			try {
				Constructor<? extends T> ctor = (Constructor<? extends T>) entityClass.getDeclaredConstructor();
				if ((!Modifier.isPublic(ctor.getModifiers()) || !Modifier.isPublic(ctor.getDeclaringClass()
						.getModifiers())) && !ctor.isAccessible()) {
					ctor.setAccessible(true);
				}
				return ctor.newInstance();
			} catch (Exception e) {
				throw new RebirthException(e.getMessage(), e);
			}
		}

	}

}
