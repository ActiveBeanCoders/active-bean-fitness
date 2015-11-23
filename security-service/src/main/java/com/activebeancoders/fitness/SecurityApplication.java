package com.activebeancoders.fitness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author Dan Barrese
 */
@Configuration
@EnableWebMvc
@ComponentScan
@EnableAutoConfiguration
@EnableConfigurationProperties
@PropertySource(value = "classpath:/security-service.properties", ignoreResourceNotFound = false)
public class SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }

}
