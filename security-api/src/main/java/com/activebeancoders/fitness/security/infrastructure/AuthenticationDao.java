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

    public void save(AuthenticationWithToken authentication) {
        Assert.isTrue(authentication.isAuthenticated());
        if (log.isInfoEnabled()) {
            log.info("Storing authentication in security context.");
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

