package com.activebeancoders.fitness.security.infrastructure;

import com.activebeancoders.fitness.security.domain.DomainUser;
import com.google.common.base.Objects;
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
    private static final AuthenticationWithToken NON_AUTHENTICATED_INSTANCE = new AuthenticationWithToken();
    private String csrfToken;

    public static AuthenticationWithToken nonAuthenticatedInstance() {
        return NON_AUTHENTICATED_INSTANCE;
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

    public AuthenticationWithToken(Object principal, Object credentials) {
        super(principal == null ? null : principal.toString(), credentials);
        csrfToken = UUID.randomUUID().toString();
    }

    public AuthenticationWithToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> grantedAuthorities) {
        super(principal, credentials, grantedAuthorities);
        csrfToken = UUID.randomUUID().toString();
    }

    public void setToken(String token) {
        setDetails(token);
    }

    public String getToken() {
        return String.valueOf(getDetails());
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public String getUsername() {
        return getPrincipal() == null ? null : getPrincipal().getUsername();
    }

    @Override
    public DomainUser getPrincipal() {
        Object principal = super.getPrincipal();
        return principal == null ? null : (DomainUser) principal;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("username", getUsername())
                .add("isAuthenticated", isAuthenticated())
                .add("sessionToken", getToken())
                .add("csrfToken", getCsrfToken())
                .toString();
    }
    // private methods
    // ```````````````````````````````````````````````````````````````````````

    private AuthenticationWithToken() {
        super(null, null);
        setAuthenticated(false);
    }

}

