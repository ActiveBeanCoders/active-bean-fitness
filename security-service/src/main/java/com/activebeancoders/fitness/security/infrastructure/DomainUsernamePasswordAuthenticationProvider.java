package com.activebeancoders.fitness.security.infrastructure;

import com.activebeancoders.fitness.security.api.AuthenticationService;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author Dan Barrese
 */
@Component
public class DomainUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private TokenService tokenService;
    private AuthenticationService authenticationService;

    @Autowired
    public DomainUsernamePasswordAuthenticationProvider(TokenService tokenService, AuthenticationService authenticationService) {
        this.tokenService = tokenService;
        this.authenticationService = authenticationService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Optional<String> username = (Optional) authentication.getPrincipal();
        Optional<String> password = (Optional) authentication.getCredentials();

        if (!username.isPresent() || !password.isPresent()) {
            throw new BadCredentialsException("Invalid Domain User Credentials");
        }

        AuthenticationWithToken resultOfAuthentication = authenticationService.authenticate(username.get(), password.get());
//        String newToken = tokenService.generateNewToken();
        String newToken = UUID.randomUUID().toString();
        resultOfAuthentication.setToken(newToken);
        System.out.println(String.format("storing token '%s'", newToken));
        System.out.println(newToken);
        tokenService.store(newToken, resultOfAuthentication);

        return resultOfAuthentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
