package com.activebeancoders.fitness.gateway;

import com.activebeancoders.fitness.security.config.SecurityClientConfig;
import com.activebeancoders.fitness.security.config.TomcatHttpsConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.stereotype.Controller;

/**
 * @author Dan Barrese
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
@Controller
@EnableZuulProxy
@PropertySource(value = "classpath:/gateway.properties", ignoreResourceNotFound = false)
@PropertySource(value = "file:/activebeancoders/global.properties", ignoreResourceNotFound = true)
@PropertySource(value = "file:/activebeancoders/gateway.properties", ignoreResourceNotFound = true)
@Import({ SecurityClientConfig.class, TomcatHttpsConfiguration.class})
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
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
