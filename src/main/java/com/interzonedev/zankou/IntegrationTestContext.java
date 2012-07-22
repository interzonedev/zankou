package com.interzonedev.zankou;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestContext;

import com.interzonedev.zankou.dataset.DataSet;
import com.interzonedev.zankou.dataset.DataSetHelper;
import com.interzonedev.zankou.dataset.DataSetValues;
import com.interzonedev.zankou.dataset.DataSets;
import com.interzonedev.zankou.dataset.handler.DataSetHandler;
import com.interzonedev.zankou.dataset.handler.Handler;
import com.interzonedev.zankou.dataset.transformer.DataSetTransformer;

/**
 * Context for an individual integration test case. Keeps track of the {@link DataSet}s to be used for the test case.
 * 
 * @author Mark Markarian - mark@interzonedev.com
 */
public class IntegrationTestContext {
	private List<DataSetValues> classDataSets = new ArrayList<DataSetValues>();

	private Map<Method, List<DataSetValues>> methodsDataSets = new HashMap<Method, List<DataSetValues>>();

	/**
	 * Meant to be called before the integration test class is run. Scans for {@link DataSet} and {@link DataSets}
	 * annotations at the class level of an integration test.
	 * 
	 * @param testContext
	 *            The Spring {@code TestContext} passed in from the {@code TestContextManager}.
	 */
	public void setup(TestContext testContext) {
		Class<?> testClass = testContext.getTestClass();

		if (testClass.isAnnotationPresent(DataSets.class)) {
			DataSets classDataSets = (DataSets) testClass.getAnnotation(DataSets.class);
			addClassDataSets(classDataSets, testContext);
		} else if (testClass.isAnnotationPresent(DataSet.class)) {
			DataSet classDataSet = (DataSet) testClass.getAnnotation(DataSet.class);
			addClassDataSet(classDataSet, testContext);
		}
	}

	/**
	 * Get the list of {@link DataSetValues} instances for the individual integration test case represented by the
	 * specified {@code TestContext}. Defaults to the values at the class level if the test method does not specify any
	 * {@link DataSet}s.
	 * 
	 * @param testContext
	 *            The Spring {@code TestContext} passed in from the {@code TestContextManager}.
	 * 
	 * @return Returns the list of {@link DataSetValues} instances for the individual integration test case represented
	 *         by the specified {@code TestContext}.
	 */
	public List<DataSetValues> getTestDataSetValues(TestContext testContext) {
		Method method = testContext.getTestMethod();

		List<DataSetValues> testDataSetValues = getMethodDataSetValues(method);

		// Attempt to get data set values from the method annotation.
		if (testDataSetValues.isEmpty()) {
			if (method.isAnnotationPresent(DataSets.class)) {
				DataSets methodDataSets = (DataSets) method.getAnnotation(DataSets.class);
				addMethodDataSets(method, methodDataSets, testContext);
			} else if (method.isAnnotationPresent(DataSet.class)) {
				DataSet methodDataSet = (DataSet) method.getAnnotation(DataSet.class);
				addMethodDataSet(method, methodDataSet, testContext);
			}
		}

		// Attempt to get data set values from the class annotation.
		if (testDataSetValues.isEmpty()) {
			testDataSetValues = getClassDataSetValues();
		}

		return testDataSetValues;
	}

	private List<DataSetValues> getClassDataSetValues() {
		return classDataSets;
	}

	private List<DataSetValues> getMethodDataSetValues(Method method) {
		List<DataSetValues> methodDataSets = getExistingOrEmptyMethodDataSets(method);
		return methodDataSets;
	}

	private void addClassDataSets(DataSets dataSets, TestContext testContext) {
		List<DataSetValues> dataSetValuesList = getDataSetValuesFromDataSets(dataSets, testContext);
		classDataSets.addAll(dataSetValuesList);
	}

	private void addClassDataSet(DataSet dataSet, TestContext testContext) {
		DataSetValues dataSetValues = getDataSetValuesFromDataSet(dataSet, testContext);
		classDataSets.add(dataSetValues);
	}

	private void addMethodDataSets(Method method, DataSets dataSets, TestContext testContext) {
		List<DataSetValues> dataSetValuesList = getDataSetValuesFromDataSets(dataSets, testContext);
		List<DataSetValues> methodDataSets = getExistingOrEmptyMethodDataSets(method);
		methodDataSets.addAll(dataSetValuesList);
	}

	private void addMethodDataSet(Method method, DataSet dataSet, TestContext testContext) {
		DataSetValues dataSetValues = getDataSetValuesFromDataSet(dataSet, testContext);
		List<DataSetValues> methodDataSets = getExistingOrEmptyMethodDataSets(method);
		methodDataSets.add(dataSetValues);
	}

	private List<DataSetValues> getExistingOrEmptyMethodDataSets(Method method) {
		List<DataSetValues> methodDataSets = methodsDataSets.get(method);
		if (null == methodDataSets) {
			methodDataSets = new ArrayList<DataSetValues>();
			methodsDataSets.put(method, methodDataSets);
		}
		return methodDataSets;
	}

	private DataSetValues getDataSetValuesFromDataSet(DataSet dataSet, TestContext testContext) {
		ApplicationContext applicationContext = testContext.getApplicationContext();

		Handler handler = dataSet.handler();
		String handlerBeanId = dataSet.handlerBeanId();

		if (null != handler) {
			handlerBeanId = handler.handlerBeanId();
		}

		if (StringUtils.isBlank(handlerBeanId)) {
			StringBuilder errorMessage = new StringBuilder("getDataSetValuesFromDataSet: ");
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

		DataSetTransformer dataSetTransformer = null;
		String transformerBeanId = dataSet.transformerBeanId();
		if (StringUtils.isNotBlank(transformerBeanId)) {
			dataSetTransformer = (DataSetTransformer) applicationContext.getBean(transformerBeanId);
		}

		DataSetValues dataSetValues = new DataSetValues(dataSetHandler, dataSetFile, dataSourceBean, dataSetTransformer);

		return dataSetValues;
	}

	private List<DataSetValues> getDataSetValuesFromDataSets(DataSets dataSets, TestContext testContext) {
		List<DataSetValues> dataSetValuesList = new ArrayList<DataSetValues>();

		for (DataSet dataSet : dataSets.dataSets()) {
			DataSetValues dataSetValues = getDataSetValuesFromDataSet(dataSet, testContext);
			dataSetValuesList.add(dataSetValues);
		}

		return dataSetValuesList;
	}
}
