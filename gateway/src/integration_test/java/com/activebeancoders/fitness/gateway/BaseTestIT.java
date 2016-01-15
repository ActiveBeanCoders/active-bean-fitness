package com.activebeancoders.fitness.gateway;

import com.activebeancoders.fitness.GatewayApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author spring.io
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GatewayApplication.class)
@WebIntegrationTest // Make sure app is NOT running outside of your IDE!
public class BaseTestIT {

    @Test
    public void validateContext() throws Exception {
        System.out.println("hi");
    }

}

