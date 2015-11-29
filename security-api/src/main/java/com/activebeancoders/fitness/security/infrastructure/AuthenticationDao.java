package com.activebeancoders.fitness.security.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author Dan Barrese
 */
@Component
public class AuthenticationDao {

    private final Logger log = LoggerFactory.getLogger(getClass());

    public AuthenticationDao() {
    }

    /**
     * Save the authentication object into the ThreadLocal context, so if the method being
     * invoked is in "this" service (the calling service), that method can retrieve the
     * currently logged-in user. And also if this service makes a remote call to another
     * service, it can pass along the authentication to the remote call.
     *
     * @param authentication
     */
    public void save(AuthenticationWithToken authentication) {
        Assert.isTrue(authentication.isAuthenticated());
        if (log.isDebugEnabled()) {
            log.debug("Storing authentication in security context.");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public AuthenticationWithToken getCurrentSessionAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return AuthenticationWithToken.nonAuthenticatedInstance();
        }
        return (AuthenticationWithToken) authentication;
    }

    public void clearCurrentSessionAuthentication() {
        SecurityContextHolder.clearContext();
    }

}

