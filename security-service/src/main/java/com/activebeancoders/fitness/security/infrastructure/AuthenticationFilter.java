package com.activebeancoders.fitness.security.infrastructure;

import com.activebeancoders.fitness.security.api.AuthenticationService;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
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
 * The security service uses this filter to authenticate requests.  Other services should
 * use the {@link com.activebeancoders.fitness.security.infrastructure.SecuredServiceAuthenticationFilter}
 * to authenticate requests.
 *
 * @author Dan Barrese
 */
public class AuthenticationFilter extends GenericFilterBean {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private UrlPathHelper urlPathHelper;
    private AuthenticationService authenticationService;
    private AuthenticationDao authenticationDao;

    public AuthenticationFilter(AuthenticationService authenticationService,
                                AuthenticationDao authenticationDao) {
        this.authenticationService = authenticationService;
        this.authenticationDao = authenticationDao;
        urlPathHelper = new UrlPathHelper();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        Optional<String> token = Optional.fromNullable(httpRequest.getHeader("X-Auth-Token"));
        String resourcePath = urlPathHelper.getPathWithinApplication(httpRequest);
        String accessPath = resourcePath + " -> " + httpRequest.getServletPath();
        if (log.isInfoEnabled()) {
            log.info("resource '{}' requested via '{}'", accessPath, httpRequest.getMethod());
        }

        AuthenticationWithToken authentication = null;
        try {
            // Validate the token if one is present.
            if (token.isPresent()) {
                authentication = authenticationService.validateToken(token);
                if (!authentication.isAuthenticated()) {
                    throw new InternalAuthenticationServiceException("Invalid session token.");
                } else {
                    authenticationDao.save(authentication);
                    com.activebeancoders.fitness.security.infrastructure.ThreadLocalContext.
                            addSessionContextToLogging(authentication);
                }
            } else {
                if (log.isInfoEnabled()) {
                    log.info("User '{}' is attempting to access '{}' without a token.", extractUsername(authentication), accessPath);
                }
            }

            if (log.isDebugEnabled()) {
                log.debug("AuthenticationFilter is passing request down the filter chain");
            }

            // Continue processing.
            chain.doFilter(request, response);
        } catch (InternalAuthenticationServiceException internalAuthenticationServiceException) {
            logFailedAccess(authentication, accessPath);
            authenticationDao.clearCurrentSessionAuthentication();
            log.error("Internal authentication service exception", internalAuthenticationServiceException);
            httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (AuthenticationException authenticationException) {
            logFailedAccess(authentication, accessPath);
            authenticationDao.clearCurrentSessionAuthentication();
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, authenticationException.getMessage());
        } finally {
            com.activebeancoders.fitness.security.infrastructure.ThreadLocalContext.
                    removeSessionContextFromLogging(authentication);
        }
    }

    // protected methods
    // ```````````````````````````````````````````````````````````````````````

    protected void logFailedAccess(AuthenticationWithToken authentication, String accessPath) {
        if (log.isDebugEnabled()) {
            log.debug("User '{}' --access-denied--> '{}'", extractUsername(authentication), accessPath);
        }
    }

    protected String extractUsername(AuthenticationWithToken authentication) {
        return authentication == null ? "<unauthorized>" : authentication.getUsername();
    }

}
