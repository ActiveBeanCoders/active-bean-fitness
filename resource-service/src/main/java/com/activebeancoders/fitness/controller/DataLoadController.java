package com.activebeancoders.fitness.controller;

import com.activebeancoders.fitness.service.DataLoaderWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Dan Barrese
 */
@RestController
@PreAuthorize("hasAuthority('ROLE_DOMAIN_USER')")
public class DataLoadController {

    private static final Logger log = LoggerFactory.getLogger(DataLoadController.class);

    //    @Autowired
    private DataLoaderWorker indexerWorker;

    /**
     * Rebilds the index with random data.
     */
    @RequestMapping(value = RestEndpoint.RELOAD, method = RequestMethod.GET)
    public String reloadActivities(@RequestParam(required = false, defaultValue = "20000") final String count) {
        indexerWorker.loadRandomRecords(Long.valueOf(count));
        return "Okay, I'm working on it!";
    }

    @RequestMapping(value = RestEndpoint.RELOAD_STATUS, method = RequestMethod.GET)
    public String reloadActivitiesStatus() {
        return indexerWorker.getLastKnownStatus();
    }

}
