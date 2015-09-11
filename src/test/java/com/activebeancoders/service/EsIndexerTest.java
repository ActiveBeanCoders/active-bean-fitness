package com.activebeancoders.service;

import com.activebeancoders.Config;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class EsIndexerTest {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public EsIndexer esIndexer;

    /**
     * Run this to erase everything in your local index, then rebuild it with random data.
     */
    @Ignore
    @Test
    public void indexRandomData() throws Exception {
        esIndexer.indexABunchOfRandomData(100L);
    }

}
