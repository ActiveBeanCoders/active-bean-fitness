package com.activebeancoders.fitness.security.service;

import com.activebeancoders.fitness.security.api.TokenValidationService;
import com.activebeancoders.fitness.security.infrastructure.AuthenticationWithToken;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * @author Dan Barrese
 */
public class TokenValidationServiceImpl implements TokenValidationService {

    @Autowired
    @Qualifier("myAuthenticationManager")
    private AuthenticationManager authenticationManager;

    @Override
    public AuthenticationWithToken getAuthenticationByToken(final String tokenString) {
        Optional<String> token = Optional.fromNullable(tokenString);
        try {
            AuthenticationWithToken authentication = processTokenAuthentication(token);
            return authentication;
        } catch (InternalAuthenticationServiceException e) {
            return AuthenticationWithToken.nonAuthenticatedInstance();
        }
    }

    private AuthenticationWithToken processTokenAuthentication(Optional<String> token) {
        AuthenticationWithToken resultOfAuthentication = tryToAuthenticateWithToken(token);
        return resultOfAuthentication;
    }

    private AuthenticationWithToken tryToAuthenticateWithToken(Optional<String> token) {
        PreAuthenticatedAuthenticationToken requestAuthentication = new PreAuthenticatedAuthenticationToken(token,
                null);
        return tryToAuthenticate(requestAuthentication);
    }

    private AuthenticationWithToken tryToAuthenticate(Authentication requestAuthentication) {
        Authentication responseAuthentication = authenticationManager.authenticate(requestAuthentication);
        if (responseAuthentication == null || !responseAuthentication.isAuthenticated()) {
            throw new InternalAuthenticationServiceException(
                    "Unable to authenticate Domain User for provided credentials");
        }
        // TODO: does this work?
        return (AuthenticationWithToken) responseAuthentication;
    }

}
