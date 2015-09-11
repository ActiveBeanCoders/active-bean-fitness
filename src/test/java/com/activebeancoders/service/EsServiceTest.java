package com.activebeancoders.service;

import com.activebeancoders.BaseTest;
import com.activebeancoders.entity.Activity;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class EsServiceTest extends BaseTest {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    EsService esService;

    /**
     * Run this to erase everything in your local index, then rebuild it with random data.
     */
    @Test
    public void setRefreshInterval() throws Exception {
        esService.setRefreshInterval(Activity.INDEX_NAME, "0");
    }

}
