package com.activebeancoders.fitness.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Dan Barrese
 */
@SpringBootApplication
@RestController
@PropertySource(value = "classpath:/ui-angular.properties", ignoreResourceNotFound = false)
@PropertySource(value = "file:${user.home}/activebeancoders/global.properties", ignoreResourceNotFound = true)
@PropertySource(value = "file:${user.home}/activebeancoders/ui-angular.properties", ignoreResourceNotFound = true)
public class UiAngularApplication {

    public static void main(String[] args) {
        SpringApplication.run(UiAngularApplication.class, args);
    }

    @Configuration
    @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
    protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.httpBasic().disable();
            http.csrf().disable();
            http.authorizeRequests()
                    .antMatchers("/**").permitAll();
        }
    }

}
