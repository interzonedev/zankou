package com.interzonedev.zankou.dataset.mongo;

import com.interzonedev.zankou.DataSetOperation;
import com.interzonedev.zankou.dataset.DataSetHelper;
import com.interzonedev.zankou.dataset.handler.DataSetHandler;
import com.interzonedev.zankou.dataset.transformer.DataSetTransformer;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import javax.inject.Named;
import java.io.File;
import java.util.Set;

/**
 * Concrete implementation of {@link DataSetHandler} for MongoDB.
 * 
 * @author Mark Markarian - mark@interzonedev.com
 */
@Named("zankouMongoDataSetHandler")
public class MongoDataSetHandler implements DataSetHandler {

    private static final Logger log = LoggerFactory.getLogger(MongoDataSetHandler.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.interzonedev.zankou.dataset.handler.DataSetHandler#cleanAndInsertData(java.io.File, java.lang.Object,
     * com.interzonedev.zankou.dataset.transformer.DataSetTransformer)
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
     * 
     * @see com.interzonedev.zankou.dataset.handler.DataSetHandler#cleanData(java.io.File, java.lang.Object)
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
                MongoCollection collection = mongoTemplate.getCollection(collectionName);

                DBObject collectionData = (DBObject) dataSetDBObject.get(collectionName);
                for (String key : collectionData.keySet()) {
                    DBObject collectionItem = (DBObject) collectionData.get(key);
                    if (null != dataSetTransformer) {
                        collectionItem = (DBObject) dataSetTransformer.transformDataSetItem(collectionItem);
                    }
                    collection.insertOne(collectionItem);
                }
            }
        }
    }
}
