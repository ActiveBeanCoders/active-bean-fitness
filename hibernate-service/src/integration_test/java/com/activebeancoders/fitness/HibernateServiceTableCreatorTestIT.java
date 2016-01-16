package com.activebeancoders.fitness;

import com.activebeancoders.fitness.config.HibernateServiceDataConfig;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Use this class to create database tables.  If the test fails, check the error message.
 *
 * @author Dan Barrese
 */
public class HibernateServiceTableCreatorTestIT extends BaseTestIT {

    @Autowired
    HibernateServiceDataConfig hibernateServiceDataConfig;

    @Test
    public void createDatabaseTables() throws Exception {
        if (!hibernateServiceDataConfig.getHibernateHbmToDdlAuto().toLowerCase().equals("create")) {
            Assert.fail("hibernateServiceDataConfig.getHibernateHbmToDdlAuto() must be set to 'create' for this test to work.  Tables were NOT created.");
        }
    }

}

