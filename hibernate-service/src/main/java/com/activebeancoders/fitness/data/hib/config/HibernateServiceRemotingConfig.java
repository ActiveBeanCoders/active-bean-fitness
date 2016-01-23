package com.activebeancoders.fitness.data.hib.config;

import com.activebeancoders.fitness.data.dao.IActivityDao;
import com.activebeancoders.fitness.data.service.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;

/**
 * @author Dan Barrese
 */
@Configuration
public class HibernateServiceRemotingConfig {

    @Autowired
    private IActivityDao activityDao;

    @Autowired
    private DataLoader dataLoader;

    @Bean(name = "/activityHibDao.http")
    public HttpInvokerServiceExporter activityHibDao() {
        HttpInvokerServiceExporter httpInvokerServiceExporter = new HttpInvokerServiceExporter();
        httpInvokerServiceExporter.setService(activityDao);
        httpInvokerServiceExporter.setServiceInterface(IActivityDao.class);
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

