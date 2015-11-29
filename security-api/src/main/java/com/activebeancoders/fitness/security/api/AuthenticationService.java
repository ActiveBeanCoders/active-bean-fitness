package com.activebeancoders.fitness.security.api;

import com.activebeancoders.fitness.security.infrastructure.AuthenticationWithToken;

/**
 * Responsible for authenticating user credentials. This is the class that is exposed to
 * external services for validating credentials.
 *
 * @author Dan Barrese
 */
public interface AuthenticationService {

    AuthenticationWithToken authenticate(String username, String password);

}

