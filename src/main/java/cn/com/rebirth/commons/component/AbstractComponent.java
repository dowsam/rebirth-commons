/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons AbstractComponent.java 2012-7-17 10:45:15 l.xue.nong$$
 */
package cn.com.rebirth.commons.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.rebirth.commons.settings.Settings;

/**
 * The Class AbstractComponent.
 *
 * @author l.xue.nong
 */
public class AbstractComponent {

	/** The logger. */
	protected final Logger logger;

	/** The settings. */
	protected final Settings settings;

	/** The component settings. */
	protected final Settings componentSettings;

	/**
	 * Instantiates a new abstract component.
	 *
	 * @param settings the settings
	 */
	public AbstractComponent(Settings settings) {
		this.logger = LoggerFactory.getLogger(getClass());
		this.settings = settings;
		this.componentSettings = settings.getComponentSettings(getClass());
	}

	/**
	 * Instantiates a new abstract component.
	 *
	 * @param settings the settings
	 * @param prefixSettings the prefix settings
	 */
	public AbstractComponent(Settings settings, String prefixSettings) {
		this.logger = LoggerFactory.getLogger(getClass());
		this.settings = settings;
		this.componentSettings = settings.getComponentSettings(prefixSettings, getClass());
	}

	/**
	 * Instantiates a new abstract component.
	 *
	 * @param settings the settings
	 * @param customClass the custom class
	 */
	public AbstractComponent(Settings settings, Class<?> customClass) {
		this.logger = LoggerFactory.getLogger(customClass);
		this.settings = settings;
		this.componentSettings = settings.getComponentSettings(customClass);
	}

	/**
	 * Instantiates a new abstract component.
	 *
	 * @param settings the settings
	 * @param prefixSettings the prefix settings
	 * @param customClass the custom class
	 */
	public AbstractComponent(Settings settings, String prefixSettings, Class<?> customClass) {
		this.logger = LoggerFactory.getLogger(customClass);
		this.settings = settings;
		this.componentSettings = settings.getComponentSettings(prefixSettings, customClass);
	}

	/**
	 * Instantiates a new abstract component.
	 *
	 * @param settings the settings
	 * @param loggerClass the logger class
	 * @param componentClass the component class
	 */
	public AbstractComponent(Settings settings, Class<?> loggerClass, Class<?> componentClass) {
		this.logger = LoggerFactory.getLogger(loggerClass);
		this.settings = settings;
		this.componentSettings = settings.getComponentSettings(componentClass);
	}

	/**
	 * Instantiates a new abstract component.
	 *
	 * @param settings the settings
	 * @param prefixSettings the prefix settings
	 * @param loggerClass the logger class
	 * @param componentClass the component class
	 */
	public AbstractComponent(Settings settings, String prefixSettings, Class<?> loggerClass, Class<?> componentClass) {
		this.logger = LoggerFactory.getLogger(loggerClass);
		this.settings = settings;
		this.componentSettings = settings.getComponentSettings(prefixSettings, componentClass);
	}

	/**
	 * Node name.
	 *
	 * @return the string
	 */
	public String nodeName() {
		return settings.get("name", "");
	}
}
