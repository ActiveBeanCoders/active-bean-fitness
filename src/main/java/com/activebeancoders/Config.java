package com.activebeancoders;

import com.activebeancoders.dao.es.ActivityEsDao;
import com.activebeancoders.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public ActivityEsDao activityDao() {
        return new ActivityEsDao();
    }

    @Bean
    public EsService esService() {
        return new EsService();
    }

    @Bean
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
    public EsObjectMapper esObjectMapper() {
        return new EsObjectMapper();
    }

}
