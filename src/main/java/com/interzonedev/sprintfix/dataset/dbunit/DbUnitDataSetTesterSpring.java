package com.interzonedev.sprintfix.dataset.dbunit;

import org.springframework.beans.factory.annotation.Autowired;

import com.interzonedev.sprintfix.dataset.helper.DataSetHelper;

/**
 * Concrete {@link AbstractDbUnitDataSetTester} implementation that is meant to be instantiated in a Spring container.
 * 
 * @author mark@interzonedev.com
 */
public class DbUnitDataSetTesterSpring extends AbstractDbUnitDataSetTester {

	@Autowired
	private DataSetHelper dataSetHelper;

	@Override
	protected DataSetHelper getDataSetHelper() {
		return dataSetHelper;
	}

}
