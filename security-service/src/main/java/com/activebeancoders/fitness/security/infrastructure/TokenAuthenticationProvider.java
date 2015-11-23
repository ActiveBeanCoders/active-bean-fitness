package com.activebeancoders.fitness.security.infrastructure;

import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * Responsible for validating a session token.  Do not use this class directly.  Instead,
 * use it through the {@link com.activebeancoders.fitness.security.config.AuthenticationWithTokenManager}
 * or the {@link com.activebeancoders.fitness.security.api.AuthenticationService} or the
 * {@link com.activebeancoders.fitness.security.api.TokenValidationService}.
 *
 * @author Dan Barrese
 */
@Component
public class TokenAuthenticationProvider implements AuthenticationProvider {

    private TokenService tokenService;

    @Autowired
    public TokenAuthenticationProvider(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public AuthenticationWithToken authenticate(Authentication authentication) throws AuthenticationException {
        Optional<String> token = (Optional) authentication.getPrincipal();
        if (!token.isPresent() || token.get().isEmpty()) {
            throw new BadCredentialsException("Invalid token");
        }
        if (!tokenService.contains(token.get())) {
            throw new BadCredentialsException("Invalid token or token expired");
        }
        return tokenService.retrieve(token.get());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(PreAuthenticatedAuthenticationToken.class);
    }

    public boolean invalidate(final Optional<String> token) throws AuthenticationException {
        if (!token.isPresent() || token.get().isEmpty()) {
            return false;
        }
        return tokenService.remove(token.get());
    }

}

