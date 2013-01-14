package com.interzonedev.zankou;

import java.io.File;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import ch.qos.logback.classic.Logger;

import com.interzonedev.zankou.dataset.DataSet;
import com.interzonedev.zankou.dataset.DataSetValues;
import com.interzonedev.zankou.dataset.handler.DataSetHandler;
import com.interzonedev.zankou.dataset.transformer.DataSetTransformer;

/**
 * {@code TestExecutionListener} implementation that handles scanning for {@link DataSet} annotations and performing
 * setup and teardown operations to the databases used for each test case via an {@link IntegrationTestContext} instance
 * bound to a {@code ThreadLocal}.
 * 
 * @author Mark Markarian - mark@interzonedev.com
 */
public class IntegrationTestExecutionListener extends AbstractTestExecutionListener {

	private final Logger log = (Logger) LoggerFactory.getLogger(getClass());

	private final ThreadLocal<IntegrationTestContext> integrationTestContext = new ThreadLocal<IntegrationTestContext>();

	/**
	 * Constructs a new {@code IntegrationTestExecutionListener} and binds a new {@link IntegrationTestContext} to a
	 * {@code ThreadLocal}.
	 */
	public IntegrationTestExecutionListener() {
		integrationTestContext.set(new IntegrationTestContext());
	}

	/**
	 * Called before the execution of all tests within the integration test class. Calls the
	 * {@link IntegrationTestContext#setup(TestContext)} method with the specified {@code TestContext}.
	 * 
	 * @param testContext
	 *            The Spring {@code TestContext} for the current integration test case.
	 */
	@Override
	public void beforeTestClass(TestContext testContext) throws Exception {
		log.debug("beforeTestClass: " + testContext.getTestClass());

		integrationTestContext.get().setup(testContext);
	}

	/**
	 * Called after the execution of all tests within the integration test class. Removes the
	 * {@link IntegrationTestContext} from the {@code ThreadLocal}.
	 * 
	 * @param testContext
	 *            The Spring {@code TestContext} for the current integration test case.
	 */
	@Override
	public void afterTestClass(TestContext testContext) throws Exception {
		log.debug("afterTestClass: " + testContext.getTestClass());

		integrationTestContext.remove();
	}

	/**
	 * Called before each test method in the integration test class. Performs a setup operation on each database being
	 * tested.
	 * 
	 * @param testContext
	 *            The Spring {@code TestContext} for the current integration test case.
	 */
	@Override
	public void beforeTestMethod(TestContext testContext) throws Exception {
		log.debug("beforeTestMethod: " + testContext.getTestMethod());

		doDatabaseOpertions(DataSetOperation.SETUP, testContext);
	}

	/**
	 * Called after each test method in the integration test class. Performs a teardown operation on each database being
	 * tested.
	 * 
	 * @param testContext
	 *            The Spring {@code TestContext} for the current integration test case.
	 */
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
