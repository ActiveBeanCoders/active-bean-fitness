package com.activebeancoders.fitness.data.hib;

import com.activebeancoders.fitness.data.hib.config.DataDbServiceDataConfig;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Use this class to create database tables.  If the test fails, check the error message.
 *
 * @author Dan Barrese
 */
public class DataDbServiceTableCreatorTestIT extends BaseTestIT {

    @Autowired
    DataDbServiceDataConfig dataDbServiceDataConfig;

    @Test
    public void createDatabaseTables() throws Exception {
        if (!dataDbServiceDataConfig.getHibernateHbmToDdlAuto().toLowerCase().equals("create")) {
            Assert.fail("hibernateServiceDataConfig.getHibernateHbmToDdlAuto() must be set to 'create' for this test to work.  Tables were NOT created.");
        }
    }

}

