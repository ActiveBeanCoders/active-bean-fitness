package com.activebeancoders.fitness.security.api;

/**
 * @author Dan Barrese
 */
public abstract class SecurityClientController {

    protected static final String API_PATH = "/api/v1";

    public static final String AUTHENTICATE_URL = API_PATH + "/authenticate";
    public static final String VERIFY_TOKEN_URL = API_PATH + "/token/verify";

    public static String getAuthenticateUri() {
        return "authenticationService.http";
    }

    public static String getTokenValidationUri() {
        return "tokenValidationService.http";
    }

}
