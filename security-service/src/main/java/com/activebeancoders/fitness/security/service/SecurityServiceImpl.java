package com.activebeancoders.fitness.security.service;

import com.activebeancoders.fitness.security.domain.DomainUser;
import com.activebeancoders.fitness.security.api.SecurityService;
import com.activebeancoders.fitness.security.infrastructure.AuthenticationWithToken;
import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.List;

/**
 * @author Dan Barrese
 */
public class SecurityServiceImpl implements SecurityService {

    @Autowired
    @Qualifier("myAuthenticationManager")
    private AuthenticationManager authenticationManager;

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

    @Override
    public AuthenticationWithToken getAuthenticationByToken(final String tokenString) {
        Optional<String> token = Optional.fromNullable(tokenString);
        try {
            AuthenticationWithToken authentication = processTokenAuthentication(token);
            return authentication;
        } catch (InternalAuthenticationServiceException e) {
            return AuthenticationWithToken.nonAuthenticatedInstance();
        }
    }

    @Override
    public String sayHello() {
        return "hello from a secured endpoint on security service";
    }

    private AuthenticationWithToken processTokenAuthentication(Optional<String> token) {
        AuthenticationWithToken resultOfAuthentication = tryToAuthenticateWithToken(token);
        return resultOfAuthentication;
    }

    private AuthenticationWithToken tryToAuthenticateWithToken(Optional<String> token) {
        PreAuthenticatedAuthenticationToken requestAuthentication = new PreAuthenticatedAuthenticationToken(token,
                null);
        return tryToAuthenticate(requestAuthentication);
    }

    private AuthenticationWithToken tryToAuthenticate(Authentication requestAuthentication) {
        Authentication responseAuthentication = authenticationManager.authenticate(requestAuthentication);
        if (responseAuthentication == null || !responseAuthentication.isAuthenticated()) {
            throw new InternalAuthenticationServiceException(
                    "Unable to authenticate Domain User for provided credentials");
        }
        // TODO: does this work?
        return (AuthenticationWithToken) responseAuthentication;
    }

}
