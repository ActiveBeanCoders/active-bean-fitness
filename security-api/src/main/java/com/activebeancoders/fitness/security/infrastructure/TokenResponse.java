package com.activebeancoders.fitness.security.infrastructure;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Dan Barrese
 */
public class TokenResponse {

    @JsonProperty
    private String token;

    public TokenResponse(String token) {
        this.token = token;
    }

}

