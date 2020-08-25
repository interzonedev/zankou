package com.interzonedev.zankou;

import com.interzonedev.zankou.dataset.DataSet;
import com.interzonedev.zankou.dataset.DataSets;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * Top level integration test class meant to be run with a JUnit test runner. This is meant to be subclassed by any
 * integration tests that use the {@link DataSets} or {@link DataSet} annotations on test classes and methods. Running
 * this will create the Zankou Spring application context.
 */
@ExtendWith(SpringExtension.class)
//@BootstrapWith(IntegrationTestContextBootstrapper.class)
@TestExecutionListeners(value = {
		IntegrationTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class
})
@ContextConfiguration(locations = { "classpath:spring/com/interzonedev/zankou/applicationContext-zankou.xml" })
public abstract class AbstractIntegrationTest {
}
