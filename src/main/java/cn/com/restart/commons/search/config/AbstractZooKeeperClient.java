/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-commons AbstractZooKeeperClient.java 2012-3-30 13:20:00 l.xue.nong$$
 */
package cn.com.restart.commons.search.config;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.restart.commons.utils.CglibProxyUtils;
import cn.com.restart.commons.utils.ExceptionUtils;
import cn.com.restart.commons.utils.PropertiesUtils;
import cn.com.restart.commons.utils.ZkUtils.ZKConfig;

/**
 * The Class AbstractZooKeeperClient.
 *
 * @author l.xue.nong
 */
public abstract class AbstractZooKeeperClient implements ZooKeeperClient {

	/** The logger. */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/** The Constant DEFAULT_PATH. */
	public static final String DEFAULT_PATH = "/zooKeeper-config.properties";

	/** The config. */
	private ZKConfig config;

	/** The zk client. */
	private ZkClient zkClient;

	/**
	 * Instantiates a new abstract zoo keeper client.
	 *
	 * @param config the config
	 */
	public AbstractZooKeeperClient(ZKConfig config) {
		super();
		this.config = config;
		if (this.config == null) {
			this.config = this.loadZkConfigFromDiamond();
		}
		this.start(this.config);
	}

	/**
	 * Gets the zk client.
	 *
	 * @return the zk client
	 */
	public ZkClient getZkClient() {
		return this.zkClient;
	}

	/**
	 * The listener interface for receiving sessionExpire events.
	 * The class that is interested in processing a sessionExpire
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addSessionExpireListener<code> method. When
	 * the sessionExpire event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see SessionExpireEvent
	 */
	private static class SessionExpireListener implements IZkStateListener {

		/* (non-Javadoc)
		 * @see org.I0Itec.zkclient.IZkStateListener#handleNewSession()
		 */
		@Override
		public void handleNewSession() throws Exception {
			System.out.println("handleNewSession");
		}

		/* (non-Javadoc)
		 * @see org.I0Itec.zkclient.IZkStateListener#handleStateChanged(org.apache.zookeeper.Watcher.Event.KeeperState)
		 */
		@Override
		public void handleStateChanged(final KeeperState state) throws Exception {
			// do nothing, since zkclient will do reconnect for us.
			System.out.println(state);
		}

	}

	/**
	 * Start.
	 *
	 * @param zkConfig the zk config
	 */
	private void start(final ZKConfig zkConfig) {
		logger.info("Initialize zk client...");
		this.zkClient = new ZkClient(zkConfig.zkConnect, zkConfig.zkSessionTimeoutMs, zkConfig.zkConnectionTimeoutMs,
				new SerializableSerializer());
		this.zkClient.subscribeStateChanges(new SessionExpireListener());
	}

	/**
	 * Load zk config from diamond.
	 *
	 * @return the zK config
	 */
	private ZKConfig loadZkConfigFromDiamond() {
		Properties properties;
		try {
			properties = new Properties();
			InputStream inputStream = AbstractZooKeeperClient.class.getResourceAsStream(DEFAULT_PATH);
			try {
				properties.load(inputStream);
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				IOUtils.closeQuietly(inputStream);
			}
		} catch (Exception e) {
			try {
				properties = PropertiesUtils.loadProperties(DEFAULT_PATH);
			} catch (IOException e1) {
				e1.printStackTrace();
				return null;
			}
		}
		if (properties == null)
			return null;
		properties = proxyProperties(properties);
		final ZKConfig zkConfig = new ZKConfig();
		if (StringUtils.isNotBlank(properties.getProperty("zk.zkConnect"))) {
			zkConfig.zkConnect = properties.getProperty("zk.zkConnect");
		}
		if (StringUtils.isNotBlank(properties.getProperty("zk.zkSessionTimeoutMs"))) {
			zkConfig.zkSessionTimeoutMs = Integer.parseInt(properties.getProperty("zk.zkSessionTimeoutMs"));
		}
		if (StringUtils.isNotBlank(properties.getProperty("zk.zkConnectionTimeoutMs"))) {
			zkConfig.zkConnectionTimeoutMs = Integer.parseInt(properties.getProperty("zk.zkConnectionTimeoutMs"));
		}
		if (StringUtils.isNotBlank(properties.getProperty("zk.zkSyncTimeMs"))) {
			zkConfig.zkSyncTimeMs = Integer.parseInt(properties.getProperty("zk.zkSyncTimeMs"));
		}
		return zkConfig;
	}

	/**
	 * Proxy properties.
	 *
	 * @param properties the properties
	 * @return the properties
	 */
	private Properties proxyProperties(final Properties properties) {
		return CglibProxyUtils.getProxyInstance(properties.getClass(), new MethodInterceptor() {

			@Override
			public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
				String methodName = method.getName();
				if ("getProperty".equals(methodName)) {
					String key = (String) args[0];
					return System.getProperty(key, properties.getProperty(key));
				}
				return method.invoke(properties, args);
			}
		});
	}

	/**
	 * After connect.
	 */
	protected void afterConnect() {

	}

	/**
	 * Before connect.
	 */
	protected void beforeConnect() {

	}

	/**
	 * After close.
	 */
	protected void afterClose() {

	}

	/**
	 * Before close.
	 */
	protected void beforeClose() {

	}

	/* (non-Javadoc)
	 * @see cn.com.summall.commons.search.config.ZooKeeperClient#create(java.lang.String)
	 */
	@Override
	public ZooKeeperClient create(String path) {
		create(path, null);
		return this;
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.commons.search.config.ZooKeeperClient#create(java.lang.String, byte[])
	 */
	@Override
	public ZooKeeperClient create(String path, Object data) {
		try {
			doCreate(getZkClient(), path, data);
		} catch (Exception e) {
			throw ExceptionUtils.unchecked(e);
		}
		return this;
	}

	/**
	 * Do create.
	 *
	 * @param zkClient the zk client
	 * @param path the path
	 * @param data the data
	 * @throws Exception the exception
	 */
	protected abstract void doCreate(ZkClient zkClient, String path, Object data) throws Exception;

	/* (non-Javadoc)
	 * @see cn.com.summall.commons.search.config.ZooKeeperClient#list(java.lang.String)
	 */
	@Override
	public List<String> list(String path) {
		try {
			return doList(getZkClient(), path);
		} catch (Exception e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * Do list.
	 *
	 * @param client the client
	 * @param path the path
	 * @return the list
	 * @throws Exception the exception
	 */
	protected abstract List<String> doList(ZkClient client, String path) throws Exception;

	/* (non-Javadoc)
	 * @see cn.com.summall.commons.search.config.ZooKeeperClient#delete(java.lang.String)
	 */
	@Override
	public ZooKeeperClient delete(String path) {
		try {
			doDelete(getZkClient(), path);
		} catch (Exception e) {
			throw ExceptionUtils.unchecked(e);
		}
		return this;
	}

	/**
	 * Do delete.
	 *
	 * @param zkClient the zk client
	 * @param path the path
	 * @throws Exception the exception
	 */
	protected abstract void doDelete(ZkClient zkClient, String path) throws Exception;

	/* (non-Javadoc)
	 * @see cn.com.summall.commons.search.config.ZooKeeperClient#get(java.lang.String)
	 */
	@Override
	public <T> T get(String path) {
		try {
			return doGet(getZkClient(), path);
		} catch (Exception e) {
			throw ExceptionUtils.unchecked(e);
		}
	}

	/**
	 * Do get.
	 *
	 * @param <T> the generic type
	 * @param zkClient the zk client
	 * @param path the path
	 * @return the t
	 * @throws KeeperException the keeper exception
	 * @throws InterruptedException the interrupted exception
	 */
	protected <T> T doGet(ZkClient zkClient, String path) throws KeeperException, InterruptedException {
		return zkClient.readData(path, true);
	}

}
