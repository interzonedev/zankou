package com.interzonedev.sprintfix.dataset.handler;

import java.io.File;

public interface DataSetHandler {
	public void cleanAndInsertData(File dataSetFile, Object dataSourceInstance);

	public void cleanData(File dataSetFile, Object dataSourceInstance);
}
