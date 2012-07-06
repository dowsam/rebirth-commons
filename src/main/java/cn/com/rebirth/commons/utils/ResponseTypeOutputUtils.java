/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons ResponseTypeOutputUtils.java 2012-7-6 10:22:13 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * The Class ResponseTypeOutputUtils.
 *
 * @author l.xue.nong
 */
public abstract class ResponseTypeOutputUtils {
	// -- header 常量定义 --//
	/** The Constant HEADER_ENCODING. */
	private static final String HEADER_ENCODING = "encoding";

	/** The Constant HEADER_NOCACHE. */
	private static final String HEADER_NOCACHE = "no-cache";

	/** The Constant DEFAULT_ENCODING. */
	public static final String DEFAULT_ENCODING = "UTF-8";

	/** The Constant DEFAULT_NOCACHE. */
	private static final boolean DEFAULT_NOCACHE = true;

	/** The mapper. */
	private static ObjectMapper mapper = new ObjectMapper();

	// -- 绕过jsp/freemaker直接输出文本的函数 --//
	/**
	 * Render.
	 *
	 * @param contentType the content type
	 * @param content the content
	 * @param response the response
	 * @param headers the headers
	 */
	public static void render(final String contentType, final String content, HttpServletResponse response,
			final String... headers) {
		response = initResponseHeader(contentType, response, headers);
		try {
			response.getWriter().write(content);
			response.getWriter().flush();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * Render text.
	 *
	 * @param response the response
	 * @param text the text
	 * @param headers the headers
	 */
	public static void renderText(final HttpServletResponse response, final String text, final String... headers) {
		render(ServletUtils.TEXT_TYPE, text, response, headers);
	}

	/**
	 * Render html.
	 *
	 * @param response the response
	 * @param html the html
	 * @param headers the headers
	 */
	public static void renderHtml(final HttpServletResponse response, final String html, final String... headers) {
		render(ServletUtils.HTML_TYPE, html, response, headers);
	}

	/**
	 * Render xml.
	 *
	 * @param response the response
	 * @param xml the xml
	 * @param headers the headers
	 */
	public static void renderXml(final HttpServletResponse response, final String xml, final String... headers) {
		render(ServletUtils.XML_TYPE, xml, response, headers);
	}

	/**
	 * Render json.
	 *
	 * @param response the response
	 * @param jsonString the json string
	 * @param headers the headers
	 */
	public static void renderJson(final HttpServletResponse response, final String jsonString, final String... headers) {
		render(ServletUtils.JSON_TYPE, jsonString, response, headers);
	}

	/**
	 * Render json.
	 *
	 * @param response the response
	 * @param data the data
	 * @param headers the headers
	 */
	public static void renderJson(HttpServletResponse response, final Object data, final String... headers) {
		response = initResponseHeader(ServletUtils.JSON_TYPE, response, headers);
		try {
			mapper.writeValue(response.getWriter(), data);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * Render jsonp.
	 *
	 * @param response the response
	 * @param callbackName the callback name
	 * @param object the object
	 * @param headers the headers
	 */
	public static void renderJsonp(final HttpServletResponse response, final String callbackName, final Object object,
			final String... headers) {
		String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(object);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}

		String result = new StringBuilder().append(callbackName).append("(").append(jsonString).append(");").toString();

		// 渲染Content-Type为javascript的返回内容,输出结果为javascript语句,
		// 如callback197("{html:'Hello World!!!'}");
		render(ServletUtils.JS_TYPE, result, response, headers);
	}

	/**
	 * Inits the response header.
	 *
	 * @param contentType the content type
	 * @param response the response
	 * @param headers the headers
	 * @return the http servlet response
	 */
	private static HttpServletResponse initResponseHeader(final String contentType, final HttpServletResponse response,
			final String... headers) {
		// 分析headers参数
		String encoding = DEFAULT_ENCODING;
		boolean noCache = DEFAULT_NOCACHE;
		for (String header : headers) {
			String headerName = StringUtils.substringBefore(header, ":");
			String headerValue = StringUtils.substringAfter(header, ":");

			if (StringUtils.equalsIgnoreCase(headerName, HEADER_ENCODING)) {
				encoding = headerValue;
			} else if (StringUtils.equalsIgnoreCase(headerName, HEADER_NOCACHE)) {
				noCache = Boolean.parseBoolean(headerValue);
			} else {
				throw new IllegalArgumentException(headerName + "不是一个合法的header类型");
			}
		}

		// 设置headers参数
		String fullContentType = contentType + ";charset=" + encoding;
		response.setContentType(fullContentType);
		if (noCache) {
			ServletUtils.setNoCacheHeader(response);
		}

		return response;
	}
}
