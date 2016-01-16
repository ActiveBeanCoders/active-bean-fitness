package com.activebeancoders.fitness.security.api;

import com.activebeancoders.fitness.security.infrastructure.AuthenticationWithToken;
import com.google.common.base.Optional;

import javax.annotation.Nonnull;

/**
 * Validates and invalidates session tokens.
 *
 * @author Dan Barrese
 */
// TODO: move this into SecurityService.
public interface TokenValidationService {

    /**
     * Makes sure the given token string represents a valid and active session.
     *
     * @param sessionToken The session token ID to be tested.
     * @return The authentication object, which "isAuthenticated" is true.
     */
    AuthenticationWithToken validateToken(Optional<String> sessionToken);

    /**
     * Invalidates a session token so that no service anywhere can use it again.
     *
     * @param sessionToken The session token ID to invalidate.
     */
    void invalidateToken(@Nonnull Optional<String> sessionToken);
}
