package com.activebeancoders.fitness.data.hib.config;

import com.activebeancoders.fitness.data.dao.ActivityDao;
import com.activebeancoders.fitness.data.service.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;

/**
 * @author Dan Barrese
 */
@Configuration
public class DataDbServiceRemotingConfig {

    @Autowired
    private ActivityDao activityDao;

    @Autowired
    private DataLoader dataLoader;

    @Bean(name = "/activityMySqlDao.http")
    public HttpInvokerServiceExporter activityMySqlDao() {
        HttpInvokerServiceExporter httpInvokerServiceExporter = new HttpInvokerServiceExporter();
        httpInvokerServiceExporter.setService(activityDao);
        httpInvokerServiceExporter.setServiceInterface(ActivityDao.class);
        return httpInvokerServiceExporter;
    }

    @Bean(name = "/hibDataLoader.http")
    public HttpInvokerServiceExporter hibDataLoader() {
        HttpInvokerServiceExporter httpInvokerServiceExporter = new HttpInvokerServiceExporter();
        httpInvokerServiceExporter.setService(dataLoader);
        httpInvokerServiceExporter.setServiceInterface(DataLoader.class);
        return httpInvokerServiceExporter;
    }

}

