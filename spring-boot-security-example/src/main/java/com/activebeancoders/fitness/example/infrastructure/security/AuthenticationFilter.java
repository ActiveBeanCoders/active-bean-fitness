package com.activebeancoders.fitness.example.infrastructure.security;

import com.activebeancoders.fitness.config.SecurityClientConfig;
import com.activebeancoders.fitness.example.api.samplestuff.SecurityService;
import com.activebeancoders.fitness.util.Assert;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UrlPathHelper;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationFilter extends GenericFilterBean {

    private final static Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);
    public static final String TOKEN_SESSION_KEY = "token";
    public static final String USER_SESSION_KEY = "user";
    private AuthenticationManager authenticationManager;
    private SecurityService securityService;

    public AuthenticationFilter(AuthenticationManager authenticationManager, SecurityService securityService) {
        this.authenticationManager = authenticationManager;
        this.securityService = securityService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println(String.format("%s -> doFilter", getClass().getSimpleName()));
        HttpServletRequest httpRequest = asHttp(request);
        HttpServletResponse httpResponse = asHttp(response);

        Optional<String> token = Optional.fromNullable(httpRequest.getHeader("X-Auth-Token"));
        System.out.println(String.format("%s -> doFilter with token=%s", getClass().getSimpleName(), token));

        String resourcePath = new UrlPathHelper().getPathWithinApplication(httpRequest);
        System.out.println(String.format("%s -> getting resource path '%s'", getClass().getSimpleName(), resourcePath));

        try {
            if (!token.isPresent()) {
                throw new InternalAuthenticationServiceException("ServletRequest is missing authentication token.");
            }

            Authentication existingAuthentication = securityService.getAuthenticationByToken(token.get());
            if (!existingAuthentication.isAuthenticated()) {
                throw new InternalAuthenticationServiceException("Invalid token.");
            } else {
                System.out.println("The token is valid!!!!!!!!!!!! yay!  Telling this app you are authenticated!");
                SecurityContextHolder.getContext().setAuthentication(existingAuthentication);
            }

            log.debug("AuthenticationFilter is passing request down the filter chain");
            addSessionContextToLogging();
            chain.doFilter(request, response);
        }
        catch (InternalAuthenticationServiceException internalAuthenticationServiceException) {
            SecurityContextHolder.clearContext();
            log.error("Internal authentication service exception", internalAuthenticationServiceException);
            httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        catch (AuthenticationException authenticationException) {
            SecurityContextHolder.clearContext();
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, authenticationException.getMessage());
        }
        finally {
            MDC.remove(TOKEN_SESSION_KEY);
            MDC.remove(USER_SESSION_KEY);
        }
    }

    private void addSessionContextToLogging() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String tokenValue = "EMPTY";
        if (authentication != null && !Strings.isNullOrEmpty(authentication.getDetails().toString())) {
            MessageDigestPasswordEncoder encoder = new MessageDigestPasswordEncoder("SHA-1");
            tokenValue = encoder.encodePassword(authentication.getDetails().toString(), "not_so_random_salt");
        }
        MDC.put(TOKEN_SESSION_KEY, tokenValue);

        String userValue = "EMPTY";
        if (authentication != null && !Strings.isNullOrEmpty(authentication.getPrincipal().toString())) {
            userValue = authentication.getPrincipal().toString();
        }
        MDC.put(USER_SESSION_KEY, userValue);
    }

    private HttpServletRequest asHttp(ServletRequest request) {
        return (HttpServletRequest) request;
    }

    private HttpServletResponse asHttp(ServletResponse response) {
        return (HttpServletResponse) response;
    }

        private void processTokenAuthentication(Optional<String> token) {
            System.out.println(
                    String.format("%s -> processTokenAuthentication with token=%s", getClass().getSimpleName(), token));
            Authentication resultOfAuthentication = tryToAuthenticateWithToken(token);
            SecurityContextHolder.getContext().setAuthentication(resultOfAuthentication);
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
            log.debug("User successfully authenticated");
            return responseAuthentication;
        }

    //    private void verifyTokenWithSecurityService(String token) {
    //        HttpsURLConnection connection = null;
    //        try {
    //            URL url = new URL("https://localhost:9999/api/v1/token/verify");
    //            connection = (HttpsURLConnection) url.openConnection();
    //            connection.setRequestMethod("POST");
    //            connection.setRequestProperty("X-Auth-Token", token);
    //            connection.setUseCaches(false);
    //            connection.setDoInput(true);
    //            connection.setDoOutput(true);
    //            connection.getInputStream().close();
    //            connection.connect();
    //
    //            int responseCode = connection.getResponseCode();
    //            switch (responseCode) {
    //                case HttpsURLConnection.HTTP_OK:
    //                    break;
    //                default:
    //                    throw new InternalAuthenticationServiceException("Invalid token.");
    //            }
    //        }
    //        catch (IOException e) {
    //            throw new InternalAuthenticationServiceException("Connecting to Security Service failed.");
    //        }
    //        finally {
    //            if (connection != null) {
    //                connection.disconnect();
    //            }
    //        }
    //    }

}

