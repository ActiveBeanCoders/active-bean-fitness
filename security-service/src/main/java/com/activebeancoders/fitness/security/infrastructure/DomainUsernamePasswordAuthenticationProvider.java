package com.activebeancoders.fitness.security.infrastructure;

import com.activebeancoders.fitness.security.dao.DomainUserCredentialsDao;
import com.activebeancoders.fitness.security.dao.DomainUserDao;
import com.activebeancoders.fitness.security.domain.DomainUser;
import com.activebeancoders.fitness.security.domain.DomainUserCredentials;
import com.activebeancoders.fitness.security.exception.MissingDomainUserException;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
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

    @Autowired
    private TokenService tokenService;

    @Autowired
    private DomainUserDao domainUserDao;

    @Autowired
    private DomainUserCredentialsDao domainUserCredentialsDao;


    public DomainUsernamePasswordAuthenticationProvider() {
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
        Optional<String> plaintextPassword = (Optional) authentication.getCredentials();

        if (!username.isPresent() || !plaintextPassword.isPresent()) {
            throw new BadCredentialsException("Invalid credentials.");
        }

        // Get user's salt and hashed password.
        DomainUserCredentials databaseCredentials = domainUserCredentialsDao.findByUsername(username.get());
        if (databaseCredentials == null) {
            // User is not in the database, so it cannot be authenticated.
            throw new BadCredentialsException("Username/password authentication failed.");
        }

        // Compare provided hashed password with hashed password in database.
        DomainUserCredentials providedCredentials = new DomainUserCredentials(username.get(), plaintextPassword.get(), databaseCredentials.getSalt());
        if (!providedCredentials.getPasswordHash().equals(databaseCredentials.getPasswordHash())) {
            throw new BadCredentialsException("Username/password authentication failed.");
        }

        // Retrieve DomainUser.
        DomainUser domainUser = domainUserDao.findByUsername(username.get());
        if (domainUser == null) {
            throw new MissingDomainUserException("DomainUser with username " + username.get() + " is missing from the database!");
        }
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(domainUser.getCommaSeparatedRoles());
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

