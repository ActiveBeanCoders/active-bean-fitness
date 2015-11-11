package com.activebeancoders.fitness.security.service;

import com.activebeancoders.fitness.security.api.AuthenticationService;
import com.activebeancoders.fitness.security.domain.DomainUser;
import com.activebeancoders.fitness.security.infrastructure.AuthenticationWithToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.List;

/**
 * @author Dan Barrese
 */
public class AuthenticationServiceImpl implements AuthenticationService {

    @Override
    //    public AuthenticatedExternalWebService authenticate(String username, String password) {
    public AuthenticationWithToken authenticate(String username, String password) {
        // TODO: Do all authentication mechanisms required by external web service protocol and validated response.
        // Throw descendant of Spring AuthenticationException in case of unsucessful authentication. For example BadCredentialsException

        // GrantedAuthorities may come from external service authentication or be hardcoded at our layer as they are here with ROLE_DOMAIN_USER
        DomainUser domainUser = new DomainUser(username);
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_DOMAIN_USER");
        AuthenticationWithToken authenticationWithToken = new AuthenticationWithToken(domainUser, null, authorities);

        return authenticationWithToken;
    }

}
