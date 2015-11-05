package com.activebeancoders.fitness.config;

import com.activebeancoders.fitness.example.api.samplestuff.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;

@Configuration
public class SecurityServiceRemotingConfig {

    @Autowired
    private SecurityService securityService;

    @Bean(name = "/free/securityService.http")
    public HttpInvokerServiceExporter securityService() {
        HttpInvokerServiceExporter httpInvokerServiceExporter = new HttpInvokerServiceExporter();
        httpInvokerServiceExporter.setService(securityService);
        httpInvokerServiceExporter.setServiceInterface(SecurityService.class);
        return httpInvokerServiceExporter;
    }

}

