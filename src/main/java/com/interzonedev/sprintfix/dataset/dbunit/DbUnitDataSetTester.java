package com.interzonedev.sprintfix.dataset.dbunit;

import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

public interface DbUnitDataSetTester {

	public void compareDataSetsIgnoreColumns(DataSource dataSource, String expectedDataSetFilename, String tableName,
			List<String> ignoreColumnNames);

	public int compareDatesToTheSecond(Date first, Date second);
}
