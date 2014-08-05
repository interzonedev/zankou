package com.interzonedev.zankou.dataset;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates test classes and methods and specifies one or more {@link DataSet} annotations. Allows for using more than
 * one database for a single test case.
 * 
 * @author Mark Markarian - mark@interzonedev.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Inherited
public @interface DataSets {
    DataSet[] dataSets();
}
