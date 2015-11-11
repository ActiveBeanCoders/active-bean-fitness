package com.activebeancoders.fitness.security.api;

import com.activebeancoders.fitness.security.infrastructure.AuthenticationWithToken;

/**
 * Validates an existing session token.
 *
 * @author Dan Barrese
 */
public interface TokenValidationService {

    AuthenticationWithToken getAuthenticationByToken(String tokenString);

}
