package com.activebeancoders.fitness.data.es;

import com.activebeancoders.fitness.data.es.dao.ActivityEsDao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author Dan Barrese
 */
@Configuration
@EnableWebMvc
@ComponentScan
@EnableAutoConfiguration
@EnableConfigurationProperties
public class ElasticsearchServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticsearchServiceApplication.class, args);
    }

}

