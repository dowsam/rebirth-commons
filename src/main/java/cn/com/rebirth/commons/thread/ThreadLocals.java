/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons ThreadLocals.java 2012-7-6 10:22:16 l.xue.nong$$
 */

package cn.com.rebirth.commons.thread;

import java.lang.ref.Reference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class ThreadLocals.
 *
 * @author l.xue.nong
 */
public class ThreadLocals {

	
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(ThreadLocals.class);

	
	/**
	 * The Class CleanableValue.
	 *
	 * @param <T> the generic type
	 * @author l.xue.nong
	 */
	public static class CleanableValue<T> {

		
		/** The value. */
		private T value;

		
		/**
		 * Instantiates a new cleanable value.
		 *
		 * @param value the value
		 */
		public CleanableValue(T value) {
			this.value = value;
		}

		
		/**
		 * Gets the.
		 *
		 * @return the t
		 */
		public T get() {
			return value;
		}

		
		/**
		 * Sets the.
		 *
		 * @param value the value
		 */
		public void set(T value) {
			this.value = value;
		}
	}

	
	/**
	 * Clear references thread locals.
	 */
	public static void clearReferencesThreadLocals() {
		try {
			Thread[] threads = getThreads();
			
			
			Field threadLocalsField = Thread.class.getDeclaredField("threadLocals");
			threadLocalsField.setAccessible(true);
			Field inheritableThreadLocalsField = Thread.class.getDeclaredField("inheritableThreadLocals");
			inheritableThreadLocalsField.setAccessible(true);
			
			
			Class<?> tlmClass = Class.forName("java.lang.ThreadLocal$ThreadLocalMap");
			Field tableField = tlmClass.getDeclaredField("table");
			tableField.setAccessible(true);

			for (int i = 0; i < threads.length; i++) {
				Object threadLocalMap;
				if (threads[i] != null) {
					
					threadLocalMap = threadLocalsField.get(threads[i]);
					clearThreadLocalMap(threadLocalMap, tableField);
					
					threadLocalMap = inheritableThreadLocalsField.get(threads[i]);
					clearThreadLocalMap(threadLocalMap, tableField);
				}
			}
		} catch (Exception e) {
			logger.warn("Failed to clean thread locals", e);
		}
	}

	

	
	/**
	 * Clear thread local map.
	 *
	 * @param map the map
	 * @param internalTableField the internal table field
	 * @throws NoSuchMethodException the no such method exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws NoSuchFieldException the no such field exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	private static void clearThreadLocalMap(Object map, Field internalTableField) throws NoSuchMethodException,
			IllegalAccessException, NoSuchFieldException, InvocationTargetException {
		if (map != null) {
			Method mapRemove = map.getClass().getDeclaredMethod("remove", ThreadLocal.class);
			mapRemove.setAccessible(true);
			Object[] table = (Object[]) internalTableField.get(map);
			int staleEntriesCount = 0;
			if (table != null) {
				for (int j = 0; j < table.length; j++) {
					if (table[j] != null) {
						boolean remove = false;
						
						Object key = ((Reference<?>) table[j]).get();
						
						Field valueField = table[j].getClass().getDeclaredField("value");
						valueField.setAccessible(true);
						Object value = valueField.get(table[j]);
						if ((value != null && CleanableValue.class.isAssignableFrom(value.getClass()))) {
							remove = true;
						}
						if (remove) {
							Object[] args = new Object[4];
							if (key != null) {
								args[0] = key.getClass().getCanonicalName();
								args[1] = key.toString();
							}
							args[2] = value.getClass().getCanonicalName();
							args[3] = value.toString();
							if (logger.isTraceEnabled()) {
								logger.trace(
										"ThreadLocal with key of type [{}] (value [{}]) and a value of type [{}] (value [{}]):  The ThreadLocal has been forcibly removed.",
										args);
							}
							if (key == null) {
								staleEntriesCount++;
							} else {
								mapRemove.invoke(map, key);
							}
						}
					}
				}
			}
			if (staleEntriesCount > 0) {
				Method mapRemoveStale = map.getClass().getDeclaredMethod("expungeStaleEntries");
				mapRemoveStale.setAccessible(true);
				mapRemoveStale.invoke(map);
			}
		}
	}

	

	
	/**
	 * Gets the threads.
	 *
	 * @return the threads
	 */
	private static Thread[] getThreads() {
		
		ThreadGroup tg = Thread.currentThread().getThreadGroup();
		
		while (tg.getParent() != null) {
			tg = tg.getParent();
		}

		int threadCountGuess = tg.activeCount() + 50;
		Thread[] threads = new Thread[threadCountGuess];
		int threadCountActual = tg.enumerate(threads);
		
		while (threadCountActual == threadCountGuess) {
			threadCountGuess *= 2;
			threads = new Thread[threadCountGuess];
			
			
			threadCountActual = tg.enumerate(threads);
		}

		return threads;
	}
}
