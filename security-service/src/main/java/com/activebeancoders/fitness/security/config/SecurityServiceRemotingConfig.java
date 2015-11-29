package com.activebeancoders.fitness.security.config;

import com.activebeancoders.fitness.security.api.AuthenticationService;
import com.activebeancoders.fitness.security.api.SecurityService;
import com.activebeancoders.fitness.security.api.TokenValidationService;
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
    private SecurityService securityService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private TokenValidationService tokenValidationService;

    @Bean(name = "/public/securityService.http")
    public HttpInvokerServiceExporter securityService() {
        HttpInvokerServiceExporter httpInvokerServiceExporter = new HttpInvokerServiceExporter();
        httpInvokerServiceExporter.setService(securityService);
        httpInvokerServiceExporter.setServiceInterface(SecurityService.class);
        return httpInvokerServiceExporter;
    }

    @Bean(name = "/public/authenticationService.http")
    public HttpInvokerServiceExporter authenticationService() {
        HttpInvokerServiceExporter httpInvokerServiceExporter = new HttpInvokerServiceExporter();
        httpInvokerServiceExporter.setService(authenticationService);
        httpInvokerServiceExporter.setServiceInterface(AuthenticationService.class);
        return httpInvokerServiceExporter;
    }

    @Bean(name = "/public/tokenValidationService.http")
    public HttpInvokerServiceExporter tokenValidationService() {
        HttpInvokerServiceExporter httpInvokerServiceExporter = new HttpInvokerServiceExporter();
        httpInvokerServiceExporter.setService(tokenValidationService);
        httpInvokerServiceExporter.setServiceInterface(TokenValidationService.class);
        return httpInvokerServiceExporter;
    }

}

