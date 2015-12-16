package com.activebeancoders.fitness.security.infrastructure;

import com.activebeancoders.fitness.security.api.TokenValidationService;
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
 * Responsible for authenticating requests by verifying them against the security-service.
 * All services, except the security-service, should use this filter.  This will allow
 * services to authenticate a session token against the security-service to allow requests
 * to secured resources.
 *
 * @author Dan Barrese
 */
public class SecuredServiceAuthenticationFilter extends GenericFilterBean {

    private final static Logger log = LoggerFactory.getLogger(SecuredServiceAuthenticationFilter.class);
    private UrlPathHelper urlPathHelper;
    private TokenValidationService tokenValidationService;
    private AuthenticationDao authenticationDao;

    public SecuredServiceAuthenticationFilter(TokenValidationService tokenValidationService,
                                              AuthenticationDao authenticationDao) {
        this.tokenValidationService = tokenValidationService;
        this.authenticationDao = authenticationDao;
        urlPathHelper = new UrlPathHelper();
    }

    /**
     * This filter will retrieve the session token (X-Auth-Token) from the request header
     * if it exists and verify it is a valid token.  If the token is invalid, the request
     * is not processed and an exception is thrown.
     * <p>
     * If no token is present on the request, this filter will pass the request to the
     * next filter in the chain.  This is because the requested resource may not be
     * secured and may not require a valid session at all.
     *
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @param chain The filter chain.
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        Optional<String> token = Optional.fromNullable(httpRequest.getHeader("X-Auth-Token"));
        String resourcePath = urlPathHelper.getPathWithinApplication(httpRequest);

        AuthenticationWithToken existingAuthentication = null;
        try {
            if (token.isPresent()) {
                existingAuthentication = tokenValidationService.validateToken(token);
                logSuccessfulAccess(existingAuthentication, resourcePath);

                // This is done so this service can forward authentication data to remote method calls.
                authenticationDao.save(existingAuthentication);
            }

            if (log.isDebugEnabled()) {
                log.debug("AuthenticationFilter is passing request down the filter chain");
            }
            ThreadLocalContext.addSessionContextToLogging(existingAuthentication);

            // Continue processing.
            chain.doFilter(request, response);
        } catch (InternalAuthenticationServiceException internalAuthenticationServiceException) {
            logFailedAccess(existingAuthentication, resourcePath);
            authenticationDao.clearCurrentSessionAuthentication();
            log.error("Internal authentication service exception", internalAuthenticationServiceException);
            httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (AuthenticationException authenticationException) {
            logFailedAccess(existingAuthentication, resourcePath);
            authenticationDao.clearCurrentSessionAuthentication();
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, authenticationException.getMessage());
        } finally {
            ThreadLocalContext.removeSessionContextFromLogging(existingAuthentication);
        }
    }

    // protected methods
    // ```````````````````````````````````````````````````````````````````````

    protected void logFailedAccess(AuthenticationWithToken authentication, String resourcePath) {
        if (log.isDebugEnabled()) {
            String username = authentication == null ? "<unauthorized>" : authentication.getUsername();
            log.debug("User '{}' --access-denied--> '{}'", username, resourcePath);
        }
    }

    protected void logSuccessfulAccess(AuthenticationWithToken authentication, String resourcePath) {
        if (log.isDebugEnabled()) {
            log.debug("User '{}' --access-granted--> '{}'", authentication.getUsername(), resourcePath);
        }
    }

}

