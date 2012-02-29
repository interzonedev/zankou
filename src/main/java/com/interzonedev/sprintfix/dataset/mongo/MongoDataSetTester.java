package com.interzonedev.sprintfix.dataset.mongo;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Assert;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.interzonedev.sprintfix.dataset.DataSetHelper;
import com.mongodb.BasicDBList;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class MongoDataSetTester {

	private Log log = LogFactory.getLog(getClass());

	public void compareDataSetsIgnoreProperties(MongoTemplate mongoTemplate, String expectedDataSetFilename,
			String collectionName, List<String> ignorePropertyNames) {

		log.debug("compareDataSetsIgnoreProperties");

		try {
			DBObject expectedCollection = getExpectedDbObjectForCollectionFromDataSet(expectedDataSetFilename,
					collectionName, ignorePropertyNames);

			DBObject actualCollection = getActualDbObjectForCollection(mongoTemplate, collectionName,
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

	private DBObject getExpectedDbObjectForCollectionFromDataSet(String expectedDataSetFilename, String collectionName,
			List<String> ignorePropertyNames) throws JsonParseException, JsonMappingException, IOException {
		File expectedDataSetFile = DataSetHelper.getDataSetFile(expectedDataSetFilename);
		String expectedDataSetFileContents = DataSetHelper.getFileContents(expectedDataSetFile);

		DBObject expectedDbObject = (DBObject) JSON.parse(expectedDataSetFileContents);

		DBObject expectedCollection = (DBObject) expectedDbObject.get(collectionName);

		removeIgnorePropertyNames(expectedCollection, ignorePropertyNames);

		return expectedCollection;
	}

	private DBObject getActualDbObjectForCollection(MongoTemplate mongoTemplate, String collectionName,
			List<String> ignorePropertyNames) {
		BasicDBList actualCollectionMembers = new BasicDBList();

		DBCollection actualCollection = mongoTemplate.getCollection(collectionName);
		DBCursor actualCollectionCursor = actualCollection.find();
		for (DBObject obj : actualCollectionCursor) {
			actualCollectionMembers.add(obj);
		}

		removeIgnorePropertyNames(actualCollectionMembers, ignorePropertyNames);

		return actualCollectionMembers;
	}

	private void removeIgnorePropertyNames(DBObject dbObj, List<String> ignorePropertyNames) {
		if (null == dbObj) {
			return;
		}
		boolean removeProps = false;
		for (String propertyName : dbObj.keySet()) {
			Object propertyValue = dbObj.get(propertyName);
			if (propertyValue instanceof DBObject) {
				removeIgnorePropertyNames((DBObject) propertyValue, ignorePropertyNames);
			} else {
				removeProps = true;
			}
		}
		if (removeProps) {
			for (String ignorePropertyName : ignorePropertyNames) {
				dbObj.removeField(ignorePropertyName);
			}
		}
	}
}
