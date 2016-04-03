package com.activebeancoders.fitness.security.config;

import com.activebeancoders.fitness.security.api.AuthenticationService;
import com.activebeancoders.fitness.security.api.SecurityClientController;
import com.activebeancoders.fitness.security.api.UserManagementService;
import com.activebeancoders.fitness.security.infrastructure.AuthenticationTokenHttpInvokerRequestExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;

/**
 * Spring configuration for security-api.
 *
 * @author Dan Barrese
 */
@Configuration
@ComponentScan(basePackages = "com.activebeancoders.fitness.security")
@PropertySource(value = "classpath:/security-api.properties", ignoreResourceNotFound = true)
@PropertySource(value = "file:/activebeancoders/security-api.properties", ignoreResourceNotFound = true)
public class SecurityClientConfig {

    @Autowired
    private AuthenticationTokenHttpInvokerRequestExecutor executor;

    @Value("${security-service.url.protocol}")
    private String protocol;

    @Value("${security-service.url.hostname}")
    private String hostname;

    @Value("${security-service.url.port}")
    private Integer port;

    @PostConstruct
    protected void init() {
        Assert.notNull(protocol);
        Assert.isTrue(!protocol.startsWith("${"));
        Assert.notNull(hostname);
        Assert.isTrue(!hostname.startsWith("${"));
        Assert.notNull(port);
    }

    @Bean
    public String securityServiceRemoteUrl() {
        return String.format("%s://%s:%d", protocol, hostname, port);
    }

    @Bean
    public UserManagementService remoteUserManagementService() {
        HttpInvokerProxyFactoryBean proxy = new HttpInvokerProxyFactoryBean();
        proxy.setServiceUrl(securityServiceRemoteUrl() + "/api/authz/securityService.http");
        proxy.setServiceInterface(UserManagementService.class);
        proxy.setHttpInvokerRequestExecutor(executor);
        proxy.afterPropertiesSet();
        return (UserManagementService) proxy.getObject();
    }

    @Bean
    public AuthenticationService remoteAuthenticationService() {
        HttpInvokerProxyFactoryBean proxy = new HttpInvokerProxyFactoryBean();
        proxy.setServiceUrl(securityServiceRemoteUrl() + "/api/authz/authenticationService.http");
        proxy.setServiceInterface(AuthenticationService.class);
        proxy.setHttpInvokerRequestExecutor(executor);
        proxy.afterPropertiesSet();
        return (AuthenticationService) proxy.getObject();
    }

}

