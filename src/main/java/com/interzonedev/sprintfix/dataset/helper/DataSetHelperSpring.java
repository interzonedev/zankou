package com.interzonedev.sprintfix.dataset.helper;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

public class DataSetHelperSpring implements DataSetHelper {
	private Log log = LogFactory.getLog(getClass());

	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public final File getDataSetFile(String dataSetFilename) {
		try {
			Resource dataSetResource = applicationContext.getResource("classpath:" + dataSetFilename);
			File dataSetFile = dataSetResource.getFile();
			return dataSetFile;
		} catch (Exception e) {
			String errorMessage = "getDataSetFile: Error getting dataset file from " + dataSetFilename;
			log.error(errorMessage, e);
			throw new RuntimeException(errorMessage, e);
		}
	}
}
