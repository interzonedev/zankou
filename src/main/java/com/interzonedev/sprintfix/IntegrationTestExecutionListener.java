package com.interzonedev.sprintfix;

import java.io.File;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import com.interzonedev.sprintfix.dataset.DataSetValues;
import com.interzonedev.sprintfix.dataset.handler.DataSetHandler;
import com.interzonedev.sprintfix.dataset.transformer.DataSetTransformer;

public class IntegrationTestExecutionListener extends AbstractTestExecutionListener {
	private Log log = LogFactory.getLog(getClass());

	private final ThreadLocal<IntegrationTestContext> integrationTestContext = new ThreadLocal<IntegrationTestContext>();

	public IntegrationTestExecutionListener() {
		integrationTestContext.set(new IntegrationTestContext());
	}

	@Override
	public void beforeTestClass(TestContext testContext) throws Exception {
		log.debug("beforeTestClass: " + testContext.getTestClass());

		integrationTestContext.get().setup(testContext);
	}

	@Override
	public void afterTestClass(TestContext testContext) throws Exception {
		log.debug("afterTestClass: " + testContext.getTestClass());

		integrationTestContext.remove();
	}

	@Override
	public void beforeTestMethod(TestContext testContext) throws Exception {
		log.debug("beforeTestMethod: " + testContext.getTestMethod());

		doDatabaseOpertions(DataSetOperation.SETUP, testContext);
	}

	@Override
	public void afterTestMethod(TestContext testContext) throws Exception {
		log.debug("afterTestMethod: " + testContext.getTestMethod());

		doDatabaseOpertions(DataSetOperation.TEARDOWN, testContext);
	}

	private void doDatabaseOpertions(DataSetOperation operation, TestContext testContext) {
		List<DataSetValues> testDataSets = integrationTestContext.get().getTestDataSetValues(testContext);

		for (DataSetValues dataSet : testDataSets) {
			DataSetHandler dataSetHandler = dataSet.getDataSetHandler();
			File dataSetFile = dataSet.getDataSetFile();
			Object dataSourceBean = dataSet.getDataSourceBean();
			DataSetTransformer dataSetTransformer = dataSet.getDataSetTransformer();

			switch (operation) {
				case SETUP:
					dataSetHandler.cleanAndInsertData(dataSetFile, dataSourceBean, dataSetTransformer);
					break;
				case TEARDOWN:
					dataSetHandler.cleanData(dataSetFile, dataSourceBean);
					break;
			}
		}
	}
}
