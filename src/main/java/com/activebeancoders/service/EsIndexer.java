package com.activebeancoders.service;

import com.activebeancoders.dao.ActivityDao;
import com.activebeancoders.entity.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EsIndexer {

    private static final Logger log = LoggerFactory.getLogger(EsIndexer.class);

    @Autowired
    private EsService esService;

    @Autowired
    private ActivityDao activityDao;

    @Autowired
    private DataLoader dataLoader;

    /**
     * Initializes all indexes.  Deletes all indexes, then rebuilds their structures without any data.
     */
    public void rebuildAllIndexStructures() {
        Map<String, Object> mapping = new HashMap<>();
        Map<String, Object> properties = new HashMap<>();
        Map<String, Object> type = new HashMap<>();

        type.put("type", "date");
        type.put("format", "yyyy-MM-dd HH:mm:SS");
        properties.put(Activity._date, type);
        mapping.put("properties", properties);

        esService.buildIndex(Activity.class.getPackage().getName(), Activity.class.getSimpleName(), mapping);
    }

    /**
     * Populates index with all data found in a JSON file within the project.
     */
    public void indexAllData() {
        try {
            List<Activity> activities = dataLoader.loadDataFromJsonFile("activity-log.json", Activity.class);
            for (Activity a : activities) {
                activityDao.save(a);
            }
        } catch (IOException e) {
            log.error("Error while indexing all data from source file.", e);
        }
    }

}
