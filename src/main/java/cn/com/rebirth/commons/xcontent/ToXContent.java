/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-search-commons ToXContent.java 2012-7-6 10:23:44 l.xue.nong$$
 */
package cn.com.rebirth.commons.xcontent;

import java.io.IOException;
import java.util.Map;

import cn.com.rebirth.commons.Booleans;


/**
 * The Interface ToXContent.
 *
 * @author l.xue.nong
 */
public interface ToXContent {

	
	/**
	 * The Interface Params.
	 *
	 * @author l.xue.nong
	 */
	public static interface Params {

		
		/**
		 * Param.
		 *
		 * @param key the key
		 * @return the string
		 */
		String param(String key);

		
		/**
		 * Param.
		 *
		 * @param key the key
		 * @param defaultValue the default value
		 * @return the string
		 */
		String param(String key, String defaultValue);

		
		/**
		 * Param as boolean.
		 *
		 * @param key the key
		 * @param defaultValue the default value
		 * @return true, if successful
		 */
		boolean paramAsBoolean(String key, boolean defaultValue);

		
		/**
		 * Param as boolean optional.
		 *
		 * @param key the key
		 * @param defaultValue the default value
		 * @return the boolean
		 */
		Boolean paramAsBooleanOptional(String key, Boolean defaultValue);
	}

	
	/** The Constant EMPTY_PARAMS. */
	public static final Params EMPTY_PARAMS = new Params() {
		@Override
		public String param(String key) {
			return null;
		}

		@Override
		public String param(String key, String defaultValue) {
			return defaultValue;
		}

		@Override
		public boolean paramAsBoolean(String key, boolean defaultValue) {
			return defaultValue;
		}

		@Override
		public Boolean paramAsBooleanOptional(String key, Boolean defaultValue) {
			return defaultValue;
		}
	};

	
	/**
	 * The Class MapParams.
	 *
	 * @author l.xue.nong
	 */
	public static class MapParams implements Params {

		
		/** The params. */
		private final Map<String, String> params;

		
		/**
		 * Instantiates a new map params.
		 *
		 * @param params the params
		 */
		public MapParams(Map<String, String> params) {
			this.params = params;
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.xcontent.ToXContent.Params#param(java.lang.String)
		 */
		@Override
		public String param(String key) {
			return params.get(key);
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.xcontent.ToXContent.Params#param(java.lang.String, java.lang.String)
		 */
		@Override
		public String param(String key, String defaultValue) {
			String value = params.get(key);
			if (value == null) {
				return defaultValue;
			}
			return value;
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.xcontent.ToXContent.Params#paramAsBoolean(java.lang.String, boolean)
		 */
		@Override
		public boolean paramAsBoolean(String key, boolean defaultValue) {
			return Booleans.parseBoolean(param(key), defaultValue);
		}

		
		/* (non-Javadoc)
		 * @see cn.com.rebirth.search.commons.xcontent.ToXContent.Params#paramAsBooleanOptional(java.lang.String, java.lang.Boolean)
		 */
		@Override
		public Boolean paramAsBooleanOptional(String key, Boolean defaultValue) {
			String sValue = param(key);
			if (sValue == null) {
				return defaultValue;
			}
			return !(sValue.equals("false") || sValue.equals("0") || sValue.equals("off"));
		}
	}

	
	/**
	 * To x content.
	 *
	 * @param builder the builder
	 * @param params the params
	 * @return the x content builder
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException;
}
