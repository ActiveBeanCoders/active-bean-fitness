package com.activebeancoders.fitness.data.es.config;

import com.activebeancoders.fitness.data.dao.ActivityDao;
import com.activebeancoders.fitness.security.infrastructure.AuthenticationTokenHttpInvokerRequestExecutor;
import com.activebeancoders.fitness.data.service.DataLoader;
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
@PropertySource(value = "classpath:/data-es-api.properties", ignoreResourceNotFound = false)
@PropertySource(value = "file:/activebeancoders/data-es-api.properties", ignoreResourceNotFound = true)
public class DataEsClientConfig {

    @Autowired
    private AuthenticationTokenHttpInvokerRequestExecutor executor;

    @Value("${data-es-service.url.protocol}")
    private String protocol;

    @Value("${data-es-service.url.hostname}")
    private String hostname;

    @Value("${data-es-service.url.port}")
    private Integer port;

    @PostConstruct
    protected void init() {
        Assert.assertStringIsInitialized(protocol);
        Assert.assertStringIsInitialized(hostname);
        Assert.assertNotNull(port);
    }

    @Bean
    public String dataEsServiceRemoteUrl() {
        return String.format("%s://%s:%d", protocol, hostname, port);
    }

    @Bean
    public ActivityDao remoteActivityEsDao() {
        HttpInvokerProxyFactoryBean proxy = new HttpInvokerProxyFactoryBean();
        proxy.setServiceUrl(dataEsServiceRemoteUrl() + "/activityEsDao.http");
        proxy.setServiceInterface(ActivityDao.class);
        proxy.setHttpInvokerRequestExecutor(executor);
        proxy.afterPropertiesSet();
        return (ActivityDao) proxy.getObject();
    }

    @Bean
    public DataLoader remoteEsDataLoader() {
        HttpInvokerProxyFactoryBean proxy = new HttpInvokerProxyFactoryBean();
        proxy.setServiceUrl(dataEsServiceRemoteUrl() + "/esDataLoader.http");
        proxy.setServiceInterface(DataLoader.class);
        proxy.setHttpInvokerRequestExecutor(executor);
        proxy.afterPropertiesSet();
        return (DataLoader) proxy.getObject();
    }

}

