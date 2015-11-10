package com.activebeancoders.fitness.security.infrastructure;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * @author Dan Barrese
 */
public class BackendAdminUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

    public BackendAdminUsernamePasswordAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

}

