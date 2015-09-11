package com.activebeancoders;

import com.activebeancoders.controller.es.ActivityController;
import com.activebeancoders.controller.es.DataLoadController;
import com.activebeancoders.service.EsClient;
import com.activebeancoders.service.EsIndexer;
import com.activebeancoders.service.EsService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.elasticsearch.repository.support.ElasticsearchRepositoryFactoryBean;

@Configuration
@EnableAutoConfiguration(exclude = ElasticsearchAutoConfiguration.class)
@Import({ ActivityController.class, DataLoadController.class })
@PropertySource(value = "classpath:/application.properties", ignoreResourceNotFound = false)
@EnableElasticsearchRepositories(basePackages = "com.activebeancoders.dao.es")
public class Config {

    @Bean
    public EsService esService() {
        return new EsService();
    }

    @Primary
    @Bean
    @Scope("singleton")
    public EsClient elasticsearchClient() {
        return new EsClient();
    }

    @Bean
    public EsIndexer esIndexer() {
        return new EsIndexer();
    }

    //To resolve ${} in @Value
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchTemplate(elasticsearchClient());
    }

}
