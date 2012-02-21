package com.interzonedev.sprintfix.dataset.mongo;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

public class MongoDataSetTester {

	private Log log = LogFactory.getLog(getClass());

	public void compareDataSetsIgnoreProperties(MongoTemplate mongoTemplate, String expectedDataSetFilename,
			String collectionName, List<String> ignorePropertyNames) {

		log.debug("compareDataSetsIgnoreProperties");

	}

}
