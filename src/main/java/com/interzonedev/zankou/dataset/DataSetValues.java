package com.interzonedev.zankou.dataset;

import java.io.File;

import com.interzonedev.zankou.dataset.handler.DataSetHandler;
import com.interzonedev.zankou.dataset.transformer.DataSetTransformer;

/**
 * Value object for holding instances represented by the properties in a {@link DataSet} annotation. Specifically holds
 * the {@link DataSetHandler}, data set {@code File}, data source and {@link DataSetTransformer}.
 * 
 * @author Mark Markarian - mark@interzonedev.com
 */
public class DataSetValues {
    private final DataSetHandler dataSetHandler;

    private final File dataSetFile;

    private final Object dataSourceBean;

    private final DataSetTransformer dataSetTransformer;

    public DataSetValues(DataSetHandler dataSetHandler, File dataSetFile, Object dataSourceBean,
            DataSetTransformer dataSetTransformer) {
        this.dataSetHandler = dataSetHandler;
        this.dataSetFile = dataSetFile;
        this.dataSourceBean = dataSourceBean;
        this.dataSetTransformer = dataSetTransformer;
    }

    public DataSetHandler getDataSetHandler() {
        return dataSetHandler;
    }

    public File getDataSetFile() {
        return dataSetFile;
    }

    public Object getDataSourceBean() {
        return dataSourceBean;
    }

    public DataSetTransformer getDataSetTransformer() {
        return dataSetTransformer;
    }
}
