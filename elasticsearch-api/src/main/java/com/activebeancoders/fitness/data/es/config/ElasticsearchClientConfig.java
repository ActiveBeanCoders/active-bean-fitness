package com.activebeancoders.fitness.data.es.config;

import com.activebeancoders.fitness.data.dao.IActivityDao;
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
@PropertySource(value = "classpath:/elasticsearch-api.properties", ignoreResourceNotFound = false)
@PropertySource(value = "file:/activebeancoders/elasticsearch-api.properties", ignoreResourceNotFound = true)
public class ElasticsearchClientConfig {

    @Autowired
    private AuthenticationTokenHttpInvokerRequestExecutor executor;

    @Value("${external-url.elasticsearch-service}")
    private String elasticsearchServiceUrl;

    @Bean
    public IActivityDao remoteActivityEsDao() {
        System.out.println(elasticsearchServiceUrl);
        HttpInvokerProxyFactoryBean proxy = new HttpInvokerProxyFactoryBean();
        proxy.setServiceUrl(elasticsearchServiceUrl + "/activityEsDao.http");
        proxy.setServiceInterface(IActivityDao.class);
        proxy.setHttpInvokerRequestExecutor(executor);
        proxy.afterPropertiesSet();
        return (IActivityDao) proxy.getObject();
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

