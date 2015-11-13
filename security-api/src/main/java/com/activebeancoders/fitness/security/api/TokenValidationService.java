package com.activebeancoders.fitness.security.api;

import com.activebeancoders.fitness.security.infrastructure.AuthenticationWithToken;
import com.google.common.base.Optional;

/**
 * Validates an existing session token.
 *
 * @author Dan Barrese
 */
public interface TokenValidationService {

    AuthenticationWithToken validateToken(Optional<String> tokenString);

}
