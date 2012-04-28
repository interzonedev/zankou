package com.interzonedev.sprintfix;

import org.junit.runners.model.InitializationError;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * The {@code SpringJUnit4ClassRunner} implementation for all integration tests meant to be run by the Sprintfix
 * framework.
 * 
 * @author Mark Markarian - mark@interzonedev.com
 */
public class IntegrationTestRunner extends SpringJUnit4ClassRunner {

	/**
	 * Constructs a new {@code IntegrationTestRunner} and initializes a {@link IntegrationTestExecutionListener} to
	 * provide Sprintfix testing functionality for each integration test.
	 * 
	 * @param testClass
	 *            The {@code Class} of the integration test case being run.
	 * 
	 * @throws InitializationError
	 */
	public IntegrationTestRunner(Class<?> testClass) throws InitializationError {
		super(testClass);

		TestContextManager testContextManager = getTestContextManager();
		testContextManager.registerTestExecutionListeners(new IntegrationTestExecutionListener());
	}

}
