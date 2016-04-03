package com.activebeancoders.fitness.security.api;

/**
 * @author Dan Barrese
 */
public abstract class SecurityClientController {

    public static final String URL_VALIDATE_AUTH_TOKEN = "/api/authz/verify-token";
    public static final String URL_AUTH_USER_CREDS = "/api/authz/authenticate";
    public static final String URL_LOGOUT = "/api/authz/logout";

}
