package com.activebeancoders.fitness.security.service;

import com.activebeancoders.fitness.security.api.AuthenticationService;
import com.activebeancoders.fitness.security.domain.DomainUser;
import com.activebeancoders.fitness.security.infrastructure.AuthenticationWithToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Dan Barrese
 */
@Primary
@Component
public class AuthenticationServiceImpl implements AuthenticationService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public AuthenticationWithToken authenticate(String username, String password) {
        // TODO: Do all authentication mechanisms required by external web service protocol and validated response.
        // Throw descendant of Spring AuthenticationException in case of unsucessful authentication. For example BadCredentialsException

        if (!username.equalsIgnoreCase("user") || !password.equalsIgnoreCase("password")) {
            throw new InternalAuthenticationServiceException("Invalid authentication credentials.");
        }

        // GrantedAuthorities may come from external service authentication or be hardcoded at our layer as they are here with ROLE_DOMAIN_USER
        DomainUser domainUser = new DomainUser(username);
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_DOMAIN_USER");
        AuthenticationWithToken authenticationWithToken = new AuthenticationWithToken(domainUser, null, authorities);
        return authenticationWithToken;
    }

}

