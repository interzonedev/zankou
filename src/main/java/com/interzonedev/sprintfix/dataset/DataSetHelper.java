package com.interzonedev.sprintfix.dataset;

import java.io.File;
import java.net.URI;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DataSetHelper {
	private static Log log = LogFactory.getLog(DataSetHelper.class);

	public static File getDataSetFile(String dataSetFilename) {
		ClassLoader classloader = null;
		try {
			classloader = Thread.currentThread().getContextClassLoader();
		} catch (Throwable t1) {
		}

		if (null == classloader) {
			classloader = DataSetHelper.class.getClassLoader();
		}

		try {
			URL url = classloader.getResource(dataSetFilename);
			URI uri = url.toURI();
			File file = new File(uri);
			return file;
		} catch (Throwable t2) {
			String errorMessage = "getDataSetFile: Error getting dataset file";
			log.error(errorMessage, t2);
			throw new RuntimeException(errorMessage, t2);
		}
	}
}
