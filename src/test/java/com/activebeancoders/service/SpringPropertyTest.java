package com.activebeancoders.service;

import com.activebeancoders.Config;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class SpringPropertyTest {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Value("${server.port}")
    public String port;

    @Test
    public void readPropertyFromFile() throws Exception {
        log.info(port);
        Assert.assertNotNull(port);
        Assert.assertFalse(port.equals("${server.port}"));
    }

}
