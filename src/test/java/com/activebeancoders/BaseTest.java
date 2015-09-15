package com.activebeancoders;

import com.activebeancoders.fitness.Config;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Config.class)
public class BaseTest {

    @Test
    public void verifyContext() {
        System.out.println("hi");
    }

}
