package com.activebeancoders.fitness.data.es.config;

import com.activebeancoders.fitness.data.dao.ActivityDao;
import com.activebeancoders.fitness.data.service.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;

/**
 * Defines beans which can be accessed from other services.
 *
 * @author Dan Barrese
 */
@Configuration
public class DataEsServiceRemotingConfig {

    @Autowired
    @Qualifier("activityEsDao")
    private ActivityDao activityDao;

    @Autowired
    @Qualifier("esDataLoader")
    private DataLoader dataLoader;

    @Bean(name = "/activityEsDao.http")
    public HttpInvokerServiceExporter activityDao() {
        HttpInvokerServiceExporter httpInvokerServiceExporter = new HttpInvokerServiceExporter();
        httpInvokerServiceExporter.setService(activityDao);
        httpInvokerServiceExporter.setServiceInterface(ActivityDao.class);
        return httpInvokerServiceExporter;
    }

    @Bean(name = "/esDataLoader.http")
    public HttpInvokerServiceExporter esDataLoader() {
        HttpInvokerServiceExporter httpInvokerServiceExporter = new HttpInvokerServiceExporter();
        httpInvokerServiceExporter.setService(dataLoader);
        httpInvokerServiceExporter.setServiceInterface(DataLoader.class);
        return httpInvokerServiceExporter;
    }

}

