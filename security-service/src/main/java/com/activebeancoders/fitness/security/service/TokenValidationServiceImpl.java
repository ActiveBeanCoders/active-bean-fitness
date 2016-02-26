package com.activebeancoders.fitness.security.service;

import com.activebeancoders.fitness.security.api.TokenValidationService;
import com.activebeancoders.fitness.security.infrastructure.AuthenticationWithToken;
import com.activebeancoders.fitness.security.infrastructure.TokenAuthenticationProvider;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

/**
 * Responsible for validating session tokens.  This is the class that is exposed to
 * external services for validating tokens.
 *
 * @author Dan Barrese
 */
@Primary
@Component
public class TokenValidationServiceImpl implements TokenValidationService {

    @Autowired
    @Qualifier("tokenAuthenticationProvider")
    private TokenAuthenticationProvider tokenAuthenticationProvider;

    public TokenValidationServiceImpl() {
    }

    @Override
    public AuthenticationWithToken validateToken(@Nonnull final Optional<String> token) {
        PreAuthenticatedAuthenticationToken requestAuthentication = new PreAuthenticatedAuthenticationToken(token, null);
        AuthenticationWithToken authentication = tokenAuthenticationProvider.authenticate(requestAuthentication);
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

