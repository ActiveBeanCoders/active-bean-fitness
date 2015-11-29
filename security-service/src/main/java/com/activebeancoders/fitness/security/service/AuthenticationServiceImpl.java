package com.activebeancoders.fitness.security.service;

import com.activebeancoders.fitness.security.AuthenticationUtils;
import com.activebeancoders.fitness.security.api.AuthenticationService;
import com.activebeancoders.fitness.security.infrastructure.AuthenticationDao;
import com.activebeancoders.fitness.security.infrastructure.AuthenticationWithToken;
import com.activebeancoders.fitness.security.infrastructure.DomainUsernamePasswordAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * Responsible for authenticated user credentials.  This is the class that is exposed to
 * external services for authentication.
 *
 * @author Dan Barrese
 */
@Primary
@Component
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @Autowired
    private DomainUsernamePasswordAuthenticationProvider domainUsernamePasswordAuthenticationProvider;

    @Autowired
    private AuthenticationDao authenticationDao;

    @Override
    public AuthenticationWithToken authenticate(final String username, final String password) {
        UsernamePasswordAuthenticationToken authenticationWithToken = AuthenticationUtils.createAuthToken(username, password);
        AuthenticationWithToken resultOfAuthentication = domainUsernamePasswordAuthenticationProvider.authenticate(authenticationWithToken);
        if (log.isInfoEnabled()) {
            log.info("User '{}' authenticating...succeeded={}", extractUsername(resultOfAuthentication), resultOfAuthentication.isAuthenticated());
        }
        authenticationDao.save(resultOfAuthentication);
        return resultOfAuthentication;
    }

    protected String extractUsername(AuthenticationWithToken authentication) {
        return authentication == null ? "<unauthorized>" : authentication.getUsername();
    }


}

