package com.activebeancoders.fitness.security.api;

import com.activebeancoders.fitness.security.AuthenticationUtils;
import com.activebeancoders.fitness.security.infrastructure.UserSession;
import com.activebeancoders.fitness.security.infrastructure.DomainUsernamePasswordAuthenticationProvider;
import com.activebeancoders.fitness.security.infrastructure.TokenAuthenticationProvider;
import com.activebeancoders.fitness.security.infrastructure.UserSessionContext;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

/**
 * Responsible for authenticated user credentials.  This is the class that is exposed to
 * external services for authentication.
 *
 * @author Dan Barrese
 */
@Primary
@Component
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @Autowired
    private DomainUsernamePasswordAuthenticationProvider domainUsernamePasswordAuthenticationProvider;

    @Autowired
    @Qualifier("tokenAuthenticationProvider")
    private TokenAuthenticationProvider tokenAuthenticationProvider;

    @Override
    public UserSession authenticate(final String username, final String plainTextPassword) {
        UsernamePasswordAuthenticationToken authenticationWithToken = AuthenticationUtils.createAuthToken(username, plainTextPassword);
        UserSession resultOfAuthentication = domainUsernamePasswordAuthenticationProvider.authenticate(authenticationWithToken);
        if (log.isInfoEnabled()) {
            log.info("User '{}' authenticating...succeeded={}", extractUsername(resultOfAuthentication), resultOfAuthentication.isAuthenticated());
        }
        UserSessionContext.set(resultOfAuthentication);
        return resultOfAuthentication;
    }

    protected String extractUsername(UserSession authentication) {
        return authentication == null ? "<unauthorized>" : authentication.getUsername();
    }

    @Override
    public UserSession validateToken(@Nonnull final Optional<String> token) {
        PreAuthenticatedAuthenticationToken requestAuthentication = new PreAuthenticatedAuthenticationToken(token, null);
        UserSession authentication = tokenAuthenticationProvider.authenticate(requestAuthentication);
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new InternalAuthenticationServiceException("Unable to authenticate Domain User for provided credentials");
        }
        return authentication;
    }

    @Override
    public void invalidateToken(@Nonnull final Optional<String> token) {
        tokenAuthenticationProvider.invalidate(token);
    }

}

