package com.activebeancoders.fitness.config;

import com.activebeancoders.fitness.dto.IActivityDto;
import com.activebeancoders.fitness.security.infrastructure.AuthenticationTokenHttpInvokerRequestExecutor;
import com.activebeancoders.fitness.service.DataLoader;
import com.activebeancoders.fitness.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import javax.annotation.PostConstruct;

/**
 * @author Dan Barrese
 */
@Configuration
@PropertySource(value = "classpath:/hibernate-api.properties", ignoreResourceNotFound = false)
@PropertySource(value = "file:${user.home}/activebeancoders/global.properties", ignoreResourceNotFound = true)
@PropertySource(value = "file:${user.home}/activebeancoders/hibernate-api.properties", ignoreResourceNotFound = true)
public class HibernateClientConfig {

    @Autowired
    private AuthenticationTokenHttpInvokerRequestExecutor executor;

    @Value("${external-url.hibernate-service}")
    private String hibernateServiceUrl;

    @PostConstruct
    protected void init() {
        Assert.assertStringIsInitialized(hibernateServiceUrl);
    }

    @Bean
    public IActivityDto remoteActivityHibDto() {
        HttpInvokerProxyFactoryBean proxy = new HttpInvokerProxyFactoryBean();
        proxy.setServiceUrl(hibernateServiceUrl + "/activityHibDto.http");
        proxy.setServiceInterface(IActivityDto.class);
        proxy.setHttpInvokerRequestExecutor(executor);
        proxy.afterPropertiesSet();
        return (IActivityDto) proxy.getObject();
    }

    @Bean
    public DataLoader remoteHibDataLoader() {
        HttpInvokerProxyFactoryBean proxy = new HttpInvokerProxyFactoryBean();
        proxy.setServiceUrl(hibernateServiceUrl + "/hibDataLoader.http");
        proxy.setServiceInterface(DataLoader.class);
        proxy.setHttpInvokerRequestExecutor(executor);
        proxy.afterPropertiesSet();
        return (DataLoader) proxy.getObject();
    }

}

