package com.activebeancoders.fitness;

import org.apache.http.client.methods.HttpPost;
import org.springframework.remoting.httpinvoker.HttpComponentsHttpInvokerRequestExecutor;
import org.springframework.remoting.httpinvoker.HttpInvokerClientConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationTokenHttpInvokerRequestExecutor extends HttpComponentsHttpInvokerRequestExecutor {

    @Override
    protected HttpPost createHttpPost(HttpInvokerClientConfiguration config) throws IOException {
        System.out.println("createHttpPost !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        HttpPost httpPost = super.createHttpPost(config);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            // authentication.getAuthorities() -> user's roles
            // authentication.getCredentials() -> ?
            // authentication.getDetails() -> token as String
            // authentication.getPrincipal() -> username
            String tokenString = (String) authentication.getDetails();
            System.out.println(String.format("injecting X-Auth-Token '%s' into request for '%s'.", tokenString, config.getServiceUrl()));
            httpPost.addHeader("X-Auth-Token", tokenString);
        }
        return httpPost;
    }

}

