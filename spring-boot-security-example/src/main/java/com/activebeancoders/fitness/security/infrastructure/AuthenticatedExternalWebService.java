package com.activebeancoders.fitness.security.infrastructure;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author Dan Barrese
 */
public class AuthenticatedExternalWebService extends AuthenticationWithToken {

    private ExampleServiceImpl exampleService;

    public AuthenticatedExternalWebService(Object aPrincipal, Object aCredentials,
                                           Collection<? extends GrantedAuthority> anAuthorities) {
        super(aPrincipal, aCredentials, anAuthorities);
    }

    public void setExternalWebService(ExampleServiceImpl exampleService) {
        this.exampleService = exampleService;
    }

    public ExampleServiceImpl getExampleService() {
        return exampleService;
    }

}

