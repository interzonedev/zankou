package com.interzonedev.sprintfix.dataset;

import java.io.File;

import com.interzonedev.sprintfix.dataset.handler.DataSetHandler;

public class DataSetValues {
	private final DataSetHandler dataSetHandler;

	private final File dataSetFile;

	private final Object dataSourceBean;

	public DataSetValues(DataSetHandler dataSetHandler, File dataSetFile, Object dataSourceBean) {
		this.dataSetHandler = dataSetHandler;
		this.dataSetFile = dataSetFile;
		this.dataSourceBean = dataSourceBean;
	}

	public DataSetHandler getDataSetHandler() {
		return dataSetHandler;
	}

	public File getDataSetFile() {
		return dataSetFile;
	}

	public Object getDataSourceBean() {
		return dataSourceBean;
	}
}
