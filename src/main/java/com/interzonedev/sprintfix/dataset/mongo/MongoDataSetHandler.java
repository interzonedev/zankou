package com.interzonedev.sprintfix.dataset.mongo;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.interzonedev.sprintfix.dataset.DataSetHelper;
import com.interzonedev.sprintfix.dataset.handler.DataSetHandler;

public class MongoDataSetHandler implements DataSetHandler {

	private Log log = LogFactory.getLog(getClass());

	@Override
	public void cleanAndInsertData(File dataSetFile, Object dataSourceInstance) {
		log.debug("cleanAndInsertData");

		String fileContents = DataSetHelper.getFileContents(dataSetFile);

		log.debug("cleanAndInsertData: " + fileContents);
	}

	@Override
	public void cleanData(File dataSetFile, Object dataSourceInstance) {
		log.debug("cleanData");
	}

}
