package com.activebeancoders.fitness.security.domain;

import java.io.Serializable;

/**
 * Represents a person using our application/service.
 *
 * @author Dan Barrese
 */
public class DomainUser implements Serializable {

    private static final long serialVersionUID = 1L;
    private String username;

    public DomainUser(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return username;
    }

}

