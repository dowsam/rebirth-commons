/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons SpringContextHolder.java 2012-7-6 10:22:15 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * The Class SpringContextHolder.
 *
 * @author l.xue.nong
 */
public class SpringContextHolder implements ApplicationContextAware, DisposableBean, BeanFactoryPostProcessor {

	/** The application context. */
	private static ApplicationContext applicationContext = null;

	/** The bean factory. */
	private static ConfigurableListableBeanFactory beanFactory = null;

	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(SpringContextHolder.class);

	/**
	 * Gets the application context.
	 *
	 * @return the application context
	 */
	public static ApplicationContext getApplicationContext() {
		assertContextInjected();
		return applicationContext;
	}

	/**
	 * Gets the bean.
	 *
	 * @param <T> the generic type
	 * @param name the name
	 * @return the bean
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		assertContextInjected();
		return (T) applicationContext.getBean(name);
	}

	/**
	 * Gets the bean.
	 *
	 * @param <T> the generic type
	 * @param requiredType the required type
	 * @return the bean
	 */
	public static <T> T getBean(Class<T> requiredType) {
		assertContextInjected();
		return applicationContext.getBean(requiredType);
	}

	/**
	 * Clear holder.
	 */
	public static void clearHolder() {
		logger.debug("清除SpringContextHolder中的ApplicationContext:" + applicationContext);
		applicationContext = null;
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		logger.debug("注入ApplicationContext到SpringContextHolder:" + applicationContext);

		if (SpringContextHolder.applicationContext != null) {
			logger.warn("SpringContextHolder中的ApplicationContext被覆盖, 原有ApplicationContext为:"
					+ SpringContextHolder.applicationContext);
		}

		SpringContextHolder.applicationContext = applicationContext; //NOSONAR
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	@Override
	public void destroy() throws Exception {
		SpringContextHolder.clearHolder();
	}

	/**
	 * Assert context injected.
	 */
	private static void assertContextInjected() {
		Validate.validState(applicationContext != null,
				"applicaitonContext属性未注入, 请在applicationContext.xml中定义SpringContextHolder.");
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.config.BeanFactoryPostProcessor#postProcessBeanFactory(org.springframework.beans.factory.config.ConfigurableListableBeanFactory)
	 */
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		SpringContextHolder.beanFactory = beanFactory;
	}

	/**
	 * Gets the bean factory.
	 *
	 * @return the bean factory
	 */
	public static ConfigurableListableBeanFactory getBeanFactory() {
		return beanFactory;
	}

}
