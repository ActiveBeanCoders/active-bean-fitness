package com.activebeancoders.fitness.example.api.samplestuff;

import com.activebeancoders.fitness.example.infrastructure.security.AuthenticationWithToken;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

public interface SecurityService {

    AuthenticationWithToken authenticate(String username, String password);

    Authentication getAuthenticationByToken(String tokenString);

    @PreAuthorize("hasAuthority('ROLE_DOMAIN_USER')")
    String sayHello();

}
