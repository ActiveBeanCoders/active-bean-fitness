package com.activebeancoders.fitness.security.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Responsible for saving/retrieving/removing authentication information.  This class
 * exists to help abstract away the details of how the session information is stored.  See
 * Issue #29 "Store session tokens in external storage".
 *
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
     * @param authentication The auth info to save.
     */
    public void save(AuthenticationWithToken authentication) {
        Assert.isTrue(authentication.isAuthenticated());
        if (log.isDebugEnabled()) {
            log.debug("Storing authentication in security context.");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * @return the authentication object from the ThreadLocal context.
     */
    public AuthenticationWithToken getCurrentSessionAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return AuthenticationWithToken.nonAuthenticatedInstance();
        }
        return (AuthenticationWithToken) authentication;
    }

    /**
     * Removes the authentication information from the ThreadLocal context.
     */
    public void clearCurrentSessionAuthentication() {
        // TODO: how does this know WHICH authentication information to remove?  Or does it just remove ALL authentication objects from the context??
        SecurityContextHolder.clearContext();
    }

}

