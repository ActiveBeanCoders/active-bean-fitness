package com.activebeancoders.fitness.service;

import com.activebeancoders.fitness.dto.es.ActivityEsDto;
import com.activebeancoders.fitness.service.es.ActivityIndexManager;
import net.pladform.elasticsearch.service.EsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class EsDataLoader implements DataLoader {

    private static final Logger log = LoggerFactory.getLogger(EsDataLoader.class);

    @Autowired
    private EsService esService;

    @Autowired
    private ActivityIndexManager activityIndexManager;

    @Override
    public boolean beforeLoad() {
        esService.setVerbose(false);
        activityIndexManager.rebuildIndex();
        esService.setRefreshInterval(ActivityEsDto.INDEX_NAME, "-1");
        return true;
    }

    @Override
    public boolean afterLoad() {
        esService.setVerbose(true);
        activityIndexManager.resetRefreshInterval();
        return true;
    }

}
