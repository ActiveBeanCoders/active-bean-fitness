package com.activebeancoders.fitness.security.api;

import com.activebeancoders.fitness.security.infrastructure.AuthenticationWithToken;
import com.activebeancoders.fitness.spring.util.Http;
import com.activebeancoders.fitness.spring.util.header.AuthcTokenHttpHeader;
import com.activebeancoders.fitness.spring.util.header.PasswordHttpHeader;
import com.activebeancoders.fitness.spring.util.header.UsernameHttpHeader;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

/**
 * @author Dan Barrese
 */
@Component
public class RemoteAuthenticationServiceImpl implements AuthenticationService {

    @Value("${external-url.security-service}")
    private String securityServiceBaseUrl;

    @Override
    public AuthenticationWithToken authenticate(String username, String password) {
        String url = securityServiceBaseUrl + SecurityClientController.Endpoint.AUTHENTICATE.getUri();
        ResponseEntity<AuthenticationWithToken> responseEntity = Http.post(url,
                AuthenticationWithToken.class,
                new UsernameHttpHeader(username),
                new PasswordHttpHeader(password));
        return responseEntity.getBody();
    }

    @Override
    public AuthenticationWithToken validateToken(@Nonnull Optional<String> token) {
        String url = securityServiceBaseUrl + SecurityClientController.Endpoint.VALIDATE_AUTHC_TOKEN.getUri();
        ResponseEntity<AuthenticationWithToken> responseEntity = Http.post(url,
                AuthenticationWithToken.class,
                new AuthcTokenHttpHeader(token.get()));
        return responseEntity.getBody();
    }

    @Override
    public void invalidateToken(@Nonnull Optional<String> token) {
        // TODO: does this work?
        String url = securityServiceBaseUrl + SecurityClientController.Endpoint.LOGOUT.getUri();
        Http.post(url, String.class,
                new AuthcTokenHttpHeader(token.get()));
    }

}

