package com.activebeancoders.controller.es;

import com.activebeancoders.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ActivityControllerTest extends BaseTest {

    @Autowired
    public ActivityController activityController;

    @Test
    public void mostRecentActivities() throws Exception {
        Assert.assertEquals(5, activityController.mostRecentActivities("5").getSize());
        Assert.assertEquals(10, activityController.mostRecentActivities("10").getSize());
    }

}
