/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-commons ZkUtils.java 2012-3-19 16:23:59 l.xue.nong$$
 */
package cn.com.restart.commons.utils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ZkUtils.
 *
 * @author l.xue.nong
 */
public class ZkUtils {

	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(ZkUtils.class);

	/**
	 * make sure a persiste.nt path exists in ZK. Create the path if not exist.
	 *
	 * @param client the client
	 * @param path the path
	 * @throws Exception the exception
	 */
	public static void makeSurePersistentPathExists(final ZkClient client, final String path) throws Exception {
		if (!client.exists(path)) {
			try {
				client.createPersistent(path, true);
			} catch (final ZkNodeExistsException e) {
			} catch (final Exception e) {
				throw e;
			}

		}
	}

	/**
	 * create the parent path.
	 *
	 * @param client the client
	 * @param path the path
	 * @throws Exception the exception
	 */
	public static void createParentPath(final ZkClient client, final String path) throws Exception {
		final String parentDir = path.substring(0, path.lastIndexOf('/'));
		if (parentDir.length() != 0) {
			client.createPersistent(parentDir, true);
		}
	}

	/**
	 * Create an ephemeral node with the given path and data. Create parents if
	 * necessary.
	 *
	 * @param client the client
	 * @param path the path
	 * @param data the data
	 * @throws Exception the exception
	 */
	public static void createEphemeralPath(final ZkClient client, final String path, final Object data)
			throws Exception {
		try {
			client.createEphemeral(path, data);
		} catch (final ZkNoNodeException e) {
			createParentPath(client, path);
			client.createEphemeral(path, data);
		}
	}

	/**
	 * Create an ephemeral node with the given path and data. Throw
	 * NodeExistException if node already exists.
	 *
	 * @param client the client
	 * @param path the path
	 * @param data the data
	 * @throws Exception the exception
	 */
	public static void createEphemeralPathExpectConflict(final ZkClient client, final String path, final Object data)
			throws Exception {
		try {
			createEphemeralPath(client, path, data);
		} catch (final ZkNodeExistsException e) {

			// this canZkConfig happen when there is connection loss; make sure
			// the data
			// is what we intend to write
			String storedData = null;
			try {
				storedData = readData(client, path);
			} catch (final ZkNoNodeException e1) {
				// the node disappeared; treat as if node existed and let caller
				// handles this
			} catch (final Exception e2) {
				throw e2;
			}
			if (storedData == null || !storedData.equals(data)) {
				throw e;
			} else {
				// otherwise, the creation succeeded, return normally
				logger.info(path + " exists with value " + data + " during connection loss; this is ok");
			}
		} catch (final Exception e) {
			throw e;
		}

	}

	/**
	 * Update the value of a persistent node with the given path and data.
	 * create parrent directory if necessary. Never throw NodeExistException.
	 *
	 * @param client the client
	 * @param path the path
	 * @param data the data
	 * @throws Exception the exception
	 */
	public static void updatePersistentPath(final ZkClient client, final String path, final Object data)
			throws Exception {
		try {
			client.writeData(path, data);
		} catch (final ZkNoNodeException e) {
			createParentPath(client, path);
			client.createPersistent(path, data);
		} catch (final Exception e) {
			throw e;
		}
	}

	/**
	 * Read data.
	 *
	 * @param client the client
	 * @param path the path
	 * @return the string
	 */
	public static <T> T readData(final ZkClient client, final String path) {
		return client.readData(path);
	}

	/**
	 * Read data maybe null.
	 *
	 * @param client the client
	 * @param path the path
	 * @return the string
	 */
	public static <T> T readDataMaybeNull(final ZkClient client, final String path) {
		return client.readData(path, true);
	}

	/**
	 * Update the value of a persistent node with the given path and data.
	 * create parrent directory if necessary. Never throw NodeExistException.
	 *
	 * @param client the client
	 * @param path the path
	 * @param data the data
	 * @throws Exception the exception
	 */
	public static void updateEphemeralPath(final ZkClient client, final String path, final Object data)
			throws Exception {
		try {
			client.writeData(path, data);
		} catch (final ZkNoNodeException e) {

			createParentPath(client, path);
			client.createEphemeral(path, data);

		} catch (final Exception e) {
			throw e;
		}
	}

	/**
	 * Delete path.
	 *
	 * @param client the client
	 * @param path the path
	 * @throws Exception the exception
	 */
	public static void deletePath(final ZkClient client, final String path) throws Exception {
		try {
			client.delete(path);
		} catch (final ZkNoNodeException e) {
			logger.info(path + " deleted during connection loss; this is ok");
		} catch (final Exception e) {
			throw e;
		}
	}

	/**
	 * Delete path recursive.
	 *
	 * @param client the client
	 * @param path the path
	 * @throws Exception the exception
	 */
	public static void deletePathRecursive(final ZkClient client, final String path) throws Exception {
		try {
			client.deleteRecursive(path);
		} catch (final ZkNoNodeException e) {
			logger.info(path + " deleted during connection loss; this is ok");

		} catch (final Exception e) {
			throw e;
		}
	}

	/**
	 * Gets the children.
	 *
	 * @param client the client
	 * @param path the path
	 * @return the children
	 */
	public static List<String> getChildren(final ZkClient client, final String path) {
		return client.getChildren(path);
	}

	/**
	 * Gets the children maybe null.
	 *
	 * @param client the client
	 * @param path the path
	 * @return the children maybe null
	 */
	public static List<String> getChildrenMaybeNull(final ZkClient client, final String path) {
		try {
			return client.getChildren(path);
		} catch (final ZkNoNodeException e) {
			return null;
		}
	}

	/**
	 * Check if the given path exists.
	 *
	 * @param client the client
	 * @param path the path
	 * @return true, if successful
	 */
	public static boolean pathExists(final ZkClient client, final String path) {
		return client.exists(path);
	}

	/**
	 * Gets the last part.
	 *
	 * @param path the path
	 * @return the last part
	 */
	public static String getLastPart(final String path) {
		if (path == null) {
			return null;
		}
		final int index = path.lastIndexOf('/');
		if (index >= 0) {
			return path.substring(index + 1);
		} else {
			return null;
		}
	}

	/**
	 * The Class StringSerializer.
	 *
	 * @author l.xue.nong
	 */
	public static class StringSerializer implements ZkSerializer {

		/* (non-Javadoc)
		 * @see org.I0Itec.zkclient.serialize.ZkSerializer#deserialize(byte[])
		 */
		@Override
		public Object deserialize(final byte[] bytes) throws ZkMarshallingError {
			try {
				return new String(bytes, "utf-8");
			} catch (final UnsupportedEncodingException e) {
				throw new ZkMarshallingError(e);
			}
		}

		/* (non-Javadoc)
		 * @see org.I0Itec.zkclient.serialize.ZkSerializer#serialize(java.lang.Object)
		 */
		@Override
		public byte[] serialize(final Object data) throws ZkMarshallingError {
			try {
				return ((String) data).getBytes("utf-8");
			} catch (final UnsupportedEncodingException e) {
				throw new ZkMarshallingError(e);
			}
		}

	}

	/**
	 * The Class ZKConfig.
	 *
	 * @author l.xue.nong
	 */
	public static class ZKConfig implements Serializable {

		/** The Constant serialVersionUID. */
		static final long serialVersionUID = -1L;

		/** The zk root. */
		public String zkRoot = "/meta";

		/** If enable zookeeper. */
		public boolean zkEnable = true;

		/** ZK host string. */
		public String zkConnect;

		/** zookeeper session timeout. */
		public int zkSessionTimeoutMs = 30000;

		/** the max time that the client waits to establish a connection to zookeeper. */
		public int zkConnectionTimeoutMs = 30000;

		/** how far a ZK follower can be behind a ZK leader. */
		public int zkSyncTimeMs = 5000;

		/**
		 * Instantiates a new zK config.
		 *
		 * @param zkConnect the zk connect
		 * @param zkSessionTimeoutMs the zk session timeout ms
		 * @param zkConnectionTimeoutMs the zk connection timeout ms
		 * @param zkSyncTimeMs the zk sync time ms
		 */
		public ZKConfig(final String zkConnect, final int zkSessionTimeoutMs, final int zkConnectionTimeoutMs,
				final int zkSyncTimeMs) {
			super();
			this.zkConnect = zkConnect;
			this.zkSessionTimeoutMs = zkSessionTimeoutMs;
			this.zkConnectionTimeoutMs = zkConnectionTimeoutMs;
			this.zkSyncTimeMs = zkSyncTimeMs;
		}

		/**
		 * Instantiates a new zK config.
		 */
		public ZKConfig() {
			super();
		}

		/**
		 * Instantiates a new zK config.
		 *
		 * @param zkRoot the zk root
		 * @param zkConnect the zk connect
		 * @param zkSessionTimeoutMs the zk session timeout ms
		 * @param zkConnectionTimeoutMs the zk connection timeout ms
		 * @param zkSyncTimeMs the zk sync time ms
		 * @param zkEnable the zk enable
		 */
		public ZKConfig(final String zkRoot, final String zkConnect, final int zkSessionTimeoutMs,
				final int zkConnectionTimeoutMs, final int zkSyncTimeMs, final boolean zkEnable) {
			super();
			this.zkRoot = zkRoot;
			this.zkConnect = zkConnect;
			this.zkSessionTimeoutMs = zkSessionTimeoutMs;
			this.zkConnectionTimeoutMs = zkConnectionTimeoutMs;
			this.zkSyncTimeMs = zkSyncTimeMs;
			this.zkEnable = zkEnable;
		}
	}
}
