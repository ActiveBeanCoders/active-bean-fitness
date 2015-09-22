package com.activebeancoders.fitness.service.es;

import com.activebeancoders.BaseTest;
import com.activebeancoders.fitness.entity.Activity;
import net.pladform.elasticsearch.service.EsService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class FitnessObjectMapperTest extends BaseTest {

    @Autowired
    EsService esService;

    @Test
    public void asdf() throws Exception {
        Activity a = new Activity();
        String json = esService.toJson(a);
        System.out.println(json);
    }

}
