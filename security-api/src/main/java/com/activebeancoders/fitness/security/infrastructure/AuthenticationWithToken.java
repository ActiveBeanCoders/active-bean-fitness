package com.activebeancoders.fitness.security.infrastructure;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

/**
 * @author Dan Barrese
 */
public class AuthenticationWithToken extends PreAuthenticatedAuthenticationToken implements Serializable {

    private static final long serialVersionUID = 1L;
    private String csrfToken;

    public static AuthenticationWithToken nonAuthenticatedInstance() {
        return new AuthenticationWithToken();
    }

    public static AuthenticationWithToken createFrom(Authentication authentication) {
        if (authentication == null) {
            return nonAuthenticatedInstance();
        }
        AuthenticationWithToken authenticationWithToken = new AuthenticationWithToken(authentication.getPrincipal(),
                authentication.getCredentials(), authentication.getAuthorities());
        authenticationWithToken.setDetails(authentication.getDetails());
        return authenticationWithToken;
    }

    public AuthenticationWithToken(Object aPrincipal, Object aCredentials) {
        super(aPrincipal, aCredentials);
        csrfToken = UUID.randomUUID().toString();
    }

    public AuthenticationWithToken(Object aPrincipal, Object aCredentials,
                                   Collection<? extends GrantedAuthority> anAuthorities) {
        super(aPrincipal, aCredentials, anAuthorities);
        csrfToken = UUID.randomUUID().toString();
    }

    public void setToken(String token) {
        setDetails(token);
    }

    public String getToken() {
        return (String) getDetails();
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    // private methods
    // ```````````````````````````````````````````````````````````````````````

    private AuthenticationWithToken() {
        super(null, null);
        setAuthenticated(false);
    }

}

