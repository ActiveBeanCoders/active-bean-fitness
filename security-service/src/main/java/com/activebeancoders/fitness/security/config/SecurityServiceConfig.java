package com.activebeancoders.fitness.security.config;

import com.activebeancoders.fitness.security.api.AuthenticationService;
import com.activebeancoders.fitness.security.api.TokenValidationService;
import com.activebeancoders.fitness.security.infrastructure.AuthenticationDao;
import com.activebeancoders.fitness.security.infrastructure.AuthenticationFilter;
import com.activebeancoders.fitness.security.infrastructure.ManagementEndpointAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
public class SecurityServiceConfig extends WebSecurityConfigurerAdapter {

    @Value("${backend.admin.role}")
    private String backendAdminRole;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    @Qualifier("domainUsernamePasswordAuthenticationProvider")
    private AuthenticationProvider domainUsernamePasswordAuthenticationProvider;

    @Autowired
    @Qualifier("backendAdminUsernamePasswordAuthenticationProvider")
    private AuthenticationProvider backendAdminUsernamePasswordAuthenticationProvider;

    @Autowired
    @Qualifier("tokenAuthenticationProvider")
    private AuthenticationProvider tokenAuthenticationProvider;

    @Autowired
    private TokenValidationService tokenValidationService;

    @Autowired
    private AuthenticationDao authenticationDao;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                csrf().disable().
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                and().
                authorizeRequests().
                antMatchers("/public/**").permitAll().
                anyRequest().authenticated().
                and().
                exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint());
        http.addFilterBefore(new AuthenticationFilter(authenticationManager(), authenticationService, tokenValidationService, authenticationDao),
                BasicAuthenticationFilter.class).
                addFilterBefore(new ManagementEndpointAuthenticationFilter(authenticationManager(), authenticationService, authenticationDao),
                        BasicAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(domainUsernamePasswordAuthenticationProvider).
                authenticationProvider(backendAdminUsernamePasswordAuthenticationProvider).
                authenticationProvider(tokenAuthenticationProvider);
    }

    @Bean(name = "fitnessAuthenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

}
