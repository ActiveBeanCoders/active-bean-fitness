package com.activebeancoders.fitness.security;

import com.google.common.base.Optional;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * @author Dan Barrese
 */
public class AuthenticationUtils {

    /**
     * Creates an authentication token that can be passed to an instance of {@link
     * org.springframework.security.authentication.AuthenticationProvider}.
     *
     * @param username
     * @param password
     * @return UsernamePasswordAuthenticationToken using the given username and password.
     */
    public static UsernamePasswordAuthenticationToken createAuthToken(String username, String password) {
        Optional<String> optionalUsername = Optional.fromNullable(username);
        Optional<String> optionalPassword = Optional.fromNullable(password);
        UsernamePasswordAuthenticationToken requestAuthentication = new UsernamePasswordAuthenticationToken(optionalUsername, optionalPassword);
        return requestAuthentication;
    }

}

