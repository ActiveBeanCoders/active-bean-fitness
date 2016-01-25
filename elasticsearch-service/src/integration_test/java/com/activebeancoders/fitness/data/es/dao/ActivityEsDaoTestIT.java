package com.activebeancoders.fitness.data.es.dao;

import com.activebeancoders.fitness.data.es.BaseTestIT;
import com.activebeancoders.fitness.data.es.dao.util.EsJsonView;
import com.activebeancoders.fitness.data.entity.Activity;
import com.activebeancoders.fitness.data.search.ActivitySearchCriteria;
import net.pladform.elasticsearch.service.EsService;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ActivityEsDaoTestIT extends BaseTestIT {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public ActivityEsDao activityEsDao;

    @Autowired
    public EsService esService;

    @Test
    public void testSaveToElasticsearchThenRetrieve() throws Exception {
        // create a run
        Activity toBeSaved = new Activity();
        toBeSaved.setId(0L);
        toBeSaved.setComment("my run around the house");
        toBeSaved.setDistance(1.2);
        toBeSaved.setUnit("Miles");

        // save
        activityEsDao.save(toBeSaved);

        // get
        Activity retrieved = activityEsDao.get(toBeSaved.getId());
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
        toBeSaved.setId(0L);
        toBeSaved.setComment("my run around the house");
        toBeSaved.setDistance(1.2);
        toBeSaved.setUnit("Miles");

        // save
        activityEsDao.save(toBeSaved);

        // update
        Activity update = new Activity();
        update.setId(toBeSaved.getId());
        update.setComment("updated");
        activityEsDao.update(update, EsJsonView.Comment.class);

        Activity retrieved = activityEsDao.get(toBeSaved.getId());

        Assert.assertNotNull(toBeSaved);
        Assert.assertNotNull(retrieved);
        Assert.assertEquals(toBeSaved.getId(), retrieved.getId());
        Assert.assertEquals(update.getComment(), retrieved.getComment());
        Assert.assertEquals(toBeSaved.getDistance(), retrieved.getDistance());
        Assert.assertEquals(toBeSaved.getUnit(), retrieved.getUnit());
    }

    @Test
    public void search() throws Exception {
        ActivitySearchCriteria sc = new ActivitySearchCriteria();
        sc.setFullText("Jog 2013 12 01");
        List<Activity> results = activityEsDao.search(sc);
        for (Activity a : results) {
            System.out.println(esService.toJson(a));
        }
    }

}
