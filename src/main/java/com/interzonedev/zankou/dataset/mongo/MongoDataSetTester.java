package com.interzonedev.zankou.dataset.mongo;

import java.util.List;

import org.junit.Assert;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import ch.qos.logback.classic.Logger;

import com.mongodb.DBObject;

/**
 * Provides the ability to compare expected values against a real MongoDB database.
 * 
 * @author Mark Markarian - mark@interzonedev.com
 */
public class MongoDataSetTester {

	private final Logger log = (Logger) LoggerFactory.getLogger(getClass());

	/**
	 * Compares the expected values in the specified dataset file in the specified collection against the MongoDB
	 * database to which the specified {@code MongoTemplate} has a connection. The expected values are parsed into a
	 * MongoDB collection which is compared to the actual collection retrieved from the real database. A
	 * {@code java.lang.AssertionError} (or subclass thereof) is thrown (via {@code org.junit.Assert}) if the
	 * comparision fails.
	 * 
	 * @param mongoTemplate
	 *            The {@code MongoTemplate} that has a connection to the MongoDB database.
	 * @param expectedDataSetFilename
	 *            The file name of the dataset file that contains the expected values.
	 * @param collectionName
	 *            The name of the collection to compare.
	 * @param ignorePropertyNames
	 *            The list of properties to ignore in the specified collection.
	 */
	public void compareDataSetsIgnoreProperties(MongoTemplate mongoTemplate, String expectedDataSetFilename,
			String collectionName, List<String> ignorePropertyNames) {

		log.debug("compareDataSetsIgnoreProperties");

		try {
			DBObject expectedCollection = MongoUtils.getCollectionFromDataSet(expectedDataSetFilename, collectionName,
					ignorePropertyNames);

			DBObject actualCollection = MongoUtils.getCollectionFromDatabase(mongoTemplate, collectionName,
					ignorePropertyNames);

			log.debug("compareDataSetsIgnoreProperties: expectedCollection = " + expectedCollection);
			log.debug("compareDataSetsIgnoreProperties: actualCollection = " + actualCollection);

			Assert.assertEquals(expectedCollection, actualCollection);
		} catch (Throwable t) {
			String errorMessage = "Error parsing expected values from the dataset file or retrieving actual values from the database";
			log.error(errorMessage, t);
			throw new RuntimeException(errorMessage, t);
		}
	}
}
