package com.activebeancoders.fitness.data.es.service;

import com.activebeancoders.fitness.data.es.BaseTestIT;
import com.activebeancoders.fitness.data.es.service.es.ActivityIndexManagerImpl;
import com.activebeancoders.fitness.data.service.DataLoaderWorker;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Dan Barrese
 */
public class EsDataLoaderImplTestIT extends BaseTestIT {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public DataLoaderWorker dataLoaderWorker;

    @Autowired
    public ActivityIndexManagerImpl activityIndexManager;

    /**
     * Run this to erase everything in your local index, then rebuild it with random data.
     */
    @Ignore
    @Test
    public void indexRandomData() throws Exception {
        activityIndexManager.rebuildIndex();
        dataLoaderWorker.loadRandomRecords(10L).get();
    }

}
