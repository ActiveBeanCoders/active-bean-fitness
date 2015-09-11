package com.activebeancoders.service;

import com.activebeancoders.BaseTest;
import com.activebeancoders.dao.es.ActivityEsDao;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class EsServiceTest extends BaseTest {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    EsService esService;

    @Ignore
    @Test
    public void setRefreshInterval() throws Exception {
        esService.setRefreshInterval(ActivityEsDao.INDEX_NAME, "-1");
    }

}
