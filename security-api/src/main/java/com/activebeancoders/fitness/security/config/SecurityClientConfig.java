package com.activebeancoders.fitness.security.config;

import com.activebeancoders.fitness.security.api.AuthenticationService;
import com.activebeancoders.fitness.security.api.UserManagementService;
import com.activebeancoders.fitness.security.infrastructure.AuthenticationTokenHttpInvokerRequestExecutor;
import com.activebeancoders.fitness.security.infrastructure.XAuthcTokenRequestInterceptor;
import com.activebeancoders.fitness.security.infrastructure.XCsrfTokenRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

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

    @Value("${external-url.security-service}")
    private String securityServiceUrl;

    @Bean
    public String securityServiceBaseUrl() {
        return securityServiceUrl;
    }

    @Bean
    public UserManagementService remoteUserManagementService() {
        HttpInvokerProxyFactoryBean proxy = new HttpInvokerProxyFactoryBean();
        proxy.setServiceUrl(securityServiceUrl + "/public/securityService.http");
        proxy.setServiceInterface(UserManagementService.class);
        proxy.setHttpInvokerRequestExecutor(executor);
        proxy.afterPropertiesSet();
        return (UserManagementService) proxy.getObject();
    }

//    @Bean
//    public AuthenticationService remoteAuthenticationService() {
//        HttpInvokerProxyFactoryBean proxy = new HttpInvokerProxyFactoryBean();
//        proxy.setServiceUrl(securityServiceUrl + "/public/authenticationService.http");
//        proxy.setServiceInterface(AuthenticationService.class);
//        proxy.setHttpInvokerRequestExecutor(executor);
//        proxy.afterPropertiesSet();
//        return (AuthenticationService) proxy.getObject();
//    }

    /**
     * @return RestTemplate to be used to make requests with user's session authentication
     * token included.
     */
    @Bean
    public RestTemplate restTemplateWithAuthcToken() {
        RestTemplate restTemplate = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new XAuthcTokenRequestInterceptor());
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }

    /**
     * @return RestTemplate to be used to make requests with user's session token(s)
     * included.
     */
    @Bean
    public RestTemplate restTemplateWithAuthcTokenCsrfToken() {
        RestTemplate restTemplate = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new XAuthcTokenRequestInterceptor());
        interceptors.add(new XCsrfTokenRequestInterceptor());
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }

}

