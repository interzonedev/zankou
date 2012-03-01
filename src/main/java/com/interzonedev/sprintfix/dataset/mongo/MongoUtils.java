package com.interzonedev.sprintfix.dataset.mongo;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.interzonedev.sprintfix.TestUtils;
import com.interzonedev.sprintfix.dataset.DataSetHelper;
import com.mongodb.BasicDBList;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class MongoUtils {
	public static final String DATABASE_PRIMARY_KEY_PROPERY_NAME = "_id";
	public static final String DATABASE_PRIMARY_KEY_OPERATOR_NAME = "$oid";
	public static final String DATABASE_TIME_CREATED_PROPERY_NAME = "time_created";
	public static final String DATABASE_TIME_UPDATED_PROPERY_NAME = "time_updated";
	public static final String DATASET_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

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

	public static void transformCollectionItemFromDataSet(DBObject collectionItem) {
		transformPrimaryKeyFromDataSet(collectionItem);
		transformTimestampFromDataSet(collectionItem, MongoUtils.DATABASE_TIME_CREATED_PROPERY_NAME);
		transformTimestampFromDataSet(collectionItem, MongoUtils.DATABASE_TIME_UPDATED_PROPERY_NAME);
	}
	
	public static void transformPrimaryKeyFromDataSet(DBObject dbObj) {
		String id = (String) dbObj.removeField(DATABASE_PRIMARY_KEY_PROPERY_NAME);
		if (StringUtils.isBlank(id)) {
			return;
		}
		dbObj.put(DATABASE_PRIMARY_KEY_PROPERY_NAME, new ObjectId(id));
	}

	public static void transformTimestampFromDataSet(DBObject dbObj, String property) {
		String dateString = (String) dbObj.get(property);
		if (StringUtils.isBlank(dateString)) {
			return;
		}
		Date date = TestUtils.parseDateInDefaultTimeZone(dateString, DATASET_DATE_FORMAT);
		Long dateAsMillis = date.getTime();
		dbObj.put(property, dateAsMillis);
	}
}
