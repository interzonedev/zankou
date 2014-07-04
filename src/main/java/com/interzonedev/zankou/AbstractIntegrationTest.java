package com.interzonedev.zankou;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import com.interzonedev.zankou.dataset.DataSet;
import com.interzonedev.zankou.dataset.DataSets;

/**
 * Top level integration test class meant to be run with a JUnit test runner. This is meant to be subclassed by any
 * integration tests that use the {@link DataSets} or {@link DataSet} annotations on test classes and methods. Running
 * this will create the Zankou Spring application context.
 * 
 * @author "Mark Markarian" &lt;mark@interzonedev.com&gt;
 */
@RunWith(IntegrationTestRunner.class)
@ContextConfiguration(locations = { "classpath:spring/com/interzonedev/zankou/applicationContext-zankou.xml" })
public abstract class AbstractIntegrationTest {
}
