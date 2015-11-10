package com.activebeancoders.fitness.security.infrastructure;

import org.apache.http.client.methods.HttpPost;
import org.springframework.remoting.httpinvoker.HttpComponentsHttpInvokerRequestExecutor;
import org.springframework.remoting.httpinvoker.HttpInvokerClientConfiguration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * Responsible for including the currently-logged-in user's authentication token in all remote service calls.
 *
 * @author Dan Barrese
 */
@Component
public class AuthenticationTokenHttpInvokerRequestExecutor extends HttpComponentsHttpInvokerRequestExecutor {

    @Override
    protected HttpPost createHttpPost(HttpInvokerClientConfiguration config) throws IOException {
        HttpPost httpPost = super.createHttpPost(config);
        AuthenticationWithToken authentication = (AuthenticationWithToken) SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            // authentication.getAuthorities() -> user's roles
            // authentication.getCredentials() -> ?
            // authentication.getDetails() -> session ID/token as String
            // authentication.getPrincipal() -> username
            // authentication.getCsrfToken() -> csrf token

            String sessionToken = authentication.getToken();
            if (StringUtils.hasLength(sessionToken)) {
                System.out.println(String.format("injecting X-Auth-Token '%s' into request for '%s'.", sessionToken, config.getServiceUrl()));
                httpPost.addHeader("X-Auth-Token", sessionToken);
            }

            String csrfToken = authentication.getCsrfToken();
            if (StringUtils.hasLength(csrfToken)) {
                System.out.println(String.format("injecting X-XSRF-TOKEN '%s' into request for '%s'.", csrfToken, config.getServiceUrl()));
                httpPost.addHeader("X-XSRF-TOKEN", csrfToken);
            }
        }
        return httpPost;
    }

}

