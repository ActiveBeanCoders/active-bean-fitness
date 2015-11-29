package com.activebeancoders.fitness.security.infrastructure;

import com.activebeancoders.fitness.security.domain.DomainUser;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Responsible for validating username and password combo.  Do not use this class
 * directly.  Instead, use it through the {@link com.activebeancoders.fitness.security.config.AuthenticationWithTokenManager}
 * or the {@link com.activebeancoders.fitness.security.api.AuthenticationService} or the
 * {@link com.activebeancoders.fitness.security.api.TokenValidationService}.
 *
 * @author Dan Barrese
 */
@Component
public class DomainUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private TokenService tokenService;

    @Autowired
    public DomainUsernamePasswordAuthenticationProvider(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * Verifies that the provided authentication information contains valid credentials
     * (username, password).
     *
     * @param authentication Must be a {@link org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken}.
     * @return The authentication info associated with the provided username/password (NOT
     * the authentication info passed into this method).
     * @throws AuthenticationException if username/password are invalid.
     */
    @Override
    public AuthenticationWithToken authenticate(Authentication authentication) throws AuthenticationException {
        Optional<String> username = (Optional) authentication.getPrincipal();
        Optional<String> password = (Optional) authentication.getCredentials();

        if (!username.isPresent() || !password.isPresent()) {
            throw new BadCredentialsException("Invalid credentials");
        }

        // TODO: make call to database to authenticate username/password
        // Throw descendant of Spring AuthenticationException in case of unsucessful authentication. For example BadCredentialsException
        if (!username.get().equalsIgnoreCase("user") || !password.get().equalsIgnoreCase("password")) {
            throw new InternalAuthenticationServiceException("Invalid authentication credentials.");
        }

        DomainUser domainUser = new DomainUser(username.get());
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_DOMAIN_USER");
        AuthenticationWithToken authenticationWithToken = new AuthenticationWithToken(domainUser, null, authorities);

        // Create session token.
        String newToken = tokenService.generateNewToken();
        authenticationWithToken.setToken(newToken);
        tokenService.store(newToken, authenticationWithToken);

        return authenticationWithToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}

