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

    // TODO: use logger
    @Override
    protected HttpPost createHttpPost(HttpInvokerClientConfiguration config) throws IOException {
        System.out.println(String.format("@calling createHttpPost"));
        HttpPost httpPost = super.createHttpPost(config);
        System.out.println(String.format("  getMethod=%s", httpPost.getMethod()));
        System.out.println(String.format("  getEntity=%s", httpPost.getEntity()));
        System.out.println(String.format("  getRequestLine=%s", httpPost.getRequestLine()));
        System.out.println(String.format("  getURI=%s", httpPost.getURI()));
        AuthenticationWithToken authentication = (AuthenticationWithToken) SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            // authentication.getAuthorities() -> user's roles
            // authentication.getCredentials() -> ?
            // authentication.getDetails() -> session ID/token as String
            // authentication.getPrincipal() -> username
            // authentication.getCsrfToken() -> csrf token

            String sessionToken = authentication.getToken();
            System.out.println(String.format("@sessionToken=%s", sessionToken));
            if (StringUtils.hasLength(sessionToken)) {
                System.out.println(String.format("injecting X-Auth-Token '%s' into request for '%s'.", sessionToken, config.getServiceUrl()));
                httpPost.addHeader("X-Auth-Token", sessionToken);
            }

            String csrfToken = authentication.getCsrfToken();
            System.out.println(String.format("@csrfToken=%s", csrfToken));
            if (StringUtils.hasLength(csrfToken)) {
                System.out.println(String.format("injecting X-XSRF-TOKEN '%s' into request for '%s'.", csrfToken, config.getServiceUrl()));
                httpPost.addHeader("X-XSRF-TOKEN", csrfToken);
            }
        }
        return httpPost;
    }

}

