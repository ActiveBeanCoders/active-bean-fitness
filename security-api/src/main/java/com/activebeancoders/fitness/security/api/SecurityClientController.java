package com.activebeancoders.fitness.security.api;

/**
 * @author Dan Barrese
 */
public abstract class SecurityClientController {

    public static final String URL_VALIDATE_AUTH_TOKEN = "/public/token/verify";
    public static final String URL_AUTH_USER_CREDS = "/public/authenticate";
    public static final String URL_LOGOUT = "/public/logout";

    public static String getLogoutEndpointFromRESTCall() {
        return URL_LOGOUT;
    }

    public static String getAuthenticateEndpointFromRESTCall() {
        return URL_AUTH_USER_CREDS;
    }

    public static String getAuthenticateEndpointFromRemoteMethodCall() {
        return "authenticationService.http";
    }

    public static String getTokenValidationEndpointFromRESTCall() {
        return URL_VALIDATE_AUTH_TOKEN;
    }

    public static String getTokenValidationEndpointFromRemoteMethodCall() {
        return "tokenValidationService.http";
    }

}
