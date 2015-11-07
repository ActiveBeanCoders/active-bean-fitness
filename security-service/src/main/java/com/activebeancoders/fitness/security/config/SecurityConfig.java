package com.activebeancoders.fitness.security.config;

import com.activebeancoders.fitness.example.api.samplestuff.SecurityService;
import com.activebeancoders.fitness.example.infrastructure.api.SecurityServiceController;
import com.activebeancoders.fitness.example.infrastructure.externalwebservice.SecurityServiceImpl;
import com.activebeancoders.fitness.example.infrastructure.security.AnonymousAuthenticationFilter;
import com.activebeancoders.fitness.example.infrastructure.security.AuthenticationFilter;
import com.activebeancoders.fitness.example.infrastructure.security.BackendAdminUsernamePasswordAuthenticationProvider;
import com.activebeancoders.fitness.example.infrastructure.security.DomainUsernamePasswordAuthenticationProvider;
import com.activebeancoders.fitness.example.infrastructure.security.ManagementEndpointAuthenticationFilter;
import com.activebeancoders.fitness.example.infrastructure.security.TokenAuthenticationProvider;
import com.activebeancoders.fitness.example.infrastructure.security.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${backend.admin.role}")
    private String backendAdminRole;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                csrf().disable().
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
                and().
                authorizeRequests().
                antMatchers("/free/**").permitAll().
                antMatchers(actuatorEndpoints()).hasRole(backendAdminRole).
                anyRequest().authenticated().
                and().
//                anonymous().authenticationFilter(anonymousAuthenticationFilter());
        exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint());

        http.addFilterBefore(new AuthenticationFilter(authenticationManager()), BasicAuthenticationFilter.class).
                addFilterBefore(new ManagementEndpointAuthenticationFilter(authenticationManager()),
                        BasicAuthenticationFilter.class);
    }

    private String[] actuatorEndpoints() {
        return new String[]{SecurityServiceController.AUTOCONFIG_ENDPOINT, SecurityServiceController.BEANS_ENDPOINT,
                SecurityServiceController.CONFIGPROPS_ENDPOINT, SecurityServiceController.ENV_ENDPOINT,
                SecurityServiceController.MAPPINGS_ENDPOINT, SecurityServiceController.METRICS_ENDPOINT,
                SecurityServiceController.SHUTDOWN_ENDPOINT};
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(domainUsernamePasswordAuthenticationProvider()).
                authenticationProvider(backendAdminUsernamePasswordAuthenticationProvider()).
                authenticationProvider(tokenAuthenticationProvider());
    }

    // TODO: autowire
    @Bean
    public TokenService tokenService() {
        return new TokenService();
    }

    // TODO: rename
    @Bean(name = "myAuthenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Primary
    @Bean
    public SecurityService someExternalServiceAuthenticator() throws Exception {
        return new SecurityServiceImpl();
    }

    @Bean
    public AuthenticationProvider domainUsernamePasswordAuthenticationProvider() throws Exception {
        return new DomainUsernamePasswordAuthenticationProvider(tokenService(), someExternalServiceAuthenticator());
    }

    @Bean
    public AuthenticationProvider backendAdminUsernamePasswordAuthenticationProvider() {
        return new BackendAdminUsernamePasswordAuthenticationProvider();
    }

    @Bean
    public AuthenticationProvider tokenAuthenticationProvider() {
        return new TokenAuthenticationProvider(tokenService());
    }

//    @Bean
//    public AnonymousAuthenticationFilter anonymousAuthenticationFilter() {
//        return new AnonymousAuthenticationFilter();
//    }

    @Bean
    public AuthenticationEntryPoint unauthorizedEntryPoint() {
        return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

}