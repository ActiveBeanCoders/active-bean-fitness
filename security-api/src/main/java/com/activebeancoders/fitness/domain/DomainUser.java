package com.activebeancoders.fitness.domain;

import java.io.Serializable;

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
