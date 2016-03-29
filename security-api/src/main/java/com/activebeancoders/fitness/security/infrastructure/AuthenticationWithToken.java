package com.activebeancoders.fitness.security.infrastructure;

import com.activebeancoders.fitness.security.domain.DomainUser;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Objects;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

/**
 * Wrapper for {@link org.springframework.security.core.Authentication} with helper
 * methods.
 *
 * @author Dan Barrese
 */
public class AuthenticationWithToken implements Authentication, Serializable {

    private static final long serialVersionUID = 1L;
    private static final AuthenticationWithToken NON_AUTHENTICATED_INSTANCE = new AuthenticationWithToken();
    private String csrfToken;
    private DomainUser principal;
    private String plaintextPassword;
    private boolean authenticated;
    private String sessionToken;
    private Collection<GrantedAuthority> authorities;

    public static AuthenticationWithToken nonAuthenticatedInstance() {
        return NON_AUTHENTICATED_INSTANCE;
    }

    public static AuthenticationWithToken createFrom(Authentication authentication) {
        if (authentication == null) {
            return nonAuthenticatedInstance();
        }
        AuthenticationWithToken authenticationWithToken = new AuthenticationWithToken(
                (String) authentication.getPrincipal(),
                (String) authentication.getCredentials(),
                (Collection<GrantedAuthority>) authentication.getAuthorities());
        authenticationWithToken.setToken((String) authentication.getDetails());
        return authenticationWithToken;
    }

    public AuthenticationWithToken(String username, String plaintextPassword,
                                   Collection<GrantedAuthority> authorities) {
        DomainUser domainUser = new DomainUser();
        domainUser.setUsername(username);
        this.principal = domainUser;
        this.plaintextPassword = plaintextPassword;
        this.authorities = authorities;
        postConstruct();
    }

    public AuthenticationWithToken(DomainUser principal, String plaintextPassword) {
        this.principal = principal;
        this.plaintextPassword = plaintextPassword;
        postConstruct();
    }

    public AuthenticationWithToken(DomainUser principal, String plaintextPassword, Collection<GrantedAuthority> authorities) {
        this.principal = principal;
        this.plaintextPassword = plaintextPassword;
        this.authorities = authorities;
        postConstruct();
    }

    protected void postConstruct() {
        this.authenticated = true;
        csrfToken = UUID.randomUUID().toString();
    }

    public String getToken() {
        return sessionToken;
    }

    public void setToken(String token) {
        this.sessionToken = token;
    }


    public String getCsrfToken() {
        return csrfToken;
    }

    /**
     * Deprecated.  Use {@link #getUsername}.
     */
    @Deprecated
    @Override
    public String getName() {
        return getUsername();
    }

    public String getUsername() {
        return principal == null ? null : principal.getUsername();
    }

    @Override
    public DomainUser getPrincipal() {
        return principal;
    }

    @JsonDeserialize(using = GrantedAuthoritySerializer.class)
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Deprecated.  Use {@link #getPlaintextPassword}.
     */
    @Deprecated
    @Override
    public Object getCredentials() {
        return getPlaintextPassword();
    }

    public String getPlaintextPassword() {
        return plaintextPassword;
    }

    /**
     * Deprecated.  Use {@link #getToken}.
     */
    @Deprecated
    @Override
    public Object getDetails() {
        return getToken();
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {
        this.authenticated = b;
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
        setAuthenticated(false);
    }

}

