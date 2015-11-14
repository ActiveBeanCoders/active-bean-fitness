package com.activebeancoders.fitness.security.config;

import com.activebeancoders.fitness.security.infrastructure.AuthenticationWithToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * @author Dan Barrese
 */
public class AuthenticationWithTokenManager implements AuthenticationManager {

    AuthenticationManager authenticationManager;

    public AuthenticationWithTokenManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthenticationWithToken authenticate(Authentication authentication) throws AuthenticationException {
        Authentication resultOfAuthentication = authenticationManager.authenticate(authentication);
        AuthenticationWithToken authenticationWithToken = AuthenticationWithToken.createFrom(resultOfAuthentication);
        return authenticationWithToken;
    }

}

