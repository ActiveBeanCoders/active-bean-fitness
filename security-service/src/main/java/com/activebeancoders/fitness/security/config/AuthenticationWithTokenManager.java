package com.activebeancoders.fitness.security.config;

import com.activebeancoders.fitness.security.infrastructure.AuthenticationWithToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * "AuthenticationWithToken" manager.
 * <p>
 * Wrapper class for {@link org.springframework.security.authentication.AuthenticationManager}
 * so I can use my own {@link com.activebeancoders.fitness.security.infrastructure.AuthenticationWithToken}
 * class instead of the default {@link org.springframework.security.core.Authentication}.
 *
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

