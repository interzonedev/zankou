package com.interzonedev.sprintfix;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import com.interzonedev.sprintfix.dataset.DataSet;
import com.interzonedev.sprintfix.dataset.DataSets;

/**
 * Top level integration test class meant to be run with a JUnit test runner. This is meant to be subclassed by any
 * integration tests that use the {@link DataSets} or {@link DataSet} annotations on test classes and methods. Running
 * this will create the sprintfix Spring application context.
 * 
 * @author "Mark Markarian" <mark@interzonedev.com>
 */
@RunWith(IntegrationTestRunner.class)
@ContextConfiguration(locations = { "classpath:spring/com/interzonedev/sprintfix/applicationContext-sprintfix.xml" })
public abstract class AbstractIntegrationTest {
}
