package com.interzonedev.sprintfix.dataset.handler;

/**
 * Enumeration that defines the databases for which the Sprintfix framework provides pre-defined {@link DataSetHandler}
 * implementations. Each member of the enumeration has the Spring bean id of the {@link DataSetHandler} implementation
 * for the database it represents.
 * 
 * @author Mark Markarian - mark@interzonedev.com
 */
public enum Handler {
	DBUNIT("sprintfixDbUnitDataSetHandler"), MONGO("sprintfixMongoDataSetHandler");

	private final String handlerBeanId;

	private Handler(String handlerBeanId) {
		this.handlerBeanId = handlerBeanId;
	}

	/**
	 * Get the Spring bean id of the {@link DataSetHandler} implementation for the database represented by this
	 * {@link Handler}.
	 * 
	 * @return Returns the Spring bean id of the {@link DataSetHandler} implementation for the database represented by
	 *         this {@link Handler}.
	 */
	public String handlerBeanId() {
		return handlerBeanId;
	}
}
