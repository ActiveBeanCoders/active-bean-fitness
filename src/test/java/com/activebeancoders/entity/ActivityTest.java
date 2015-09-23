package com.activebeancoders.entity;

import com.activebeancoders.fitness.entity.Activity;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivityTest {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Test
    public void testStringSplitting() throws Exception {
        Activity activity = new Activity();
        activity.setComment("ab-c \"def\": gh, i. J! *k^lmnop");
        System.out.println(activity.getAllText());
        Assert.assertEquals("def lmnop", activity.getAllText());
    }

}
