package com.interzonedev.sprintfix.dataset.dbunit;

import java.io.File;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dbunit.Assertion;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;

import com.interzonedev.sprintfix.dataset.DataSetHelper;

public class DbUnitDataSetTester {

	private Log log = LogFactory.getLog(getClass());

	/**
	 * Compares the specified expected data set with the actual data in the database. Ignores the specified columns.
	 * 
	 * @param dataSource
	 *            - The JDBC {@code DataSource} that represents the database to from which to get the actual data.
	 * @param expectedDataSetFilename
	 *            - The file name of the DbUnit XML data set to compare against the actual data.
	 * @param tableName
	 *            - The name of the table in the database and data set to compare.
	 * @param ignoreColumnNames
	 *            - A {@code List} of column names to be ignored in the comparison.
	 */
	public void compareDataSetsIgnoreColumns(DataSource dataSource, String expectedDataSetFilename, String tableName,
			List<String> ignoreColumnNames) {
		IDatabaseConnection databaseConnection = null;

		try {
			if (null == ignoreColumnNames) {
				ignoreColumnNames = Collections.emptyList();
			}

			databaseConnection = DbUnitUtils.getDatabaseConnection(dataSource);

			IDataSet actualDataSet = databaseConnection.createDataSet();

			File expectedDataSetFile = DataSetHelper.getDataSetFile(expectedDataSetFilename);

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
					log.debug("compareDataSetsIgnoreColumns: Closed connection " + databaseConnection + " from "
							+ dataSource);
				} catch (SQLException e) {
					String errorMessage = "compareDataSetsIgnoreColumns: Error closing database connection";
					log.error(errorMessage, e);
					throw new RuntimeException(errorMessage, e);
				}
			}
		}
	}

	/**
	 * Uses the {@code java.util.Date#compareTo(java.util.Date)} method to compare the specified dates after setting the
	 * milliseconds to zero on both.
	 * 
	 * @param first
	 *            - The first {@code java.util.Date} to compare.
	 * @param second
	 *            - The first {@code java.util.Date} to compare.
	 * 
	 * @return Returns 0 if the first and seconds dates are equal down to the second. Returns less than 0 if the first
	 *         date is before the seconds date down to the second. Returns greater than 0 if the first date is after the
	 *         second date down to the second.
	 */
	public int compareDatesToTheSecond(Date first, Date second) {
		Calendar firstCalendar = new GregorianCalendar();
		firstCalendar.setTime(first);
		firstCalendar.set(Calendar.MILLISECOND, 0);
		Date firstNoMillis = firstCalendar.getTime();

		Calendar secondCalendar = new GregorianCalendar();
		secondCalendar.setTime(second);
		secondCalendar.set(Calendar.MILLISECOND, 0);
		Date secondNoMillis = secondCalendar.getTime();

		int compare = firstNoMillis.compareTo(secondNoMillis);

		return compare;
	}
}
