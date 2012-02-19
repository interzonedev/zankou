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

/**
 * Static utilities helper class for getting database connection and data sets for DbUnit based tests.
 * 
 * @author mark@interzonedev.com
 */
public class DbUnitUtils {
	private static Log log = LogFactory.getLog(DbUnitUtils.class);

	/**
	 * Gets an {@code IDatabaseConnection} wrapped connection to the database represented by the specified
	 * {@code DataSource}.
	 * 
	 * @param dataSource
	 *            - The JDBC {@code DataSource} that represents the database to which to open the connection.
	 * 
	 * @return Returns an {@code IDatabaseConnection} wrapped connection to the database represented by the specified
	 *         {@code DataSource}.
	 * 
	 * @throws SQLException
	 * @throws DatabaseUnitException
	 */
	protected static IDatabaseConnection getDatabaseConnection(DataSource dataSource) throws SQLException,
			DatabaseUnitException {
		log.debug("getDatabaseConnection: Get connection from " + dataSource);

		Connection connection = dataSource.getConnection();

		IDatabaseConnection databaseConnection = new DatabaseConnection(connection);

		IDataTypeFactory dataTypeFactory = getDataTypeFactoryForConnection(connection);

		if (null != dataTypeFactory) {
			DatabaseConfig dbConfig = databaseConnection.getConfig();
			dbConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, dataTypeFactory);
		}

		return databaseConnection;
	}

	/**
	 * Get an {@code IDataSet} representation of the specified DbUnit XML dataset file.
	 * 
	 * @param dataSetFile
	 *            - The DbUnit XML dataset file to turn into an {@code IDataSet} instance.
	 * 
	 * @return Returns an {@code IDataSet} representation of the specified DbUnit XML dataset file.
	 * 
	 * @throws MalformedURLException
	 * @throws DataSetException
	 */
	protected static IDataSet getDataSet(File dataSetFile) throws MalformedURLException, DataSetException {
		FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
		FlatXmlDataSet flatXmlDataSet = builder.build(dataSetFile);
		return flatXmlDataSet;
	}

	/**
	 * Gets a specific {@code IDataTypeFactory} instance for the underlying database to which the specified
	 * {@code Connection} instance is connected. The {@code IDataTypeFactory} is chosen according to the specific
	 * database vendor to which the {@code Connection} is connected.
	 * 
	 * @param connection
	 *            - The {@code Connection} instance from which to determine the specific database vendor.
	 * 
	 * @return Returns a specific {@code IDataTypeFactory} instance for the underlying database to which the specified
	 *         {@code Connection} instance is connected. Returns null if the database vendor is not recognized.
	 * 
	 * @throws SQLException
	 */
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
		} else {
			log.warn("getDataTypeFactoryForConnection: Unrecognized database vendor");
		}

		return dataTypeFactory;
	}
}
