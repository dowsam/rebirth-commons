/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-commons ZooKeeperExpand.java 2012-3-30 14:47:02 l.xue.nong$$
 */
package cn.com.restart.commons.search.config.support;

import java.io.IOException;
import java.util.List;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.KeeperException;

import cn.com.restart.commons.search.config.AbstractZooKeeperClient;
import cn.com.restart.commons.search.config.ZooKeeperClient;
import cn.com.restart.commons.utils.ZkUtils;
import cn.com.restart.commons.utils.ZkUtils.ZKConfig;

/**
 * The Class ZooKeeperExpand.
 *
 * @author l.xue.nong
 */
public class ZooKeeperExpand extends AbstractZooKeeperClient implements ZooKeeperClient {

	/**
	 * Gets the single instance of ZooKeeperExpand.
	 *
	 * @return single instance of ZooKeeperExpand
	 */
	public static ZooKeeperExpand getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * The Class SingletonHolder.
	 *
	 * @author l.xue.nong
	 */
	private static class SingletonHolder {

		/** The instance. */
		static ZooKeeperExpand instance = new ZooKeeperExpand();
	}

	/**
	 * Instantiates a new zoo keeper expand.
	 */
	private ZooKeeperExpand() {
		super(null);
	}

	/**
	 * Instantiates a new zoo keeper expand.
	 *
	 * @param config the config
	 */
	public ZooKeeperExpand(ZKConfig config) {
		super(config);
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.commons.search.config.AbstractZooKeeperClient#doCreate(org.I0Itec.zkclient.ZkClient, java.lang.String, java.lang.Object)
	 */
	@Override
	protected void doCreate(ZkClient zkClient, String path, Object data) throws Exception {
		if (!ZkUtils.pathExists(zkClient, path)) {
			ZkUtils.createEphemeralPath(zkClient, path, data);
		} else {
			ZkUtils.updateEphemeralPath(zkClient, path, data);
		}
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.commons.search.config.AbstractZooKeeperClient#doList(org.I0Itec.zkclient.ZkClient, java.lang.String)
	 */
	@Override
	protected List<String> doList(ZkClient client, String path) throws Exception {
		return ZkUtils.getChildren(client, path);
	}

	/* (non-Javadoc)
	 * @see cn.com.summall.commons.search.config.AbstractZooKeeperClient#doDelete(org.I0Itec.zkclient.ZkClient, java.lang.String)
	 */
	@Override
	protected void doDelete(ZkClient zkClient, String path) throws Exception {
		ZkUtils.deletePathRecursive(zkClient, path);
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws KeeperException the keeper exception
	 * @throws InterruptedException the interrupted exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException the class not found exception
	 */
	public static void main(String[] args) throws KeeperException, InterruptedException, IOException,
			ClassNotFoundException {
		ZooKeeperExpand zooKeeperExpand = new ZooKeeperExpand();
		printelnList(zooKeeperExpand);
		//		zooKeeperExpand.create("/demo/test");
		//		printelnList(zooKeeperExpand);
		//		zooKeeperExpand.create("/demo1/test1/test3/test4");
		//		printelnList(zooKeeperExpand);
		System.out.println(ZkUtils.pathExists(zooKeeperExpand.getZkClient(), "/summall/search/server/config"));
		//zooKeeperExpand.create("/summall/search/server/config");
		Object object = zooKeeperExpand.get("/summall/search/server/config2");
		System.out.println(object);
		Object object1 = zooKeeperExpand.get("/summall/search/server/config");
		System.out.println(object1);
	}

	/**
	 * Printeln list.
	 *
	 * @param zooKeeperExpand the zoo keeper expand
	 */
	private static void printelnList(ZooKeeperExpand zooKeeperExpand) {
		List<String> strings = zooKeeperExpand.list("/");
		for (String string : strings) {
			System.out.println(string);
		}
	}
}
