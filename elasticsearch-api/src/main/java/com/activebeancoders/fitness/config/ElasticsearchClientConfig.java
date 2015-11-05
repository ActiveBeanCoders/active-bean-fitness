package com.activebeancoders.fitness.config;

import com.activebeancoders.fitness.dto.IActivityDto;
import com.activebeancoders.fitness.service.DataLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

/**
 * @author Dan Barrese
 */
@Configuration
public class ElasticsearchClientConfig {

    @Bean
    public HttpInvokerProxyFactoryBean remoteActivityEsDto() {
        HttpInvokerProxyFactoryBean proxy = new HttpInvokerProxyFactoryBean();
        proxy.setServiceUrl("http://localhost:8083/activityEsDto.http");
        proxy.setServiceInterface(IActivityDto.class);
        return proxy;
    }

    @Bean
    public HttpInvokerProxyFactoryBean remoteEsDataLoader() {
        HttpInvokerProxyFactoryBean proxy = new HttpInvokerProxyFactoryBean();
        proxy.setServiceUrl("http://localhost:8083/esDataLoader.http");
        proxy.setServiceInterface(DataLoader.class);
        return proxy;
    }

}

