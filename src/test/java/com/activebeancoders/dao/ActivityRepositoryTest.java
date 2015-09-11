package com.activebeancoders.dao;

import com.activebeancoders.BaseTest;
import com.activebeancoders.controller.es.DataLoadController;
import com.activebeancoders.dao.es.ActivityRepository;
import com.activebeancoders.entity.Activity;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ActivityRepositoryTest extends BaseTest {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public ActivityRepository activityRepository;

    @Autowired
    public DataLoadController dataLoadController;

    @Test
    public void blah() throws Exception {
        dataLoadController.reloadActivities("100");
        Thread.sleep(2000L);
        Activity activity = activityRepository.findOne("1");
        log.debug(String.valueOf(activity));
    }

}
