package com.activebeancoders.fitness;

import com.activebeancoders.fitness.dto.IActivityDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author Dan Barrese
 */
public class RemotingTestIT extends BaseTestIT {

    @Autowired
    @Qualifier("activityDto")
    IActivityDto activityDto;

    @Test
    public void asdf() throws Exception {
        Long id = activityDto.findMaxId();
        System.out.println(id);
    }

}
