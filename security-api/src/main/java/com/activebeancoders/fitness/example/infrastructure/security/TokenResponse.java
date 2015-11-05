package com.activebeancoders.fitness.example.infrastructure.security;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Dan Barrese
 */
public class TokenResponse {

    @JsonProperty
    private String token;

    public TokenResponse() {
    }

    public TokenResponse(String token) {
        this.token = token;
    }

}

