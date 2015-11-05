package com.activebeancoders.fitness.config;

import com.activebeancoders.fitness.AuthenticationTokenHttpInvokerRequestExecutor;
import com.activebeancoders.fitness.example.api.samplestuff.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

@Configuration
public class SecurityClientConfig {

    @Autowired
    private AuthenticationTokenHttpInvokerRequestExecutor executor;

    @Bean
    public SecurityService remoteSecurityService() {
        HttpInvokerProxyFactoryBean proxy = new HttpInvokerProxyFactoryBean();
        // TODO: use https
        proxy.setServiceUrl("http://localhost:9999/free/securityService.http");
        proxy.setServiceInterface(SecurityService.class);
        proxy.setHttpInvokerRequestExecutor(executor);
        proxy.afterPropertiesSet();
        return (SecurityService) proxy.getObject();
    }

}

