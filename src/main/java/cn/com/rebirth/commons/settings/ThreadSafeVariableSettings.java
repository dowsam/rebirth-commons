/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-knowledge-web-admin ThreadSafeVariableSettings.java 2012-8-16 16:03:59 l.xue.nong$$
 */
package cn.com.rebirth.commons.settings;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;

import cn.com.rebirth.commons.Booleans;
import cn.com.rebirth.commons.RebirthCommonsVersion;
import cn.com.rebirth.commons.Strings;
import cn.com.rebirth.commons.Version;
import cn.com.rebirth.commons.io.stream.StreamInput;
import cn.com.rebirth.commons.io.stream.StreamOutput;
import cn.com.rebirth.commons.settings.loader.SettingsLoader;
import cn.com.rebirth.commons.settings.loader.SettingsLoaderFactory;
import cn.com.rebirth.commons.unit.ByteSizeUnit;
import cn.com.rebirth.commons.unit.ByteSizeValue;
import cn.com.rebirth.commons.unit.SizeValue;
import cn.com.rebirth.commons.unit.TimeValue;
import cn.com.rebirth.commons.utils.TemplateMatcher;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * The Class ThreadSafeVariableSettings.
 *
 * @author l.xue.nong
 */
public class ThreadSafeVariableSettings implements Settings {

	private static final long serialVersionUID = -4929609535479433732L;

	/** The settings. */
	private java.util.concurrent.ConcurrentMap<String, String> settings;

	/** The class loader. */
	private transient ClassLoader classLoader;

	/**
	 * Instantiates a new immutable settings.
	 *
	 * @param settings the settings
	 * @param classLoader the class loader
	 */
	private ThreadSafeVariableSettings(Map<String, String> settings, ClassLoader classLoader) {
		this.settings = Maps.newConcurrentMap();
		this.settings.putAll(settings);
		this.classLoader = classLoader;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.settings.Settings#getClassLoader()
	 */
	@Override
	public ClassLoader getClassLoader() {
		return this.classLoader == null ? Thread.currentThread().getContextClassLoader() : classLoader;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.settings.Settings#getClassLoaderIfSet()
	 */
	@Override
	public ClassLoader getClassLoaderIfSet() {
		return this.classLoader;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.settings.Settings#getAsMap()
	 */
	@Override
	public Map<String, String> getAsMap() {
		return this.settings;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.settings.Settings#getComponentSettings(java.lang.Class)
	 */
	@Override
	public Settings getComponentSettings(Class<?> component) {
		if (component.getName().startsWith("cn.com.rebirth")) {
			return getComponentSettings("cn.com.rebirth", component);
		}

		return getComponentSettings(component.getName().substring(0, component.getName().indexOf('.')), component);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.settings.Settings#getComponentSettings(java.lang.String, java.lang.Class)
	 */
	@Override
	public Settings getComponentSettings(String prefix, Class<?> component) {
		String type = component.getName();
		if (!type.startsWith(prefix)) {
			throw new SettingsException("Component [" + type + "] does not start with prefix [" + prefix + "]");
		}
		String settingPrefix = type.substring(prefix.length() + 1);
		settingPrefix = settingPrefix.substring(0, settingPrefix.length() - component.getSimpleName().length());
		return getByPrefix(settingPrefix);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.settings.Settings#getByPrefix(java.lang.String)
	 */
	@Override
	public Settings getByPrefix(String prefix) {
		Builder builder = new Builder();
		for (Map.Entry<String, String> entry : getAsMap().entrySet()) {
			if (entry.getKey().startsWith(prefix)) {
				if (entry.getKey().length() < prefix.length()) {

					continue;
				}
				builder.put(entry.getKey().substring(prefix.length()), entry.getValue());
			}
		}
		builder.classLoader(classLoader);
		return builder.build();
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.settings.Settings#get(java.lang.String)
	 */
	@Override
	public String get(String setting) {
		String retVal = settings.get(setting);
		if (retVal != null) {
			return retVal;
		}

		return settings.get(Strings.toCamelCase(setting));
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.settings.Settings#get(java.lang.String, java.lang.String)
	 */
	@Override
	public String get(String setting, String defaultValue) {
		String retVal = settings.get(setting);
		return retVal == null ? defaultValue : retVal;
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.settings.Settings#getAsFloat(java.lang.String, java.lang.Float)
	 */
	@Override
	public Float getAsFloat(String setting, Float defaultValue) {
		String sValue = get(setting);
		if (sValue == null) {
			return defaultValue;
		}
		try {
			return Float.parseFloat(sValue);
		} catch (NumberFormatException e) {
			throw new SettingsException("Failed to parse float setting [" + setting + "] with value [" + sValue + "]",
					e);
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.settings.Settings#getAsDouble(java.lang.String, java.lang.Double)
	 */
	@Override
	public Double getAsDouble(String setting, Double defaultValue) {
		String sValue = get(setting);
		if (sValue == null) {
			return defaultValue;
		}
		try {
			return Double.parseDouble(sValue);
		} catch (NumberFormatException e) {
			throw new SettingsException("Failed to parse double setting [" + setting + "] with value [" + sValue + "]",
					e);
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.settings.Settings#getAsInt(java.lang.String, java.lang.Integer)
	 */
	@Override
	public Integer getAsInt(String setting, Integer defaultValue) {
		String sValue = get(setting);
		if (sValue == null) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(sValue);
		} catch (NumberFormatException e) {
			throw new SettingsException("Failed to parse int setting [" + setting + "] with value [" + sValue + "]", e);
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.settings.Settings#getAsLong(java.lang.String, java.lang.Long)
	 */
	@Override
	public Long getAsLong(String setting, Long defaultValue) {
		String sValue = get(setting);
		if (sValue == null) {
			return defaultValue;
		}
		try {
			return Long.parseLong(sValue);
		} catch (NumberFormatException e) {
			throw new SettingsException("Failed to parse long setting [" + setting + "] with value [" + sValue + "]", e);
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.settings.Settings#getAsBoolean(java.lang.String, java.lang.Boolean)
	 */
	@Override
	public Boolean getAsBoolean(String setting, Boolean defaultValue) {
		return Booleans.parseBoolean(get(setting), defaultValue);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.settings.Settings#getAsTime(java.lang.String, cn.com.rebirth.search.commons.unit.TimeValue)
	 */
	@Override
	public TimeValue getAsTime(String setting, TimeValue defaultValue) {
		return TimeValue.parseTimeValue(get(setting), defaultValue);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.settings.Settings#getAsBytesSize(java.lang.String, cn.com.rebirth.search.commons.unit.ByteSizeValue)
	 */
	@Override
	public ByteSizeValue getAsBytesSize(String setting, ByteSizeValue defaultValue) throws SettingsException {
		return ByteSizeValue.parseBytesSizeValue(get(setting), defaultValue);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.settings.Settings#getAsSize(java.lang.String, cn.com.rebirth.search.commons.unit.SizeValue)
	 */
	@Override
	public SizeValue getAsSize(String setting, SizeValue defaultValue) throws SettingsException {
		return SizeValue.parseSizeValue(get(setting), defaultValue);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.settings.Settings#getAsClass(java.lang.String, java.lang.Class)
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	public <T> Class<? extends T> getAsClass(String setting, Class<? extends T> defaultClazz)
			throws NoClassSettingsException {
		String sValue = get(setting);
		if (sValue == null) {
			return defaultClazz;
		}
		try {
			return (Class<? extends T>) getClassLoader().loadClass(sValue);
		} catch (ClassNotFoundException e) {
			throw new NoClassSettingsException("Failed to load class setting [" + setting + "] with value [" + sValue
					+ "]", e);
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.settings.Settings#getAsClass(java.lang.String, java.lang.Class, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings({ "unchecked" })
	@Override
	public <T> Class<? extends T> getAsClass(String setting, Class<? extends T> defaultClazz, String prefixPackage,
			String suffixClassName) throws NoClassSettingsException {
		String sValue = get(setting);
		if (sValue == null) {
			return defaultClazz;
		}
		String fullClassName = sValue;
		try {
			return (Class<? extends T>) getClassLoader().loadClass(fullClassName);
		} catch (ClassNotFoundException e) {
			String prefixValue = prefixPackage;
			int packageSeparator = sValue.lastIndexOf('.');
			if (packageSeparator > 0) {
				prefixValue = sValue.substring(0, packageSeparator + 1);
				sValue = sValue.substring(packageSeparator + 1);
			}
			fullClassName = prefixValue + Strings.capitalize(Strings.toCamelCase(sValue)) + suffixClassName;
			try {
				return (Class<? extends T>) getClassLoader().loadClass(fullClassName);
			} catch (ClassNotFoundException e1) {
				fullClassName = prefixValue + Strings.toCamelCase(sValue).toLowerCase() + "."
						+ Strings.capitalize(Strings.toCamelCase(sValue)) + suffixClassName;
				try {
					return (Class<? extends T>) getClassLoader().loadClass(fullClassName);
				} catch (ClassNotFoundException e2) {
					throw new NoClassSettingsException("Failed to load class setting [" + setting + "] with value ["
							+ get(setting) + "]", e);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.settings.Settings#getAsArray(java.lang.String)
	 */
	@Override
	public String[] getAsArray(String settingPrefix) throws SettingsException {
		return getAsArray(settingPrefix, Strings.EMPTY_ARRAY);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.settings.Settings#getAsArray(java.lang.String, java.lang.String[])
	 */
	@Override
	public String[] getAsArray(String settingPrefix, String[] defaultArray) throws SettingsException {
		List<String> result = Lists.newArrayList();

		if (get(settingPrefix) != null) {
			String[] strings = Strings.splitStringByCommaToArray(get(settingPrefix));
			if (strings.length > 0) {
				for (String string : strings) {
					result.add(string.trim());
				}
			}
		}

		int counter = 0;
		while (true) {
			String value = get(settingPrefix + '.' + (counter++));
			if (value == null) {
				break;
			}
			result.add(value.trim());
		}
		if (result.isEmpty()) {
			return defaultArray;
		}
		return result.toArray(new String[result.size()]);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.settings.Settings#getGroups(java.lang.String)
	 */
	@Override
	public Map<String, Settings> getGroups(String settingPrefix) throws SettingsException {
		if (settingPrefix.charAt(settingPrefix.length() - 1) != '.') {
			settingPrefix = settingPrefix + ".";
		}

		Map<String, Map<String, String>> map = new LinkedHashMap<String, Map<String, String>>();
		for (Object o : settings.keySet()) {
			String setting = (String) o;
			if (setting.startsWith(settingPrefix)) {
				String nameValue = setting.substring(settingPrefix.length());
				int dotIndex = nameValue.indexOf('.');
				if (dotIndex == -1) {
					throw new SettingsException("Failed to get setting group for [" + settingPrefix
							+ "] setting prefix and setting [" + setting + "] because of a missing '.'");
				}
				String name = nameValue.substring(0, dotIndex);
				String value = nameValue.substring(dotIndex + 1);
				Map<String, String> groupSettings = map.get(name);
				if (groupSettings == null) {
					groupSettings = new LinkedHashMap<String, String>();
					map.put(name, groupSettings);
				}
				groupSettings.put(value, get(setting));
			}
		}
		Map<String, Settings> retVal = new LinkedHashMap<String, Settings>();
		for (Map.Entry<String, Map<String, String>> entry : map.entrySet()) {
			retVal.put(entry.getKey(), new ThreadSafeVariableSettings(Collections.unmodifiableMap(entry.getValue()),
					classLoader));
		}
		return Collections.unmodifiableMap(retVal);
	}

	/* (non-Javadoc)
	 * @see cn.com.rebirth.search.commons.settings.Settings#getAsVersion(java.lang.String, cn.com.rebirth.commons.Version)
	 */
	@Override
	public Version getAsVersion(String setting, Version defaultVersion) throws SettingsException {
		String sValue = get(setting);
		if (sValue == null) {
			return defaultVersion;
		}
		try {
			return new RebirthCommonsVersion();
		} catch (Exception e) {
			throw new SettingsException(
					"Failed to parse version setting [" + setting + "] with value [" + sValue + "]", e);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		ThreadSafeVariableSettings that = (ThreadSafeVariableSettings) o;

		if (classLoader != null ? !classLoader.equals(that.classLoader) : that.classLoader != null)
			return false;
		if (settings != null ? !settings.equals(that.settings) : that.settings != null)
			return false;

		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = settings != null ? settings.hashCode() : 0;
		result = 31 * result + (classLoader != null ? classLoader.hashCode() : 0);
		return result;
	}

	/**
	 * Read settings from stream.
	 *
	 * @param in the in
	 * @return the settings
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Settings readSettingsFromStream(StreamInput in) throws IOException {
		Builder builder = new Builder();
		int numberOfSettings = in.readVInt();
		for (int i = 0; i < numberOfSettings; i++) {
			builder.put(in.readUTF(), in.readUTF());
		}
		return builder.build();
	}

	/**
	 * Write settings to stream.
	 *
	 * @param settings the settings
	 * @param out the out
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeSettingsToStream(Settings settings, StreamOutput out) throws IOException {
		out.writeVInt(settings.getAsMap().size());
		for (Map.Entry<String, String> entry : settings.getAsMap().entrySet()) {
			out.writeUTF(entry.getKey());
			out.writeUTF(entry.getValue());
		}
	}

	/**
	 * Settings builder.
	 *
	 * @return the builder
	 */
	public static Builder settingsBuilder() {
		return new Builder();
	}

	/**
	 * The Class Builder.
	 *
	 * @author l.xue.nong
	 */
	public static class Builder implements Settings.Builder {

		/** The Constant EMPTY_SETTINGS. */
		public static final Settings EMPTY_SETTINGS = new Builder().build();

		/** The map. */
		private final Map<String, String> map = new LinkedHashMap<String, String>();

		/** The class loader. */
		private ClassLoader classLoader;

		/**
		 * Instantiates a new builder.
		 */
		private Builder() {

		}

		/**
		 * Internal map.
		 *
		 * @return the map
		 */
		public Map<String, String> internalMap() {
			return this.map;
		}

		/**
		 * Removes the.
		 *
		 * @param key the key
		 * @return the string
		 */
		public String remove(String key) {
			return map.remove(key);
		}

		/**
		 * Gets the.
		 *
		 * @param key the key
		 * @return the string
		 */
		public String get(String key) {
			String retVal = map.get(key);
			if (retVal != null) {
				return retVal;
			}

			return map.get(Strings.toCamelCase(key));
		}

		/**
		 * Put.
		 *
		 * @param key the key
		 * @param value the value
		 * @return the builder
		 */
		public Builder put(String key, String value) {
			map.put(key, value);
			return this;
		}

		/**
		 * Put.
		 *
		 * @param key the key
		 * @param clazz the clazz
		 * @return the builder
		 */
		public Builder put(String key, Class<?> clazz) {
			map.put(key, clazz.getName());
			return this;
		}

		/**
		 * Put.
		 *
		 * @param setting the setting
		 * @param value the value
		 * @return the builder
		 */
		public Builder put(String setting, boolean value) {
			put(setting, String.valueOf(value));
			return this;
		}

		/**
		 * Put.
		 *
		 * @param setting the setting
		 * @param value the value
		 * @return the builder
		 */
		public Builder put(String setting, int value) {
			put(setting, String.valueOf(value));
			return this;
		}

		/**
		 * Put.
		 *
		 * @param setting the setting
		 * @param version the version
		 * @return the builder
		 */
		public Builder put(String setting, Version version) {
			put(setting, version.getModuleVersion());
			return this;
		}

		/**
		 * Put.
		 *
		 * @param setting the setting
		 * @param value the value
		 * @return the builder
		 */
		public Builder put(String setting, long value) {
			put(setting, String.valueOf(value));
			return this;
		}

		/**
		 * Put.
		 *
		 * @param setting the setting
		 * @param value the value
		 * @return the builder
		 */
		public Builder put(String setting, float value) {
			put(setting, String.valueOf(value));
			return this;
		}

		/**
		 * Put.
		 *
		 * @param setting the setting
		 * @param value the value
		 * @return the builder
		 */
		public Builder put(String setting, double value) {
			put(setting, String.valueOf(value));
			return this;
		}

		/**
		 * Put.
		 *
		 * @param setting the setting
		 * @param value the value
		 * @param timeUnit the time unit
		 * @return the builder
		 */
		public Builder put(String setting, long value, TimeUnit timeUnit) {
			put(setting, timeUnit.toMillis(value));
			return this;
		}

		/**
		 * Put.
		 *
		 * @param setting the setting
		 * @param value the value
		 * @param sizeUnit the size unit
		 * @return the builder
		 */
		public Builder put(String setting, long value, ByteSizeUnit sizeUnit) {
			put(setting, sizeUnit.toBytes(value));
			return this;
		}

		/**
		 * Put array.
		 *
		 * @param setting the setting
		 * @param values the values
		 * @return the builder
		 */
		public Builder putArray(String setting, String... values) {
			remove(setting);
			int counter = 0;
			while (true) {
				String value = map.remove(setting + '.' + (counter++));
				if (value == null) {
					break;
				}
			}
			for (int i = 0; i < values.length; i++) {
				put(setting + "." + i, values[i]);
			}
			return this;
		}

		/**
		 * Put.
		 *
		 * @param settingPrefix the setting prefix
		 * @param groupName the group name
		 * @param settings the settings
		 * @param values the values
		 * @return the builder
		 * @throws SettingsException the settings exception
		 */
		public Builder put(String settingPrefix, String groupName, String[] settings, String[] values)
				throws SettingsException {
			if (settings.length != values.length) {
				throw new SettingsException("The settings length must match the value length");
			}
			for (int i = 0; i < settings.length; i++) {
				if (values[i] == null) {
					continue;
				}
				put(settingPrefix + "." + groupName + "." + settings[i], values[i]);
			}
			return this;
		}

		/**
		 * Put.
		 *
		 * @param settings the settings
		 * @return the builder
		 */
		public Builder put(Settings settings) {
			map.putAll(settings.getAsMap());
			classLoader = settings.getClassLoaderIfSet();
			return this;
		}

		/**
		 * Put.
		 *
		 * @param settings the settings
		 * @return the builder
		 */
		public Builder put(Map<String, String> settings) {
			map.putAll(settings);
			return this;
		}

		/**
		 * Put.
		 *
		 * @param properties the properties
		 * @return the builder
		 */
		public Builder put(Properties properties) {
			for (Map.Entry<Object, Object> entry : properties.entrySet()) {
				map.put((String) entry.getKey(), (String) entry.getValue());
			}
			return this;
		}

		/**
		 * Load from source.
		 *
		 * @param source the source
		 * @return the builder
		 */
		public Builder loadFromSource(String source) {
			SettingsLoader settingsLoader = SettingsLoaderFactory.loaderFromSource(source);
			try {
				Map<String, String> loadedSettings = settingsLoader.load(source);
				put(loadedSettings);
			} catch (Exception e) {
				throw new SettingsException("Failed to load settings from [" + source + "]", e);
			}
			return this;
		}

		/**
		 * Load from url.
		 *
		 * @param url the url
		 * @return the builder
		 * @throws SettingsException the settings exception
		 */
		public Builder loadFromUrl(URL url) throws SettingsException {
			try {
				return loadFromStream(url.toExternalForm(), url.openStream());
			} catch (IOException e) {
				throw new SettingsException("Failed to open stream for url [" + url.toExternalForm() + "]", e);
			}
		}

		/**
		 * Load from stream.
		 *
		 * @param resourceName the resource name
		 * @param is the is
		 * @return the builder
		 * @throws SettingsException the settings exception
		 */
		public Builder loadFromStream(String resourceName, InputStream is) throws SettingsException {
			SettingsLoader settingsLoader = SettingsLoaderFactory.loaderFromResource(resourceName);
			try {
				Map<String, String> loadedSettings = settingsLoader.load(IOUtils.toString(new InputStreamReader(is,
						"UTF-8")));
				put(loadedSettings);
			} catch (Exception e) {
				throw new SettingsException("Failed to load settings from [" + resourceName + "]", e);
			}
			return this;
		}

		/**
		 * Load from classpath.
		 *
		 * @param resourceName the resource name
		 * @return the builder
		 * @throws SettingsException the settings exception
		 */
		public Builder loadFromClasspath(String resourceName) throws SettingsException {
			ClassLoader classLoader = this.classLoader;
			if (classLoader == null) {
				classLoader = Thread.currentThread().getContextClassLoader();
			}
			InputStream is = classLoader.getResourceAsStream(resourceName);
			if (is == null) {
				return this;
			}

			return loadFromStream(resourceName, is);
		}

		/**
		 * Class loader.
		 *
		 * @param classLoader the class loader
		 * @return the builder
		 */
		public Builder classLoader(ClassLoader classLoader) {
			this.classLoader = classLoader;
			return this;
		}

		/**
		 * Put properties.
		 *
		 * @param prefix the prefix
		 * @param properties the properties
		 * @return the builder
		 */
		public Builder putProperties(String prefix, Properties properties) {
			for (Object key1 : properties.keySet()) {
				String key = (String) key1;
				String value = properties.getProperty(key);
				if (key.startsWith(prefix)) {
					map.put(key.substring(prefix.length()), value);
				}
			}
			return this;
		}

		/**
		 * Replace property placeholders.
		 *
		 * @return the builder
		 */
		public Builder replacePropertyPlaceholders() {
			TemplateMatcher propertyPlaceholder = new TemplateMatcher("${", "}");
			TemplateMatcher.VariableResolver placeholderResolver = new TemplateMatcher.VariableResolver() {
				@Override
				public String resolve(String placeholderName) {
					String value = System.getProperty(placeholderName);
					if (value != null) {
						return value;
					}
					value = System.getenv(placeholderName);
					if (value != null) {
						return value;
					}
					return map.get(placeholderName);
				}

			};
			for (Map.Entry<String, String> entry : map.entrySet()) {
				map.put(entry.getKey(), propertyPlaceholder.replace(entry.getValue(), placeholderResolver));
			}
			return this;
		}

		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.settings.Settings.Builder#build()
		 */
		public Settings build() {
			return new ThreadSafeVariableSettings(Collections.unmodifiableMap(map), classLoader);
		}
	}

}
