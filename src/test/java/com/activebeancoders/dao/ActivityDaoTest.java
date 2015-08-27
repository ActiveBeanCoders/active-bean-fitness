package com.activebeancoders.dao;

import com.activebeancoders.Config;
import com.activebeancoders.entity.Activity;
import com.activebeancoders.entity.util.View;
import com.activebeancoders.search.SearchCriteria;
import com.activebeancoders.service.EsService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class ActivityDaoTest {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public ActivityDao activityDao;

    @Autowired
    public EsService esService;

    @Test
    public void testSaveToElasticsearchThenRetrieve() throws Exception {
        // create a run
        Activity toBeSaved = new Activity();
        toBeSaved.setId("0");
        toBeSaved.setComment("my run around the house");
        toBeSaved.setDistance(1.2);
        toBeSaved.setUnit("Miles");

        // save
        activityDao.save(toBeSaved);

        // get
        Activity retrieved = activityDao.get(toBeSaved.getId());
        System.out.println(retrieved);

        Assert.assertNotNull(toBeSaved);
        Assert.assertNotNull(retrieved);
        Assert.assertEquals(toBeSaved.getId(), retrieved.getId());
        Assert.assertEquals(toBeSaved.getComment(), retrieved.getComment());
        Assert.assertEquals(toBeSaved.getDistance(), retrieved.getDistance());
        Assert.assertEquals(toBeSaved.getUnit(), retrieved.getUnit());
    }

    @Test
    public void update() throws Exception {
        // create a run
        Activity toBeSaved = new Activity();
        toBeSaved.setId("0");
        toBeSaved.setComment("my run around the house");
        toBeSaved.setDistance(1.2);
        toBeSaved.setUnit("Miles");

        // save
        activityDao.save(toBeSaved);

        // update
        Activity update = new Activity();
        update.setId(toBeSaved.getId());
        update.setComment("updated");
        activityDao.update(update, View.Comment.class);

        Activity retrieved = activityDao.get(toBeSaved.getId());

        Assert.assertNotNull(toBeSaved);
        Assert.assertNotNull(retrieved);
        Assert.assertEquals(toBeSaved.getId(), retrieved.getId());
        Assert.assertEquals(update.getComment(), retrieved.getComment());
        Assert.assertEquals(toBeSaved.getDistance(), retrieved.getDistance());
        Assert.assertEquals(toBeSaved.getUnit(), retrieved.getUnit());
    }

    @Test
    public void search() throws Exception {
        SearchCriteria sc = new SearchCriteria();
        sc.setFullText("Jog 2013 12 01");
        List<Activity> results = activityDao.search(sc);
        for (Activity a : results) {
            System.out.println(esService.toJson(a));
        }
    }

}
