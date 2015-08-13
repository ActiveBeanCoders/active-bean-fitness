package com.activebeancoders.controller;

import com.activebeancoders.service.EsIndexer;
import com.google.common.util.concurrent.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Executors;

@RestController
public class DataLoadController {

    private static final Logger log = LoggerFactory.getLogger(DataLoadController.class);

    @Autowired
    private EsIndexer esIndexer;
    private String lastKnownStatus = "Inactive.";

    /**
     * Rebilds the index with random data.
     */
    @RequestMapping(value = RestEndpoint.RELOAD, method = RequestMethod.GET)
    public String reloadActivities() {
        lastKnownStatus = "Loading...";
        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());
        Runnable runnable = () -> {
            esIndexer.rebuildAllIndexStructures();
            esIndexer.indexAllData();
        };
        ListenableFuture<?> future = executorService.submit(runnable);
        Futures.addCallback(future, new FutureCallback<Object>() {
            @Override
            public void onSuccess(Object o) {
                log.info("Data reload complete.");
                lastKnownStatus = "Data reload complete.";
            }
            @Override
            public void onFailure(Throwable t) {
                log.error("Failed to reload data.", t);
                lastKnownStatus = "Failed to reload data";
            }
        });
        executorService.shutdown();
        return "Okay, I'm working on it!";
    }

    @RequestMapping(value = RestEndpoint.RELOAD_STATUS, method = RequestMethod.GET)
    public String reloadActivitiesStatus() {
        return lastKnownStatus;
    }

}
