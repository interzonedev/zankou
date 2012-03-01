package com.interzonedev.sprintfix.dataset.mongo;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.DBObject;

public class MongoDataSetTester {

	private Log log = LogFactory.getLog(getClass());

	public void compareDataSetsIgnoreProperties(MongoTemplate mongoTemplate, String expectedDataSetFilename,
			String collectionName, List<String> ignorePropertyNames) {

		log.debug("compareDataSetsIgnoreProperties");

		try {
			DBObject expectedCollection = MongoUtils.getCollectionFromDataSet(
					expectedDataSetFilename, collectionName, ignorePropertyNames);

			DBObject actualCollection = MongoUtils.getCollectionFromDatabase(mongoTemplate, collectionName,
					ignorePropertyNames);

			log.debug("compareDataSetsIgnoreProperties: expectedCollection = " + expectedCollection);
			log.debug("compareDataSetsIgnoreProperties: actualCollection = " + actualCollection);

			Assert.assertEquals(expectedCollection, actualCollection);
		} catch (Throwable t) {
			String errorMessage = "";
			log.error(errorMessage, t);
			throw new RuntimeException(errorMessage, t);
		}
	}
}
