package com.activebeancoders.fitness;

import com.activebeancoders.fitness.controller.es.ActivityController;
import com.activebeancoders.fitness.controller.es.DataLoadController;
import com.activebeancoders.fitness.dao.IActivityDao;
import com.activebeancoders.fitness.dao.es.ActivityEsDao;
import com.activebeancoders.fitness.service.*;
import com.activebeancoders.fitness.service.es.ActivityIndexManager;
import com.activebeancoders.fitness.service.es.FitnessObjectMapper;
import net.pladform.elasticsearch.service.EsClient;
import net.pladform.elasticsearch.service.EsService;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@Import({ ActivityController.class, DataLoadController.class })
@PropertySource(value = "classpath:/application.properties", ignoreResourceNotFound = false)
public class Config {

    @Bean
    public IActivityDao activityEsDao() {
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
    public FitnessObjectMapper fitnessObjectMapper() {
        return new FitnessObjectMapper();
    }

    @Bean
    public EsMappings esMappings() {
        return new EsMappings();
    }

    //To resolve ${} in @Value
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public ActivityIndexManager activityIndexManager() {
        return new ActivityIndexManager();
    }

}
