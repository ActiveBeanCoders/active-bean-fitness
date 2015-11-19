package com.activebeancoders.fitness.security.api;

import com.activebeancoders.fitness.security.infrastructure.AuthenticationWithToken;

/**
 * Used to authenticate user credentials.
 *
 * @author Dan Barrese
 */
public interface AuthenticationService {

    AuthenticationWithToken authenticate(String username, String password);

}

