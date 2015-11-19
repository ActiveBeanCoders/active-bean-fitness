package com.activebeancoders.fitness;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

import com.activebeancoders.fitness.security.domain.CurrentlyLoggedUser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Dan Barrese
 */
@SpringBootApplication
@RestController
public class UiAngularApplication {

    @RequestMapping("/user")
    public Map<String, String> user(@CurrentlyLoggedUser Principal user) {
        return Collections.singletonMap("name", user.getName());
    }

    public static void main(String[] args) {
        SpringApplication.run(UiAngularApplication.class, args);
    }

    @Configuration
    @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
    protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .httpBasic().and()
                    .authorizeRequests()
                    .antMatchers("/**").permitAll();
//                    .anyRequest().hasRole("USER");
        }
    }

}
