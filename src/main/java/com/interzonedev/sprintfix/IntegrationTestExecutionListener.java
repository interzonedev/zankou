package com.interzonedev.sprintfix;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import com.interzonedev.sprintfix.dataset.DataSet;
import com.interzonedev.sprintfix.dataset.DataSetHelper;
import com.interzonedev.sprintfix.dataset.DataSets;
import com.interzonedev.sprintfix.dataset.handler.DataSetHandler;
import com.interzonedev.sprintfix.dataset.handler.Handler;

public class IntegrationTestExecutionListener extends AbstractTestExecutionListener {
	private Log log = LogFactory.getLog(getClass());

	private final ThreadLocal<IntegrationTestContext> integrationTestContext = new ThreadLocal<IntegrationTestContext>();

	private ApplicationContext applicationContext;

	public IntegrationTestExecutionListener() {
		integrationTestContext.set(new IntegrationTestContext());
	}

	@Override
	public void beforeTestClass(TestContext testContext) throws Exception {
		log.debug("beforeTestClass");

		Class<?> testClass = testContext.getTestClass();

		if (testClass.isAnnotationPresent(DataSets.class)) {
			DataSets classDataSets = (DataSets) testClass.getAnnotation(DataSets.class);
			integrationTestContext.get().addClassDataSets(classDataSets);
		} else if (testClass.isAnnotationPresent(DataSet.class)) {
			DataSet classDataSet = (DataSet) testClass.getAnnotation(DataSet.class);
			integrationTestContext.get().addClassDataSet(classDataSet);
		}

		applicationContext = testContext.getApplicationContext();
	}

	@Override
	public void afterTestClass(TestContext testContext) throws Exception {
		integrationTestContext.remove();
	}

	@Override
	public void beforeTestMethod(TestContext testContext) throws Exception {
		log.debug("beforeTestMethod");

		doDatabaseOpertions(DataSetOperation.SETUP, testContext);
	}

	@Override
	public void afterTestMethod(TestContext testContext) throws Exception {
		log.debug("afterTestMethod");

		doDatabaseOpertions(DataSetOperation.TEARDOWN, testContext);
	}

	private List<DataSet> getTestDataSets(TestContext testContext) {
		Method method = testContext.getTestMethod();

		List<DataSet> testDataSets = integrationTestContext.get().getMethodDataSets(method);

		if (testDataSets.isEmpty()) {
			if (method.isAnnotationPresent(DataSets.class)) {
				DataSets methodDataSets = (DataSets) method.getAnnotation(DataSets.class);
				integrationTestContext.get().addMethodDataSets(method, methodDataSets);
			} else if (method.isAnnotationPresent(DataSet.class)) {
				DataSet methodDataSet = (DataSet) method.getAnnotation(DataSet.class);
				integrationTestContext.get().addMethodDataSet(method, methodDataSet);
			}
		}

		if (testDataSets.isEmpty()) {
			testDataSets = integrationTestContext.get().getClassDataSets();
		}

		return testDataSets;
	}

	private void doDatabaseOpertions(DataSetOperation operation, TestContext testContext) {
		List<DataSet> testDataSets = getTestDataSets(testContext);

		for (DataSet dataSet : testDataSets) {
			Handler handler = dataSet.handler();
			String handlerBeanId = dataSet.handlerBeanId();

			if (null != handler) {
				handlerBeanId = handler.handlerBeanId();
			}

			if (StringUtils.isBlank(handlerBeanId)) {
				StringBuilder errorMessage = new StringBuilder("doDatabaseOpertions: ");
				errorMessage.append("The test method ").append(testContext.getTestMethod().getName());
				errorMessage.append(" on the test class ").append(testContext.getTestClass().getName());
				errorMessage.append(" does not specify a handler bean id.");
				throw new RuntimeException(errorMessage.toString());
			}

			DataSetHandler dataSetHandler = (DataSetHandler) applicationContext.getBean(handlerBeanId);

			String dataSetFilename = dataSet.filename();

			File dataSetFile = DataSetHelper.getDataSetFile(dataSetFilename);

			String dataSourceBeanId = dataSet.dataSourceBeanId();

			Object dataSourceBean = applicationContext.getBean(dataSourceBeanId);

			switch (operation) {
				case SETUP:
					dataSetHandler.cleanAndInsertData(dataSetFile, dataSourceBean);
					break;
				case TEARDOWN:
					dataSetHandler.cleanData(dataSetFile, dataSourceBean);
					break;
			}
		}
	}
}
