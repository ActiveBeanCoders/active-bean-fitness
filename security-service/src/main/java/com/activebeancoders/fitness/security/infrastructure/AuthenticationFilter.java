package com.activebeancoders.fitness.security.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.activebeancoders.fitness.security.api.SecurityClientController;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The security service uses this filter to authenticate requests.
 *
 * @author Dan Barrese
 */
public class AuthenticationFilter extends GenericFilterBean {

    public static final String TOKEN_SESSION_KEY = "token";
    public static final String USER_SESSION_KEY = "user";

    private final static Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);
    private UrlPathHelper urlPathHelper;
    private AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        urlPathHelper = new UrlPathHelper();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = asHttp(request);
        HttpServletResponse httpResponse = asHttp(response);

        Optional<String> username = Optional.fromNullable(httpRequest.getHeader("X-Auth-Username"));
        Optional<String> password = Optional.fromNullable(httpRequest.getHeader("X-Auth-Password"));
        Optional<String> token = Optional.fromNullable(httpRequest.getHeader("X-Auth-Token"));
        String resourcePath = urlPathHelper.getPathWithinApplication(httpRequest);

        Authentication authentication = null;
        try {
            // Is another service trying to just verify a token is still valid?
            if (postToVerifyToken(httpRequest, resourcePath)) {
                authentication = processTokenAuthentication(token);
                if (log.isInfoEnabled()) {
                    log.info("User '{}' verifying token...authenticated={}", extractUsername(authentication), authentication.isAuthenticated());
                }
                return;
            }

            // Is a user trying to authenticate by username/password?
            if (postToAuthenticate(httpRequest, resourcePath)) {
                authentication = processUsernamePasswordAuthentication(httpResponse, username, password);
                if (log.isInfoEnabled()) {
                    log.info("User '{}' authenticating...succeeded={}", extractUsername(authentication), authentication.isAuthenticated());
                }
                return;
            }

            // Validate the token if one is present.
            if (token.isPresent()) {
                authentication = processTokenAuthentication(token);
            } else {
                if (log.isInfoEnabled()) {
                    log.info("User '{}' is attempting to access '{}' without a token.", extractUsername(authentication), resourcePath);
                }
            }

            if (log.isInfoEnabled() && authentication != null && authentication.isAuthenticated()) {
                log.info("User '{}' --access-granted--> '{}'", extractUsername(authentication), resourcePath);
            }
            if (log.isDebugEnabled()) {
                log.debug("AuthenticationFilter is passing request down the filter chain");
            }
            addSessionContextToLogging();

            // Continue processing.
            chain.doFilter(request, response);
        } catch (InternalAuthenticationServiceException internalAuthenticationServiceException) {
            logFailedAccess(authentication, resourcePath);
            SecurityContextHolder.clearContext();
            log.error("Internal authentication service exception", internalAuthenticationServiceException);
            httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (AuthenticationException authenticationException) {
            logFailedAccess(authentication, resourcePath);
            SecurityContextHolder.clearContext();
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, authenticationException.getMessage());
        } finally {
            MDC.remove(TOKEN_SESSION_KEY);
            MDC.remove(USER_SESSION_KEY);
        }
    }

    // protected methods
    // ```````````````````````````````````````````````````````````````````````

    protected void logFailedAccess(Authentication authentication, String resourcePath) {
        if (log.isInfoEnabled()) {
            log.info("User '{}' --access-denied--> '{}'", extractUsername(authentication), resourcePath);
        }
    }

    protected String extractUsername(Authentication authentication) {
        return authentication == null ? "<unauthorized>" : authentication.getPrincipal().toString();
    }

    // private methods
    // ```````````````````````````````````````````````````````````````````````

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

    private boolean postToAuthenticate(HttpServletRequest httpRequest, String resourcePath) {
        return SecurityClientController.AUTHENTICATE_URL.equalsIgnoreCase(resourcePath) && httpRequest.getMethod().equals("POST");
    }

    private boolean postToVerifyToken(HttpServletRequest httpRequest, String resourcePath) {
        return SecurityClientController.VERIFY_TOKEN_URL.equalsIgnoreCase(resourcePath) && httpRequest.getMethod().equals("POST");
    }

    private Authentication processUsernamePasswordAuthentication(HttpServletResponse httpResponse, Optional<String> username, Optional<String> password) throws IOException {
        Authentication resultOfAuthentication = tryToAuthenticateWithUsernameAndPassword(username, password);
        SecurityContextHolder.getContext().setAuthentication(resultOfAuthentication);
        httpResponse.setStatus(HttpServletResponse.SC_OK);
        TokenResponse tokenResponse = new TokenResponse(resultOfAuthentication.getDetails().toString());
        String tokenJsonResponse = new ObjectMapper().writeValueAsString(tokenResponse);
        httpResponse.addHeader("Content-Type", "application/json");
        httpResponse.getWriter().print(tokenJsonResponse);
        return resultOfAuthentication;
    }

    private Authentication tryToAuthenticateWithUsernameAndPassword(Optional<String> username, Optional<String> password) {
        UsernamePasswordAuthenticationToken requestAuthentication = new UsernamePasswordAuthenticationToken(username, password);
        return tryToAuthenticate(requestAuthentication);
    }

    private Authentication processTokenAuthentication(Optional<String> token) {
        Authentication resultOfAuthentication = tryToAuthenticateWithToken(token);
        SecurityContextHolder.getContext().setAuthentication(resultOfAuthentication);
        return resultOfAuthentication;
    }

    private Authentication tryToAuthenticateWithToken(Optional<String> token) {
        PreAuthenticatedAuthenticationToken requestAuthentication = new PreAuthenticatedAuthenticationToken(token, null);
        return tryToAuthenticate(requestAuthentication);
    }

    private Authentication tryToAuthenticate(Authentication requestAuthentication) {
        Authentication responseAuthentication = authenticationManager.authenticate(requestAuthentication);
        if (responseAuthentication == null || !responseAuthentication.isAuthenticated()) {
            throw new InternalAuthenticationServiceException("Unable to authenticate Domain User for provided credentials");
        }
        if (log.isDebugEnabled()) {
            log.debug("User successfully authenticated");
        }
        return responseAuthentication;
    }
}
