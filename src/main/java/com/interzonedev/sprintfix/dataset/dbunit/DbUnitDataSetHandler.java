package com.interzonedev.sprintfix.dataset.dbunit;

import java.io.File;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.operation.DatabaseOperation;

import com.interzonedev.sprintfix.dataset.handler.DataSetHandler;

public class DbUnitDataSetHandler implements DataSetHandler {

	private Log log = LogFactory.getLog(getClass());

	@Override
	public void cleanAndInsertData(File dataSetFile, Object dataSourceBean) {
		try {
			DataSource dataSource = (DataSource) dataSourceBean;
			DbUnitUtils.doDatabaseOperation(DatabaseOperation.CLEAN_INSERT, dataSource, dataSetFile);
		} catch (Throwable t) {
			String errorMessage = "cleanAndInsertData: Error setting up database";
			log.error(errorMessage, t);
			throw new RuntimeException(errorMessage, t);
		}
	}

	@Override
	public void cleanData(File dataSetFile, Object dataSourceBean) {
		try {
			DataSource dataSource = (DataSource) dataSourceBean;
			DbUnitUtils.doDatabaseOperation(DatabaseOperation.DELETE_ALL, dataSource, dataSetFile);
		} catch (Throwable t) {
			String errorMessage = "cleanData: Error tearing down database";
			log.error(errorMessage, t);
			throw new RuntimeException(errorMessage, t);
		}
	}
}
