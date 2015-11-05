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
public class HibernateClientConfig {

    @Bean
    public HttpInvokerProxyFactoryBean remoteActivityHibDto() {
        HttpInvokerProxyFactoryBean proxy = new HttpInvokerProxyFactoryBean();
        proxy.setServiceUrl("http://localhost:8086/activityHibDto.http");
        proxy.setServiceInterface(IActivityDto.class);
        return proxy;
    }

    @Bean
    public HttpInvokerProxyFactoryBean remoteHibDataLoader() {
        HttpInvokerProxyFactoryBean proxy = new HttpInvokerProxyFactoryBean();
        proxy.setServiceUrl("http://localhost:8086/hibDataLoader.http");
        proxy.setServiceInterface(DataLoader.class);
        return proxy;
    }

}

