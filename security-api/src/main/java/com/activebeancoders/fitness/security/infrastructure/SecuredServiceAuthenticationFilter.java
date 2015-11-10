package com.activebeancoders.fitness.security.infrastructure;

import com.activebeancoders.fitness.security.api.SecurityService;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
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
 * External services can use this filter to authenticate requests against the security service.
 *
 * @author Dan Barrese
 */
public class SecuredServiceAuthenticationFilter extends GenericFilterBean {

    public static final String TOKEN_SESSION_KEY = "token";
    public static final String USER_SESSION_KEY = "user";

    private final static Logger log = LoggerFactory.getLogger(SecuredServiceAuthenticationFilter.class);
    private UrlPathHelper urlPathHelper;
    private SecurityService securityService;

    public SecuredServiceAuthenticationFilter(SecurityService securityService) {
        this.securityService = securityService;
        urlPathHelper = new UrlPathHelper();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = asHttp(request);
        HttpServletResponse httpResponse = asHttp(response);

        Optional<String> token = Optional.fromNullable(httpRequest.getHeader("X-Auth-Token"));
        String resourcePath = urlPathHelper.getPathWithinApplication(httpRequest);

        AuthenticationWithToken existingAuthentication = null;
        try {
            if (token.isPresent()) {
                existingAuthentication = securityService.getAuthenticationByToken(token.get());
                if (!existingAuthentication.isAuthenticated()) {
                    throw new InternalAuthenticationServiceException("Invalid token.");
                } else {
                    // This is done so that the authentication data may be forwarded to remote method calls if needed.
                    logSuccessfulAccess(existingAuthentication, resourcePath);
                    SecurityContextHolder.getContext().setAuthentication(existingAuthentication);
                }
            }

            if (log.isDebugEnabled()) {
                log.debug("AuthenticationFilter is passing request down the filter chain");
            }
            addSessionContextToLogging();

            // Continue processing.
            chain.doFilter(request, response);
        } catch (InternalAuthenticationServiceException internalAuthenticationServiceException) {
            logFailedAccess(existingAuthentication, resourcePath);
            SecurityContextHolder.clearContext();
            log.error("Internal authentication service exception", internalAuthenticationServiceException);
            httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (AuthenticationException authenticationException) {
            logFailedAccess(existingAuthentication, resourcePath);
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
            String username = authentication == null ? "<unauthorized>" : authentication.getPrincipal().toString();
            log.info("User '{}' --access-denied--> '{}'", username, resourcePath);
        }
    }

    protected void logSuccessfulAccess(Authentication authentication, String resourcePath) {
        if (log.isInfoEnabled()) {
            log.info("User '{}' --access-granted--> '{}'", authentication.getPrincipal(), resourcePath);
        }
    }

    protected void addSessionContextToLogging() {
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

    protected HttpServletRequest asHttp(ServletRequest request) {
        return (HttpServletRequest) request;
    }

    protected HttpServletResponse asHttp(ServletResponse response) {
        return (HttpServletResponse) response;
    }

}

