package com.interzonedev.sprintfix;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

@RunWith(IntegrationTestRunner.class)
@ContextConfiguration(locations = { "classpath:spring/com/interzonedev/sprintfix/applicationContext-sprintfix.xml" })
public abstract class AbstractIntegrationTest {
}
