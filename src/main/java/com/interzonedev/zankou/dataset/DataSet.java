package com.interzonedev.zankou.dataset;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.interzonedev.zankou.dataset.handler.DataSetHandler;
import com.interzonedev.zankou.dataset.handler.Handler;
import com.interzonedev.zankou.dataset.transformer.DataSetTransformer;

/**
 * Annotates test classes and methods. Specifies the filename of the dataset, the Spring bean id of the
 * {@link DataSetHandler}, the {@link Handler}, the Spring bean id of the datasource and the Spring bean id of the
 * {@link DataSetTransformer} to be used for the test case.
 * 
 * @author Mark Markarian - mark@interzonedev.com
 */
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
