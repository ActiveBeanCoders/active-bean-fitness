package com.activebeancoders.fitness;

import com.activebeancoders.fitness.config.DataAccessServiceConfig;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Dan Barrese
 */
@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DataAccessServiceConfig.class})
public class BaseResourceApplicationTest {
}

