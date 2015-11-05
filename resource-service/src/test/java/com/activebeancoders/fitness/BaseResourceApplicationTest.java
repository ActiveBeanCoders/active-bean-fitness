package com.activebeancoders.fitness;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author Dan Barrese
 */
@Configuration
@ComponentScan("com.activebeancoders.fitness")
@EnableAutoConfiguration
@EnableRedisHttpSession
@EnableWebSecurity // TODO: needed in main?
public class BaseResourceApplicationTest {
}
