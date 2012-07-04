/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-commons HttpClientUtils.java 2012-3-13 14:41:12 l.xue.nong$$
 */
package cn.com.restart.commons.utils;

import java.io.File;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

/**
 * The Class HttpClientUtils.
 *
 * @author l.xue.nong
 */
public class HttpClientUtils {

	/** The http client. */
	private static HttpClient httpClient;

	/** The lock. */
	private static Object lock = new Object();

	/**
	 * Instantiates a new http client utils.
	 */
	private HttpClientUtils() {
		super();
		init();
		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public void run() {
				HttpClientUtils.this.destroy();
			}

		});
	}

	/**
	 * Gets the single instance of HttpClientUtils.
	 *
	 * @return single instance of HttpClientUtils
	 */
	public static HttpClientUtils getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * The Class SingletonHolder.
	 *
	 * @author l.xue.nong
	 */
	private static class SingletonHolder {

		/** The instance. */
		static HttpClientUtils instance = new HttpClientUtils();
	}

	/**
	 * Inits the.
	 */
	private void init() {
		synchronized (lock) {
			if (httpClient == null) {
				PoolingClientConnectionManager cm = new PoolingClientConnectionManager();
				cm.setMaxTotal(1000);
				httpClient = new DefaultHttpClient(cm);
			}
		}
	}

	/**
	 * Gets the http entity.
	 *
	 * @param targetUrl the target url
	 * @return the http entity
	 */
	public HttpEntity getHttpEntity(String targetUrl) {
		return invoke(new DefaultInvokeCallable(targetUrl));
	}

	/**
	 * Gets the http response.
	 *
	 * @param targetUrl the target url
	 * @return the http response
	 */
	public HttpResponse getHttpResponse(final String targetUrl) {
		return invoke(new InvokeCallable<HttpResponse>() {

			@Override
			public HttpResponse call(HttpClient httpClient) {
				HttpGet httpGet = new HttpGet(targetUrl);
				HttpContext context = new BasicHttpContext();
				try {
					return httpClient.execute(httpGet, context);
				} catch (Exception e) {
					httpGet.abort();
					return null;
				} finally {
					if (httpGet != null)
						httpGet.releaseConnection();
				}
			}
		});
	}

	/**
	 * Upload file.
	 *
	 * @param targetUrl the target url
	 * @param file the file
	 * @param paramName the param name
	 * @return true, if successful
	 */
	public boolean uploadFile(String targetUrl, File file, String paramName) {
		return invoke(new FileUploadInvokeCallable(targetUrl, file, paramName)) != null;
	}

	/**
	 * Upload input stream.
	 *
	 * @param targetUrl the target url
	 * @param in the in
	 * @param paramName the param name
	 * @param fileName the file name
	 * @return true, if successful
	 */
	public boolean uploadInputStream(String targetUrl, InputStream in, String paramName, String fileName) {
		return invoke(new InputStreamUploadInvokeCallable(targetUrl, in, paramName, fileName)) != null;
	}

	/**
	 * Invoke.
	 *
	 * @param <T> the generic type
	 * @param callable the callable
	 * @return the t
	 */
	public <T> T invoke(InvokeCallable<T> callable) {
		try {
			return callable.call(httpClient);
		} catch (Exception e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * Destroy.
	 */
	private void destroy() {
		synchronized (lock) {
			if (httpClient != null) {
				httpClient.getConnectionManager().shutdown();
				httpClient = null;
			}
		}
	}

	/**
	 * The Interface InvokeCallable.
	 *
	 * @param <T> the generic type
	 * @author l.xue.nong
	 */
	public static interface InvokeCallable<T> {

		/**
		 * Call.
		 *
		 * @param httpClient the http client
		 * @return the t
		 */
		T call(HttpClient httpClient);
	}

	/**
	 * The Class AbstractUploadInvokeCallable.
	 *
	 * @param <T> the generic type
	 * @author l.xue.nong
	 */
	static abstract class AbstractUploadInvokeCallable<T> implements InvokeCallable<HttpEntity> {

		/** The target url. */
		private final String targetUrl;

		/** The entity. */
		private final T entity;

		/** The name. */
		private final String name;

		/** The file name. */
		private String fileName;

		/**
		 * Instantiates a new abstract upload invoke callable.
		 *
		 * @param targetUrl the target url
		 * @param entity the entity
		 * @param name the name
		 */
		public AbstractUploadInvokeCallable(String targetUrl, T entity, String name) {
			super();
			this.targetUrl = targetUrl;
			this.entity = entity;
			this.name = name;
		}

		/**
		 * Instantiates a new abstract upload invoke callable.
		 *
		 * @param targetUrl the target url
		 * @param entity the entity
		 * @param name the name
		 * @param fileName the file name
		 */
		public AbstractUploadInvokeCallable(String targetUrl, T entity, String name, String fileName) {
			super();
			this.targetUrl = targetUrl;
			this.entity = entity;
			this.name = name;
			this.fileName = fileName;
		}

		/* (non-Javadoc)
		 * @see cn.com.summall.commons.utils.HttpClientUtils.InvokeCallable#call(org.apache.http.client.HttpClient)
		 */
		@Override
		public HttpEntity call(HttpClient httpClient) {
			httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			HttpPost httpPost = new HttpPost(targetUrl);
			MultipartEntity multipartEntity = new MultipartEntity();
			InputStream inputStream = null;
			if (entity instanceof File) {
				multipartEntity.addPart(name, new FileBody((File) entity));
			} else if (entity instanceof InputStream) {
				inputStream = (InputStream) entity;
				multipartEntity.addPart(name, new InputStreamBody(inputStream, fileName));
			}
			httpPost.setEntity(multipartEntity);
			System.out.println("executing request " + httpPost.getRequestLine());
			HttpContext context = new BasicHttpContext();
			try {
				HttpResponse remoteResponse = httpClient.execute(httpPost, context);
				System.out.println("remoteResponse:" + remoteResponse);
				return remoteResponse.getEntity();
			} catch (Exception e) {
				httpPost.abort();
				return null;
			} finally {
				IOUtils.closeQuietly(inputStream);
				if (httpPost != null)
					httpPost.releaseConnection();
			}
		}
	}

	/**
	 * The Class FileUploadInvokeCallable.
	 *
	 * @author l.xue.nong
	 */
	static class FileUploadInvokeCallable extends AbstractUploadInvokeCallable<File> {

		/**
		 * Instantiates a new file upload invoke callable.
		 *
		 * @param targetUrl the target url
		 * @param entity the entity
		 * @param name the name
		 */
		public FileUploadInvokeCallable(String targetUrl, File entity, String name) {
			super(targetUrl, entity, name);
		}

	}

	/**
	 * The Class InputStreamUploadInvokeCallable.
	 *
	 * @author l.xue.nong
	 */
	static class InputStreamUploadInvokeCallable extends AbstractUploadInvokeCallable<InputStream> {

		/**
		 * Instantiates a new input stream upload invoke callable.
		 *
		 * @param targetUrl the target url
		 * @param entity the entity
		 * @param name the name
		 * @param fileName the file name
		 */
		public InputStreamUploadInvokeCallable(String targetUrl, InputStream entity, String name, String fileName) {
			super(targetUrl, entity, name, fileName);
		}

	}

	/**
	 * The Class DefaultInvokeCallable.
	 *
	 * @author l.xue.nong
	 */
	static class DefaultInvokeCallable implements InvokeCallable<HttpEntity> {

		/** The target url. */
		private final String targetUrl;

		/**
		 * Instantiates a new default invoke callable.
		 *
		 * @param targetUrl the target url
		 */
		public DefaultInvokeCallable(String targetUrl) {
			super();
			this.targetUrl = targetUrl;
		}

		/* (non-Javadoc)
		 * @see cn.com.summall.commons.utils.HttpClientUtils.InvokeCallable#call(org.apache.http.client.HttpClient)
		 */
		@Override
		public HttpEntity call(HttpClient httpClient) {
			HttpGet httpGet = new HttpGet(targetUrl);
			HttpContext context = new BasicHttpContext();
			try {
				HttpResponse remoteResponse = httpClient.execute(httpGet, context);
				return remoteResponse.getEntity();
			} catch (Exception e) {
				httpGet.abort();
				return null;
			} finally {
				if (httpGet != null)
					httpGet.releaseConnection();
			}
		}

	}

}
