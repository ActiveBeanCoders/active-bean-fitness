package com.activebeancoders.fitness.security.api;

/**
 * @author Dan Barrese
 */
public abstract class SecurityClientController {

    protected static final String API_PATH = "/api/v1";

    public static String getLogoutEndpointFromRESTCall() {
        return "/public/logout";
    }

    public static String getAuthenticateEndpointFromRESTCall() {
        return "/public/authenticate";
    }

    public static String getAuthenticateEndpointFromRemoteMethodCall() {
        return "authenticationService.http";
    }

    public static String getTokenValidationEndpointFromRESTCall() {
        return "public/token/verify";
    }

    public static String getTokenValidationEndpointFromRemoteMethodCall() {
        return "tokenValidationService.http";
    }

}
