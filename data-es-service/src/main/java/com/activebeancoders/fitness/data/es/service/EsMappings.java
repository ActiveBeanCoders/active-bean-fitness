package com.activebeancoders.fitness.data.es.service;

import com.activebeancoders.fitness.data.es.service.es.ActivityIndexManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Responsible for defining the object mappings for the index.
 *
 * @author Dan Barrese
 */
@Component
public class EsMappings {

    private static final Logger log = LoggerFactory.getLogger(DataLoaderEsImpl.class);

    @Autowired
    private ActivityIndexManagerImpl activityIndexManager;

    public void defineAllMappings() {
        activityIndexManager.rebuildIndex();
    }

}
