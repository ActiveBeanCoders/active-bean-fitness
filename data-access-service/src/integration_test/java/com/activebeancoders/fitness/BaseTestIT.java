package com.activebeancoders.fitness;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DataAccessServiceApplication.class)
@WebIntegrationTest // Make sure app is NOT running outside of your IDE!
public class BaseTestIT {

    @Test
    public void verifyContext() {
        System.out.println("hi");
    }

}
