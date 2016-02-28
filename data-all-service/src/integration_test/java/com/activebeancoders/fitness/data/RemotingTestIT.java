package com.activebeancoders.fitness.data;

import com.activebeancoders.fitness.data.dao.ActivityDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author Dan Barrese
 */
public class RemotingTestIT extends BaseTestIT {

    @Autowired
    @Qualifier("activityDao")
    ActivityDao activityDao;

    @Test
    public void asdf() throws Exception {
        Long id = activityDao.findMaxId();
        System.out.println(id);
    }

}
