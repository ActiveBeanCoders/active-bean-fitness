package com.activebeancoders.fitness.security.infrastructure;

import com.activebeancoders.fitness.security.api.AuthenticationService;
import com.activebeancoders.fitness.security.api.SecurityClientController;
import com.activebeancoders.fitness.security.api.TokenValidationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * The security service uses this filter to authenticate requests.
 *
 * @author Dan Barrese
 */
public class AuthenticationFilter extends GenericFilterBean {

    public static final String TOKEN_SESSION_KEY = "token";
    public static final String USER_SESSION_KEY = "user";

    private final Logger log = LoggerFactory.getLogger(getClass());
    private UrlPathHelper urlPathHelper;
    private AuthenticationService authenticationService;
    private TokenValidationService tokenValidationService;
    private AuthenticationDao authenticationDao;
    private ObjectMapper jsonMapper;

    public AuthenticationFilter(AuthenticationService authenticationService,
                                TokenValidationService tokenValidationService,
                                AuthenticationDao authenticationDao) {
        this.authenticationService = authenticationService;
        this.tokenValidationService = tokenValidationService;
        this.authenticationDao = authenticationDao;
        urlPathHelper = new UrlPathHelper();
        jsonMapper = new ObjectMapper();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = asHttp(request);
        HttpServletResponse httpResponse = asHttp(response);

        Optional<String> token = Optional.fromNullable(httpRequest.getHeader("X-Auth-Token"));
        String resourcePath = urlPathHelper.getPathWithinApplication(httpRequest);
        String accessPath = resourcePath + " -> " + httpRequest.getServletPath();
        if (log.isInfoEnabled()) {
            log.info("resource '{}' requested via '{}'", accessPath, httpRequest.getMethod());
        }

        AuthenticationWithToken authentication = null;
        try {
            // Is another service trying to just verify a token is still valid?
            // TODO: I don't think the verify token should be exposed as REST endpoint.
//            if (postToVerifyTokenRestEndpoint(httpRequest, resourcePath)) {
//                authentication = tokenValidationService.validateToken(token);
//                if (log.isInfoEnabled()) {
//                    log.info("User '{}' verifying token...authenticated={}", extractUsername(authentication), authentication.isAuthenticated());
//                }
//                return;
//            }

            // Is a user trying to authenticate by username/password?
            if (postToAuthenticateRestEndpoint(httpRequest, resourcePath)) {
                authentication = authenticationService.authenticate(httpRequest.getHeader("X-Auth-Username"), httpRequest.getHeader("X-Auth-Password"));
                httpResponse.setStatus(HttpServletResponse.SC_OK);
                TokenResponse tokenResponse = new TokenResponse(authentication.getToken());
                String tokenJsonResponse = jsonMapper.writeValueAsString(tokenResponse);
                httpResponse.addHeader("Content-Type", "application/json");
                httpResponse.getWriter().print(tokenJsonResponse);
                return;
            }

            // Validate the token if one is present.
            if (token.isPresent()) {
                authentication = tokenValidationService.validateToken(token);
                if (!authentication.isAuthenticated()) {
                    throw new InternalAuthenticationServiceException("Invalid session token.");
                }
            } else {
                if (log.isInfoEnabled()) {
                    log.info("User '{}' is attempting to access '{}' without a token.", extractUsername(authentication), accessPath);
                }
            }

            if (log.isDebugEnabled()) {
                log.debug("AuthenticationFilter is passing request down the filter chain");
            }
            addSessionContextToLogging();

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
            MDC.remove(TOKEN_SESSION_KEY);
            MDC.remove(USER_SESSION_KEY);
        }
    }

    // protected methods
    // ```````````````````````````````````````````````````````````````````````

    protected void logFailedAccess(AuthenticationWithToken authentication, String accessPath) {
        if (log.isInfoEnabled()) {
            log.info("User '{}' --access-denied--> '{}'", extractUsername(authentication), accessPath);
        }
    }

    protected String extractUsername(AuthenticationWithToken authentication) {
        return authentication == null ? "<unauthorized>" : authentication.getUsername();
    }

    // private methods
    // ```````````````````````````````````````````````````````````````````````

    private void addSessionContextToLogging() {
        AuthenticationWithToken authentication = authenticationDao.getCurrentSessionAuthentication();
        String tokenValue = "EMPTY";
        if (authentication != null && !Strings.isNullOrEmpty(authentication.getToken())) {
            // TODO: SHA-1 ok?
            MessageDigestPasswordEncoder encoder = new MessageDigestPasswordEncoder("SHA-1");
            // TODO: redo salt
            tokenValue = encoder.encodePassword(authentication.getToken(), "not_so_random_salt");
        }
        MDC.put(TOKEN_SESSION_KEY, tokenValue);

        String userValue = "EMPTY";
        if (authentication != null && !Strings.isNullOrEmpty(authentication.getUsername())) {
            userValue = authentication.getUsername();
        }
        MDC.put(USER_SESSION_KEY, userValue);
    }

    private HttpServletRequest asHttp(ServletRequest request) {
        return (HttpServletRequest) request;
    }

    private HttpServletResponse asHttp(ServletResponse response) {
        return (HttpServletResponse) response;
    }

    private boolean postToAuthenticateRestEndpoint(HttpServletRequest httpRequest, String resourcePath) {
        return httpRequest.getMethod().equals("POST") &&
                SecurityClientController.getAuthenticateEndpointFromRESTCall().equalsIgnoreCase(resourcePath);

    }

//    private boolean postToVerifyTokenRestEndpoint(HttpServletRequest httpRequest, String resourcePath) {
//        return httpRequest.getMethod().equals("POST") &&
//                SecurityClientController.getTokenValidationEndpointFromRESTCall().equalsIgnoreCase(resourcePath);
//    }

    // TODO: move to common api
    private String toStringRequest(HttpServletRequest httpRequest) {
        StringBuilder sb = new StringBuilder();
        final String NEWLINE = System.lineSeparator();
        sb.append(String.format("  getParameterMap=%s", httpRequest.getParameterMap())).append(NEWLINE);
        sb.append(httpRequest.toString()).append(NEWLINE);
        sb.append(String.format("  getAsyncContext=%s", httpRequest.getAsyncContext())).append(NEWLINE);
        Enumeration<String> attributeNames = httpRequest.getAttributeNames();
        if (attributeNames != null) {
            while (attributeNames.hasMoreElements()) {
                String attributeName = attributeNames.nextElement();
                sb.append(String.format("  getAttributeNames=%s = %s", attributeName, httpRequest.getAttribute(attributeName))).append(NEWLINE);
            }
        }
        sb.append(String.format("  getAuthType=%s", httpRequest.getAuthType())).append(NEWLINE);
        sb.append(String.format("  getContentType=%s", httpRequest.getContentType())).append(NEWLINE);
        sb.append(String.format("  getContextPath=%s", httpRequest.getContextPath())).append(NEWLINE);
        if (httpRequest.getCookies() != null) {
            for (int i = 0; i < httpRequest.getCookies().length; i++) {
                sb.append(String.format("  getCookies=%s : %s", httpRequest.getCookies()[i].getName(), httpRequest.getCookies()[i].getValue())).append(NEWLINE);
            }
        }
        sb.append(String.format("  getHeaderNames=%s", httpRequest.getHeaderNames())).append(NEWLINE);
        Enumeration<String> headerNames = httpRequest.getAttributeNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                sb.append(String.format("  getHeaderNames=%s = %s", headerName, httpRequest.getHeader(headerName))).append(NEWLINE);
            }
        }
        sb.append(String.format("  getLocalAddr=%s", httpRequest.getLocalAddr())).append(NEWLINE);
        sb.append(String.format("  getLocalName=%s", httpRequest.getLocalName())).append(NEWLINE);
        sb.append(String.format("  getLocalPort=%d", httpRequest.getLocalPort())).append(NEWLINE);
        sb.append(String.format("  getMethod=%s", httpRequest.getMethod())).append(NEWLINE);
        Enumeration<String> parameterNames = httpRequest.getParameterNames();
        if (parameterNames != null) {
            while (parameterNames.hasMoreElements()) {
                String parameterName = parameterNames.nextElement();
                sb.append(String.format("  getParameterNames=%s = %s", parameterName, httpRequest.getParameter(parameterName))).append(NEWLINE);
            }
        }
//        sb.append(String.format("  getParts=%s", httpRequest.getParts())).append(NEWLINE);
        sb.append(String.format("  getPathInfo=%s", httpRequest.getPathInfo())).append(NEWLINE);
        sb.append(String.format("  getPathTranslated=%s", httpRequest.getPathTranslated())).append(NEWLINE);
        sb.append(String.format("  getProtocol=%s", httpRequest.getProtocol())).append(NEWLINE);
        sb.append(String.format("  getQueryString=%s", httpRequest.getQueryString())).append(NEWLINE);
        sb.append(String.format("  getRemoteAddr=%s", httpRequest.getRemoteAddr())).append(NEWLINE);
        sb.append(String.format("  getRemoteHost=%s", httpRequest.getRemoteHost())).append(NEWLINE);
        sb.append(String.format("  getRemotePort=%d", httpRequest.getRemotePort())).append(NEWLINE);
        sb.append(String.format("  getRemoteUser=%s", httpRequest.getRemoteUser())).append(NEWLINE);
        sb.append(String.format("  getRequestURI=%s", httpRequest.getRequestURI())).append(NEWLINE);
        sb.append(String.format("  getRequestURL=%s", httpRequest.getRequestURL())).append(NEWLINE);
        sb.append(String.format("  getRequestedSessionId=%s", httpRequest.getRequestedSessionId())).append(NEWLINE);
        sb.append(String.format("  getScheme=%s", httpRequest.getScheme())).append(NEWLINE);
        sb.append(String.format("  getServerName=%s", httpRequest.getServerName())).append(NEWLINE);
        sb.append(String.format("  getServerPort=%d", httpRequest.getServerPort())).append(NEWLINE);
        ServletContext context = httpRequest.getServletContext();
        if (context != null) {
            sb.append(String.format("  getServletContext.getContextPath=%s", httpRequest.getServletContext().getContextPath())).append(NEWLINE);
            sb.append(String.format("  getServletContext.getServerInfo=%s", httpRequest.getServletContext().getServerInfo())).append(NEWLINE);
            sb.append(String.format("  getServletContext.getServletContextName=%s", httpRequest.getServletContext().getServletContextName())).append(NEWLINE);
            sb.append(String.format("  getServletContext.getServletRegistrations=%s", httpRequest.getServletContext().getServletRegistrations())).append(NEWLINE);
            sb.append(String.format("  getServletContext.getVirtualServerName=%s", httpRequest.getServletContext().getVirtualServerName())).append(NEWLINE);
            Enumeration<String> names = httpRequest.getServletContext().getAttributeNames();
            if (names != null) {
                while (names.hasMoreElements()) {
                    String name = names.nextElement();
                    sb.append(String.format("  getServletContext.getAttributeNames=%s = %s", name, httpRequest.getHeader(name))).append(NEWLINE);
                }
            }
        }
        sb.append(String.format("  getServletPath=%s", httpRequest.getServletPath())).append(NEWLINE);
        sb.append(String.format("  getSession=%s", httpRequest.getSession())).append(NEWLINE);
        sb.append(String.format("  getUserPrincipal=%s", httpRequest.getUserPrincipal())).append(NEWLINE);

        sb.append(String.format("  getDispatcherType=%s", httpRequest.getDispatcherType())).append(NEWLINE);
        return sb.toString();
    }

}
