package com.activebeancoders.fitness.data.config;

import com.activebeancoders.fitness.security.api.AuthenticationService;
import com.activebeancoders.fitness.security.config.SecurityClientConfig;
import com.activebeancoders.fitness.security.infrastructure.AuthenticationDao;
import com.activebeancoders.fitness.security.infrastructure.SecuredServiceAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Dan Barrese
 */
@Configuration
@EnableWebMvcSecurity
@EnableScheduling
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import(SecurityClientConfig.class)
public class DataAllServiceSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("remoteAuthenticationService")
    private AuthenticationService authenticationService;

    @Autowired
    private AuthenticationDao authenticationDao;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable();
        http.
                csrf().disable().
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                and().
                authorizeRequests().
                anyRequest().authenticated().
                and().
                anonymous().disable().
                exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint());
        http.addFilterBefore(new SecuredServiceAuthenticationFilter(authenticationService, authenticationDao),
                BasicAuthenticationFilter.class);
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

}
