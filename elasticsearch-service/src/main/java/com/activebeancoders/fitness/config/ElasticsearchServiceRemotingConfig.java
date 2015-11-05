package com.activebeancoders.fitness.config;

import com.activebeancoders.fitness.dto.IActivityDto;
import com.activebeancoders.fitness.service.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;

/**
 * Defines beans which can be accessed from other services.
 *
 * @author Dan Barrese
 */
@Configuration
public class ElasticsearchServiceRemotingConfig {

    @Autowired
    private IActivityDto activityDto;

    @Autowired
    private DataLoader dataLoader;

    @Bean(name = "/activityEsDto.http")
    public HttpInvokerServiceExporter activityDto() {
        HttpInvokerServiceExporter httpInvokerServiceExporter = new HttpInvokerServiceExporter();
        httpInvokerServiceExporter.setService(activityDto);
        httpInvokerServiceExporter.setServiceInterface(IActivityDto.class);
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

