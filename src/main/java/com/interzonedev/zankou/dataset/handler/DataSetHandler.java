package com.interzonedev.zankou.dataset.handler;

import java.io.File;

import com.interzonedev.zankou.dataset.transformer.DataSetTransformer;

/**
 * Interface for seting up (clean and insert) and tearing down (clean) the database for each test. An implementation
 * needs to be provided for each specific datastore being tested.
 * 
 * @author Mark Markarian - mark@interzonedev.com
 */
public interface DataSetHandler {

	/**
	 * Cleans and inserts the data represented in the specified dataset file from the database connected to by the
	 * specified data source. Each record in the dataset file can be altered by the specified {@link DataSetTransformer}
	 * if it is set.
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
