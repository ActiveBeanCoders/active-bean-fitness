package com.activebeancoders.fitness.security.config;

import com.activebeancoders.fitness.security.api.AuthenticationService;
import com.activebeancoders.fitness.security.api.SecurityClientController;
import com.activebeancoders.fitness.security.api.TokenValidationService;
import com.activebeancoders.fitness.security.infrastructure.AuthenticationTokenHttpInvokerRequestExecutor;
import com.activebeancoders.fitness.security.api.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

/**
 * @author Dan Barrese
 */
@Configuration
public class SecurityClientConfig {

    @Autowired
    private AuthenticationTokenHttpInvokerRequestExecutor executor;

    @Bean
    public SecurityService remoteSecurityService() {
        HttpInvokerProxyFactoryBean proxy = new HttpInvokerProxyFactoryBean();
        // TODO: use https
        proxy.setServiceUrl("http://localhost:9999/public/securityService.http");
        proxy.setServiceInterface(SecurityService.class);
        proxy.setHttpInvokerRequestExecutor(executor);
        proxy.afterPropertiesSet();
        return (SecurityService) proxy.getObject();
    }

    @Bean
    public AuthenticationService remoteAuthenticationService() {
        HttpInvokerProxyFactoryBean proxy = new HttpInvokerProxyFactoryBean();
        // TODO: use https
        proxy.setServiceUrl("http://localhost:9999/public/" + SecurityClientController.getAuthenticateUri());
        proxy.setServiceInterface(AuthenticationService.class);
        proxy.setHttpInvokerRequestExecutor(executor);
        proxy.afterPropertiesSet();
        return (AuthenticationService) proxy.getObject();
    }

    @Bean
    public TokenValidationService remoteTokenValidationService() {
        HttpInvokerProxyFactoryBean proxy = new HttpInvokerProxyFactoryBean();
        // TODO: use https
        proxy.setServiceUrl("http://localhost:9999/public/" + SecurityClientController.getTokenValidationUri());
        proxy.setServiceInterface(TokenValidationService.class);
        proxy.setHttpInvokerRequestExecutor(executor);
        proxy.afterPropertiesSet();
        return (TokenValidationService) proxy.getObject();
    }

}

