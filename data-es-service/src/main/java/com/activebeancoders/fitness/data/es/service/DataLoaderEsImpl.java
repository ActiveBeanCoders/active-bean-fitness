package com.activebeancoders.fitness.data.es.service;

import com.activebeancoders.fitness.data.es.dao.ActivityDaoEsImpl;
import com.activebeancoders.fitness.data.service.DataLoader;
import com.activebeancoders.fitness.data.es.service.es.ActivityIndexManagerImpl;
import net.pladform.elasticsearch.service.EsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * Responsible for loading data into Elasticsearch.
 *
 * @author Dan Barrese
 */
@Component
@Primary
public class DataLoaderEsImpl implements DataLoader {

    private static final Logger log = LoggerFactory.getLogger(DataLoaderEsImpl.class);

    @Autowired
    private EsService esService;

    @Autowired
    private ActivityIndexManagerImpl activityIndexManager;

    @Override
    public boolean beforeLoad() {
        esService.setVerbose(false);
        activityIndexManager.rebuildIndex();
        esService.setRefreshInterval(ActivityDaoEsImpl.INDEX_NAME, "-1");
        return true;
    }

    @Override
    public boolean afterLoad() {
        esService.setVerbose(true);
        activityIndexManager.resetRefreshInterval();
        return true;
    }

}
