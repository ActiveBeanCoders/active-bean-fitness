package com.activebeancoders.fitness.data.es.config;

import com.activebeancoders.fitness.data.dao.ActivityDao;
import com.activebeancoders.fitness.security.infrastructure.AuthenticationTokenHttpInvokerRequestExecutor;
import com.activebeancoders.fitness.data.service.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

/**
 * @author Dan Barrese
 */
@Configuration
@PropertySource(value = "classpath:/data-es-api.properties", ignoreResourceNotFound = false)
@PropertySource(value = "file:/activebeancoders/data-es-api.properties", ignoreResourceNotFound = true)
public class DataEsClientConfig {

    @Autowired
    private AuthenticationTokenHttpInvokerRequestExecutor executor;

    @Value("${external-url.elasticsearch-service}")
    private String elasticsearchServiceUrl;

    @Bean
    public ActivityDao remoteActivityEsDao() {
        System.out.println(elasticsearchServiceUrl);
        HttpInvokerProxyFactoryBean proxy = new HttpInvokerProxyFactoryBean();
        proxy.setServiceUrl(elasticsearchServiceUrl + "/activityEsDao.http");
        proxy.setServiceInterface(ActivityDao.class);
        proxy.setHttpInvokerRequestExecutor(executor);
        proxy.afterPropertiesSet();
        return (ActivityDao) proxy.getObject();
    }

    @Bean
    public DataLoader remoteEsDataLoader() {
        HttpInvokerProxyFactoryBean proxy = new HttpInvokerProxyFactoryBean();
        proxy.setServiceUrl(elasticsearchServiceUrl + "/esDataLoader.http");
        proxy.setServiceInterface(DataLoader.class);
        proxy.setHttpInvokerRequestExecutor(executor);
        proxy.afterPropertiesSet();
        return (DataLoader) proxy.getObject();
    }

}

