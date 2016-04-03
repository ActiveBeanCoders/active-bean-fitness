package com.activebeancoders.fitness.security.config;

import com.activebeancoders.fitness.security.api.AuthenticationService;
import com.activebeancoders.fitness.security.api.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;

/**
 * Contains bean definitions which can be remoted to from other services.
 *
 * @author Dan Barrese
 */
@Configuration
public class SecurityServiceRemotingConfig {

    @Autowired
    private UserManagementService userManagementService;

    @Autowired
    private AuthenticationService authenticationService;

    @Bean(name = "/api/authz/securityService.http")
    public HttpInvokerServiceExporter securityService() {
        HttpInvokerServiceExporter httpInvokerServiceExporter = new HttpInvokerServiceExporter();
        httpInvokerServiceExporter.setService(userManagementService);
        httpInvokerServiceExporter.setServiceInterface(UserManagementService.class);
        return httpInvokerServiceExporter;
    }

    @Bean(name = "/api/authz/authenticationService.http")
    public HttpInvokerServiceExporter authenticationService() {
        HttpInvokerServiceExporter httpInvokerServiceExporter = new HttpInvokerServiceExporter();
        httpInvokerServiceExporter.setService(authenticationService);
        httpInvokerServiceExporter.setServiceInterface(AuthenticationService.class);
        return httpInvokerServiceExporter;
    }

}

