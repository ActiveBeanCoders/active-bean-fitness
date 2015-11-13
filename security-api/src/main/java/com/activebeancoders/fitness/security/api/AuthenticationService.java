package com.activebeancoders.fitness.security.api;

import com.activebeancoders.fitness.security.infrastructure.AuthenticationWithToken;
import org.springframework.security.core.Authentication;

/**
 * Used to authenticate user credentials.
 *
 * @author Dan Barrese
 */
public interface AuthenticationService {

    AuthenticationWithToken authenticate(String username, String password);

    void storeValidAuthentication(Authentication authentication);

}
