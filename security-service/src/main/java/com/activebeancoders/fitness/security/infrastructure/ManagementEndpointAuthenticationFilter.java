package com.activebeancoders.fitness.security.infrastructure;

import com.activebeancoders.fitness.security.api.AuthenticationService;
import com.activebeancoders.fitness.security.api.SecurityServiceController;
import com.activebeancoders.fitness.security.config.AuthenticationWithTokenManager;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
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
import java.util.HashSet;
import java.util.Set;

/**
 * @author Dan Barrese
 */
public class ManagementEndpointAuthenticationFilter extends GenericFilterBean {

    private final static Logger log = LoggerFactory.getLogger(ManagementEndpointAuthenticationFilter.class);
    private AuthenticationWithTokenManager authenticationManager;
    private AuthenticationService authenticationService;
    private AuthenticationDao authenticationDao;
    private Set<String> managementEndpoints;

    public ManagementEndpointAuthenticationFilter(AuthenticationWithTokenManager authenticationManager,
                                                  AuthenticationService authenticationService,
                                                  AuthenticationDao authenticationDao) {
        this.authenticationManager = authenticationManager;
        this.authenticationService = authenticationService;
        this.authenticationDao = authenticationDao;
        prepareManagementEndpointsSet();
    }

    private void prepareManagementEndpointsSet() {
        managementEndpoints = new HashSet<>();
        managementEndpoints.add(SecurityServiceController.AUTOCONFIG_ENDPOINT);
        managementEndpoints.add(SecurityServiceController.BEANS_ENDPOINT);
        managementEndpoints.add(SecurityServiceController.CONFIGPROPS_ENDPOINT);
        managementEndpoints.add(SecurityServiceController.ENV_ENDPOINT);
        managementEndpoints.add(SecurityServiceController.MAPPINGS_ENDPOINT);
        managementEndpoints.add(SecurityServiceController.METRICS_ENDPOINT);
        managementEndpoints.add(SecurityServiceController.SHUTDOWN_ENDPOINT);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (log.isDebugEnabled()) {
            log.debug("'{}'#doFilter(...) called", getClass().getSimpleName());
        }
        HttpServletRequest httpRequest = asHttp(request);
        HttpServletResponse httpResponse = asHttp(response);

        Optional<String> username = Optional.fromNullable(httpRequest.getHeader("X-Auth-Username"));
        Optional<String> password = Optional.fromNullable(httpRequest.getHeader("X-Auth-Password"));

        String resourcePath = new UrlPathHelper().getPathWithinApplication(httpRequest);

        try {
            if (postToManagementEndpoints(resourcePath)) {
                processManagementEndpointUsernamePasswordAuthentication(username, password);
            }

            if (log.isDebugEnabled()) {
                log.debug("ManagementEndpointAuthenticationFilter is passing request down the filter chain");
            }
            chain.doFilter(request, response);
        } catch (AuthenticationException authenticationException) {
            authenticationDao.clearCurrentSessionAuthentication();
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, authenticationException.getMessage());
        }
    }

    private HttpServletRequest asHttp(ServletRequest request) {
        return (HttpServletRequest) request;
    }

    private HttpServletResponse asHttp(ServletResponse response) {
        return (HttpServletResponse) response;
    }

    private boolean postToManagementEndpoints(String resourcePath) {
        return managementEndpoints.contains(resourcePath);
    }

    private void processManagementEndpointUsernamePasswordAuthentication(Optional<String> username, Optional<String> password) throws IOException {
        AuthenticationWithToken resultOfAuthentication = tryToAuthenticateWithUsernameAndPassword(username, password);
        authenticationDao.save(resultOfAuthentication); // TODO: delete?
    }

    private AuthenticationWithToken tryToAuthenticateWithUsernameAndPassword(Optional<String> username, Optional<String> password) {
        BackendAdminUsernamePasswordAuthenticationToken requestAuthentication = new BackendAdminUsernamePasswordAuthenticationToken(username, password);
        return tryToAuthenticate(requestAuthentication);
    }

    private AuthenticationWithToken tryToAuthenticate(Authentication requestAuthentication) {
        AuthenticationWithToken responseAuthentication = authenticationManager.authenticate(requestAuthentication);
        if (responseAuthentication == null || !responseAuthentication.isAuthenticated()) {
            throw new InternalAuthenticationServiceException("Unable to authenticate Backend Admin for provided credentials");
        }
        if (log.isDebugEnabled()) {
            log.debug("Backend Admin successfully authenticated");
        }
        return responseAuthentication;
    }
}
