package com.activebeancoders;

import com.activebeancoders.fitness.config.FitnessConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FitnessConfig.class)
public class BaseTest {

    @Test
    public void verifyContext() {
        System.out.println("hi");
    }

}
