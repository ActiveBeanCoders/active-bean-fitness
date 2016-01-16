package com.activebeancoders.fitness;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DataAccessServiceApplication.class)
@WebIntegrationTest
@IntegrationTest({ "server.port:0" })
public class BaseTestIT {

    @Test
    public void verifyContext() {
        System.out.println("hi");
    }

}
