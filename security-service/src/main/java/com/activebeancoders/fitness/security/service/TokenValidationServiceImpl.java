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
        System.out.println(String.format("validating token '%s'", tokenString));
        Optional<String> token = Optional.fromNullable(tokenString);
        try {
            AuthenticationWithToken authentication = processTokenAuthentication(token);
            System.out.println(String.format("valid? %s", String.valueOf(authentication.isAuthenticated())));
            return authentication;
        } catch (InternalAuthenticationServiceException e) {
            System.out.println(String.format("invalid"));
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
        return (AuthenticationWithToken) responseAuthentication;

//        return AuthenticationWithToken.createFrom(responseAuthentication);
    }

}
