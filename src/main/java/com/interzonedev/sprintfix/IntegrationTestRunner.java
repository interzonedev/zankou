package com.interzonedev.sprintfix;

import org.junit.runners.model.InitializationError;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class IntegrationTestRunner extends SpringJUnit4ClassRunner {

	public IntegrationTestRunner(Class<?> testClass) throws InitializationError {
		super(testClass);

		TestContextManager testContextManager = getTestContextManager();
		testContextManager.registerTestExecutionListeners(new IntegrationTestExecutionListener());
	}

}
