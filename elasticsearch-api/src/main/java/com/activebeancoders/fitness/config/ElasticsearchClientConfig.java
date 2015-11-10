package com.activebeancoders.fitness.config;

import com.activebeancoders.fitness.dto.IActivityDto;
import com.activebeancoders.fitness.security.infrastructure.AuthenticationTokenHttpInvokerRequestExecutor;
import com.activebeancoders.fitness.service.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

/**
 * @author Dan Barrese
 */
@Configuration
public class ElasticsearchClientConfig {

    @Autowired
    private AuthenticationTokenHttpInvokerRequestExecutor executor;

    @Bean
    public IActivityDto remoteActivityEsDto() {
        HttpInvokerProxyFactoryBean proxy = new HttpInvokerProxyFactoryBean();
        // TODO: use https
        proxy.setServiceUrl("http://localhost:8083/activityEsDto.http");
        proxy.setServiceInterface(IActivityDto.class);
        proxy.setHttpInvokerRequestExecutor(executor);
        proxy.afterPropertiesSet();
        return (IActivityDto) proxy.getObject();
    }

    @Bean
    public DataLoader remoteEsDataLoader() {
        HttpInvokerProxyFactoryBean proxy = new HttpInvokerProxyFactoryBean();
        // TODO: use https
        proxy.setServiceUrl("http://localhost:8083/esDataLoader.http");
        proxy.setServiceInterface(DataLoader.class);
        proxy.setHttpInvokerRequestExecutor(executor);
        proxy.afterPropertiesSet();
        return (DataLoader) proxy.getObject();
    }

}

