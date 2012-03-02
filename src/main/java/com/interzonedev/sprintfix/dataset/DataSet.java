package com.interzonedev.sprintfix.dataset;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.interzonedev.sprintfix.dataset.handler.Handler;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Inherited
public @interface DataSet {
	String filename();

	String handlerBeanId() default "";

	Handler handler() default Handler.DBUNIT;

	String dataSourceBeanId();

	String transformerBeanId() default "";
}
