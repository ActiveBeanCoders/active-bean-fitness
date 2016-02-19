package com.activebeancoders.fitness.security.infrastructure;

import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.httpinvoker.HttpComponentsHttpInvokerRequestExecutor;
import org.springframework.remoting.httpinvoker.HttpInvokerClientConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * Responsible for including the currently-logged-in user's authentication token in all remote service calls.
 * Handles only HTTP POST requests.
 *
 * @author Dan Barrese
 */
// TODO: how to use this invoker for HTTPS requests?  Is there a property I need to set
@Component
public class AuthenticationTokenHttpInvokerRequestExecutor extends HttpComponentsHttpInvokerRequestExecutor {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private AuthenticationDao authenticationDao;

    @Autowired
    public AuthenticationTokenHttpInvokerRequestExecutor(AuthenticationDao authenticationDao) {
        this.authenticationDao = authenticationDao;
    }

    @Override
    protected HttpPost createHttpPost(HttpInvokerClientConfiguration config) throws IOException {
        HttpPost httpPost = super.createHttpPost(config);
        AuthenticationWithToken authentication = authenticationDao.getCurrentSessionAuthentication();
        if (authentication.isAuthenticated()) {
            // authentication.getAuthorities() -> user's roles
            // authentication.getCredentials() -> ?
            // authentication.getDetails() -> session ID/token as String
            // authentication.getPrincipal() -> DomainUser
            // authentication.getCsrfToken() -> csrf token

            String sessionToken = authentication.getToken();
            if (StringUtils.hasLength(sessionToken)) {
                httpPost.addHeader("X-Auth-Token", sessionToken);
            }

            String csrfToken = authentication.getCsrfToken();
            if (StringUtils.hasLength(csrfToken)) {
                httpPost.addHeader("X-XSRF-TOKEN", csrfToken);
            }
        }
        return httpPost;
    }

}

