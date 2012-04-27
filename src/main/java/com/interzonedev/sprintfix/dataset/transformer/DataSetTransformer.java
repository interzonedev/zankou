package com.interzonedev.sprintfix.dataset.transformer;

import com.interzonedev.sprintfix.dataset.DataSet;

/**
 * Interface for transforming individual dataset items. When an implementation is instantiated as a Spring bean the bean
 * id can be set in the {@link DataSet#transformerBeanId()} annotation property.
 * 
 * @author Mark Markarian - mark@interzonedev.com
 */
public interface DataSetTransformer {

	/**
	 * Called for each item in a data set. Allows for altering the properties of the data set item being processed.
	 * 
	 * @param dataSetItem
	 *            The current data set item being processed. The type and content depend on the datastore being tested.
	 * 
	 * @return Returns the current data set item with the properties altered as necessary.
	 */
	public Object transformDataSetItem(Object dataSetItem);
}
