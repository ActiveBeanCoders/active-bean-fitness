package com.activebeancoders.fitness.data.dao;

import com.activebeancoders.fitness.data.BaseTestIT;
import com.activebeancoders.fitness.data.entity.Activity;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Dan Barrese
 */
public class ActivityDaoIT extends BaseTestIT {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public ActivityDao activityDao;

    @Test
    public void save() throws Exception {
        Activity activity = new Activity();
        // do not set id
        activity.setActivity("Running");
        activity.setComment("hello 1 12 123 1234");
//        activity.setDate(new Date());
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

    @Test
    public void findMostRecentActivities() throws Exception {
        List<Activity> list = activityDao.findMostRecentActivities(5);
        Assert.assertNotNull(list);
        Assert.assertEquals(5, list.size());
    }

}
