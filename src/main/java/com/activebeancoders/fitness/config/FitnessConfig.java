package com.activebeancoders.fitness.config;

import com.activebeancoders.fitness.controller.es.ActivityController;
import com.activebeancoders.fitness.controller.es.DataLoadController;
import com.activebeancoders.fitness.dao.ActivityDao;
import com.activebeancoders.fitness.dao.IActivityDao;
import com.activebeancoders.fitness.dao.IdGenerator;
import com.activebeancoders.fitness.dao.es.ActivityEsDao;
import com.activebeancoders.fitness.dao.hib.ActivityHibDao;
import com.activebeancoders.fitness.service.DataLoader;
import com.activebeancoders.fitness.service.EsIndexer;
import com.activebeancoders.fitness.service.EsMappings;
import com.activebeancoders.fitness.service.es.ActivityIndexManager;
import com.activebeancoders.fitness.service.es.FitnessObjectMapper;
import net.pladform.elasticsearch.service.EsClient;
import net.pladform.elasticsearch.service.EsService;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Import({ ActivityController.class, DataLoadController.class })
@PropertySource(value = "classpath:/application.properties", ignoreResourceNotFound = false)
@ComponentScan({ "com.activebeancoders.*.config" })
public class FitnessConfig {

    @Primary
    @Bean
    public IActivityDao activityDao() {
        Map<String, IActivityDao> daos = new HashMap<>();
        daos.put("es", activityEsDao());
        daos.put("hib", activityHibDao());
        return new ActivityDao("es", daos);
    }

    @Bean
    public IActivityDao activityEsDao() {
        return new ActivityEsDao();
    }

   @Bean
    public IActivityDao activityHibDao() {
        return new ActivityHibDao();
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

    @Bean
    @Scope("singleton")
    @Lazy
    public IdGenerator idGenerator() {
        return new IdGenerator();
    }

}

