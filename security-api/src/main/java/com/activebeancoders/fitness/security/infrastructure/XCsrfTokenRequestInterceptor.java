package com.activebeancoders.fitness.security.infrastructure;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

/**
 * Interceptor that adds the user's CSRF token as a request header on a RestTemplate
 * request.
 *
 * @author Dan Barrese
 */
public class XCsrfTokenRequestInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        AuthenticationWithToken auth = (AuthenticationWithToken)
                SecurityContextHolder.getContext().getAuthentication();
        headers.add("X-CSRF-Token", auth.getToken());
        return execution.execute(request, body);
    }

}

