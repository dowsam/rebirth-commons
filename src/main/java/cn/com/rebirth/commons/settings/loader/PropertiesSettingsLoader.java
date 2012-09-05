/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons PropertiesSettingsLoader.java 2012-7-9 12:29:29 l.xue.nong$$
 */
package cn.com.rebirth.commons.settings.loader;

import static com.google.common.collect.Maps.newHashMap;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.Properties;

import cn.com.rebirth.commons.utils.TemplateMatcher;

import com.google.common.collect.Maps;
import com.google.common.io.Closeables;

/**
 * The Class PropertiesSettingsLoader.
 *
 * @author l.xue.nong
 */
public class PropertiesSettingsLoader implements SettingsLoader {

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.settings.loader.SettingsLoader#load(java.lang.String)
	 */
	@Override
	public Map<String, String> load(String source) throws IOException {
		Properties props = new Properties();
		StringReader reader = new StringReader(source);
		try {
			props.load(reader);
			Map<String, String> result = newHashMap();
			for (Map.Entry<Object, Object> entry : props.entrySet()) {
				result.put((String) entry.getKey(), (String) entry.getValue());
			}
			return actionParam(result);
		} finally {
			Closeables.closeQuietly(reader);
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.settings.loader.SettingsLoader#load(byte[])
	 */
	@Override
	public Map<String, String> load(byte[] source) throws IOException {
		Properties props = new Properties();
		ByteArrayInputStream stream = new ByteArrayInputStream(source);
		try {
			props.load(stream);
			Map<String, String> result = newHashMap();
			for (Map.Entry<Object, Object> entry : props.entrySet()) {
				result.put((String) entry.getKey(), (String) entry.getValue());
			}
			return actionParam(result);
		} finally {
			Closeables.closeQuietly(stream);
		}
	}

	/**
	 * Action param.
	 *
	 * @param result the result
	 * @return the map
	 */
	protected Map<String, String> actionParam(final Map<String, String> result) {
		if (result == null || result.isEmpty())
			return result;
		TemplateMatcher templateMatcher = new TemplateMatcher("${", "}");
		TemplateMatcher.VariableResolver variableResolver = new TemplateMatcher.VariableResolver() {

			@Override
			public String resolve(String variable) {
				return bulidValue(variable);
			}
		};
		Map<String, String> proxy = Maps.newHashMapWithExpectedSize(result.size());
		for (Map.Entry<String, String> entry : result.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			key = templateMatcher.replace(key, variableResolver);
			String _value = bulidValue(key);
			if (_value == key) {
				proxy.put(key, value);
			} else {
				proxy.put(key, _value);
			}
		}
		return proxy;
	}

	/**
	 * Bulid value.
	 *
	 * @param variable the variable
	 * @return the string
	 */
	protected String bulidValue(String variable) {
		String value = System.getProperty(variable);
		if (value != null) {
			return value;
		}
		value = System.getenv(variable);
		if (value != null) {
			return value;
		}
		return variable;
	}
}
