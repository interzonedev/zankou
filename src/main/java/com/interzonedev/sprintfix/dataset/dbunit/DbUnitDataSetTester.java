package com.interzonedev.sprintfix.dataset.dbunit;

import java.io.File;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.Assertion;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.springframework.beans.factory.annotation.Autowired;

import com.interzonedev.sprintfix.dataset.helper.DataSetHelper;

public class DbUnitDataSetTester {

	private Log log = LogFactory.getLog(getClass());

	@Autowired
	private DataSetHelper dataSetHelper;

	public void compareDataSetsIgnoreColumns(DataSource dataSource, String expectedDataSetFilename, String tableName,
			List<String> ignoreColumnNames) {
		IDatabaseConnection databaseConnection = null;

		try {
			if (null == ignoreColumnNames) {
				ignoreColumnNames = Collections.emptyList();
			}

			databaseConnection = DbUnitUtils.getDatabaseConnection(dataSource);

			IDataSet actualDataSet = databaseConnection.createDataSet();

			File expectedDataSetFile = dataSetHelper.getDataSetFile(expectedDataSetFilename);

			IDataSet expectedDataSet = DbUnitUtils.getDataSet(expectedDataSetFile);

			Assertion.assertEqualsIgnoreCols(expectedDataSet, actualDataSet, tableName,
					ignoreColumnNames.toArray(new String[] {}));
		} catch (Throwable t) {
			String errorMessage = "compareDataSetsIgnoreColumns: Error comparing datasets";
			log.error(errorMessage, t);
			throw new RuntimeException(errorMessage, t);
		} finally {
			if (null != databaseConnection) {
				try {
					databaseConnection.close();
				} catch (SQLException e) {
					String errorMessage = "compareDataSetsIgnoreColumns: Error closing database connection";
					log.error(errorMessage, e);
					throw new RuntimeException(errorMessage, e);
				}
			}
		}
	}
}
