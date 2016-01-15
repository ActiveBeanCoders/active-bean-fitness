package com.activebeancoders.fitness;

import com.activebeancoders.fitness.dto.IActivityDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Dan Barrese
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DataAccessServiceApplication.class)
@WebIntegrationTest // Make sure app is NOT running outside of your IDE!
public class RemotingTestIT {

    @Autowired
    @Qualifier("activityDto")
    IActivityDto activityDto;

    @Test
    public void asdf() throws Exception {
        Long id = activityDto.findMaxId();
        System.out.println(id);
    }

}
