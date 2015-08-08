package com.activebeancoders.controller;

import com.activebeancoders.service.EsIndexer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class DataLoadController {

    private static final Logger log = LoggerFactory.getLogger(DataLoadController.class);

    @Autowired
    private EsIndexer esIndexer;

    /**
     * Rebilds the index with random data.
     */
    @RequestMapping(value = RestEndpoint.RELOAD, method = RequestMethod.GET)
    public void reloadActivities() {
        esIndexer.rebuildAllIndexStructures();
        esIndexer.indexAllData();
    }

}
