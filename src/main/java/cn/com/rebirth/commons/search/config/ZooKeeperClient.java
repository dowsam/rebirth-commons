/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons ZooKeeperClient.java 2012-7-6 10:22:16 l.xue.nong$$
 */
package cn.com.rebirth.commons.search.config;

import java.util.List;

/**
 * The Interface ZooKeeperClient.
 *
 * @author l.xue.nong
 */
public interface ZooKeeperClient {
	
	/**
	 * Creates the.
	 *
	 * @param path the path
	 * @return the zoo keeper client
	 */
	public ZooKeeperClient create(String path);

	/**
	 * Creates the.
	 *
	 * @param path the path
	 * @param data the data
	 * @return the zoo keeper client
	 */
	public ZooKeeperClient create(String path, Object data);

	/**
	 * Gets the.
	 *
	 * @param <T> the generic type
	 * @param path the path
	 * @return the t
	 */
	public <T> T get(String path);

	/**
	 * List.
	 *
	 * @param path the path
	 * @return the list
	 */
	public List<String> list(String path);

	/**
	 * Delete.
	 *
	 * @param path the path
	 * @return the zoo keeper client
	 */
	public ZooKeeperClient delete(String path);

}
