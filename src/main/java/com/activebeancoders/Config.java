package com.activebeancoders;

import com.activebeancoders.controller.es.ActivityController;
import com.activebeancoders.controller.es.DataLoadController;
import com.activebeancoders.dao.es.ActivityEsDao;
import com.activebeancoders.service.*;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@Import({ ActivityController.class, DataLoadController.class })
@PropertySource(value = "/application.properties", ignoreResourceNotFound = false)
public class Config {

    @Bean
    public ActivityEsDao activityEsDao() {
        return new ActivityEsDao();
    }

    @Bean
    public EsService esService() {
        return new EsService();
    }

    @Bean
    @Scope("singleton")
    public EsClient esClient() {
        return new EsClient();
    }

    @Bean
    public DataLoader dataLoader() {
        return new DataLoader();
    }

    @Bean
    public EsIndexer esIndexer() {
        return new EsIndexer();
    }

    @Bean
    @Scope("singleton")
    public EsObjectMapper esObjectMapper() {
        return new EsObjectMapper();
    }

    @Bean
    public EsMappings esMappings() {
        return new EsMappings();
    }

    //To resolve ${} in @Value
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
