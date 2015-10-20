package com.activebeancoders.fitness.config;

import com.activebeancoders.fitness.dto.IActivityDto;
import com.activebeancoders.fitness.service.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;

@Configuration
public class HibernateServiceRemotingConfig {

    @Autowired
    private IActivityDto activityDto;

    @Autowired
    private DataLoader dataLoader;

    @Bean(name = "/activityHibDto.http")
    public HttpInvokerServiceExporter activityHibDto() {
        HttpInvokerServiceExporter httpInvokerServiceExporter = new HttpInvokerServiceExporter();
        httpInvokerServiceExporter.setService(activityDto);
        httpInvokerServiceExporter.setServiceInterface(IActivityDto.class);
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

