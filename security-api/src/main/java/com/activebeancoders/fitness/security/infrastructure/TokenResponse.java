package com.activebeancoders.fitness.security.infrastructure;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Used to return a session ID token to the browser.  This will be converted to JSON and
 * the token can be accessed from the UI side via something like "data.token".
 *
 * @author Dan Barrese
 */
public class TokenResponse {

    @JsonProperty
    private String token;

    public TokenResponse(String token) {
        this.token = token;
    }

}

