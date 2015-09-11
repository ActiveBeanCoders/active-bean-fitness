package com.activebeancoders.controller.es;

import com.activebeancoders.Config;
import com.activebeancoders.entity.Activity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Config.class)
public class ActivityControllerTest {

    @Autowired
    public ActivityController activityController;

    @Test
    public void mostRecentActivities() throws Exception {
        Page<Activity> results = activityController.mostRecentActivities("5");
        Assert.assertNotNull(results);
        Assert.assertEquals(5, results.getSize());
    }

}
