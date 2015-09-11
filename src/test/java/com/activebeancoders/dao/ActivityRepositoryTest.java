package com.activebeancoders.dao;

import com.activebeancoders.Config;
import com.activebeancoders.controller.es.DataLoadController;
import com.activebeancoders.dao.es.ActivityRepository;
import com.activebeancoders.entity.Activity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class ActivityRepositoryTest {

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
