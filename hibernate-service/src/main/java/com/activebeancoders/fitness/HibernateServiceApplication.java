package com.activebeancoders.fitness;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Dan Barrese
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
@RestController
//@EnableRedisHttpSession
//public class HibernateServiceApplication extends WebSecurityConfigurerAdapter {
public class HibernateServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(new Class[]{HibernateServiceApplication.class}, args);
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        // We need this to prevent the browser from popping up a dialog on a 401
//        http.httpBasic().disable();
//
//        // Allow "USER" role to change data.
//        http.authorizeRequests().antMatchers(HttpMethod.POST, "/**").hasRole("USER").anyRequest().authenticated();
//    }

}
