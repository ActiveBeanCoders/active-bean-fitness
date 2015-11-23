package com.activebeancoders.fitness.config;

import com.activebeancoders.fitness.dto.ActivityDto;
import com.activebeancoders.fitness.dto.IActivityDto;
import com.activebeancoders.fitness.dto.IdGenerator;
import com.activebeancoders.fitness.service.AllDataLoaderWorker;
import com.activebeancoders.fitness.service.DataLoader;
import com.activebeancoders.fitness.service.DataLoaderWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dan Barrese
 */
@Configuration
@PropertySource(value = "classpath:/data-access-service.properties", ignoreResourceNotFound = false)
public class DataAccessServiceConfig {

    @Autowired
    @Qualifier("remoteActivityEsDto")
    private IActivityDto activityEsDto;

    @Autowired
    @Qualifier("remoteEsDataLoader")
    private DataLoader esDataLoader;

    @Autowired
    @Qualifier("remoteActivityHibDto")
    private IActivityDto activityHibDto;

    @Autowired
    @Qualifier("remoteHibDataLoader")
    private DataLoader hibDataLoader;

    @Bean
    public ActivityDto activityDto() {
        List<IActivityDto> dtos = new ArrayList<>();
        dtos.add(activityEsDto);
        dtos.add(activityHibDto);
        return new ActivityDto(dtos);
    }

    @Bean
    public DataLoaderWorker indexerWorker() {
        Map<String, DataLoader> loaders = new LinkedHashMap<>();
        loaders.put("es", esDataLoader);
        loaders.put("hib", hibDataLoader);
        return new AllDataLoaderWorker(loaders);
    }

    //To resolve ${} in @Value
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    @Scope("singleton")
    @Lazy
    public IdGenerator idGenerator() {
        return new IdGenerator();
    }

}

