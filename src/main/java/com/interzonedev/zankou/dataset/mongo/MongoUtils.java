package com.interzonedev.zankou.dataset.mongo;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.interzonedev.zankou.dataset.DataSetHelper;
import com.mongodb.BasicDBList;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

/**
 * Static utility methods for MongoDB operations.
 * 
 * @author Mark Markarian - mark@interzonedev.com
 */
public class MongoUtils {

	/**
	 * Parses the specified content into a {@code DBObject} instance.
	 * 
	 * @param fileContents
	 *            The contents to parse into the {@code DBObject} instance.
	 * 
	 * @return Returns a {@code DBObject} instance that represents the structure of the specified contents.
	 */
	public static DBObject getDBObjectFromFileContents(String fileContents) {
		DBObject dbObject = (DBObject) JSON.parse(fileContents);

		return dbObject;
	}

	/**
	 * Gets the collection names from the resultant {@code DBObject} instance parsed from the specified contents.
	 * 
	 * @param fileContents
	 *            The contents from which to get the collection names.
	 * 
	 * @return Returns the collection names from the resultant {@code DBObject} instance parsed from the specified
	 *         contents.
	 */
	public static Set<String> getCollectionNamesFromFileContents(String fileContents) {
		DBObject dbObject = getDBObjectFromFileContents(fileContents);

		return dbObject.keySet();
	}

	/**
	 * Get the collection present in the specified data set file with the specified name. Property names in the ignore
	 * list are removed from the collection.
	 * 
	 * @param dataSetFilename
	 *            The name of the dataset file to parse.
	 * @param collectionName
	 *            The name of the collection to parse from the dataset file.
	 * @param ignorePropertyNames
	 *            The list of properties to remove from the collection.
	 * 
	 * @return Returns the collection present in the specified data set file with the specified name.
	 */
	public static DBObject getCollectionFromDataSet(String dataSetFilename, String collectionName,
			List<String> ignorePropertyNames) {
		File dataSetFile = DataSetHelper.getDataSetFile(dataSetFilename);
		String dataSetFileContents = DataSetHelper.getFileContents(dataSetFile);

		DBObject dbObject = getDBObjectFromFileContents(dataSetFileContents);

		DBObject collection = (DBObject) dbObject.get(collectionName);

		removeIgnorePropertyNames(collection, ignorePropertyNames);

		return collection;
	}

	/**
	 * Get the collection with the specified name from the MongoDB database to which the specified {@code MongoTemplate}
	 * has a connection. Property names in the ignore list are removed from the collection.
	 * 
	 * @param mongoTemplate
	 *            The {@code MongoTemplate} that has a connection to the MongoDB database.
	 * @param collectionName
	 *            The name of the collection to retrieve.
	 * @param ignorePropertyNames
	 *            The list of properties to remove from the collection.
	 * 
	 * @return Returns the collection with the specified name from the MongoDB database to which the specified
	 *         {@code MongoTemplate} has a connection.
	 */
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

	/**
	 * Removes the properties in the specified list of names from the specified {@code DBObject} instance.
	 * 
	 * @param dbObj
	 *            The {@code DBObject} instance from which to remove the properties.
	 * @param ignorePropertyNames
	 *            The list of property names to remove.
	 */
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
