package com.interzonedev.sprintfix.dataset.handler;

import java.io.File;

import com.interzonedev.sprintfix.dataset.transformer.DataSetTransformer;

public interface DataSetHandler {
	public void cleanAndInsertData(File dataSetFile, Object dataSourceInstance, DataSetTransformer dataSetTransformer);

	public void cleanData(File dataSetFile, Object dataSourceInstance);
}
