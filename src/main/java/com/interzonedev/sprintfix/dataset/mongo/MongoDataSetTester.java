package com.interzonedev.sprintfix.dataset.mongo;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.interzonedev.sprintfix.dataset.DataSetHelper;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class MongoDataSetTester {

	private Log log = LogFactory.getLog(getClass());

	public void compareDataSetsIgnoreProperties(MongoTemplate mongoTemplate, String expectedDataSetFilename,
			String collectionName, List<String> ignorePropertyNames) {

		log.debug("compareDataSetsIgnoreProperties");

		try {
			File expectedDataSetFile = DataSetHelper.getDataSetFile(expectedDataSetFilename);
			String expectedDataSetFileContents = DataSetHelper.getFileContents(expectedDataSetFile);

			@SuppressWarnings("unchecked")
			Map<String, Object> expectedObjectMap = (new ObjectMapper()).readValue(expectedDataSetFileContents,
					HashMap.class);

			DBObject expectedDbObject = new BasicDBObject(expectedObjectMap);
			removeIgnorePropertyNames(expectedDbObject, ignorePropertyNames);

			DBObject actualDbObject = new BasicDBObject();
			BasicDBList collectionMembers = new BasicDBList();
			DBCollection locationsCollection = mongoTemplate.getCollection("locations");
			DBCursor cursor = locationsCollection.find();
			for (DBObject obj : cursor) {
				removeIgnorePropertyNames(obj, ignorePropertyNames);
				collectionMembers.add(obj);
			}
			actualDbObject.put(collectionName, collectionMembers);

			log.debug("compareDataSetsIgnoreProperties: " + expectedDbObject);

		} catch (Throwable t) {
			String errorMessage = "";
			log.error(errorMessage, t);
			throw new RuntimeException(errorMessage, t);
		}
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
