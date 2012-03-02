package com.interzonedev.sprintfix.dataset.dbunit;

import java.io.File;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.operation.DatabaseOperation;

import com.interzonedev.sprintfix.dataset.handler.DataSetHandler;
import com.interzonedev.sprintfix.dataset.transformer.DataSetTransformer;

public class DbUnitDataSetHandler implements DataSetHandler {

	private Log log = LogFactory.getLog(getClass());

	@Override
	public void cleanAndInsertData(File dataSetFile, Object dataSourceInstance, DataSetTransformer dataSetTransformer) {
		try {
			DataSource dataSource = (DataSource) dataSourceInstance;
			doDatabaseOperation(DatabaseOperation.CLEAN_INSERT, dataSource, dataSetFile);
		} catch (Throwable t) {
			String errorMessage = "cleanAndInsertData: Error setting up database";
			log.error(errorMessage, t);
			throw new RuntimeException(errorMessage, t);
		}
	}

	@Override
	public void cleanData(File dataSetFile, Object dataSourceInstance) {
		try {
			DataSource dataSource = (DataSource) dataSourceInstance;
			doDatabaseOperation(DatabaseOperation.DELETE_ALL, dataSource, dataSetFile);
		} catch (Throwable t) {
			String errorMessage = "cleanData: Error tearing down database";
			log.error(errorMessage, t);
			throw new RuntimeException(errorMessage, t);
		}
	}

	private void doDatabaseOperation(DatabaseOperation databaseOperation, DataSource dataSource, File dataSetFile) {
		IDatabaseConnection databaseConnection = null;

		try {
			databaseConnection = DbUnitUtils.getDatabaseConnection(dataSource);
			IDataSet dataSet = DbUnitUtils.getDataSet(dataSetFile);
			databaseOperation.execute(databaseConnection, dataSet);
		} catch (Throwable t) {
			String errorMessage = "doDatabaseOperation: Error performing database operation: " + databaseOperation;
			log.error(errorMessage, t);
			throw new RuntimeException(errorMessage, t);
		} finally {
			if (null != databaseConnection) {
				try {
					databaseConnection.close();
					log.debug("doDatabaseOperation: Closed connection " + databaseConnection + " from " + dataSource);
				} catch (SQLException e) {
					String errorMessage = "doDatabaseOperation: Error closing database connection";
					log.error(errorMessage, e);
					throw new RuntimeException(errorMessage, e);
				}
			}
		}
	}
}
