package com.activebeancoders.fitness.example.infrastructure;

import com.activebeancoders.fitness.example.infrastructure.externalwebservice.UserServiceImpl;
import com.activebeancoders.fitness.example.infrastructure.security.AuthenticationWithToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author Dan Barrese
 */
public class AuthenticatedUserService extends AuthenticationWithToken {

    private UserServiceImpl userService;

    public AuthenticatedUserService(Object aPrincipal, Object aCredentials,
            Collection<? extends GrantedAuthority> anAuthorities) {
        super(aPrincipal, aCredentials, anAuthorities);
    }

    public void setUserService(UserServiceImpl userService) {
        this.userService = userService;
    }

    public UserServiceImpl getUserService() {
        return userService;
    }

}
