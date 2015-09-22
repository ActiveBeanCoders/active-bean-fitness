package com.activebeancoders.fitness.service;

import com.activebeancoders.BaseTest;
import com.activebeancoders.fitness.service.es.ActivityIndexManager;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class EsAllDataLoaderWorkerTest extends BaseTest {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public DataLoaderWorker dataLoaderWorker;

    @Autowired
    public ActivityIndexManager activityIndexManager;

    /**
     * Run this to erase everything in your local index, then rebuild it with random data.
     */
//    @Ignore
    @Test
    public void indexRandomData() throws Exception {
        activityIndexManager.rebuildIndex();
        dataLoaderWorker.loadRandomRecords(10L).get();
    }

}
