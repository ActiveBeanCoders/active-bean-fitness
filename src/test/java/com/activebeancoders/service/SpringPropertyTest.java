package com.activebeancoders.service;

import com.activebeancoders.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class SpringPropertyTest extends BaseTest {

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
