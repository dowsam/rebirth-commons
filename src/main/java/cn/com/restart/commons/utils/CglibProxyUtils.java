/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-commons CglibProxyUtils.java 2012-2-2 10:11:22 l.xue.nong$$
 */
package cn.com.restart.commons.utils;
import java.lang.reflect.Method;
import java.util.Map;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;
/**
 * The Class CglibProxyUtils.
 *
 * @author l.xue.nong
 */
public abstract class CglibProxyUtils {
	/**
	 * 产生代理对象，需要原先类的class，以及一个callback函数。.
	 *
	 * @param <T> the generic type
	 * @param objectClass the object class
	 * @param callback the callback
	 * @return the proxy instance
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getProxyInstance(Class<T> objectClass, Callback callback) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(objectClass);
		enhancer.setCallback(callback);
		return (T) enhancer.create();
	}

	/**
	 * 产生代理对象继承并实现了接口，需要原先类的class，代理对象接口类别列表，以及一个callback函数。.
	 *
	 * @param <T> the generic type
	 * @param objectClass the object class
	 * @param interfaces the interfaces
	 * @param callback the callback
	 * @return the proxy instance
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getProxyInstance(Class<T> objectClass, Class<?> interfaces, Callback callback) {
		Enhancer enhancer = new Enhancer();
		enhancer.setInterfaces(new Class[] { interfaces }); //处理对象实现的接口
		enhancer.setSuperclass(objectClass);
		enhancer.setCallback(callback);
		return (T) enhancer.create();
	}

	/**
	 * 产生代理对象继承并实现了接口，需要原先类的class，代理对象接口类别列表，以及一个callback函数。.
	 *
	 * @param <T> the generic type
	 * @param objectClass the object class
	 * @param interfaces the interfaces
	 * @param callback the callback
	 * @return the proxy instance
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getProxyInstance(Class<T> objectClass, Class<?>[] interfaces, Callback callback) {
		Enhancer enhancer = new Enhancer();
		enhancer.setInterfaces(interfaces); //处理对象实现的接口
		enhancer.setSuperclass(objectClass);
		enhancer.setCallback(callback);
		return (T) enhancer.create();
	}

	/**
	 * 带方法过滤得对象产生器，需要一个map作为过滤器列表，参数为<方法名,第几个拦截器>，作为指定执行相应的callback函数。.
	 *
	 * @param <T> the generic type
	 * @param objectClass the object class
	 * @param callbacks the callbacks
	 * @param methodName the method name
	 * @return the proxy instance
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getProxyInstance(Class<T> objectClass, Callback[] callbacks, Map<String, Integer> methodName) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(objectClass);
		Callback[] callback = new Callback[callbacks.length + 1];
		System.arraycopy(callbacks, 0, callback, 0, callbacks.length);
		callback[callbacks.length] = NoOp.INSTANCE; //无处理方法
		enhancer.setCallbacks(callback);
		enhancer.setCallbackFilter(new CallbackFilterImpl(callback.length, methodName));
		return (T) enhancer.create();
	}

	/**
	 * The Class CallbackFilterImpl.
	 *
	 * @author l.xue.nong
	 */
	private static class CallbackFilterImpl implements CallbackFilter {

		/** The callback length. */
		private int callbackLength;

		/** The method name. */
		private Map<String, Integer> methodName;

		/**
		 * Instantiates a new callback filter impl.
		 *
		 * @param callbackLength the callback length
		 * @param methodName the method name
		 */
		private CallbackFilterImpl(int callbackLength, Map<String, Integer> methodName) {
			this.callbackLength = callbackLength - 1;
			this.methodName = methodName;
		}

		//哪个方法由第几个拦截器执行
		/* (non-Javadoc)
		 * @see net.sf.cglib.proxy.CallbackFilter#accept(java.lang.reflect.Method)
		 */
		/**
		 * Accept.
		 *
		 * @param method the method
		 * @return the int
		 */
		public int accept(Method method) {
			int result = callbackLength;
			for (Map.Entry<String, Integer> entry : methodName.entrySet()) {
				if (method.getName().equals(entry.getKey())) {
					result = (Integer) entry.getValue();
					if (result > callbackLength)
						result = callbackLength;
					break;
				}
			}
			return result;
		}
	}
}
