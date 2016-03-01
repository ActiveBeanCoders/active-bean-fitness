package com.activebeancoders.fitness.security.api;

import com.activebeancoders.fitness.security.infrastructure.AuthenticationWithToken;
import com.google.common.base.Optional;

import javax.annotation.Nonnull;

/**
 * Responsible for authenticating user credentials. This is the class that is exposed to
 * external services for validating credentials.
 *
 * @author Dan Barrese
 */
public interface AuthenticationService {

    AuthenticationWithToken authenticate(String username, String password);

    AuthenticationWithToken validateToken(@Nonnull final Optional<String> token);

    void invalidateToken(@Nonnull final Optional<String> token);

}

