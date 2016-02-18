package com.activebeancoders.fitness.data.config;

import com.activebeancoders.fitness.data.dao.ActivityDao;
import com.activebeancoders.fitness.data.dao.IdGenerator;
import com.activebeancoders.fitness.data.dao.IActivityDao;
import com.activebeancoders.fitness.data.service.AllDataLoaderWorker;
import com.activebeancoders.fitness.data.service.DataLoader;
import com.activebeancoders.fitness.data.service.DataLoaderWorker;
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
@PropertySource(value = "file:/activebeancoders/data-access-service.properties", ignoreResourceNotFound = true)
public class DataAccessServiceConfig {

    @Autowired
    @Qualifier("remoteActivityEsDao")
    private IActivityDao activityEsDao;

    @Autowired
    @Qualifier("remoteEsDataLoader")
    private DataLoader esDataLoader;

    @Autowired
    @Qualifier("remoteActivityHibDao")
    private IActivityDao activityHibDao;

    @Autowired
    @Qualifier("remoteHibDataLoader")
    private DataLoader hibDataLoader;

    @Bean
    public ActivityDao activityDao() {
        List<IActivityDao> daos = new ArrayList<>();
        daos.add(activityEsDao);
        daos.add(activityHibDao);
        return new ActivityDao(daos);
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

