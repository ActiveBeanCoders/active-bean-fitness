package com.activebeancoders.fitness.data.hib.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * @author Dan Barrese
 */
@Configuration
@PropertySource(value = "classpath:/data-db-service.properties", ignoreResourceNotFound = false)
@PropertySource(value = "file:/activebeancoders/data-db-service.properties", ignoreResourceNotFound = true)
@PropertySource(value = "file:/activebeancoders/urls.properties", ignoreResourceNotFound = true)
public class DataDbServiceConfig {

    //To resolve ${} in @Value
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}

