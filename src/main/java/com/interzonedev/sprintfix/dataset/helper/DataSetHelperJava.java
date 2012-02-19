package com.interzonedev.sprintfix.dataset.helper;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DataSetHelperJava implements DataSetHelper {
	private Log log = LogFactory.getLog(getClass());

	@Override
	public File getDataSetFile(String dataSetFilename) {
		try {
			URL url = getClass().getClassLoader().getResource(dataSetFilename);
			URI uri = url.toURI();
			File file = new File(uri);
			return file;
		} catch (URISyntaxException e) {
			String errorMessage = "getDataSetFile: Error getting dataset file";
			log.error(errorMessage, e);
			throw new RuntimeException(errorMessage, e);
		}
	}

}
