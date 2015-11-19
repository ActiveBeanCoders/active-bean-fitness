package com.activebeancoders.fitness.security.api;

import com.activebeancoders.fitness.security.infrastructure.AuthenticationWithToken;
import com.google.common.base.Optional;

/**
 * Validates an existing session token.
 *
 * @author Dan Barrese
 */
public interface TokenValidationService {

    /**
     * Makes sure the given token string represents a valid and active session.
     *
     * @param tokenString The session token string to be tested.
     * @return The authentication object, which "isAuthenticated" is true.
     */
    AuthenticationWithToken validateToken(Optional<String> tokenString);

}
