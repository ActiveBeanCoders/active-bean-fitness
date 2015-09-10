package com.activebeancoders;

import com.activebeancoders.controller.es.ActivityController;
import com.activebeancoders.controller.es.DataLoadController;
import com.activebeancoders.dao.es.ActivityEsDao;
import com.activebeancoders.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

@Configuration
@Import({ ActivityController.class, DataLoadController.class })
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

}
