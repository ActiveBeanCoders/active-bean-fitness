package com.activebeancoders.dao;

import com.activebeancoders.entity.Activity;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class RunEsDaoTest {

    public static ActivityDao runEsDao;

    @BeforeClass
    public static void beforeClass() {
        runEsDao = new ActivityDao();
    }

    @Test
    public void testSaveToElasticsearchThenRetrieve() throws Exception {
        // create a run
        Activity toBeSaved = new Activity();
        toBeSaved.setId("1");
        toBeSaved.setComment("my run around the house");
        toBeSaved.setDistance(1.2);
        toBeSaved.setUnit("Miles");

        // save
        runEsDao.save(toBeSaved);

        // get
        Activity retrieved = runEsDao.get(toBeSaved.getId());
        System.out.println(retrieved);

        Assert.assertNotNull(toBeSaved);
        Assert.assertNotNull(retrieved);
        Assert.assertEquals(toBeSaved.getId(), retrieved.getId());
        Assert.assertEquals(toBeSaved.getComment(), retrieved.getComment());
        Assert.assertEquals(toBeSaved.getDistance(), retrieved.getDistance());
        Assert.assertEquals(toBeSaved.getUnit(), retrieved.getUnit());
    }

}
