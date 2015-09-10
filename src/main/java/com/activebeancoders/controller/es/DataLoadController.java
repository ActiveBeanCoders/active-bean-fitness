package com.activebeancoders.controller.es;

import com.activebeancoders.controller.RestEndpoint;
import com.activebeancoders.service.EsIndexer;
import com.activebeancoders.service.EsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class DataLoadController {

    private static final Logger log = LoggerFactory.getLogger(DataLoadController.class);

    @Autowired
    private EsIndexer esIndexer;

    @Autowired
    private EsService esService;

    /**
     * Rebilds the index with random data.
     */
    @RequestMapping(value = RestEndpoint.RELOAD, method = RequestMethod.GET)
    public String reloadActivities(@RequestParam(required = false, defaultValue = "20000") final String count) {
        esIndexer.loadRandomRecords(Long.valueOf(count));
        return "Okay, I'm working on it!";
    }

    @RequestMapping(value = RestEndpoint.RELOAD_STATUS, method = RequestMethod.GET)
    public String reloadActivitiesStatus() {
        return esIndexer.getLastKnownStatus();
    }

}
