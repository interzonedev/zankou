package com.interzonedev.zankou;

import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.support.DefaultTestContextBootstrapper;

import java.util.Set;

public class IntegrationTestContextBootstrapper extends DefaultTestContextBootstrapper {

    @Override
    protected Set<Class<? extends TestExecutionListener>> getDefaultTestExecutionListenerClasses() {
        Set<Class<? extends TestExecutionListener>> defaultTestExecutionListenerClasses = super.getDefaultTestExecutionListenerClasses();
        defaultTestExecutionListenerClasses.add(IntegrationTestExecutionListener.class);

        return defaultTestExecutionListenerClasses;
    }

}
