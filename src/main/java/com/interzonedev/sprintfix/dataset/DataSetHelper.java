package com.interzonedev.sprintfix.dataset;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

	public static String getFileContents(File file) {
		try {
			long fileSize = file.length();

			byte[] bytes = new byte[(int) fileSize];

			InputStream is = new FileInputStream(file);

			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}

			if (offset < bytes.length) {
				throw new IOException("Could not completely read file " + file.getPath());
			}

			String contents = new String(bytes);

			return contents;
		} catch (Throwable t) {
			String errorMessage = "getFileContents: Error getting file contents from " + file.getPath();
			log.error(errorMessage, t);
			throw new RuntimeException(errorMessage, t);
		}
	}
}
