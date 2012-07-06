/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons Settings.java 2012-7-6 10:22:14 l.xue.nong$$
 */


package cn.com.rebirth.commons.settings;

import java.util.Map;

import cn.com.rebirth.commons.Nullable;
import cn.com.rebirth.commons.Version;
import cn.com.rebirth.commons.unit.ByteSizeValue;
import cn.com.rebirth.commons.unit.SizeValue;
import cn.com.rebirth.commons.unit.TimeValue;

import com.google.common.collect.ImmutableMap;


/**
 * The Interface Settings.
 *
 * @author l.xue.nong
 */
public interface Settings {

    
    /**
     * Gets the component settings.
     *
     * @param component the component
     * @return the component settings
     */
    Settings getComponentSettings(Class<?> component);

    
    /**
     * Gets the component settings.
     *
     * @param prefix the prefix
     * @param component the component
     * @return the component settings
     */
    Settings getComponentSettings(String prefix, Class<?> component);

    
    /**
     * Gets the by prefix.
     *
     * @param prefix the prefix
     * @return the by prefix
     */
    Settings getByPrefix(String prefix);

    
    /**
     * Gets the class loader.
     *
     * @return the class loader
     */
    ClassLoader getClassLoader();

    
    /**
     * Gets the class loader if set.
     *
     * @return the class loader if set
     */
    @Nullable
    ClassLoader getClassLoaderIfSet();

    
    /**
     * Gets the as map.
     *
     * @return the as map
     */
    ImmutableMap<String, String> getAsMap();

    
    /**
     * Gets the.
     *
     * @param setting the setting
     * @return the string
     */
    String get(String setting);

    
    /**
     * Gets the.
     *
     * @param setting the setting
     * @param defaultValue the default value
     * @return the string
     */
    String get(String setting, String defaultValue);

    
    /**
     * Gets the groups.
     *
     * @param settingPrefix the setting prefix
     * @return the groups
     * @throws SettingsException the settings exception
     */
    Map<String, Settings> getGroups(String settingPrefix) throws SettingsException;

    
    /**
     * Gets the as float.
     *
     * @param setting the setting
     * @param defaultValue the default value
     * @return the as float
     * @throws SettingsException the settings exception
     */
    Float getAsFloat(String setting, Float defaultValue) throws SettingsException;

    
    /**
     * Gets the as double.
     *
     * @param setting the setting
     * @param defaultValue the default value
     * @return the as double
     * @throws SettingsException the settings exception
     */
    Double getAsDouble(String setting, Double defaultValue) throws SettingsException;

    
    /**
     * Gets the as int.
     *
     * @param setting the setting
     * @param defaultValue the default value
     * @return the as int
     * @throws SettingsException the settings exception
     */
    Integer getAsInt(String setting, Integer defaultValue) throws SettingsException;

    
    /**
     * Gets the as long.
     *
     * @param setting the setting
     * @param defaultValue the default value
     * @return the as long
     * @throws SettingsException the settings exception
     */
    Long getAsLong(String setting, Long defaultValue) throws SettingsException;

    
    /**
     * Gets the as boolean.
     *
     * @param setting the setting
     * @param defaultValue the default value
     * @return the as boolean
     * @throws SettingsException the settings exception
     */
    Boolean getAsBoolean(String setting, Boolean defaultValue) throws SettingsException;

    
    /**
     * Gets the as time.
     *
     * @param setting the setting
     * @param defaultValue the default value
     * @return the as time
     * @throws SettingsException the settings exception
     */
    TimeValue getAsTime(String setting, TimeValue defaultValue) throws SettingsException;

    
    /**
     * Gets the as bytes size.
     *
     * @param setting the setting
     * @param defaultValue the default value
     * @return the as bytes size
     * @throws SettingsException the settings exception
     */
    ByteSizeValue getAsBytesSize(String setting, ByteSizeValue defaultValue) throws SettingsException;

    
    /**
     * Gets the as size.
     *
     * @param setting the setting
     * @param defaultValue the default value
     * @return the as size
     * @throws SettingsException the settings exception
     */
    SizeValue getAsSize(String setting, SizeValue defaultValue) throws SettingsException;

    
    /**
     * Gets the as class.
     *
     * @param <T> the generic type
     * @param setting the setting
     * @param defaultClazz the default clazz
     * @return the as class
     * @throws NoClassSettingsException the no class settings exception
     */
    <T> Class<? extends T> getAsClass(String setting, Class<? extends T> defaultClazz) throws NoClassSettingsException;

    
    /**
     * Gets the as class.
     *
     * @param <T> the generic type
     * @param setting the setting
     * @param defaultClazz the default clazz
     * @param prefixPackage the prefix package
     * @param suffixClassName the suffix class name
     * @return the as class
     * @throws NoClassSettingsException the no class settings exception
     */
    <T> Class<? extends T> getAsClass(String setting, Class<? extends T> defaultClazz, String prefixPackage, String suffixClassName) throws NoClassSettingsException;

    
    /**
     * Gets the as array.
     *
     * @param settingPrefix the setting prefix
     * @param defaultArray the default array
     * @return the as array
     * @throws SettingsException the settings exception
     */
    String[] getAsArray(String settingPrefix, String[] defaultArray) throws SettingsException;

    
    /**
     * Gets the as array.
     *
     * @param settingPrefix the setting prefix
     * @return the as array
     * @throws SettingsException the settings exception
     */
    String[] getAsArray(String settingPrefix) throws SettingsException;

    
    /**
     * Gets the as version.
     *
     * @param setting the setting
     * @param defaultVersion the default version
     * @return the as version
     * @throws SettingsException the settings exception
     */
    Version getAsVersion(String setting, Version defaultVersion) throws SettingsException;

    
    /**
     * The Interface Builder.
     *
     * @author l.xue.nong
     */
    interface Builder {

        
        /**
         * Builds the.
         *
         * @return the settings
         */
        Settings build();
    }
}
