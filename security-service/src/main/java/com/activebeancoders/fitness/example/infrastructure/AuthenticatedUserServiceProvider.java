package com.activebeancoders.fitness.example.infrastructure;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUserServiceProvider {

    public AuthenticatedUserService provide() {
        return (AuthenticatedUserService) SecurityContextHolder.getContext().getAuthentication();
    }

}

