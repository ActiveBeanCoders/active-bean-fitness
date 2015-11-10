package com.activebeancoders.fitness.security.api;

import com.activebeancoders.fitness.security.infrastructure.AuthenticationWithToken;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

/**
 * @author Dan Barrese
 */
public interface SecurityService {

    AuthenticationWithToken authenticate(String username, String password);

    Authentication getAuthenticationByToken(String tokenString);

    @PreAuthorize("hasAuthority('ROLE_DOMAIN_USER')")
    String sayHello();

}
