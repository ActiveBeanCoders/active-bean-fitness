package com.activebeancoders.fitness.gateway;

import com.activebeancoders.fitness.GatewayApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

/**
 * @author spring.io
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GatewayApplication.class)
public class GatewayApplicationTestIT {

    @Value("${local.server.port}")
    private int port;

    private RestTemplate template = new TestRestTemplate();

    @Test
    public void homePageLoads() {
        ResponseEntity<String> response = template.getForEntity("http://localhost:"
                + port + "/", String.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}

