package com.activebeancoders.fitness.security;

import com.activebeancoders.fitness.security.config.SecurityServiceDataConfig;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Use this class to create database tables.  If the test fails, check the error message.
 *
 * @author Dan Barrese
 */
public class SecurityServiceTableCreatorTestIT extends BaseTestIT {

    @Autowired
    SecurityServiceDataConfig securityServiceDataConfig;

    @Test
    public void createDatabaseTables() throws Exception {
        if (!securityServiceDataConfig.getHibernateHbmToDdlAuto().toLowerCase().equals("create")) {
            Assert.fail("securityServiceDataConfig.getHibernateHbmToDdlAuto() must be set to 'create' for this test to work.  Tables were NOT created.");
        }
    }

}

