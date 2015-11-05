package com.activebeancoders.fitness.example.infrastructure.externalwebservice;

import com.activebeancoders.fitness.domain.DomainUser;
import com.activebeancoders.fitness.example.api.samplestuff.SecurityService;
import com.activebeancoders.fitness.example.infrastructure.security.AuthenticationWithToken;
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

public class SecurityServiceImpl implements SecurityService {

    @Autowired
    @Qualifier("myAuthenticationManager")
    private AuthenticationManager authenticationManager;

    @Override
    //    public AuthenticatedExternalWebService authenticate(String username, String password) {
    public AuthenticationWithToken authenticate(String username, String password) {
        System.out.println(
                String.format("%s -> authenticate called with username=%s password=%s", getClass().getSimpleName(),
                        username, password));
        //        ExternalWebServiceStub externalWebService = new ExternalWebServiceStub();

        // TODO: Do all authentication mechanisms required by external web service protocol and validated response.
        // Throw descendant of Spring AuthenticationException in case of unsucessful authentication. For example BadCredentialsException

        // ...
        // ...

        // If authentication to external service succeeded then create authenticated wrapper with proper Principal and GrantedAuthorities.
        // GrantedAuthorities may come from external service authentication or be hardcoded at our layer as they are here with ROLE_DOMAIN_USER
        DomainUser domainUser = new DomainUser(username);
        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_DOMAIN_USER");
        AuthenticationWithToken authenticationWithToken = new AuthenticationWithToken(domainUser, null, authorities);
        //        authenticationWithToken.setExternalWebService(externalWebService);

        return authenticationWithToken;
    }

    @Override
    public Authentication getAuthenticationByToken(final String tokenString) {
        System.out.println("isTokenValid !!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        Optional<String> token = Optional.fromNullable(tokenString);
        try {
            Authentication authentication = processTokenAuthentication(token);
            System.out.println(String.format("token %s authentication=%s", tokenString, authentication));
            return authentication;
        }
        catch (InternalAuthenticationServiceException e) {
            System.out.println(String.format("token '%s' is invalid, returning null", tokenString));
            return null; // TODO: return authentication object that indicates NO authentication.
        }
    }

    @Override
    public String sayHello() {
        return "hello from a secured endpoint on security service";
    }

    private Authentication processTokenAuthentication(Optional<String> token) {
        System.out.println(
                String.format("%s -> processTokenAuthentication with token=%s", getClass().getSimpleName(), token));
        Authentication resultOfAuthentication = tryToAuthenticateWithToken(token);
        return resultOfAuthentication;
    }

    private Authentication tryToAuthenticateWithToken(Optional<String> token) {
        System.out.println(
                String.format("%s -> tryToAuthenticateWithToken with token=%s", getClass().getSimpleName(), token));
        PreAuthenticatedAuthenticationToken requestAuthentication = new PreAuthenticatedAuthenticationToken(token,
                null);
        return tryToAuthenticate(requestAuthentication);
    }

    private Authentication tryToAuthenticate(Authentication requestAuthentication) {
        Authentication responseAuthentication = authenticationManager.authenticate(requestAuthentication);
        if (responseAuthentication == null || !responseAuthentication.isAuthenticated()) {
            throw new InternalAuthenticationServiceException(
                    "Unable to authenticate Domain User for provided credentials");
        }
        return responseAuthentication;
    }

}
