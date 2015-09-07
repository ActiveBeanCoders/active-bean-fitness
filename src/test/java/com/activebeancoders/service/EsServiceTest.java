package com.activebeancoders.service;

import com.activebeancoders.Config;
import com.activebeancoders.dao.es.ActivityEsDao;
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
public class EsServiceTest {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    EsService esService;

    /**
     * Run this to erase everything in your local index, then rebuild it with random data.
     */
    @Test
    public void setRefreshInterval() throws Exception {
        esService.setRefreshInterval(ActivityEsDao.INDEX_NAME, "0");
    }

}
