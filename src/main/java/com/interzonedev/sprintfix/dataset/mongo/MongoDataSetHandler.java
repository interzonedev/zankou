package com.interzonedev.sprintfix.dataset.mongo;

import java.io.File;
import java.util.Set;

import javax.inject.Named;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import com.interzonedev.sprintfix.DataSetOperation;
import com.interzonedev.sprintfix.dataset.DataSetHelper;
import com.interzonedev.sprintfix.dataset.handler.DataSetHandler;
import com.interzonedev.sprintfix.dataset.transformer.DataSetTransformer;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * Concrete implementation of {@link DataSetHandler} for MongoDB.
 * 
 * @author Mark Markarian - mark@interzonedev.com
 */
@Named("sprintfixMongoDataSetHandler")
public class MongoDataSetHandler implements DataSetHandler {

	private Log log = LogFactory.getLog(getClass());

	/*
	 * (non-Javadoc)
	 * @see com.interzonedev.sprintfix.dataset.handler.DataSetHandler#cleanAndInsertData(java.io.File, java.lang.Object, com.interzonedev.sprintfix.dataset.transformer.DataSetTransformer)
	 */
	@Override
	public void cleanAndInsertData(File dataSetFile, Object dataSourceInstance, DataSetTransformer dataSetTransformer) {
		log.debug("cleanAndInsertData: " + dataSetFile.getName());

		try {
			MongoTemplate mongoTemplate = (MongoTemplate) dataSourceInstance;
			doDatabaseOperation(DataSetOperation.SETUP, mongoTemplate, dataSetFile, dataSetTransformer);
			log.debug("cleanAndInsertData: Inserted collections");
		} catch (Throwable t) {
			String errorMessage = "cleanAndInsertData: Error setting up database";
			log.error(errorMessage, t);
			throw new RuntimeException(errorMessage, t);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.interzonedev.sprintfix.dataset.handler.DataSetHandler#cleanData(java.io.File, java.lang.Object)
	 */
	@Override
	public void cleanData(File dataSetFile, Object dataSourceInstance) {
		log.debug("cleanData: " + dataSetFile.getName());

		try {
			MongoTemplate mongoTemplate = (MongoTemplate) dataSourceInstance;
			doDatabaseOperation(DataSetOperation.TEARDOWN, mongoTemplate, dataSetFile, null);
			log.debug("cleanData: Emptied collections");
		} catch (Throwable t) {
			String errorMessage = "cleanAndInsertData: Error tearing down database";
			log.error(errorMessage, t);
			throw new RuntimeException(errorMessage, t);
		}
	}

	private void doDatabaseOperation(DataSetOperation operation, MongoTemplate mongoTemplate, File dataSetFile,
			DataSetTransformer dataSetTransformer) {
		String dataSetFileContents = DataSetHelper.getFileContents(dataSetFile);

		DBObject dataSetDBObject = MongoUtils.getDBObjectFromFileContents(dataSetFileContents);

		Set<String> collectionNames = MongoUtils.getCollectionNamesFromFileContents(dataSetFileContents);

		for (String collectionName : collectionNames) {
			mongoTemplate.remove(new Query(), collectionName);
		}

		if (DataSetOperation.SETUP.equals(operation)) {

			for (String collectionName : collectionNames) {
				DBCollection collection = mongoTemplate.getCollection(collectionName);

				DBObject collectionData = (DBObject) dataSetDBObject.get(collectionName);
				for (String key : collectionData.keySet()) {
					DBObject collectionItem = (DBObject) collectionData.get(key);
					if (null != dataSetTransformer) {
						collectionItem = (DBObject) dataSetTransformer.transformDataSetItem(collectionItem);
					}
					collection.insert(collectionItem);
				}
			}
		}
	}
}
