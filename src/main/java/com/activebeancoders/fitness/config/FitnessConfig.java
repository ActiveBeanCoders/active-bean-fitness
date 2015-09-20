package com.activebeancoders.fitness.config;

import com.activebeancoders.fitness.controller.es.ActivityController;
import com.activebeancoders.fitness.controller.es.DataLoadController;
import com.activebeancoders.fitness.dto.ActivityDto;
import com.activebeancoders.fitness.dto.IActivityDto;
import com.activebeancoders.fitness.dto.IdGenerator;
import com.activebeancoders.fitness.dto.es.ActivityEsDto;
import com.activebeancoders.fitness.dto.hib.ActivityHibDto;
import com.activebeancoders.fitness.service.*;
import com.activebeancoders.fitness.service.es.ActivityIndexManager;
import com.activebeancoders.fitness.service.es.FitnessObjectMapper;
import net.pladform.elasticsearch.service.EsClient;
import net.pladform.elasticsearch.service.EsService;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Import({ FitnessDataConfig.class, ActivityController.class, DataLoadController.class })
@PropertySource(value = "classpath:/application.properties", ignoreResourceNotFound = false)
public class FitnessConfig {

    @Primary
    @Bean
    public IActivityDto activityDto() {
        Map<String, IActivityDto> dtos = new HashMap<>();
        dtos.put("es", activityEsDto());
        dtos.put("hib", activityHibDto());
        return new ActivityDto("es", dtos);
    }

    @Bean
    public DataLoaderWorker indexerWorker() {
        Map<String, DataLoader> loaders = new HashMap<>();
        loaders.put("es", esDataLoader());
        loaders.put("hib", hibDataLoader());
        return new AllDataLoaderWorker(loaders);
    }

    //To resolve ${} in @Value
    @Bean public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() { return new PropertySourcesPlaceholderConfigurer(); }

    @Bean @Scope("singleton") @Lazy public IdGenerator idGenerator() { return new IdGenerator(); }

    // elasticsearch
    // ```````````````````````````````````````````````````````````````````````

    @Bean public EsService esService() { return new EsService(); }
    @Bean @Scope("singleton") public EsClient esClient() { return new EsClient(); }
    @Bean public DataLoader esDataLoader() { return new EsDataLoader(); }
    @Bean public IActivityDto activityEsDto() { return new ActivityEsDto(); }
    @Bean @Scope("singleton") public FitnessObjectMapper fitnessObjectMapper() { return new FitnessObjectMapper(); }
    @Bean public EsMappings esMappings() { return new EsMappings(); }
    @Bean public ActivityIndexManager activityIndexManager() { return new ActivityIndexManager(); }

    // hibernate
    // ```````````````````````````````````````````````````````````````````````

    @Bean public DataLoader hibDataLoader() { return new HibDataLoader(); }
    @Bean public IActivityDto activityHibDto() { return new ActivityHibDto(); }

}

