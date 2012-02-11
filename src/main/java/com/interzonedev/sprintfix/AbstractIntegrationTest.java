package com.interzonedev.sprintfix;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

@RunWith(IntegrationTestRunner.class)
@ContextConfiguration(locations = { "classpath:spring/applicationContext-integrationTest.xml" })
public abstract class AbstractIntegrationTest {
}
