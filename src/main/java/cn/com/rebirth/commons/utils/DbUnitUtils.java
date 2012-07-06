/*
 * Copyright (c) 2005-2012 www.china-cti.com All rights reserved
 * Info:rebirth-commons DbUnitUtils.java 2012-7-6 10:22:14 l.xue.nong$$
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
	 * Load data.
	 *
	 * @param h2DataSource the h2 data source
	 * @param xmlFilePaths the xml file paths
	 * @throws Exception the exception
	 */
	public static void loadData(DataSource h2DataSource, String... xmlFilePaths) throws Exception {
		execute(DatabaseOperation.CLEAN_INSERT, h2DataSource, xmlFilePaths);
	}

	/**
	 * Append data.
	 *
	 * @param h2DataSource the h2 data source
	 * @param xmlFilePaths the xml file paths
	 * @throws Exception the exception
	 */
	public static void appendData(DataSource h2DataSource, String... xmlFilePaths) throws Exception {
		execute(DatabaseOperation.INSERT, h2DataSource, xmlFilePaths);
	}

	/**
	 * Removes the data.
	 *
	 * @param h2DataSource the h2 data source
	 * @param xmlFilePaths the xml file paths
	 * @throws Exception the exception
	 */
	public static void removeData(DataSource h2DataSource, String... xmlFilePaths) throws Exception {
		execute(DatabaseOperation.DELETE_ALL, h2DataSource, xmlFilePaths);
	}

	/**
	 * Execute.
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
