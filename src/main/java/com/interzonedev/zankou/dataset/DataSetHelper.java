package com.interzonedev.zankou.dataset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

/**
 * Static utility methods for accessing dataset files and contents.
 * 
 * @author Mark Markarian - mark@interzonedev.com
 */
public class DataSetHelper {

    private static final Logger log = LoggerFactory.getLogger(DataSetHelper.class);

    /**
     * Gets the {@code File} on the classpath with the specified name.
     * 
     * @param dataSetFilename The name of the {@code File} to retrieve.
     * 
     * @return Returns the {@code File} on the classpath with the specified name.
     */
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

    /**
     * Gets the contents of the specifed {@code File}.
     * 
     * @param file The {@code File} to get the contents of.
     * 
     * @return Returns the contents of the specifed {@code File}.
     */
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
