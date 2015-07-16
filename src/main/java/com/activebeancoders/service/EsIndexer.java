package com.activebeancoders.service;

import com.activebeancoders.dao.ActivityDao;
import com.activebeancoders.entity.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class EsIndexer {

    @Autowired
    private ActivityDao activityDao;

    @Autowired
    private DataLoader dataLoader;

    public void indexAllData() {
        try {
            List<Activity> activities = dataLoader.loadDataFromJsonFile("activity-log.json", Activity.class);
            for (Activity a : activities) {
                activityDao.save(a);
            }
        } catch (IOException e) {
            e.printStackTrace(); // TODO: log exception
        }
    }
    
}
