package com.activebeancoders.fitness.dao;

import com.activebeancoders.BaseTest;
import com.activebeancoders.fitness.entity.Activity;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class ActivityDaoTest extends BaseTest {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public IActivityDao activityDao;

    @Test
    public void save() throws Exception {
        Activity activity = new Activity();
        // do not set id
        activity.setUserId(1L);
        activity.setComment("hello 1 12 123 1234");
        activity.setDate(new Date());
        boolean success = activityDao.save(activity);
        Assert.assertTrue(success);
    }

    @Test
    public void get() throws Exception {
        Activity activity = activityDao.get(1L);
        Assert.assertNotNull(activity);
        Assert.assertNotNull(activity.getId());
        log.info(activity.toString());
    }

}
