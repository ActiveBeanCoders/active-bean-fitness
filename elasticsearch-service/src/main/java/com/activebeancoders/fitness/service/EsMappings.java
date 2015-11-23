package com.activebeancoders.fitness.service;

import com.activebeancoders.fitness.service.es.ActivityIndexManager;
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

    private static final Logger log = LoggerFactory.getLogger(EsDataLoader.class);

    @Autowired
    private ActivityIndexManager activityIndexManager;

    public void defineAllMappings() {
        activityIndexManager.rebuildIndex();
    }

}
