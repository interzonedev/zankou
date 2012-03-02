package com.interzonedev.sprintfix.dataset.mongo;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.interzonedev.sprintfix.dataset.DataSetHelper;
import com.mongodb.BasicDBList;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class MongoUtils {

	public static DBObject getDBObjectFromFileContents(String fileContents) {
		DBObject dbObject = (DBObject) JSON.parse(fileContents);

		return dbObject;
	}

	public static Set<String> getCollectionNamesFromFileContents(String fileContents) {
		DBObject dbObject = getDBObjectFromFileContents(fileContents);

		return dbObject.keySet();
	}

	public static DBObject getCollectionFromDataSet(String dataSetFilename, String collectionName,
			List<String> ignorePropertyNames) throws JsonParseException, JsonMappingException, IOException {
		File dataSetFile = DataSetHelper.getDataSetFile(dataSetFilename);
		String dataSetFileContents = DataSetHelper.getFileContents(dataSetFile);

		DBObject dbObject = getDBObjectFromFileContents(dataSetFileContents);

		DBObject collection = (DBObject) dbObject.get(collectionName);

		removeIgnorePropertyNames(collection, ignorePropertyNames);

		return collection;
	}

	public static DBObject getCollectionFromDatabase(MongoTemplate mongoTemplate, String collectionName,
			List<String> ignorePropertyNames) {
		BasicDBList collectionMembers = new BasicDBList();

		DBCollection collection = mongoTemplate.getCollection(collectionName);
		DBCursor collectionCursor = collection.find();
		for (DBObject obj : collectionCursor) {
			collectionMembers.add(obj);
		}

		removeIgnorePropertyNames(collectionMembers, ignorePropertyNames);

		return collectionMembers;
	}

	public static void removeIgnorePropertyNames(DBObject dbObj, List<String> ignorePropertyNames) {
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
