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
 * Represents the user's session and all the info that goes along with it such as the
 * session token, the user object, whether the session is authenticated, etc.
 *
 * @author Dan Barrese
 */
public class UserSession extends PreAuthenticatedAuthenticationToken implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final UserSession NON_AUTHENTICATED_INSTANCE = new UserSession();
    private String csrfToken;

    public static UserSession nonAuthenticatedInstance() {
        return NON_AUTHENTICATED_INSTANCE;
    }

    public static UserSession createFrom(Authentication authentication) {
        if (authentication == null) {
            return nonAuthenticatedInstance();
        }
        UserSession userSession = new UserSession(authentication.getPrincipal(),
                authentication.getCredentials(), authentication.getAuthorities());
        userSession.setDetails(authentication.getDetails());
        return userSession;
    }

    public UserSession(Object principal, Object credentials) {
        super(principal == null ? null : principal.toString(), credentials);
        csrfToken = UUID.randomUUID().toString();
    }

    public UserSession(Object principal, Object credentials, Collection<? extends GrantedAuthority> grantedAuthorities) {
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

    private UserSession() {
        super(null, null);
        setAuthenticated(false);
    }

}

