package com.interzonedev.sprintfix.dataset.handler;

public enum Handler {
	DBUNIT("sprintfixDbUnitDataSetHandler"), MONGO("sprintfixMongoDataSetHandler");

	private final String handlerBeanId;

	private Handler(String handlerBeanId) {
		this.handlerBeanId = handlerBeanId;
	}

	public String handlerBeanId() {
		return handlerBeanId;
	}
}
