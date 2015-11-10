package com.activebeancoders.fitness.security.infrastructure;

import com.activebeancoders.fitness.security.api.SecurityServiceController;
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

    private final static Logger logger = LoggerFactory.getLogger(ManagementEndpointAuthenticationFilter.class);
    private AuthenticationManager authenticationManager;
    private Set<String> managementEndpoints;

    public ManagementEndpointAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
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
        System.out.println(String.format("%s -> doFilter", getClass().getSimpleName()));
        HttpServletRequest httpRequest = asHttp(request);
        HttpServletResponse httpResponse = asHttp(response);

        Optional<String> username = Optional.fromNullable(httpRequest.getHeader("X-Auth-Username"));
        Optional<String> password = Optional.fromNullable(httpRequest.getHeader("X-Auth-Password"));
        System.out.println(
                String.format("%s -> doFilter with username=%s password=%s", getClass().getSimpleName(), username,
                        password));

        String resourcePath = new UrlPathHelper().getPathWithinApplication(httpRequest);
        System.out.println(String.format("%s -> getting resource path '%s'", getClass().getSimpleName(), resourcePath));

        try {
            if (postToManagementEndpoints(resourcePath)) {
                System.out.println(String.format("%s -> posting to management endpoint", getClass().getSimpleName()));
                logger.debug("Trying to authenticate user {} for management endpoint by X-Auth-Username method", username);
                processManagementEndpointUsernamePasswordAuthentication(username, password);
            }

            logger.debug("ManagementEndpointAuthenticationFilter is passing request down the filter chain");
            chain.doFilter(request, response);
        } catch (AuthenticationException authenticationException) {
            SecurityContextHolder.clearContext();
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
        Authentication resultOfAuthentication = tryToAuthenticateWithUsernameAndPassword(username, password);
        SecurityContextHolder.getContext().setAuthentication(resultOfAuthentication);
    }

    private Authentication tryToAuthenticateWithUsernameAndPassword(Optional<String> username, Optional<String> password) {
        BackendAdminUsernamePasswordAuthenticationToken requestAuthentication = new BackendAdminUsernamePasswordAuthenticationToken(username, password);
        return tryToAuthenticate(requestAuthentication);
    }

    private Authentication tryToAuthenticate(Authentication requestAuthentication) {
        System.out.println(String.format("%s -> tryToAuthenticate requestAuthentication=%s", getClass().getSimpleName(),
                requestAuthentication));
        Authentication responseAuthentication = authenticationManager.authenticate(requestAuthentication);
        if (responseAuthentication == null || !responseAuthentication.isAuthenticated()) {
            throw new InternalAuthenticationServiceException("Unable to authenticate Backend Admin for provided credentials");
        }
        logger.debug("Backend Admin successfully authenticated");
        return responseAuthentication;
    }
}
