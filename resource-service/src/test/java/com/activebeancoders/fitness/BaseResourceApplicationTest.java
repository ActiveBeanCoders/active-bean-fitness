package com.activebeancoders.fitness;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * @author Dan Barrese
 */
@Configuration
@ComponentScan("com.activebeancoders.fitness")
@EnableAutoConfiguration
@EnableWebSecurity // TODO: needed in main?
public class BaseResourceApplicationTest {
}
