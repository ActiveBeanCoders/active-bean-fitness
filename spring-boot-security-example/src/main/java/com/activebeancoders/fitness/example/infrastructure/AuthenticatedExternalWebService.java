package com.activebeancoders.fitness.example.infrastructure;

import com.activebeancoders.fitness.example.infrastructure.security.AuthenticationWithToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

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

