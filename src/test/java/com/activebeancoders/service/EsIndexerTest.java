package com.activebeancoders.service;

import com.activebeancoders.BaseTest;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class EsIndexerTest extends BaseTest {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public EsIndexer esIndexer;

    /**
     * Run this to erase everything in your local index, then rebuild it with random data.
     */
    @Ignore
    @Test
    public void indexRandomData() throws Exception {
        esIndexer.rebuildAllIndexStructures();
        esIndexer.loadRandomRecords(1000L).get();
    }

}
