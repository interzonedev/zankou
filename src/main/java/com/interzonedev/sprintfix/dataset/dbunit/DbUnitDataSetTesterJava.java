package com.interzonedev.sprintfix.dataset.dbunit;

import com.interzonedev.sprintfix.dataset.helper.DataSetHelper;
import com.interzonedev.sprintfix.dataset.helper.DataSetHelperJava;

public class DbUnitDataSetTesterJava extends AbstractDbUnitDataSetTester {

	private DataSetHelper dataSetHelper;

	public DbUnitDataSetTesterJava() {
		this.dataSetHelper = new DataSetHelperJava();
	}

	@Override
	protected DataSetHelper getDataSetHelper() {
		return dataSetHelper;
	}
}
