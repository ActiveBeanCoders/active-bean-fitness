package com.activebeancoders.fitness.security.api;

import com.activebeancoders.fitness.common.RestApiArgument;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dan Barrese
 */
public abstract class SecurityClientController {

    public static final String URI_VALIDATE_AUTHC_TOKEN = "/public/token/verify";
    public static final String URI_AUTHENTICATE = "/public/authenticate";
    public static final String URI_LOGOUT = "/public/logout";

    public enum Endpoint {
        VALIDATE_AUTHC_TOKEN(URI_VALIDATE_AUTHC_TOKEN,
                "header:string:X-Auth-Token:required"),

        AUTHENTICATE(URI_AUTHENTICATE,
                "header:string:X-Auth-Username:required",
                "header:string:X-Auth-Password:required"),

        LOGOUT(URI_LOGOUT,
                "header:string:X-Auth-Token:required");

        private String uri;
        private List<RestApiArgument> args = new ArrayList<>();

        Endpoint(String uri, String... args) {
            this.uri = uri;
            for (String arg : args) {
                this.args.add(new RestApiArgument(arg.split(":")));
            }
        }

        public String getUri() {
            return uri;
        }

        public List<RestApiArgument> getArgs() {
            return args;
        }
    }

}

