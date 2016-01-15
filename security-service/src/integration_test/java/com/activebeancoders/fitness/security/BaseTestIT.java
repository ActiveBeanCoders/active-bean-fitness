package com.activebeancoders.fitness.security;

import com.activebeancoders.fitness.SecurityApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Dan Barrese
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SecurityApplication.class)
@WebIntegrationTest
@IntegrationTest({ "server.port:0" })
public class BaseTestIT {

    @Test
    public void verifyContext() throws Exception {
        System.out.println("hi");
    }

}

