package com.interzonedev.sprintfix.dataset;

import java.io.File;

import com.interzonedev.sprintfix.dataset.handler.DataSetHandler;
import com.interzonedev.sprintfix.dataset.transformer.DataSetTransformer;

public class DataSetValues {
	private final DataSetHandler dataSetHandler;

	private final File dataSetFile;

	private final Object dataSourceBean;

	private final DataSetTransformer dataSetTransformer;

	public DataSetValues(DataSetHandler dataSetHandler, File dataSetFile, Object dataSourceBean,
			DataSetTransformer dataSetTransformer) {
		this.dataSetHandler = dataSetHandler;
		this.dataSetFile = dataSetFile;
		this.dataSourceBean = dataSourceBean;
		this.dataSetTransformer = dataSetTransformer;
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

	public DataSetTransformer getDataSetTransformer() {
		return dataSetTransformer;
	}
}
