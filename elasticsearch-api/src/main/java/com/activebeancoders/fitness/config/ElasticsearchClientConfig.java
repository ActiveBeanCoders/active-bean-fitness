package com.activebeancoders.fitness.config;

import com.activebeancoders.fitness.dto.IActivityDto;
import com.activebeancoders.fitness.security.infrastructure.AuthenticationTokenHttpInvokerRequestExecutor;
import com.activebeancoders.fitness.service.DataLoader;
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
public class ElasticsearchClientConfig {

    @Autowired
    private AuthenticationTokenHttpInvokerRequestExecutor executor;

    @Value("${external-url.elasticsearch-service}")
    private String elasticsearchServiceUrl;

    @Bean
    public IActivityDto remoteActivityEsDto() {
        HttpInvokerProxyFactoryBean proxy = new HttpInvokerProxyFactoryBean();
        proxy.setServiceUrl(elasticsearchServiceUrl + "/activityEsDto.http");
        proxy.setServiceInterface(IActivityDto.class);
        proxy.setHttpInvokerRequestExecutor(executor);
        proxy.afterPropertiesSet();
        return (IActivityDto) proxy.getObject();
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

