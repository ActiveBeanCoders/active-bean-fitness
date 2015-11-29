package com.activebeancoders.fitness;

import com.activebeancoders.fitness.security.domain.CurrentlyLoggedInUser;
import com.activebeancoders.fitness.security.domain.DomainUser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

/**
 * Main class for running the security service.
 *
 * @author Dan Barrese
 */
@Configuration
@EnableWebMvc
@ComponentScan
@EnableAutoConfiguration
@EnableConfigurationProperties
@PropertySource(value = "classpath:/security-service.properties", ignoreResourceNotFound = false)
@RestController
public class SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }

    @RequestMapping("/user")
    public Map<String, String> user(@CurrentlyLoggedInUser DomainUser domainUser) {
        return Collections.singletonMap("username", domainUser.getUsername());
    }

}

