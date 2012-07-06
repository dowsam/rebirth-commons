/*
 * Copyright (c) 2005-2012 www.summall.com.cn All rights reserved
 * Info:summall-commons DbUnitUtils.java 2012-2-2 10:43:34 l.xue.nong$$
 */
package cn.com.rebirth.commons.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.h2.H2Connection;
import org.dbunit.operation.DatabaseOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

/**
 * The Class DbUnitUtils.
 *
 * @author l.xue.nong
 */
public abstract class DbUnitUtils {

	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(DbUnitUtils.class);

	/** The resource loader. */
	private static ResourceLoader resourceLoader = new DefaultResourceLoader();

	/**
	 * 初始化XML数据文件到H2数据库, XML数据文件中涉及的表在插入数据前先进行清除.
	 *
	 * @param h2DataSource the h2 data source
	 * @param xmlFilePaths the xml file paths
	 * @throws Exception the exception
	 */
	public static void loadData(DataSource h2DataSource, String... xmlFilePaths) throws Exception {
		execute(DatabaseOperation.CLEAN_INSERT, h2DataSource, xmlFilePaths);
	}

	/**
	 * 插入XML数据文件到H2数据库.
	 *
	 * @param h2DataSource the h2 data source
	 * @param xmlFilePaths the xml file paths
	 * @throws Exception the exception
	 */
	public static void appendData(DataSource h2DataSource, String... xmlFilePaths) throws Exception {
		execute(DatabaseOperation.INSERT, h2DataSource, xmlFilePaths);
	}

	/**
	 * 在H2数据库中删除XML数据文件中涉及的表的数据.
	 *
	 * @param h2DataSource the h2 data source
	 * @param xmlFilePaths the xml file paths
	 * @throws Exception the exception
	 */
	public static void removeData(DataSource h2DataSource, String... xmlFilePaths) throws Exception {
		execute(DatabaseOperation.DELETE_ALL, h2DataSource, xmlFilePaths);
	}

	/**
	 * 在ClassPath中查找XML数据文件并执行DBUnit Operation.
	 *
	 * @param operation the operation
	 * @param h2DataSource the h2 data source
	 * @param xmlFilePaths the xml file paths
	 * @throws DatabaseUnitException the database unit exception
	 * @throws SQLException the sQL exception
	 */
	private static void execute(DatabaseOperation operation, DataSource h2DataSource, String... xmlFilePaths)
			throws DatabaseUnitException, SQLException {
		IDatabaseConnection connection = new H2Connection(h2DataSource.getConnection(), "");
		for (String xmlPath : xmlFilePaths) {
			try {
				InputStream input = resourceLoader.getResource(xmlPath).getInputStream();
				IDataSet dataSet = new FlatXmlDataSetBuilder().setColumnSensing(true).build(input);
				operation.execute(connection, dataSet);
			} catch (IOException e) {
				logger.warn(xmlPath + " file not found", e);
			}
		}
	}
}
