package com.activebeancoders.fitness.config;

import net.pladform.elasticsearch.service.EsClient;
import net.pladform.elasticsearch.service.EsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * @author Dan Barrese
 */
@Configuration
@PropertySource(value = "classpath:/elasticsearch-service.properties", ignoreResourceNotFound = false)
public class ElasticsearchServiceConfig {

    //To resolve ${} in @Value
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean // from elasticsearch-util
    public EsService esService() {
        return new EsService();
    }

    @Bean // from elasticsearch-util
    @Scope("singleton")
    public EsClient esClient() {
        return new EsClient();
    }

}

