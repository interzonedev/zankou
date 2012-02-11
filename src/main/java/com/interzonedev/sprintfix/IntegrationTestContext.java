package com.interzonedev.sprintfix;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.interzonedev.sprintfix.dataset.DataSet;
import com.interzonedev.sprintfix.dataset.DataSets;

public class IntegrationTestContext {
	private List<DataSet> classDataSets = new ArrayList<DataSet>();

	private Map<Method, List<DataSet>> methodsDataSets = new HashMap<Method, List<DataSet>>();

	public List<DataSet> getClassDataSets() {
		return classDataSets;
	}

	public void addClassDataSets(DataSets dataSets) {
		classDataSets.addAll(Arrays.asList(dataSets.dataSets()));
	}

	public void addClassDataSet(DataSet dataSet) {
		classDataSets.add(dataSet);
	}

	public List<DataSet> getMethodDataSets(Method method) {
		List<DataSet> methodDataSets = getExistingOrEmptyMethodDataSets(method);
		return methodDataSets;
	}

	public void addMethodDataSets(Method method, DataSets dataSets) {
		List<DataSet> methodDataSets = getExistingOrEmptyMethodDataSets(method);
		methodDataSets.addAll(Arrays.asList(dataSets.dataSets()));
	}

	public void addMethodDataSet(Method method, DataSet dataSet) {
		List<DataSet> methodDataSets = getExistingOrEmptyMethodDataSets(method);
		methodDataSets.add(dataSet);
	}

	private List<DataSet> getExistingOrEmptyMethodDataSets(Method method) {
		List<DataSet> methodDataSets = methodsDataSets.get(method);
		if (null == methodDataSets) {
			methodDataSets = new ArrayList<DataSet>();
			methodsDataSets.put(method, methodDataSets);
		}
		return methodDataSets;
	}
}
