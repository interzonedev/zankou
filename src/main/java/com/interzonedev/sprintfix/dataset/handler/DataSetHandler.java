package com.interzonedev.sprintfix.dataset.handler;

import java.io.File;

import com.interzonedev.sprintfix.dataset.transformer.DataSetTransformer;

/**
 * Interface for seting up and tearing down the database for each test.
 * 
 * @author Mark Markarian - mark@interzonedev.com
 */
public interface DataSetHandler {

	/**
	 * Cleans and inserts the data represented in the specified dataset file from the database connected to by the
	 * specified data source. Each record in the dataset file is altered by the specified {@link DataSetTransformer} if
	 * it is set.
	 * 
	 * @param dataSetFile
	 *            The file that contains the data with which to set up the database.
	 * @param dataSourceInstance
	 *            A datasource that is connected to the database being tested.
	 * @param dataSetTransformer
	 *            An instance of {@link DataSetTransformer} that can alter each record parsed from the dataset file.
	 */
	public void cleanAndInsertData(File dataSetFile, Object dataSourceInstance, DataSetTransformer dataSetTransformer);

	/**
	 * Cleans the data represented in the specified dataset file from the database connected to by the specified data
	 * source.
	 * 
	 * @param dataSetFile
	 *            The file that contains the data to clean from the database.
	 * @param dataSourceInstance
	 *            A datasource that is connected to the database being tested.
	 */
	public void cleanData(File dataSetFile, Object dataSourceInstance);
}
