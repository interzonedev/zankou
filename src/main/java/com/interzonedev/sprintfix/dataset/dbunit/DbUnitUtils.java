package com.interzonedev.sprintfix.dataset.dbunit;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;

public class DbUnitUtils {
	private static Log log = LogFactory.getLog(DbUnitUtils.class);

	public static void doDatabaseOperation(DatabaseOperation databaseOperation, DataSource dataSource, File dataSetFile) {
		IDatabaseConnection databaseConnection = null;

		try {
			databaseConnection = getDatabaseConnection(dataSource);
			IDataSet dataSet = getDataSet(dataSetFile);
			databaseOperation.execute(databaseConnection, dataSet);
		} catch (Throwable t) {
			String errorMessage = "doDatabaseOperation: Error performing database operation: " + databaseOperation;
			log.error(errorMessage, t);
			throw new RuntimeException(errorMessage, t);
		} finally {
			if (null != databaseConnection) {
				try {
					databaseConnection.close();
				} catch (SQLException e) {
					String errorMessage = "doDatabaseOperation: Error closing database connection";
					log.error(errorMessage, e);
					throw new RuntimeException(errorMessage, e);
				}
			}
		}
	}

	public static IDatabaseConnection getDatabaseConnection(DataSource dataSource) throws SQLException,
			DatabaseUnitException {
		Connection connection = dataSource.getConnection();

		IDatabaseConnection databaseConnection = new DatabaseConnection(connection);

		return databaseConnection;
	}

	public static IDataSet getDataSet(File dataSetFile) throws MalformedURLException, DataSetException {
		FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
		FlatXmlDataSet flatXmlDataSet = builder.build(dataSetFile);
		return flatXmlDataSet;
	}
}
