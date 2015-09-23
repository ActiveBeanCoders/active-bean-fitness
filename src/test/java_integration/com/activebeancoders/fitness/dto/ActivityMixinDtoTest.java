package com.activebeancoders.fitness.dto;

import com.activebeancoders.BaseTest;
import com.activebeancoders.fitness.dto.es.ActivityEsDto;
import com.activebeancoders.fitness.dto.es.util.EsJsonView;
import com.activebeancoders.fitness.entity.Activity;
import com.activebeancoders.fitness.search.ActivitySearchCriteria;
import net.pladform.elasticsearch.service.EsService;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ActivityMixinDtoTest extends BaseTest {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public ActivityEsDto activityEsDto;

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
        activityEsDto.save(toBeSaved);

        // get
        Activity retrieved = activityEsDto.get(toBeSaved.getId());
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
        activityEsDto.save(toBeSaved);

        // update
        Activity update = new Activity();
        update.setId(toBeSaved.getId());
        update.setComment("updated");
        activityEsDto.update(update, EsJsonView.Comment.class);

        Activity retrieved = activityEsDto.get(toBeSaved.getId());

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
        List<Activity> results = activityEsDto.search(sc);
        for (Activity a : results) {
            System.out.println(esService.toJson(a));
        }
    }

}
