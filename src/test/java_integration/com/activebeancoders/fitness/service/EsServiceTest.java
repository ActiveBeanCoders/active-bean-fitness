package com.activebeancoders.fitness.service;

import com.activebeancoders.BaseTest;
import com.activebeancoders.fitness.dto.es.ActivityEsDto;
import net.pladform.elasticsearch.service.EsService;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

// TODO: move to other project
public class EsServiceTest extends BaseTest {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    EsService esService;

    @Ignore
    @Test
    public void setRefreshInterval() throws Exception {
        esService.setRefreshInterval(ActivityEsDto.INDEX_NAME, "-1");
    }

}
