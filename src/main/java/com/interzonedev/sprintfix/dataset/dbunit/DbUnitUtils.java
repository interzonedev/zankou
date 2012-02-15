package com.interzonedev.sprintfix.dataset.dbunit;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.datatype.IDataTypeFactory;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.db2.Db2DataTypeFactory;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.ext.mckoi.MckoiDataTypeFactory;
import org.dbunit.ext.mssql.MsSqlDataTypeFactory;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.ext.oracle.OracleDataTypeFactory;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;
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

		IDataTypeFactory dataTypeFactory = getDataTypeFactoryForConnection(connection);

		if (null != dataTypeFactory) {
			DatabaseConfig dbConfig = databaseConnection.getConfig();
			dbConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, dataTypeFactory);
		}

		return databaseConnection;
	}

	public static IDataSet getDataSet(File dataSetFile) throws MalformedURLException, DataSetException {
		FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
		FlatXmlDataSet flatXmlDataSet = builder.build(dataSetFile);
		return flatXmlDataSet;
	}

	private static IDataTypeFactory getDataTypeFactoryForConnection(Connection connection) throws SQLException {
		DatabaseMetaData metaData = connection.getMetaData();
		String databaseProductName = metaData.getDatabaseProductName().toLowerCase();

		IDataTypeFactory dataTypeFactory = null;

		if ("oracle".toLowerCase().equals(databaseProductName)) {
			dataTypeFactory = new OracleDataTypeFactory();
		} else if ("PostgreSQL".toLowerCase().equals(databaseProductName)) {
			dataTypeFactory = new PostgresqlDataTypeFactory();
		} else if ("mysql".toLowerCase().equals(databaseProductName)) {
			dataTypeFactory = new MySqlDataTypeFactory();
		} else if ("mssql".toLowerCase().equals(databaseProductName)
				|| "Microsoft SQL Server".toLowerCase().equals(databaseProductName)) {
			dataTypeFactory = new MsSqlDataTypeFactory();
		} else if ("Mckoi".toLowerCase().equals(databaseProductName)) {
			dataTypeFactory = new MckoiDataTypeFactory();
		} else if ("hsql".toLowerCase().equals(databaseProductName)) {
			dataTypeFactory = new HsqldbDataTypeFactory();
		} else if ("h2".toLowerCase().equals(databaseProductName)) {
			dataTypeFactory = new H2DataTypeFactory();
		} else if ("db2".toLowerCase().equals(databaseProductName)) {
			dataTypeFactory = new Db2DataTypeFactory();
		}

		return dataTypeFactory;
	}
}
